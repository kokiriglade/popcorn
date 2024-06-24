package dev.kokiriglade.popcorn.inventory.gui.type.abstraction;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * A smithing table inventory
 *
 * @since 3.0.0
 */
public abstract class SmithingTableInventory {

    /**
     * The inventory holder
     */
    @NonNull
    protected InventoryHolder inventoryHolder;

    /**
     * Creates a new smithing table inventory for the specified inventory holder
     *
     * @param inventoryHolder the inventory holder
     * @since 3.0.0
     */
    public SmithingTableInventory(@NonNull InventoryHolder inventoryHolder) {
        this.inventoryHolder = inventoryHolder;
    }

    /**
     * Opens the inventory for the specified player
     *
     * @param player the player to open the inventory for
     * @param title the title of the inventory
     * @param items the top items
     * @since 3.0.0
     */
    public abstract void openInventory(@NonNull Player player, @NonNull Component title, @Nullable ItemStack[] items);

    /**
     * Sends the top items to the inventory for the specified player.
     *
     * @param player the player for which to open the smithing table
     * @param items the items to send
     * @param cursor the cursor item
     * @since 3.0.0
     */
    public abstract void sendItems(@NonNull Player player, @Nullable ItemStack[] items, @NonNull ItemStack cursor);

    /**
     * Sends the result item to the specified player
     *
     * @param player the player to send the item to
     * @param item the item to send
     * @since 3.0.0
     */
    public abstract void sendResultItem(@NonNull Player player, @Nullable ItemStack item);

    /**
     * Sends the first item to the specified player
     *
     * @param player the player to send the item to
     * @param item the item to send
     * @since 3.0.0
     */
    public abstract void sendFirstItem(@NonNull Player player, @Nullable ItemStack item);

    /**
     * Sends the second item to the specified player
     *
     * @param player the player to send the item to
     * @param item the item to send
     * @since 3.0.0
     */
    public abstract void sendSecondItem(@NonNull Player player, @Nullable ItemStack item);

    /**
     * Sets the cursor of the given player
     *
     * @param player the player to set the cursor
     * @param item the item to set the cursor to
     * @since 3.0.0
     */
    public abstract void setCursor(@NonNull Player player, @NonNull ItemStack item);

    /**
     * Clears the cursor of the specified player
     *
     * @param player the player to clear the cursor of
     * @since 3.0.0
     */
    public abstract void clearCursor(@NonNull Player player);

    /**
     * Clears the result item for the specified player
     *
     * @param player the player to clear the result item of
     * @since 3.0.0
     */
    public abstract void clearResultItem(@NonNull Player player);
}
