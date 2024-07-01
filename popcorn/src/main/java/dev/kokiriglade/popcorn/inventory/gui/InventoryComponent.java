package dev.kokiriglade.popcorn.inventory.gui;

import dev.kokiriglade.popcorn.inventory.gui.type.util.Gui;
import dev.kokiriglade.popcorn.inventory.pane.Pane;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.jetbrains.annotations.Contract;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Represents a component within an inventory that can hold items. This is always in the shape of a rectangular grid.
 *
 * @since 3.0.0
 */
@SuppressWarnings({"unused", "BooleanMethodIsAlwaysInverted", "SameParameterValue"})
public class InventoryComponent {

    /**
     * A set of all panes in this inventory. This is guaranteed to be sorted in order of the pane's priorities, from the
     * lowest priority to the highest priority. The order of panes with the same priority is unspecified.
     */
    protected final @NonNull List<@NonNull Pane> panes = new ArrayList<>();

    /**
     * The items this inventory component has, stored in row-major order. Slots that are empty are represented as null.
     */
    private final @Nullable ItemStack @NonNull [] @NonNull [] items;

    /**
     * The length and height of this inventory component
     */
    private final int length, height;

    /**
     * Creates a new inventory component with the specified length and width. If either the length or the width is less
     * than zero, an {@link IllegalArgumentException} will be thrown.
     *
     * @param length the length of the component
     * @param height the height of the component
     * @since 3.0.0
     */
    public InventoryComponent(final int length, final int height) {
        if (length < 0 || height < 0) {
            throw new IllegalArgumentException("Sizes must be greater or equal to zero");
        }

        this.length = length;
        this.height = height;

        this.items = new ItemStack[length][height];
    }

    /**
     * Adds a pane to the current collection of panes.
     *
     * @param pane the pane to add
     * @since 3.0.0
     */
    public void addPane(final @NonNull Pane pane) {
        final int size = getPanes().size();

        if (size == 0) {
            getPanes().add(pane);

            return;
        }

        final Pane.Priority priority = pane.getPriority();

        int left = 0;
        int right = size - 1;

        while (left <= right) {
            final int middle = (left + right) / 2;

            final Pane.Priority middlePriority = getPane(middle).getPriority();

            if (middlePriority == priority) {
                getPanes().add(middle, pane);

                return;
            }

            if (middlePriority.isLessThan(priority)) {
                left = middle + 1;
            } else if (middlePriority.isGreaterThan(priority)) {
                right = middle - 1;
            }
        }

        getPanes().add(right + 1, pane);
    }

    /**
     * This will make each pane in this component render their items in this inventory component. The panes are
     * displayed according to their priority, with the lowest priority rendering first and the highest priority (note:
     * highest priority, not {@link Pane.Priority#HIGHEST} priority) rendering last. The items displayed in this
     * inventory component will be put into the specified inventory. The slots will start at the given offset up to this
     * component's size + the offset specified.
     *
     * @param inventory the inventory to place the items in
     * @param offset    the offset from which to start counting the slots
     * @see #display(PlayerInventory, int)
     * @since 3.0.0
     */
    public void display(final @NonNull Inventory inventory, final int offset) {
        display();

        placeItems(inventory, offset);
    }

    /**
     * This will make each pane in this component render their items in this inventory component. The panes are
     * displayed according to their priority, with the lowest priority rendering first and the highest priority (note:
     * highest priority, not {@link Pane.Priority#HIGHEST} priority) rendering last. The items displayed in this
     * inventory component will be put into the inventory found in {@link dev.kokiriglade.popcorn.inventory.gui.type.util.InventoryBased#getInventory()}. The slots will
     * be placed from the top-right to the bottom-left, continuing from left-to-right, top-to-bottom plus the specified
     * offset. This ordering is different from the normal ordering of the indices of a {@link PlayerInventory}. See for
     * the normal ordering of a {@link PlayerInventory}'s slots its documentation.
     *
     * @param inventory the inventory to place the items in
     * @param offset    the offset from which to start counting the slots
     * @see #display(Inventory, int)
     * @since 3.0.0
     */
    public void display(final @NonNull PlayerInventory inventory, final int offset) {
        display();

        placeItems(inventory, offset);
    }

