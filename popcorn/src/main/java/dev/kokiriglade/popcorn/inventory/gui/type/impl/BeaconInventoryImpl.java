package dev.kokiriglade.popcorn.inventory.gui.type.impl;

import dev.kokiriglade.popcorn.inventory.gui.type.abstraction.BeaconInventory;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundContainerSetContentPacket;
import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
import net.minecraft.network.protocol.game.ClientboundOpenScreenPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerPlayerConnection;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.BeaconMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.craftbukkit.inventory.CraftInventory;
import org.bukkit.craftbukkit.inventory.CraftInventoryBeacon;
import org.bukkit.craftbukkit.inventory.CraftInventoryView;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryHolder;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.jetbrains.annotations.Contract;

import java.lang.reflect.Field;

/**
 * Internal beacon inventory
 *
 * @since 3.0.0
 */
public class BeaconInventoryImpl extends BeaconInventory {

    public BeaconInventoryImpl(@NonNull InventoryHolder inventoryHolder) {
        super(inventoryHolder);
    }

    @Override
    public void openInventory(@NonNull Player player, org.bukkit.inventory.@Nullable ItemStack item) {
        ServerPlayer serverPlayer = getServerPlayer(player);
        ContainerBeaconImpl containerBeacon = new ContainerBeaconImpl(serverPlayer, item);

        serverPlayer.containerMenu = containerBeacon;

        int id = containerBeacon.containerId;
        Component beacon = Component.literal("Beacon");

        serverPlayer.connection.send(new ClientboundOpenScreenPacket(id, MenuType.BEACON, beacon));

        sendItem(player, item);
    }

    @Override
    public void sendItem(@NonNull Player player, org.bukkit.inventory.@Nullable ItemStack item) {
        NonNullList<ItemStack> items = NonNullList.of(
            ItemStack.EMPTY, //the first item doesn't count for some reason, so send a dummy item
            CraftItemStack.asNMSCopy(item)
        );

        ServerPlayer serverPlayer = getServerPlayer(player);
        int containerId = getContainerId(serverPlayer);
        int state = serverPlayer.containerMenu.incrementStateId();
        ItemStack cursor = CraftItemStack.asNMSCopy(player.getItemOnCursor());
        ServerPlayerConnection playerConnection = getPlayerConnection(serverPlayer);

        playerConnection.send(new ClientboundContainerSetContentPacket(containerId, state, items, cursor));
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
     * A custom container beacon
     *
     * @since 3.0.0
     */
    private class ContainerBeaconImpl extends BeaconMenu {

        /**
         * The player for this beacon container
         */
        @NonNull
        private final Player player;

        /**
         * The internal bukkit entity for this container beacon
         */
        @Nullable
        private CraftInventoryView bukkitEntity;

        /**
         * Field for accessing the beacon field
         */
        @NonNull
        private final Field beaconField;

        public ContainerBeaconImpl(@NonNull ServerPlayer serverPlayer, org.bukkit.inventory.@Nullable ItemStack item) {
            super(serverPlayer.nextContainerCounter(), serverPlayer.getInventory());

            this.player = serverPlayer.getBukkitEntity();
            setTitle(Component.empty());

            try {
                //noinspection JavaReflectionMemberAccess
                this.beaconField = BeaconMenu.class.getDeclaredField("s"); //beacon
                this.beaconField.setAccessible(true);
            } catch (NoSuchFieldException exception) {
                throw new RuntimeException(exception);
            }

            try {
                ItemStack itemStack = CraftItemStack.asNMSCopy(item);

                ((Container) beaconField.get(this)).setItem(0, itemStack);
            } catch (IllegalAccessException exception) {
                throw new RuntimeException(exception);
            }
        }

        @NonNull
        @Override
        public CraftInventoryView getBukkitView() {
            if (bukkitEntity == null) {
                try {
                    CraftInventory inventory = new CraftInventoryBeacon((Container) beaconField.get(this)) {
                        @NonNull
                        @Contract(pure = true)
                        @Override
                        public InventoryHolder getHolder() {
                            return inventoryHolder;
                        }
                    };

                    bukkitEntity = new CraftInventoryView(player, inventory, this);
                } catch (IllegalAccessException exception) {
                    throw new RuntimeException(exception);
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
