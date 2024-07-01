package dev.kokiriglade.popcorn.inventory.gui.type.impl;

import dev.kokiriglade.popcorn.inventory.gui.type.abstraction.AnvilInventory;
import dev.kokiriglade.popcorn.inventory.gui.type.impl.util.CustomInventoryUtil;
import io.papermc.paper.adventure.PaperAdventure;
import net.kyori.adventure.text.Component;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.network.protocol.game.ClientboundContainerSetContentPacket;
import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
import net.minecraft.network.protocol.game.ClientboundOpenScreenPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerPlayerConnection;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.craftbukkit.event.CraftEventFactory;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.jetbrains.annotations.Contract;

/**
 * Internal anvil inventory
 *
 * @since 3.0.0
 */
public class AnvilInventoryImpl extends AnvilInventory {

    /**
     * Create an internal anvil inventory
     * @param inventoryHolder the {@code InventoryHolder}
     * @since 3.0.0
     */
    public AnvilInventoryImpl(final @NonNull InventoryHolder inventoryHolder) {
        super(inventoryHolder);
    }

    @Override
    public Inventory openInventory(final @NonNull Player player, final @NonNull Component title, final org.bukkit.inventory.@Nullable ItemStack[] items) {
        final int itemAmount = items.length;

        if (itemAmount != 3) {
            throw new IllegalArgumentException(
                "The amount of items for an anvil should be 3, but is '" + itemAmount + "'"
            );
        }

        final ServerPlayer serverPlayer = getServerPlayer(player);

        CraftEventFactory.handleInventoryCloseEvent(serverPlayer, InventoryCloseEvent.Reason.OPEN_NEW);

        serverPlayer.containerMenu = serverPlayer.inventoryMenu;

        final net.minecraft.network.chat.Component message = PaperAdventure.asVanilla(title);

        final ContainerAnvilImpl containerAnvil = new ContainerAnvilImpl(serverPlayer, message);

        final Inventory inventory = containerAnvil.getBukkitView().getTopInventory();

        inventory.setItem(0, items[0]);
        inventory.setItem(1, items[1]);
        inventory.setItem(2, items[2]);

        final int containerId = containerAnvil.getContainerId();

        serverPlayer.connection.send(new ClientboundOpenScreenPacket(containerId, MenuType.ANVIL, message));
        serverPlayer.containerMenu = containerAnvil;
        serverPlayer.initMenu(containerAnvil);

        return inventory;
    }

    @Override
    public void sendItems(final @NonNull Player player, final org.bukkit.inventory.@Nullable ItemStack[] items) {
        final NonNullList<ItemStack> nmsItems = CustomInventoryUtil.convertToNMSItems(items);
        final ServerPlayer serverPlayer = getServerPlayer(player);
        final int containerId = serverPlayer.containerMenu.containerId;
        final int state = serverPlayer.containerMenu.incrementStateId();
        final ItemStack cursor = CraftItemStack.asNMSCopy(player.getItemOnCursor());
        final ServerPlayerConnection playerConnection = serverPlayer.connection;

        playerConnection.send(new ClientboundContainerSetContentPacket(containerId, state, nmsItems, cursor));
    }

    @Override
    public void sendFirstItem(final @NonNull Player player, final org.bukkit.inventory.@Nullable ItemStack item) {
        final ServerPlayer serverPlayer = getServerPlayer(player);
        final int containerId = serverPlayer.containerMenu.containerId;
        final ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
        final int state = serverPlayer.containerMenu.incrementStateId();

        serverPlayer.connection.send(new ClientboundContainerSetSlotPacket(containerId, state, 0, nmsItem));
    }

    @Override
    public void sendSecondItem(final @NonNull Player player, final org.bukkit.inventory.@Nullable ItemStack item) {
        final ServerPlayer serverPlayer = getServerPlayer(player);
        final int containerId = serverPlayer.containerMenu.containerId;
        final ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
        final int state = serverPlayer.containerMenu.incrementStateId();

        serverPlayer.connection.send(new ClientboundContainerSetSlotPacket(containerId, state, 1, nmsItem));
    }