    /**
     * This places the items currently existing in this inventory component into the specified player inventory. The
     * slots will be placed from the top-right to the bottom-left, continuing from left-to-right, top-to-bottom plus the
     * specified offset. This ordering is different from the normal ordering of the indices of a
     * {@link PlayerInventory}. See for the normal ordering of a {@link PlayerInventory}'s slots its documentation. In
     * contrast to {@link #display(PlayerInventory, int)} this does not render the panes of this component.
     *
     * @param inventory the inventory to place the items in
     * @param offset    the offset from which to start counting the slots
     * @see #placeItems(Inventory, int)
     * @since 3.0.0
     */
    public void placeItems(final @NonNull PlayerInventory inventory, final int offset) {
        for (int x = 0; x < getLength(); x++) {
            for (int y = 0; y < getHeight(); y++) {
                final int slot;

                if (y == getHeight() - 1) {
                    slot = x + offset;
                } else {
                    slot = (y + 1) * getLength() + x + offset;
                }

                inventory.setItem(slot, getItem(x, y));
            }
        }
    }

    /**
     * This places the items currently existing in this inventory component into the specified inventory. The slots will
     * start at the given offset up to this component's size + the offset specified. In contrast to
     * {@link #display(Inventory, int)} this does not render the panes of this component.
     *
     * @param inventory the inventory to place the items in
     * @param offset    the offset from which to start counting the slots
     * @see #placeItems(PlayerInventory, int)
     * @since 3.0.0
     */
    public void placeItems(final @NonNull Inventory inventory, final int offset) {
        for (int x = 0; x < getLength(); x++) {
            for (int y = 0; y < getHeight(); y++) {
                inventory.setItem(y * getLength() + x + offset, getItem(x, y));
            }
        }
    }

    /**
     * Delegates the handling of the specified click event to the panes of this component. This will call
     * {@link Pane#click(Gui, InventoryComponent, InventoryClickEvent, int, int, int, int, int)} on each pane until the
     * right item has been found.
     *
     * @param gui   the gui this inventory component belongs to
     * @param event the event to delegate
     * @param slot  the slot that was clicked
     * @since 3.0.0
     */
    public void click(final @NonNull Gui gui, final @NonNull InventoryClickEvent event, final int slot) {
        final List<@NonNull Pane> panes = new ArrayList<>(getPanes());

        //loop panes in reverse, because the highest priority pane (last in list) is most likely to have the right item
        for (int i = panes.size() - 1; i >= 0; i--) {
            if (panes.get(i).click(
                gui, this, event, slot, 0, 0, getLength(), getHeight()
            )) {
                break;
            }
        }
    }

    /**
     * Creates a deep copy of this inventory component. This means that all internal items will be cloned and all panes
     * will be copied as per their own {@link ItemStack#clone()} and {@link Pane#copy()} methods. The returned inventory
     * component is guaranteed to not reference equals this inventory component.
     *
     * @return the new inventory component
     * @since 3.0.0
     */
    @Contract(pure = true)
    public @NonNull InventoryComponent copy() {
        final InventoryComponent inventoryComponent = new InventoryComponent(getLength(), getHeight());

        for (int x = 0; x < getLength(); x++) {
            for (int y = 0; y < getHeight(); y++) {
                final ItemStack item = getItem(x, y);

                if (item == null) {
                    continue;
                }

                inventoryComponent.setItem(item.clone(), x, y);
            }
        }

        for (final Pane pane : getPanes()) {
            inventoryComponent.addPane(pane.copy());
        }

        return inventoryComponent;
    }

