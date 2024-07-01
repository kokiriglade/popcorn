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

    /**
     * Create an internal cartography table inventory
     * @param inventoryHolder the {@code InventoryHolder}
     * @since 3.0.0
     */
    public CartographyTableInventoryImpl(final @NonNull InventoryHolder inventoryHolder) {
        super(inventoryHolder);
    }

    @Override
    public void openInventory(final @NonNull Player player, final net.kyori.adventure.text.@NonNull Component title,
                              final org.bukkit.inventory.@Nullable ItemStack[] items) {
        final int itemAmount = items.length;

        if (itemAmount != 3) {
            throw new IllegalArgumentException(
                "The amount of items for a cartography table should be 3, but is '" + itemAmount + "'"
            );
        }


        final ServerPlayer serverPlayer = getServerPlayer(player);
        final Component message = PaperAdventure.asVanilla(title);

        final ContainerCartographyTableImpl containerCartographyTable = new ContainerCartographyTableImpl(
            serverPlayer, items, message
        );

        serverPlayer.containerMenu = containerCartographyTable;

        final int id = containerCartographyTable.containerId;

        serverPlayer.connection.send(new ClientboundOpenScreenPacket(id, MenuType.CARTOGRAPHY_TABLE, message));

        sendItems(player, items);
    }

    @Override
    public void sendItems(final @NonNull Player player, final org.bukkit.inventory.@Nullable ItemStack[] items) {
        final NonNullList<ItemStack> nmsItems = CustomInventoryUtil.convertToNMSItems(items);
        final ServerPlayer serverPlayer = getServerPlayer(player);
        final int containerId = getContainerId(serverPlayer);
        final int state = serverPlayer.containerMenu.incrementStateId();
        final ItemStack cursor = CraftItemStack.asNMSCopy(player.getItemOnCursor());
        final ServerPlayerConnection playerConnection = getPlayerConnection(serverPlayer);

        playerConnection.send(new ClientboundContainerSetContentPacket(containerId, state, nmsItems, cursor));
    }

    @Override
    public void clearCursor(final @NonNull Player player) {
        final ServerPlayer serverPlayer = getServerPlayer(player);
        final int state = serverPlayer.containerMenu.incrementStateId();

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
    private int getContainerId(final net.minecraft.world.entity.player.@NonNull Player nmsPlayer) {
        return nmsPlayer.containerMenu.containerId;
    }

    /**
     * Gets the player connection for the specified player
     *
     * @param serverPlayer the player to get the player connection from
     * @return the player connection
     * @since 3.0.0
     */
    @Contract(pure = true)
    private @NonNull ServerPlayerConnection getPlayerConnection(final @NonNull ServerPlayer serverPlayer) {
        return serverPlayer.connection;
    }

    /**
     * Gets the server player associated to this player
     *
     * @param player the player to get the server player from
     * @return the server player
     * @since 3.0.0
     */
    @Contract(pure = true)
    private @NonNull ServerPlayer getServerPlayer(final @NonNull Player player) {
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
        private final @NonNull Player player;
        /**
         * Field for accessing the result inventory field
         */
        private final @NonNull Field resultContainerField;
        /**
         * The internal bukkit entity for this container cartography table
         */
        private @Nullable CraftInventoryView bukkitEntity;

        public ContainerCartographyTableImpl(final @NonNull ServerPlayer serverPlayer,
                                             final org.bukkit.inventory.@Nullable ItemStack[] items,
                                             final @NonNull Component title) {
            super(serverPlayer.nextContainerCounter(), serverPlayer.getInventory());

            this.player = serverPlayer.getBukkitEntity();

            setTitle(title);

            try {
                //noinspection JavaReflectionMemberAccess
                this.resultContainerField = CartographyTableMenu.class.getDeclaredField("u"); //resultContainer
                this.resultContainerField.setAccessible(true);
            } catch (final NoSuchFieldException exception) {
                throw new RuntimeException(exception);
            }

            container.setItem(0, CraftItemStack.asNMSCopy(items[0]));
            container.setItem(1, CraftItemStack.asNMSCopy(items[1]));

            getResultInventory().setItem(0, CraftItemStack.asNMSCopy(items[2]));
        }

        @Override
        public @NonNull CraftInventoryView getBukkitView() {
            if (bukkitEntity == null) {
                final CraftInventory inventory = new CraftInventoryCartography(super.container, getResultInventory()) {
                    @Contract(pure = true)
                    @Override
                    public @NonNull InventoryHolder getHolder() {
                        return inventoryHolder;
                    }
                };

                bukkitEntity = new CraftInventoryView(player, inventory, this);
            }

            return bukkitEntity;
        }

        @Contract(pure = true, value = "_ -> true")
        @Override
        public boolean stillValid(final net.minecraft.world.entity.player.@Nullable Player nmsPlayer) {
            return true;
        }

        @Override
        public void slotsChanged(final @NonNull Container container) {
        }

        @Override
        public void removed(final net.minecraft.world.entity.player.@NonNull Player nmsPlayer) {
        }

        @Contract(pure = true)
        private @NonNull Container getResultInventory() {
            try {
                return (Container) resultContainerField.get(this);
            } catch (final IllegalAccessException exception) {
                throw new RuntimeException(exception);
            }
        }

    }

}
