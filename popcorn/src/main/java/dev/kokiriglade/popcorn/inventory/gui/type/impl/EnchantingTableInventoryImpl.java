package dev.kokiriglade.popcorn.inventory.gui.type.impl;

import dev.kokiriglade.popcorn.inventory.gui.type.abstraction.EnchantingTableInventory;
import io.papermc.paper.adventure.PaperAdventure;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundContainerSetContentPacket;
import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
import net.minecraft.network.protocol.game.ClientboundOpenScreenPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerPlayerConnection;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.EnchantmentMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.craftbukkit.inventory.CraftInventory;
import org.bukkit.craftbukkit.inventory.CraftInventoryEnchanting;
import org.bukkit.craftbukkit.inventory.CraftInventoryView;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryHolder;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.jetbrains.annotations.Contract;

import java.lang.reflect.Field;

/**
 * Internal enchanting table inventory
 *
 * @since 3.0.0
 */
@SuppressWarnings("DuplicatedCode")
public class EnchantingTableInventoryImpl extends EnchantingTableInventory {

    public EnchantingTableInventoryImpl(@NonNull InventoryHolder inventoryHolder) {
        super(inventoryHolder);
    }

    @Override
    public void openInventory(@NonNull Player player, net.kyori.adventure.text.@NonNull Component title,
                              @Nullable org.bukkit.inventory.ItemStack[] items) {
        int itemAmount = items.length;

        if (itemAmount != 2) {
            throw new IllegalArgumentException(
                "The amount of items for an enchanting table should be 2, but is '" + itemAmount + "'"
            );
        }

        ServerPlayer serverPlayer = getServerPlayer(player);
        Component message = PaperAdventure.asVanilla(title);
        ContainerEnchantingTableImpl containerEnchantmentTable = new ContainerEnchantingTableImpl(
            serverPlayer, items, message
        );

        serverPlayer.containerMenu = containerEnchantmentTable;

        int id = containerEnchantmentTable.containerId;

        serverPlayer.connection.send(new ClientboundOpenScreenPacket(id, MenuType.ENCHANTMENT, message));

        sendItems(player, items);
    }

    @Override
    public void sendItems(@NonNull Player player, @Nullable org.bukkit.inventory.ItemStack[] items) {
        NonNullList<ItemStack> nmsItems = NonNullList.of(
            ItemStack.EMPTY,
            CraftItemStack.asNMSCopy(items[0]),
            CraftItemStack.asNMSCopy(items[1])
        );

        ServerPlayer serverPlayer = getServerPlayer(player);
        int containerId = getContainerId(serverPlayer);
        int state = serverPlayer.containerMenu.incrementStateId();
        ItemStack cursor = CraftItemStack.asNMSCopy(player.getItemOnCursor());
        ServerPlayerConnection playerConnection = getPlayerConnection(serverPlayer);

        playerConnection.send(new ClientboundContainerSetContentPacket(containerId, state, nmsItems, cursor));
    }

    @Override
    public void clearCursor(@NonNull Player player) {
        ServerPlayer serverPlayer = getServerPlayer(player);
        int state = serverPlayer.containerMenu.incrementStateId();

        getPlayerConnection(serverPlayer).send(new ClientboundContainerSetSlotPacket(-1, state, -1, ItemStack.EMPTY));
    }

    /**
     * Gets the containerId id for the inventory view the player currently has open
     *
     * @param nmsPlayer the player to get the containerId id for
     * @return the containerId id
     * @since 3.0.0
     */
    @Contract(pure = true)
    private int getContainerId(net.minecraft.world.entity.player.@NonNull Player nmsPlayer) {
        return nmsPlayer.containerMenu.containerId;
    }

    /**
     * Gets the player connection for the specified player
     *
     * @param serverPlayer the player to get the player connection from
     * @return the player connection
     * @since 3.0.0
     */
    @NonNull
    @Contract(pure = true)
    private ServerPlayerConnection getPlayerConnection(@NonNull ServerPlayer serverPlayer) {
        return serverPlayer.connection;
    }

    /**
     * Gets the server player associated to this player
     *
     * @param player the player to get the server player from
     * @return the server player
     * @since 3.0.0
     */
    @NonNull
    @Contract(pure = true)
    private ServerPlayer getServerPlayer(@NonNull Player player) {
        return ((CraftPlayer) player).getHandle();
    }

    /**
     * A custom container enchanting table
     *
     * @since 3.0.0
     */
    @SuppressWarnings("CallToPrintStackTrace")
    private class ContainerEnchantingTableImpl extends EnchantmentMenu {

        /**
         * The player for this enchanting table container
         */
        @NonNull
        private final Player player;

        /**
         * The internal bukkit entity for this container enchanting table
         */
        @Nullable
        private CraftInventoryView bukkitEntity;

        /**
         * Field for accessing the enchant slots field
         */
        @NonNull
        private final Field enchantSlotsField;

        public ContainerEnchantingTableImpl(@NonNull ServerPlayer serverPlayer,
                                            @Nullable org.bukkit.inventory.ItemStack[] items,
                                            @NonNull Component title) {
            super(serverPlayer.nextContainerCounter(), serverPlayer.getInventory());

            this.player = serverPlayer.getBukkitEntity();

            setTitle(title);

            try {
                //noinspection JavaReflectionMemberAccess
                this.enchantSlotsField = EnchantmentMenu.class.getDeclaredField("o"); //enchantSlots
                this.enchantSlotsField.setAccessible(true);
            } catch (NoSuchFieldException exception) {
                throw new RuntimeException(exception);
            }

            try {
                Container input = (Container) enchantSlotsField.get(this);

                input.setItem(0, CraftItemStack.asNMSCopy(items[0]));
                input.setItem(1, CraftItemStack.asNMSCopy(items[1]));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        @NonNull
        @Override
        public CraftInventoryView getBukkitView() {
            if (bukkitEntity == null) {
                try {
                    CraftInventory inventory = new CraftInventoryEnchanting((Container) enchantSlotsField.get(this)) {
                        @NonNull
                        @Contract(pure = true)
                        @Override
                        public InventoryHolder getHolder() {
                            return inventoryHolder;
                        }
                    };

                    bukkitEntity = new CraftInventoryView(player, inventory, this);
                } catch (IllegalAccessException exception) {
                    exception.printStackTrace();
                }
            }

            return bukkitEntity;
        }

        @Contract(pure = true, value = "_ -> true")
        @Override
        public boolean stillValid(net.minecraft.world.entity.player.@Nullable Player nmsPlayer) {
            return true;
        }

        @Override
        public void slotsChanged(@NonNull Container container) {}

        @Override
        public void removed(net.minecraft.world.entity.player.@NonNull Player nmsPlayer) {}

    }
}