    /**
     * Returns a new inventory component, excluding the range of specified rows. The new inventory component will have
     * its size shrunk so only the included rows are present and any items in the excluded rows are discarded. All panes
     * will stay present. Note that while this does make a new inventory component, it does not make a copy. For
     * example, the panes in the new inventory component will be the exact same panes as in this one and will not be
     * copied. This is also true for any retained items. The specified range is 0-indexed: the first row starts at index
     * 0 and the last row ends at height - 1. The range is inclusive on both ends, the row specified at either parameter
     * will also be excluded. When the range specified is invalid - that is, part of the range contains rows that are
     * not included in this inventory component, and {@link IllegalArgumentException} will be thrown.
     *
     * @param from the starting index of the range
     * @param end  the ending index of the range
     * @return the new, shrunk inventory component
     * @since 3.0.0
     */
    @Contract(pure = true)
    public @NonNull InventoryComponent excludeRows(final int from, final int end) {
        if (from < 0 || end >= getHeight()) {
            throw new IllegalArgumentException("Specified range includes non-existent rows");
        }

        final int newHeight = getHeight() - (end - from + 1);

        final InventoryComponent newInventoryComponent = new InventoryComponent(getLength(), newHeight);

        for (final Pane pane : getPanes()) {
            newInventoryComponent.addPane(pane);
        }

        for (int x = 0; x < getLength(); x++) {
            int newY = 0;

            for (int y = 0; y < getHeight(); y++) {
                final ItemStack item = getItem(x, y);

                if (y >= from && y <= end) {
                    continue;
                }

                if (item != null) {
                    newInventoryComponent.setItem(item, x, newY);
                }

                newY++;
            }
        }

        return newInventoryComponent;
    }

