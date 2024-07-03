package dev.kokiriglade.popcorn.inventory.gui.type.impl;

import dev.kokiriglade.popcorn.inventory.gui.type.abstraction.SmithingTableInventory;
import dev.kokiriglade.popcorn.inventory.gui.type.impl.util.CustomInventoryUtil;
import io.papermc.paper.adventure.PaperAdventure;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundContainerSetContentPacket;
import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
import net.minecraft.network.protocol.game.ClientboundOpenScreenPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerPlayerConnection;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.SmithingMenu;
import net.minecraft.world.item.ItemStack;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.craftbukkit.event.CraftEventFactory;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.jetbrains.annotations.Contract;

/**
 * Internal smithing table inventory
 *
 * @since 3.0.0
 */
public class SmithingTableInventoryImpl extends SmithingTableInventory {

    /**
     * Create a smithing table inventory
     *
     * @param inventoryHolder the {@code InventoryHolder}
     * @since 3.0.0
     */
    public SmithingTableInventoryImpl(final @NonNull InventoryHolder inventoryHolder) {
        super(inventoryHolder);
    }

    @Override
    public Inventory openInventory(final @NonNull Player player, final net.kyori.adventure.text.@NonNull Component title,
                                   final org.bukkit.inventory.@Nullable ItemStack[] items) {
        final int itemAmount = items.length;

        if (itemAmount != 4) {
            throw new IllegalArgumentException(
                "The amount of items for a smithing table should be 4, but is '" + itemAmount + "'"
            );
        }

        final ServerPlayer serverPlayer = getServerPlayer(player);

        //ignore deprecation: superseding method is only available on Paper
        //noinspection deprecation
        CraftEventFactory.handleInventoryCloseEvent(serverPlayer);

        serverPlayer.containerMenu = serverPlayer.inventoryMenu;

        final Component message = PaperAdventure.asVanilla(title);
        final ContainerSmithingTableImpl containerSmithingTable = new ContainerSmithingTableImpl(serverPlayer, message);

        final Inventory inventory = containerSmithingTable.getBukkitView().getTopInventory();

        inventory.setItem(0, items[0]);
        inventory.setItem(1, items[1]);
        inventory.setItem(2, items[2]);
        inventory.setItem(3, items[3]);

        final int containerId = containerSmithingTable.getContainerId();

        serverPlayer.connection.send(new ClientboundOpenScreenPacket(containerId, MenuType.SMITHING, message));
        serverPlayer.containerMenu = containerSmithingTable;
        serverPlayer.initMenu(containerSmithingTable);

        return inventory;
    }

    @Override
    public void sendItems(final @NonNull Player player, final org.bukkit.inventory.@Nullable ItemStack[] items,
                          final org.bukkit.inventory.@Nullable ItemStack cursor) {
        final NonNullList<ItemStack> nmsItems = CustomInventoryUtil.convertToNMSItems(items);
        final ServerPlayer serverPlayer = getServerPlayer(player);
        final int containerId = getContainerId(serverPlayer);
        final int state = serverPlayer.containerMenu.incrementStateId();
        final ItemStack nmsCursor = CraftItemStack.asNMSCopy(cursor);
        final ServerPlayerConnection playerConnection = getPlayerConnection(serverPlayer);

        playerConnection.send(new ClientboundContainerSetContentPacket(containerId, state, nmsItems, nmsCursor));
    }

    @Override
    public void sendFirstItem(final @NonNull Player player, final org.bukkit.inventory.@Nullable ItemStack item) {
        final ServerPlayer serverPlayer = getServerPlayer(player);
        final ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
        final int containerId = getContainerId(serverPlayer);
        final int state = serverPlayer.containerMenu.incrementStateId();

        getPlayerConnection(serverPlayer).send(new ClientboundContainerSetSlotPacket(containerId, state, 0, nmsItem));
    }

    @Override
    public void sendSecondItem(final @NonNull Player player, final org.bukkit.inventory.@Nullable ItemStack item) {
        final ServerPlayer serverPlayer = getServerPlayer(player);
        final ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
        final int containerId = getContainerId(serverPlayer);
        final int state = serverPlayer.containerMenu.incrementStateId();

        getPlayerConnection(serverPlayer).send(new ClientboundContainerSetSlotPacket(containerId, state, 1, nmsItem));
    }

    @Override
    public void sendResultItem(final @NonNull Player player, final org.bukkit.inventory.@Nullable ItemStack item) {
        sendResultItem(player, CraftItemStack.asNMSCopy(item));
    }

    @Override
    public void clearResultItem(final @NonNull Player player) {
        sendResultItem(player, ItemStack.EMPTY);
    }

    @Override
    public void setCursor(final @NonNull Player player, final org.bukkit.inventory.@NonNull ItemStack item) {
        setCursor(player, CraftItemStack.asNMSCopy(item));
    }