    @Override
    public void sendResultItem(final @NonNull Player player, final org.bukkit.inventory.@Nullable ItemStack item) {
        sendResultItem(player, CraftItemStack.asNMSCopy(item));
    }

    /**
     * Sends the result item to the specified player with the given item
     *
     * @param player the player to send the result item to
     * @param item   the result item
     * @since 3.0.0
     */
    private void sendResultItem(final @NonNull Player player, final @NonNull ItemStack item) {
        final ServerPlayer serverPlayer = getServerPlayer(player);
        final int containerId = serverPlayer.containerMenu.containerId;
        final int state = serverPlayer.containerMenu.incrementStateId();

        serverPlayer.connection.send(new ClientboundContainerSetSlotPacket(containerId, state, 2, item));
    }

    @Override
    public void clearResultItem(final @NonNull Player player) {
        sendResultItem(player, ItemStack.EMPTY);
    }

    @Override
    public void setCursor(final @NonNull Player player, final org.bukkit.inventory.@NonNull ItemStack item) {
        setCursor(player, CraftItemStack.asNMSCopy(item));
    }

    /**
     * Sets the cursor of the given player
     *
     * @param player the player to set the cursor
     * @param item   the item to set the cursor to
     * @since 3.0.0
     */
    private void setCursor(final @NonNull Player player, final @NonNull ItemStack item) {
        final ServerPlayer serverPlayer = getServerPlayer(player);
        final int state = serverPlayer.containerMenu.incrementStateId();

        serverPlayer.connection.send(new ClientboundContainerSetSlotPacket(-1, state, -1, item));
    }

    @Override
    public void clearCursor(final @NonNull Player player) {
        final ServerPlayer serverPlayer = getServerPlayer(player);
        final int state = serverPlayer.containerMenu.incrementStateId();

        serverPlayer.connection.send(new ClientboundContainerSetSlotPacket(-1, state, -1, ItemStack.EMPTY));
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
     * A custom container anvil for responding to item renaming
     *
     * @since 3.0.0
     */
    private class ContainerAnvilImpl extends AnvilMenu {

        /**
         * Creates a new custom anvil container for the specified player
         *
         * @param serverPlayer the player for whom this anvil container is
         * @param title        the title of the inventory
         * @since 3.0.0
         */
        public ContainerAnvilImpl(final @NonNull ServerPlayer serverPlayer, final net.minecraft.network.chat.Component title) {
            super(serverPlayer.nextContainerCounter(), serverPlayer.getInventory(),
                ContainerLevelAccess.create(serverPlayer.getCommandSenderWorld(), new BlockPos(0, 0, 0)));

            this.checkReachable = false;
            this.cost.set(AnvilInventoryImpl.super.cost);

            setTitle(title);

            final Slot originalSlot = this.slots.get(2);

            this.slots.set(2, new Slot(originalSlot.container, originalSlot.index, originalSlot.x, originalSlot.y) {
                @Override
                public void onTake(final net.minecraft.world.entity.player.@NonNull Player player, final @NonNull ItemStack stack) {
                    originalSlot.onTake(player, stack);
                }
            });
        }

        @Override
        public boolean setItemName(@Nullable String name) {
            name = name == null ? "" : name;

            /* Only update if the name is actually different. This may be called even if the name is not different,
               particularly when putting an item in the first slot. */
            if (!name.equals(AnvilInventoryImpl.super.observableText.get())) {
                AnvilInventoryImpl.super.observableText.set(name);
            }

            //the client predicts the output result, so we broadcast the state again to override it
            broadcastFullState();
            return true; //no idea what this is for
        }

        @Override
        public void createResult() {
        }

        @Override
        public void removed(final net.minecraft.world.entity.player.@NonNull Player nmsPlayer) {
        }

        @Override
        protected void clearContainer(final net.minecraft.world.entity.player.@NonNull Player player,
                                      final @NonNull Container inventory) {
        }

        @Override
        protected void onTake(final net.minecraft.world.entity.player.@NonNull Player player, final @NonNull ItemStack stack) {
        }

        public int getContainerId() {
            return this.containerId;
        }

    }

}
