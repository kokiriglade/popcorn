package dev.kokiriglade.popcorn.inventory.pane;

import dev.kokiriglade.popcorn.inventory.gui.GuiItem;
import dev.kokiriglade.popcorn.inventory.gui.InventoryComponent;
import dev.kokiriglade.popcorn.inventory.gui.type.util.Gui;
import dev.kokiriglade.popcorn.inventory.pane.util.Mask;
import dev.kokiriglade.popcorn.inventory.pane.util.Slot;
import dev.kokiriglade.popcorn.inventory.util.GeometryUtil;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.Contract;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * A pane for items that should be outlined
 * @since 3.0.0
 */
@SuppressWarnings({"unused"})
public class OutlinePane extends Pane implements Flippable, Orientable, Rotatable {

    /**
     * A set of items inside this pane
     */
    private final @NonNull List<GuiItem> items;

    /**
     * The orientation of the items in this pane
     */
    private @NonNull Orientation orientation;

    /**
     * The clockwise rotation of this pane in degrees
     */
    private int rotation;

    /**
     * The amount of empty spots in between each item
     */
    private int gap;

    /**
     * Whether the items should be repeated to fill the entire pane
     */
    private boolean repeat;

    /**
     * Whether the items should be flipped horizontally and/or vertically
     */
    private boolean flipHorizontally, flipVertically;

    /**
     * The alignment of this pane
     */
    private @NonNull Alignment alignment = Alignment.BEGIN;

    /**
     * The mask for this pane
     */
    private @NonNull Mask mask;

    /**
     * Creates a new outline pane
     *
     * @param slot     the slot of the pane
     * @param length   the length of the pane
     * @param height   the height of the pane
     * @param priority the priority of the pane
     * @since 3.0.0
     */
    public OutlinePane(final @NonNull Slot slot, final int length, final int height, final @NonNull Priority priority) {
        super(slot, length, height, priority);

        this.items = new ArrayList<>(length * height);
        this.orientation = Orientation.HORIZONTAL;

        final String[] mask = new String[height];

        Arrays.fill(mask, "1".repeat(Math.max(0, length)));

        this.mask = new Mask(mask);
    }

    /**
     * Creates a new outline pane
     * @param x        x-axis of slot
     * @param y        y-axis of slot
     * @param length   the length of the pane
     * @param height   the height of the pane
     * @param priority the priority of the pane
     * @since 3.0.0
     */
    public OutlinePane(final int x, final int y, final int length, final int height, final @NonNull Priority priority) {
        this(Slot.fromXY(x, y), length, height, priority);
    }


    /**
     * Creates a new outline pane
     *
     * @param slot   the slot of the pane
     * @param length the length of the pane
     * @param height the height of the pane
     * @since 3.0.0
     */
    public OutlinePane(final @NonNull Slot slot, final int length, final int height) {
        this(slot, length, height, Priority.NORMAL);
    }

    /**
     * Creates a new outline pane
     * @param x        x-axis of slot
     * @param y        y-axis of slot
     * @param length   the length of the pane
     * @param height   the height of the pane
     * @since 3.0.0
     */
    public OutlinePane(final int x, final int y, final int length, final int height) {
        this(x, y, length, height, Priority.NORMAL);
    }

    /**
     * Creates a new outline pane
     * @param length   the length of the pane
     * @param height   the height of the pane
     * @since 3.0.0
     */
    public OutlinePane(final int length, final int height) {
        this(0, 0, length, height);
    }

