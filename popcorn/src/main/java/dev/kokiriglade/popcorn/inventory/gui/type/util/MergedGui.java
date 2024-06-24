package dev.kokiriglade.popcorn.inventory.gui.type.util;

import dev.kokiriglade.popcorn.inventory.gui.GuiItem;
import dev.kokiriglade.popcorn.inventory.gui.InventoryComponent;
import dev.kokiriglade.popcorn.inventory.pane.Pane;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.Contract;

import java.util.Collection;
import java.util.List;

/**
 * Represents a chest-like gui in which the top and bottom inventories are merged together and only exist of one
 * inventory component.
 *
 * @since 3.0.0
 */
public interface MergedGui {

    /**
     * Adds a pane to this gui
     *
     * @param pane the pane to add
     * @since 3.0.0
     */
    void addPane(@NonNull Pane pane);

    /**
     * Gets all the panes in this gui. This includes child panes from other panes.
     *
     * @return all panes
     * @since 3.0.0
     */
    @NonNull
    @Contract(pure = true)
    List<Pane> getPanes();

    /**
     * Gets all the items in all underlying panes
     *
     * @return all items
     * @since 3.0.0
     */
    @NonNull
    @Contract(pure = true)
    Collection<GuiItem> getItems();

    /**
     * Gets the inventory component for this gui
     *
     * @return the inventory component
     * @since 3.0.0
     */
    @NonNull
    @Contract(pure = true)
    InventoryComponent getInventoryComponent();

}