    /**
     * Checks whether this component has at least one item. If it does, true is returned; false otherwise.
     *
     * @return true if this has an item, false otherwise
     * @since 3.0.0
     */
    @Contract(pure = true)
    public boolean hasItem() {
        for (int x = 0; x < getLength(); x++) {
            for (int y = 0; y < getHeight(); y++) {
                if (getItem(x, y) != null) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * This will make each pane in this component render their items in this inventory component. The panes are
     * displayed according to their priority, with the lowest priority rendering first and the highest priority (note:
     * highest priority, not {@link Pane.Priority#HIGHEST} priority) rendering last.
     *
     * @see #display(Inventory, int)
     * @since 3.0.0
     */
    public void display() {
        clearItems();

        for (final Pane pane : getPanes()) {
            if (!pane.isVisible()) {
                continue;
            }

            pane.display(this, 0, 0, getLength(), getHeight());
        }
    }

    /**
     * Checks whether the item at the specified coordinates exists. If the specified coordinates are not within this
     * inventory component, an {@link IllegalArgumentException} will be thrown.
     *
     * @param x the x coordinate
     * @param y the y coordinate
     * @return true if an item exists at the given coordinates, false otherwise
     * @throws IllegalArgumentException when the coordinates are out of bounds
     * @since 3.0.0
     */
    @Contract(pure = true)
    public boolean hasItem(final int x, final int y) {
        return getItem(x, y) != null;
    }

    /**
     * Gets the item at the specified coordinates, or null if this cell is empty. If the specified coordinates are not
     * within this inventory component, an {@link IllegalArgumentException} will be thrown.
     *
     * @param x the x coordinate
     * @param y the y coordinate
     * @return the item or null
     * @throws IllegalArgumentException when the coordinates are out of bounds
     * @since 3.0.0
     */
    @Contract(pure = true)
    public @Nullable ItemStack getItem(final int x, final int y) {
        if (!isInBounds(x, y)) {
            throw new IllegalArgumentException("Coordinates must be in-bounds: x = " + x + ", y = " + y +
                "; should be below " + getLength() + " and " + getHeight());
        }

        return this.items[x][y];
    }

    /**
     * Gets a list of panes this inventory component contains. The returned list is modifiable. If this inventory
     * component currently does not have any panes, an empty list is returned. This list is guaranteed to be sorted
     * according to the panes' priorities.
     *
     * @return the panes this component has
     * @since 3.0.0
     */
    @Contract(pure = true)
    public @NonNull List<@NonNull Pane> getPanes() {
        return this.panes;
    }

    /**
     * Adds the specified item in the slot at the specified positions. This will override an already set item if it
     * resides in the same position as specified. If the position specified is outside of the boundaries set by this
     * component, an {@link IllegalArgumentException} will be thrown.
     *
     * @param guiItem the item to place in this inventory component
     * @param x       the x coordinate of the item
     * @param y       the y coordinate of the item
     * @since 3.0.0
     */
    public void setItem(final @NonNull GuiItem guiItem, final int x, final int y) {
        if (!isInBounds(x, y)) {
            throw new IllegalArgumentException("Coordinates must be in-bounds: x = " + x + ", y = " + y +
                "; should be below " + getLength() + " and " + getHeight());
        }

        final GuiItem copy = guiItem.copy();
        copy.applyUUID();

        this.items[x][y] = copy.getItem();
    }

    /**
     * Adds the specified item in the slot at the specified positions. This will override an already set item if it
     * resides in the same position as specified. If the position specified is outside the boundaries set by this
     * component, an {@link IllegalArgumentException} will be thrown.
     *
     * @param item the item to place in this inventory component
     * @param x    the x coordinate of the item
     * @param y    the y coordinate of the item
     * @since 3.0.0
     * @deprecated usage of {@link #setItem(GuiItem, int, int)} is preferred so gui item's item meta can be freely
     * edited without losing important internal data
     */
    @Deprecated
    public void setItem(final @NonNull ItemStack item, final int x, final int y) {
        if (!isInBounds(x, y)) {
            throw new IllegalArgumentException("Coordinates must be in-bounds: x = " + x + ", y = " + y +
                "; should be below " + getLength() + " and " + getHeight());
        }

        this.items[x][y] = item;
    }

    /**
     * Gets the total size of this inventory component.
     *
     * @return the size
     * @since 3.0.0
     */
    @Contract(pure = true)
    public int getSize() {
        return getLength() * getHeight();
    }

    /**
     * Gets the height of this inventory component.
     *
     * @return the height
     * @since 3.0.0
     */
    @Contract(pure = true)
    public int getHeight() {
        return this.height;
    }

    /**
     * Gets the length of this inventory component.
     *
     * @return the length
     * @since 3.0.0
     */
    @Contract(pure = true)
    public int getLength() {
        return this.length;
    }

    /**
     * Clears the items of this inventory component.
     *
     * @since 3.0.0
     */
    private void clearItems() {
        for (final ItemStack @NonNull [] items : this.items) {
            Arrays.fill(items, null);
        }
    }

    /**
     * Returns whether the specified coordinates are inside the boundary of this inventory component or outside of this
     * inventory component; true is returned for the former case and false for the latter case.
     *
     * @param x the x coordinate
     * @param y the y coordinate
     * @return true if the coordinates are in bounds, false otherwise
     * @since 3.0.0
     */
    @Contract(pure = true)
    private boolean isInBounds(final int x, final int y) {
        final boolean xBounds = isInBounds(0, getLength() - 1, x);
        final boolean yBounds = isInBounds(0, getHeight() - 1, y);

        return xBounds && yBounds;
    }

    /**
     * Gets the pane at the specified index.
     *
     * @param index the index of the pane
     * @return the pane
     * @since 3.0.0
     */
    @Contract(pure = true)
    private @NonNull Pane getPane(final int index) {
        if (!isInBounds(0, this.panes.size() - 1, index)) {
            throw new IllegalArgumentException("Index not in pane list");
        }

        return this.panes.get(index);
    }

    /**
     * Checks whether a number is within the specified number bound (inclusive on both ends).
     *
     * @param lowerBound the lower bound of the range
     * @param upperBound the upper bound of the range
     * @param value      the value to check
     * @return true if the value is within the bounds, false otherwise
     * @since 3.0.0
     */
    @Contract(pure = true)
    private boolean isInBounds(final int lowerBound, final int upperBound, final int value) {
        return lowerBound <= value && value <= upperBound;
    }

}
