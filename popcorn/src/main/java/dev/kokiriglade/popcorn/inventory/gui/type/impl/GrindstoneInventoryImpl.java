package dev.kokiriglade.popcorn.inventory.gui.type.impl;

import dev.kokiriglade.popcorn.inventory.gui.type.abstraction.GrindstoneInventory;
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
import net.minecraft.world.inventory.GrindstoneMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
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
 * Internal grindstone inventory
 *
 * @since 3.0.0
 */
public class GrindstoneInventoryImpl extends GrindstoneInventory {

    /**
     * Create an internal grindstone inventory
     * @param inventoryHolder the {@code InventoryHolder}
     * @since 3.0.0
     */
    public GrindstoneInventoryImpl(final @NonNull InventoryHolder inventoryHolder) {
        super(inventoryHolder);
    }

    @Override
    public Inventory openInventory(final @NonNull Player player, final net.kyori.adventure.text.@NonNull Component title,
                                   final org.bukkit.inventory.@Nullable ItemStack[] items) {
        final int itemAmount = items.length;

        if (itemAmount != 3) {
            throw new IllegalArgumentException(
                "The amount of items for a grindstone should be 3, but is '" + itemAmount + "'"
            );
        }

        final ServerPlayer serverPlayer = getServerPlayer(player);

        //ignore deprecation: superseding method is only available on Paper
        //noinspection deprecation
        CraftEventFactory.handleInventoryCloseEvent(serverPlayer);

        serverPlayer.containerMenu = serverPlayer.inventoryMenu;

        final Component message = PaperAdventure.asVanilla(title);
        final ContainerGrindstoneImpl containerGrindstone = new ContainerGrindstoneImpl(serverPlayer, message);

        final Inventory inventory = containerGrindstone.getBukkitView().getTopInventory();

        inventory.setItem(0, items[0]);
        inventory.setItem(1, items[1]);
        inventory.setItem(2, items[2]);

        final int containerId = containerGrindstone.getContainerId();

        serverPlayer.connection.send(new ClientboundOpenScreenPacket(containerId, MenuType.GRINDSTONE, message));
        serverPlayer.containerMenu = containerGrindstone;
        serverPlayer.initMenu(containerGrindstone);

        return inventory;
    }

    @Override
    public void sendItems(final @NonNull Player player, final org.bukkit.inventory.@Nullable ItemStack[] items,
                          final org.bukkit.inventory.@NonNull ItemStack cursor) {

        final NonNullList<ItemStack> nmsItems = CustomInventoryUtil.convertToNMSItems(items);
        final ServerPlayer serverPlayer = getServerPlayer(player);
        final int containerId = getContainerId(serverPlayer);
        final int state = serverPlayer.containerMenu.incrementStateId();
        final ItemStack nmsCursor = CraftItemStack.asNMSCopy(cursor);
        final ServerPlayerConnection playerConnection = getPlayerConnection(serverPlayer);

        playerConnection.send(new ClientboundContainerSetContentPacket(containerId, state, nmsItems, nmsCursor));
    }

    @Override
    public void clearCursor(final @NonNull Player player) {
        final ServerPlayer serverPlayer = getServerPlayer(player);
        final int state = serverPlayer.containerMenu.incrementStateId();

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
     * A custom container grindstone
     *
     * @since 3.0.0
     */
    private static class ContainerGrindstoneImpl extends GrindstoneMenu {

        /**
         * Creates a new grindstone container
         *
         * @param serverPlayer the player for whom this container should be opened
         * @param title        the title of the gui
         * @since 3.0.0
         */
        public ContainerGrindstoneImpl(final @NonNull ServerPlayer serverPlayer, final @NonNull Component title) {
            super(serverPlayer.nextContainerCounter(), serverPlayer.getInventory());

            setTitle(title);

            final Slot firstSlot = this.slots.get(0);
            final Slot secondSlot = this.slots.get(1);
            final Slot thirdSlot = this.slots.get(2);

            this.slots.set(0, new Slot(firstSlot.container, firstSlot.index, firstSlot.x, firstSlot.y) {
            });

            this.slots.set(1, new Slot(secondSlot.container, secondSlot.index, secondSlot.x, secondSlot.y) {
            });

            this.slots.set(2, new Slot(thirdSlot.container, thirdSlot.index, thirdSlot.x, thirdSlot.y) {

                @Override
                public void onTake(final net.minecraft.world.entity.player.@NonNull Player player, final @NonNull ItemStack stack) {
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

        public int getContainerId() {
            return this.containerId;
        }

    }

}