    @Override
    public void display(final @NonNull InventoryComponent inventoryComponent, final int paneOffsetX, final int paneOffsetY, final int maxLength,
                        final int maxHeight) {
        final int length = Math.min(this.length, maxLength);
        final int height = Math.min(this.height, maxHeight);

        int itemIndex = 0;
        int gapCount = 0;

        final int size;

        if (getOrientation() == Orientation.HORIZONTAL) {
            size = height;
        } else if (getOrientation() == Orientation.VERTICAL) {
            size = length;
        } else {
            throw new IllegalStateException("Unknown orientation '" + getOrientation() + "'");
        }

        for (int vectorIndex = 0; vectorIndex < size && getItems().size() > itemIndex; vectorIndex++) {
            final boolean[] maskLine;

            if (getOrientation() == Orientation.HORIZONTAL) {
                maskLine = mask.getRow(vectorIndex);
            } else if (getOrientation() == Orientation.VERTICAL) {
                maskLine = mask.getColumn(vectorIndex);
            } else {
                throw new IllegalStateException("Unknown orientation '" + getOrientation() + "'");
            }

            int enabled = 0;

            for (final boolean bool : maskLine) {
                if (bool) {
                    enabled++;
                }
            }

            final GuiItem[] items;

            if (doesRepeat()) {
                items = new GuiItem[enabled];
            } else {
                final int remainingPositions = gapCount + (getItems().size() - itemIndex - 1) * (getGap() + 1) + 1;

                items = new GuiItem[Math.min(enabled, remainingPositions)];
            }

            for (int index = 0; index < items.length; index++) {
                if (gapCount == 0) {
                    items[index] = getItems().get(itemIndex);

                    itemIndex++;

                    if (doesRepeat() && itemIndex >= getItems().size()) {
                        itemIndex = 0;
                    }

                    gapCount = getGap();
                } else {
                    items[index] = null;

                    gapCount--;
                }
            }

            int index;

            if (getAlignment() == Alignment.BEGIN) {
                index = 0;
            } else if (getAlignment() == Alignment.CENTER) {
                index = -((enabled - items.length) / 2);
            } else {
                throw new IllegalStateException("Unknown alignment '" + getAlignment() + "'");
            }

            for (int opposingVectorIndex = 0; opposingVectorIndex < maskLine.length; opposingVectorIndex++) {
                if (!maskLine[opposingVectorIndex]) {
                    continue;
                }

                if (index >= 0 && index < items.length && items[index] != null) {
                    int x, y;

                    if (getOrientation() == Orientation.HORIZONTAL) {
                        x = opposingVectorIndex;
                        y = vectorIndex;
                    } else if (getOrientation() == Orientation.VERTICAL) {
                        x = vectorIndex;
                        y = opposingVectorIndex;
                    } else {
                        throw new IllegalStateException("Unknown orientation '" + getOrientation() + "'");
                    }

                    if (flipHorizontally) {
                        x = length - x - 1;
                    }

                    if (flipVertically) {
                        y = height - y - 1;
                    }

                    final Map.Entry<Integer, Integer> coordinates = GeometryUtil.processClockwiseRotation(x, y,
                        length, height, rotation);

                    x = coordinates.getKey();
                    y = coordinates.getValue();

                    if (x >= 0 && x < length && y >= 0 && y < height) {
                        final Slot slot = getSlot();

                        final int finalRow = slot.getY(maxLength) + y + paneOffsetY;
                        final int finalColumn = slot.getX(maxLength) + x + paneOffsetX;

                        final GuiItem item = items[index];
                        if (item.isVisible()) {
                            inventoryComponent.setItem(item, finalColumn, finalRow);
                        }
                    }
                }

                index++;
            }
        }
    }

    @Override
    public boolean click(final @NonNull Gui gui, final @NonNull InventoryComponent inventoryComponent,
                         final @NonNull InventoryClickEvent event, final int slot, final int paneOffsetX, final int paneOffsetY, final int maxLength,
                         final int maxHeight) {
        final int length = Math.min(this.length, maxLength);
        final int height = Math.min(this.height, maxHeight);

        final Slot paneSlot = getSlot();

        final int xPosition = paneSlot.getX(maxLength);
        final int yPosition = paneSlot.getY(maxLength);

        final int totalLength = inventoryComponent.getLength();

        final int adjustedSlot = slot - (xPosition + paneOffsetX) - totalLength * (yPosition + paneOffsetY);

        final int x = adjustedSlot % totalLength;
        final int y = adjustedSlot / totalLength;

        //this isn't our item
        if (x < 0 || x >= length || y < 0 || y >= height) {
            return false;
        }

        callOnClick(event);

        final ItemStack itemStack = event.getCurrentItem();

        if (itemStack == null) {
            return false;
        }

        final GuiItem item = findMatchingItem(items, itemStack);

        if (item == null) {
            return false;
        }

        item.callAction(event);

        return true;
    }

    @Contract(pure = true)
    @Override
    public @NonNull OutlinePane copy() {
        final OutlinePane outlinePane = new OutlinePane(getSlot(), length, height, getPriority());

        for (final GuiItem item : items) {
            outlinePane.addItem(item.copy());
        }

        outlinePane.setVisible(isVisible());
        outlinePane.onClick = onClick;

        outlinePane.uuid = uuid;

        outlinePane.orientation = orientation;
        outlinePane.rotation = rotation;
        outlinePane.gap = gap;
        outlinePane.repeat = repeat;
        outlinePane.flipHorizontally = flipHorizontally;
        outlinePane.flipVertically = flipVertically;
        outlinePane.mask = mask;
        outlinePane.alignment = alignment;

        return outlinePane;
    }

    /**
     * Adds a gui item in the specified index
     *
     * @param item  the item to add
     * @param index the item's index
     * @since 3.0.0
     */
    public void insertItem(final @NonNull GuiItem item, final int index) {
        items.add(index, item);
    }