    @Override
    public void clearCursor(final @NonNull Player player) {
        final ServerPlayer serverPlayer = getServerPlayer(player);
        final int state = serverPlayer.containerMenu.incrementStateId();

        getPlayerConnection(serverPlayer).send(new ClientboundContainerSetSlotPacket(-1, state, -1, ItemStack.EMPTY));
    }

    /**
     * Sets the cursor of the given player
     *
     * @param player the player to set the cursor
     * @param item   the item to set the cursor to
     * @since 3.0.0
     * @deprecated no longer used internally
     */
    @Deprecated
    private void setCursor(final @NonNull Player player, final @NonNull ItemStack item) {
        final ServerPlayer serverPlayer = getServerPlayer(player);
        final int state = serverPlayer.containerMenu.incrementStateId();

        getPlayerConnection(serverPlayer).send(new ClientboundContainerSetSlotPacket(-1, state, -1, item));
    }

    /**
     * Sends the result item to the specified player with the given item
     *
     * @param player the player to send the result item to
     * @param item   the result item
     * @since 3.0.0
     * @deprecated no longer used internally
     */
    @Deprecated
    private void sendResultItem(final @NonNull Player player, final @NonNull ItemStack item) {
        final ServerPlayer serverPlayer = getServerPlayer(player);
        final int containerId = getContainerId(serverPlayer);
        final int state = serverPlayer.containerMenu.incrementStateId();

        getPlayerConnection(serverPlayer).send(new ClientboundContainerSetSlotPacket(containerId, state, 2, item));
    }

    /**
     * Gets the container id for the inventory view the player currently has open
     *
     * @param nmsPlayer the player to get the container id for
     * @return the container id
     * @since 3.0.0
     * @deprecated no longer used internally
     */
    @Contract(pure = true)
    @Deprecated
    private int getContainerId(final net.minecraft.world.entity.player.@NonNull Player nmsPlayer) {
        return nmsPlayer.containerMenu.containerId;
    }

    /**
     * Gets the player connection for the specified player
     *
     * @param serverPlayer the player to get the player connection from
     * @return the player connection
     * @since 3.0.0
     * @deprecated no longer used internally
     */
    @Contract(pure = true)
    @Deprecated
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
     * A custom container smithing table
     *
     * @since 3.0.0
     */
    private static class ContainerSmithingTableImpl extends SmithingMenu {

        /**
         * Creates a new custom smithing table container for the specified player
         *
         * @param serverPlayer the player for whom this anvil container is
         * @param title        the title of the inventory
         * @since 3.0.0
         */
        public ContainerSmithingTableImpl(final @NonNull ServerPlayer serverPlayer, final @NonNull Component title) {
            super(serverPlayer.nextContainerCounter(), serverPlayer.getInventory(),
                ContainerLevelAccess.create(serverPlayer.getCommandSenderWorld(), new BlockPos(0, 0, 0)));

            setTitle(title);

            this.checkReachable = false;

            final Slot slotOne = this.slots.get(0);
            final Slot slotTwo = this.slots.get(1);
            final Slot slotThree = this.slots.get(2);
            final Slot slotFour = this.slots.get(3);

            this.slots.set(0, new Slot(slotOne.container, slotOne.index, slotOne.x, slotOne.y) {
                @Override
                public boolean mayPlace(final @NonNull ItemStack stack) {
                    return true;
                }

                @Override
                public void onTake(final net.minecraft.world.entity.player.@NonNull Player player, final @NonNull ItemStack stack) {
                    slotOne.onTake(player, stack);
                }
            });

            this.slots.set(1, new Slot(slotTwo.container, slotTwo.index, slotTwo.x, slotTwo.y) {
                @Override
                public boolean mayPlace(final @NonNull ItemStack stack) {
                    return true;
                }

                @Override
                public void onTake(final net.minecraft.world.entity.player.@NonNull Player player, final @NonNull ItemStack stack) {
                    slotTwo.onTake(player, stack);
                }
            });

            this.slots.set(2, new Slot(slotThree.container, slotThree.index, slotThree.x, slotThree.y) {
                @Override
                public boolean mayPlace(final @NonNull ItemStack stack) {
                    return true;
                }

                @Override
                public void onTake(final net.minecraft.world.entity.player.@NonNull Player player, final @NonNull ItemStack stack) {
                    slotThree.onTake(player, stack);
                }
            });

            this.slots.set(3, new Slot(slotFour.container, slotFour.index, slotFour.x, slotFour.y) {
                @Override
                public boolean mayPlace(final @NonNull ItemStack stack) {
                    return true;
                }

                @Override
                public void onTake(final net.minecraft.world.entity.player.@NonNull Player player, final @NonNull ItemStack stack) {
                    slotFour.onTake(player, stack);
                }
            });
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

        @Override
        public void createResult() {
        }

        @Override
        protected void onTake(final net.minecraft.world.entity.player.@NonNull Player player, final @NonNull ItemStack stack) {
        }

        @Override
        protected boolean mayPickup(final net.minecraft.world.entity.player.@NonNull Player player, final boolean present) {
            return true;
        }

        public int getContainerId() {
            return this.containerId;
        }

    }

}
