package dev.kokiriglade.popcorn.inventory.gui.type.util;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.Contract;

/**
 * An inventory-based {@code InventoryHolder}.
 *
 * @since 3.0.0
 */
public interface InventoryBased extends InventoryHolder {

    /**
     * Creates a new inventory of the type of the implementing class.
     *
     * @return the new inventory
     * @since 3.0.0
     */
    @Contract(pure = true)
    @NonNull Inventory createInventory();

}