    /**
     * Adds a gui item at the specific spot in the pane
     *
     * @param item the item to set
     * @since 3.0.0
     */
    public void addItem(final @NonNull GuiItem item) {
        items.add(item);
    }

    /**
     * Removes the specified item from the pane
     *
     * @param item the item to remove
     * @since 3.0.0
     */
    public void removeItem(final @NonNull GuiItem item) {
        items.remove(item);
    }

    @Override
    public void clear() {
        items.clear();
    }

    /**
     * Applies a custom mask to this pane. This will throw an {@link IllegalArgumentException} when the mask's dimension
     * differs from this pane's dimension.
     *
     * @param mask the mask to apply to this pane
     * @throws IllegalArgumentException when the mask's dimension is incorrect
     * @since 3.0.0
     */
    public void applyMask(final @NonNull Mask mask) {
        if (length != mask.getLength() || height != mask.getHeight()) {
            throw new IllegalArgumentException("Mask's dimension must be the same as the pane's dimension");
        }

        this.mask = mask;
    }

    @Override
    public void setLength(final int length) {
        super.setLength(length);

        applyMask(getMask().setLength(length));
    }

    @Override
    public void setHeight(final int height) {
        super.setHeight(height);

        applyMask(getMask().setHeight(height));
    }

    /**
     * Aligns the pane in the way specified by the provided alignment.
     *
     * @param alignment the new alignment
     * @since 3.0.0
     */
    public void align(final @NonNull Alignment alignment) {
        this.alignment = alignment;
    }

    @Override
    public void flipHorizontally(final boolean flipHorizontally) {
        this.flipHorizontally = flipHorizontally;
    }

    @Override
    public void flipVertically(final boolean flipVertically) {
        this.flipVertically = flipVertically;
    }

    /**
     * Sets whether this pane should repeat itself
     *
     * @param repeat whether the pane should repeat
     * @since 3.0.0
     */
    public void setRepeat(final boolean repeat) {
        this.repeat = repeat;
    }

    @Contract(pure = true)
    @Override
    public @NonNull Collection<Pane> getPanes() {
        return new HashSet<>();
    }

    /**
     * Gets the alignment set on this pane.
     *
     * @return the alignment
     * @since 3.0.0
     */
    @Contract(pure = true)
    public @NonNull Alignment getAlignment() {
        return this.alignment;
    }

    /**
     * Gets whether this outline pane repeats itself
     *
     * @return true if this pane repeats, false otherwise
     * @since 3.0.0
     */
    @Contract(pure = true)
    public boolean doesRepeat() {
        return repeat;
    }

    /**
     * Gets the gap of the pane
     *
     * @return the gap
     * @since 3.0.0
     */
    @Contract(pure = true)
    public int getGap() {
        return gap;
    }

    /**
     * Sets the gap of the pane
     *
     * @param gap the new gap
     * @since 3.0.0
     */
    public void setGap(final int gap) {
        this.gap = gap;
    }

    @Override
    public @NonNull List<GuiItem> getItems() {
        return items;
    }

    /**
     * Gets the mask applied to this pane.
     *
     * @return the mask
     * @since 3.0.0
     */
    @Contract(pure = true)
    public @NonNull Mask getMask() {
        return mask;
    }

    /**
     * Gets the orientation of this outline pane
     *
     * @return the orientation
     * @since 3.0.0
     */
    @Contract(pure = true)
    @Override
    public @NonNull Orientation getOrientation() {
        return orientation;
    }

    @Override
    public void setOrientation(final @NonNull Orientation orientation) {
        this.orientation = orientation;
    }

    @Contract(pure = true)
    @Override
    public int getRotation() {
        return rotation;
    }

    @Override
    public void setRotation(final int rotation) {
        if (length != height) {
            throw new UnsupportedOperationException("length and height are different");
        }

        if (rotation % 90 != 0) {
            throw new IllegalArgumentException("rotation isn't divisible by 90");
        }

        this.rotation = rotation % 360;
    }

    @Contract(pure = true)
    @Override
    public boolean isFlippedHorizontally() {
        return flipHorizontally;
    }

    @Contract(pure = true)
    @Override
    public boolean isFlippedVertically() {
        return flipVertically;
    }

    /**
     * An enum containing different alignments that can be used on the outline pane.
     *
     * @since 3.0.0
     */
    public enum Alignment {

        /**
         * Aligns the items at the start of the pane.
         *
         * @since 3.0.0
         */
        BEGIN,

        /**
         * Aligns the items in the center of the pane. If there is no exact center, this will preference the left (for a
         * horizontal orientation) or the top (for a vertical orientation).
         *
         * @since 3.0.0
         */
        CENTER
    }

}
