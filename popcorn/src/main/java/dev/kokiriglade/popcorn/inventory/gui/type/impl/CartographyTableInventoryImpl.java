package dev.kokiriglade.popcorn.inventory.gui.type.impl;

import dev.kokiriglade.popcorn.inventory.gui.type.abstraction.CartographyTableInventory;
import dev.kokiriglade.popcorn.inventory.gui.type.impl.util.CustomInventoryUtil;
import io.papermc.paper.adventure.PaperAdventure;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundContainerSetContentPacket;
import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
import net.minecraft.network.protocol.game.ClientboundOpenScreenPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerPlayerConnection;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.CartographyTableMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.craftbukkit.inventory.CraftInventory;
import org.bukkit.craftbukkit.inventory.CraftInventoryCartography;
import org.bukkit.craftbukkit.inventory.CraftInventoryView;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryHolder;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.jetbrains.annotations.Contract;

import java.lang.reflect.Field;

/**
 * Internal cartography table inventory
 *
 * @since 3.0.0
 */
public class CartographyTableInventoryImpl extends CartographyTableInventory {

    public CartographyTableInventoryImpl(@NonNull InventoryHolder inventoryHolder) {
        super(inventoryHolder);
    }

    @Override
    public void openInventory(@NonNull Player player, net.kyori.adventure.text.@NonNull Component title,
                              org.bukkit.inventory.@Nullable ItemStack[] items) {
        int itemAmount = items.length;

        if (itemAmount != 3) {
            throw new IllegalArgumentException(
                "The amount of items for a cartography table should be 3, but is '" + itemAmount + "'"
            );
        }


        ServerPlayer serverPlayer = getServerPlayer(player);
        Component message = PaperAdventure.asVanilla(title);

        ContainerCartographyTableImpl containerCartographyTable = new ContainerCartographyTableImpl(
            serverPlayer, items, message
        );

        serverPlayer.containerMenu = containerCartographyTable;

        int id = containerCartographyTable.containerId;

        serverPlayer.connection.send(new ClientboundOpenScreenPacket(id, MenuType.CARTOGRAPHY_TABLE, message));

        sendItems(player, items);
    }

    @Override
    public void sendItems(@NonNull Player player, org.bukkit.inventory.@Nullable ItemStack[] items) {
        NonNullList<ItemStack> nmsItems = CustomInventoryUtil.convertToNMSItems(items);
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
     * Gets the container id for the inventory view the player currently has open
     *
     * @param nmsPlayer the player to get the container id for
     * @return the container id
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
     * A custom container cartography table
     *
     * @since 3.0.0
     */
    private class ContainerCartographyTableImpl extends CartographyTableMenu {

        /**
         * The player for this cartography table container
         */
        @NonNull
        private final Player player;

        /**
         * The internal bukkit entity for this container cartography table
         */
        @Nullable
        private CraftInventoryView bukkitEntity;

        /**
         * Field for accessing the result inventory field
         */
        @NonNull
        private final Field resultContainerField;

        public ContainerCartographyTableImpl(@NonNull ServerPlayer serverPlayer,
                                             org.bukkit.inventory.@Nullable ItemStack[] items,
                                             @NonNull Component title) {
            super(serverPlayer.nextContainerCounter(), serverPlayer.getInventory());

            this.player = serverPlayer.getBukkitEntity();

            setTitle(title);

            try {
                //noinspection JavaReflectionMemberAccess
                this.resultContainerField = CartographyTableMenu.class.getDeclaredField("u"); //resultContainer
                this.resultContainerField.setAccessible(true);
            } catch (NoSuchFieldException exception) {
                throw new RuntimeException(exception);
            }

            container.setItem(0, CraftItemStack.asNMSCopy(items[0]));
            container.setItem(1, CraftItemStack.asNMSCopy(items[1]));

            getResultInventory().setItem(0, CraftItemStack.asNMSCopy(items[2]));
        }

        @NonNull
        @Override
        public CraftInventoryView getBukkitView() {
            if (bukkitEntity == null) {
                CraftInventory inventory = new CraftInventoryCartography(super.container, getResultInventory()) {
                    @NonNull
                    @Contract(pure = true)
                    @Override
                    public InventoryHolder getHolder() {
                        return inventoryHolder;
                    }
                };

                bukkitEntity = new CraftInventoryView(player, inventory, this);
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

        @NonNull
        @Contract(pure = true)
        private Container getResultInventory() {
            try {
                return (Container) resultContainerField.get(this);
            } catch (IllegalAccessException exception) {
                throw new RuntimeException(exception);
            }
        }

    }
}
