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

    public GrindstoneInventoryImpl(@NonNull InventoryHolder inventoryHolder) {
        super(inventoryHolder);
    }

    @Override
    public Inventory openInventory(@NonNull Player player, net.kyori.adventure.text.@NonNull Component title,
                                   org.bukkit.inventory.@Nullable ItemStack[] items) {
        int itemAmount = items.length;

        if (itemAmount != 3) {
            throw new IllegalArgumentException(
                "The amount of items for a grindstone should be 3, but is '" + itemAmount + "'"
            );
        }

        ServerPlayer serverPlayer = getServerPlayer(player);

        //ignore deprecation: superseding method is only available on Paper
        //noinspection deprecation
        CraftEventFactory.handleInventoryCloseEvent(serverPlayer);

        serverPlayer.containerMenu = serverPlayer.inventoryMenu;

        Component message = PaperAdventure.asVanilla(title);
        ContainerGrindstoneImpl containerGrindstone = new ContainerGrindstoneImpl(serverPlayer, message);

        Inventory inventory = containerGrindstone.getBukkitView().getTopInventory();

        inventory.setItem(0, items[0]);
        inventory.setItem(1, items[1]);
        inventory.setItem(2, items[2]);

        int containerId = containerGrindstone.getContainerId();

        serverPlayer.connection.send(new ClientboundOpenScreenPacket(containerId, MenuType.GRINDSTONE, message));
        serverPlayer.containerMenu = containerGrindstone;
        serverPlayer.initMenu(containerGrindstone);

        return inventory;
    }

    @Override
    public void sendItems(@NonNull Player player, org.bukkit.inventory.@Nullable ItemStack[] items,
                          org.bukkit.inventory.@NonNull ItemStack cursor) {

        NonNullList<ItemStack> nmsItems = CustomInventoryUtil.convertToNMSItems(items);
        ServerPlayer serverPlayer = getServerPlayer(player);
        int containerId = getContainerId(serverPlayer);
        int state = serverPlayer.containerMenu.incrementStateId();
        ItemStack nmsCursor = CraftItemStack.asNMSCopy(cursor);
        ServerPlayerConnection playerConnection = getPlayerConnection(serverPlayer);

        playerConnection.send(new ClientboundContainerSetContentPacket(containerId, state, nmsItems, nmsCursor));
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
     * A custom container grindstone
     *
     * @since 3.0.0
     */
    private static class ContainerGrindstoneImpl extends GrindstoneMenu {

        /**
         * Creates a new grindstone container
         *
         * @param serverPlayer the player for whom this container should be opened
         * @param title the title of the gui
         * @since 3.0.0
         */
        public ContainerGrindstoneImpl(@NonNull ServerPlayer serverPlayer, @NonNull Component title) {
            super(serverPlayer.nextContainerCounter(), serverPlayer.getInventory());

            setTitle(title);

            Slot firstSlot = this.slots.get(0);
            Slot secondSlot = this.slots.get(1);
            Slot thirdSlot = this.slots.get(2);

            this.slots.set(0, new Slot(firstSlot.container, firstSlot.index, firstSlot.x, firstSlot.y) {
            });

            this.slots.set(1, new Slot(secondSlot.container, secondSlot.index, secondSlot.x, secondSlot.y) {
            });

            this.slots.set(2, new Slot(thirdSlot.container, thirdSlot.index, thirdSlot.x, thirdSlot.y) {

                @Override
                public void onTake(net.minecraft.world.entity.player.@NonNull Player player, @NonNull ItemStack stack) {}
            });
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

        public int getContainerId() {
            return this.containerId;
        }
    }
}
