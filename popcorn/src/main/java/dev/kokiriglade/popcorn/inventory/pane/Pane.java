package dev.kokiriglade.popcorn.inventory.pane;

import dev.kokiriglade.popcorn.inventory.gui.GuiItem;
import dev.kokiriglade.popcorn.inventory.gui.InventoryComponent;
import dev.kokiriglade.popcorn.inventory.gui.type.util.Gui;
import dev.kokiriglade.popcorn.inventory.pane.util.Mask;
import dev.kokiriglade.popcorn.inventory.pane.util.Slot;
import dev.kokiriglade.popcorn.persistence.UUIDTagType;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.jetbrains.annotations.Contract;

import java.util.Collection;
import java.util.UUID;
import java.util.function.Consumer;

/**
 * The base class for all panes.
 * @since 3.0.0
 */
@SuppressWarnings("unused")
public abstract class Pane {

    /**
     * The starting position of this pane, which is 0 by default
     */
    @Deprecated
    protected int x = 0, y = 0;

    /**
     * The position of this pane, which is (0,0) by default
     */
    protected @NonNull Slot slot = Slot.fromXY(0, 0);

    /**
     * Length is horizontal, height is vertical
     */
    protected int length, height;
    /**
     * The consumer that will be called once a players clicks in this pane
     */
    protected @Nullable Consumer<InventoryClickEvent> onClick;
    /**
     * A unique identifier for panes to locate them by
     */
    protected UUID uuid;
    /**
     * The visibility state of the pane
     */
    private boolean visible;
    /**
     * The priority of the pane, determines when it will be rendered
     */
    private @NonNull Priority priority;

    /**
     * Constructs a new default pane
     *
     * @param slot     the slot of the pane
     * @param length   the length of the pane
     * @param height   the height of the pane
     * @param priority the priority of the pane
     * @since 3.0.0
     */
    protected Pane(final @NonNull Slot slot, final int length, final int height, final @NonNull Priority priority) {
        if (length == 0 || height == 0) {
            throw new IllegalArgumentException("Length and height of pane must be greater than zero");
        }

        setSlot(slot);

        this.length = length;
        this.height = height;

        this.priority = priority;
        this.visible = true;

        this.uuid = UUID.randomUUID();
    }

    /**
     * Constructs a new default pane
     *
     * @param x        the upper left x coordinate of the pane
     * @param y        the upper left y coordinate of the pane
     * @param length   the length of the pane
     * @param height   the height of the pane
     * @param priority the priority of the pane
     * @since 3.0.0
     */
    protected Pane(final int x, final int y, final int length, final int height, final @NonNull Priority priority) {
        this(Slot.fromXY(x, y), length, height, priority);
    }

    /**
     * Constructs a new default pane, with no position
     *
     * @param length the length of the pane
     * @param height the height of the pane
     * @since 3.0.0
     */
    protected Pane(final int length, final int height) {
        if (length == 0 || height == 0) {
            throw new IllegalArgumentException("Length and height of pane must be greater than zero");
        }

        this.length = length;
        this.height = height;

        this.priority = Priority.NORMAL;
        this.visible = true;

        this.uuid = UUID.randomUUID();
    }

    /**
     * Constructs a new default pane
     *
     * @param slot   the slot of the pane
     * @param length the length of the pane
     * @param height the height of the pane
     * @since 3.0.0
     */
    protected Pane(final @NonNull Slot slot, final int length, final int height) {
        this(slot, length, height, Priority.NORMAL);
    }

    /**
     * Constructs a new default pane
     *
     * @param x      the upper left x coordinate of the pane
     * @param y      the upper left y coordinate of the pane
     * @param length the length of the pane
     * @param height the height of the pane
     * @since 3.0.0
     */
    protected Pane(final int x, final int y, final int length, final int height) {
        this(x, y, length, height, Priority.NORMAL);
    }

    /**
     * Checks whether a {@link GuiItem} is the same item as the given {@link ItemStack}. The item will be compared using
     * internal data. When the item does not have this data, this method will return false. If the item does have such
     * data, but its value does not match, false is also returned. This method will not mutate any of the provided
     * arguments.
     *
     * @param guiItem the gui item to check
     * @param item    the item which the gui item should be checked against
     * @return true if the {@link GuiItem} matches the {@link ItemStack}, false otherwise
     * @since 3.0.0
     */
    @Contract(pure = true)
    protected static boolean matchesItem(final @NonNull GuiItem guiItem, final @NonNull ItemStack item) {
        final ItemMeta meta = item.getItemMeta();

        if (meta == null) {
            return false;
        }

        return guiItem.getUUID().equals(meta.getPersistentDataContainer().get(guiItem.getKey(), UUIDTagType.INSTANCE));
    }

    /**
     * Finds a type of {@link GuiItem} from the provided collection of items based on the provided {@link ItemStack}.
     * The items will be compared using internal data. When the item does not have this data, this method will return
     * null. If the item does have such data, but its value cannot be found in the provided list, null is also returned.
     * This method will not mutate any of the provided arguments, nor any of the contents inside of the arguments. The
     * provided collection may be unmodifiable if preferred. This method will always return a type of {@link GuiItem}
     * that is in the provided collection - when the returned result is not null - such that an element E inside the
     * provided collection reference equals the returned type of {@link GuiItem}.
     *
     * @param items a collection of items in which will be searched
     * @param item  the item for which an {@link GuiItem} should be found
     * @param <T>   a type of GuiItem, which will be used in the provided collection and as return type
     * @return the found type of {@link GuiItem} or null if none was found
     * @since 3.0.0
     */
    @Contract(pure = true)
    protected static <T extends GuiItem> @Nullable T findMatchingItem(final @NonNull Collection<T> items, final @NonNull ItemStack item) {
        for (final T guiItem : items) {
            if (matchesItem(guiItem, item)) {
                return guiItem;
            }
        }

        return null;
    }

    /**
     * Creates a pane which displays as a border around the outside of the pane consisting of the provided item. The
     * slot, length and height parameters are used for the respective properties of the pane. If either the length or
     * height is negative an {@link IllegalArgumentException} will be thrown.
     *
     * @param slot   the slot of the pane
     * @param length the length of the pane
     * @param height the height of the pane
     * @param item   the item of which the border is made
     * @return the created pane which displays a border
     * @throws IllegalArgumentException if length or height is negative
     * @since 3.0.0
     */
    @Contract(pure = true)
    public static @NonNull Pane createBorder(final @NonNull Slot slot, final int length, final int height, final @NonNull GuiItem item) {
        if (length < 0) {
            throw new IllegalArgumentException("Length should be non-negative");
        }

        if (height < 0) {
            throw new IllegalArgumentException("Height should be non-negative");
        }

        final String[] mask = new String[height];

        if (height > 0) {
            mask[0] = createLine(length);
        }

        if (height > 1) {
            mask[height - 1] = createLine(length);
        }

        for (int yIndex = 1; yIndex < height - 1; yIndex++) {

            final String builder = "1" + "0".repeat(Math.max(0, length - 2)) +
                '1';

            mask[yIndex] = builder;
        }

        final OutlinePane pane = new OutlinePane(slot, length, height);
        pane.applyMask(new Mask(mask));
        pane.addItem(item);
        pane.setRepeat(true);

        return pane;
    }

    /**
     * Creates a pane which displays as a border around the outside of the pane consisting of the provided item. The x,
     * y, length and height parameters are used for the respective properties of the pane. If either the length or
     * height is negative an {@link IllegalArgumentException} will be thrown.
     *
     * @param x      the x coordinate of the pane
     * @param y      the y coordinate of the pane
     * @param length the length of the pane
     * @param height the height of the pane
     * @param item   the item of which the border is made
     * @return the created pane which displays a border
     * @throws IllegalArgumentException if length or height is negative
     * @since 3.0.0
     */
    @Contract(pure = true)
    public static @NonNull Pane createBorder(final int x, final int y, final int length, final int height, final @NonNull GuiItem item) {
        return createBorder(Slot.fromXY(x, y), length, height, item);
    }

    /**
     * Creates a string containing the character '1' repeated length amount of times. If the provided length is negative
     * an {@link IllegalArgumentException} will be thrown.
     *
     * @param length the length of the string
     * @return the string containing '1's
     * @throws IllegalArgumentException if length is negative
     * @since 3.0.0
     */
    @Contract(pure = true)
    private static @NonNull String createLine(final int length) {
        if (length < 0) {
            throw new IllegalArgumentException("Length should be non-negative");
        }

        return "1".repeat(length);
    }

    /**
     * Makes a copy of this pane and returns it. This makes a deep copy of the pane. This entails that the underlying
     * panes and/or items will be copied as well. The returned pane will never be reference equal to the current pane.
     *
     * @return a copy of this pane
     * @since 3.0.0
     */
    @Contract(pure = true)
    public @NonNull Pane copy() {
        throw new UnsupportedOperationException("The implementing pane hasn't overridden the copy method");
    }

    /**
     * Returns the length of this pane
     *
     * @return the length
     * @since 3.0.0
     */
    @Contract(pure = true)
    public int getLength() {
        return length;
    }

    /**
     * Set the length of this pane
     *
     * @param length the new length
     * @since 3.0.0
     */
    public void setLength(final int length) {
        this.length = length;
    }

    /**
     * Returns the height of this pane
     *
     * @return the height
     * @since 3.0.0
     */
    @Contract(pure = true)
    public int getHeight() {
        return height;
    }

    /**
     * Set the height of this pane
     *
     * @param height the new height
     * @since 3.0.0
     */
    public void setHeight(final int height) {
        this.height = height;
    }

    /**
     * Gets the {@link UUID} associated with this pane.
     *
     * @return the uuid
     * @since 3.0.0
     */
    @Contract(pure = true)
    public @NonNull UUID getUUID() {
        return uuid;
    }

    /**
     * Gets the slot of the position of this pane
     *
     * @return the slot
     * @since 3.0.0
     */
    @Contract(pure = true)
    public @NonNull Slot getSlot() {
        return this.slot;
    }

    /**
     * Sets the slot of this pane.
     *
     * @param slot the slot
     * @since 3.0.0
     */
    public void setSlot(final @NonNull Slot slot) {
        this.slot = slot;

        //the length should be the length of the parent container, but we don't have that, so just use one
        this.x = slot.getX(1);
        this.y = slot.getY(1);
    }

    /**
     * Gets the x coordinate of this pane
     *
     * @return the x coordinate
     * @since 3.0.0
     * @deprecated when the slot was specified as an indexed position, this may return the wrong value;
     * {@link #getSlot()} should be used instead
     */
    @Contract(pure = true)
    @Deprecated
    public int getX() {
        return x;
    }

    /**
     * Set the x coordinate of this pane
     *
     * @param x the new x coordinate
     * @since 3.0.0
     */
    public void setX(final int x) {
        this.x = x;

        this.slot = Slot.fromXY(x, getY());
    }

    /**
     * Gets the y coordinate of this pane
     *
     * @return the y coordinate
     * @since 3.0.0
     * @deprecated when the slot was specified as an indexed position, this may return the wrong value;
     * {@link #getSlot()} should be used instead
     */
    @Contract(pure = true)
    @Deprecated
    public int getY() {
        return y;
    }

    /**
     * Set the y coordinate of this pane
     *
     * @param y the new y coordinate
     * @since 3.0.0
     */
    public void setY(final int y) {
        this.y = y;

        this.slot = Slot.fromXY(getX(), y);
    }

    /**
     * Has to set all the items in the right spot inside the inventory
     *
     * @param inventoryComponent the inventory component in which the items should be displayed
     * @param paneOffsetX        the pane's offset on the x-axis
     * @param paneOffsetY        the pane's offset on the y-axis
     * @param maxLength          the maximum length of the pane
     * @param maxHeight          the maximum height of the pane
     * @since 3.0.0
     */
    public abstract void display(final @NonNull InventoryComponent inventoryComponent, final int paneOffsetX, final int paneOffsetY,
                                 final int maxLength, final int maxHeight);

    /**
     * Returns the pane's visibility state
     *
     * @return the pane's visibility
     * @since 3.0.0
     */
    @Contract(pure = true)
    public boolean isVisible() {
        return visible;
    }

    /**
     * Sets whether this pane is visible or not
     *
     * @param visible the pane's visibility
     * @since 3.0.0
     */
    public void setVisible(final boolean visible) {
        this.visible = visible;
    }

    /**
     * Called whenever there is being clicked on this pane
     *
     * @param gui                the gui in which was clicked
     * @param inventoryComponent the inventory component in which this pane resides
     * @param event              the event that occurred while clicking on this item
     * @param slot               the slot that was clicked in
     * @param paneOffsetX        the pane's offset on the x axis
     * @param paneOffsetY        the pane's offset on the y axis
     * @param maxLength          the maximum length of the pane
     * @param maxHeight          the maximum height of the pane
     * @return whether the item was found or not
     * @since 3.0.0
     */
    public abstract boolean click(final @NonNull Gui gui, final @NonNull InventoryComponent inventoryComponent,
                                  final @NonNull InventoryClickEvent event, final int slot, final int paneOffsetX, final int paneOffsetY,
                                  final int maxLength, final int maxHeight);

    /**
     * Returns the priority of the pane
     *
     * @return the priority
     * @since 3.0.0
     */
    public @NonNull Priority getPriority() {
        return priority;
    }

    /**
     * Sets the priority of this pane
     *
     * @param priority the priority
     * @since 3.0.0
     */
    public void setPriority(final @NonNull Priority priority) {
        this.priority = priority;
    }

    /**
     * Gets all the items in this pane and all underlying panes.
     * The returned collection is not guaranteed to be mutable or to be a view of the underlying data.
     * (So changes to the gui are not guaranteed to be visible in the returned value.)
     *
     * @return all items
     * @since 3.0.0
     */
    @Contract(pure = true)
    public abstract @NonNull Collection<GuiItem> getItems();

    /**
     * Gets all the panes in this panes, including any child panes from other panes.
     * The returned collection is not guaranteed to be mutable or to be a view of the underlying data.
     * (So changes to the gui are not guaranteed to be visible in the returned value.)
     *
     * @return all panes
     * @since 3.0.0
     */
    @Contract(pure = true)
    public abstract @NonNull Collection<Pane> getPanes();

    /**
     * Clears the entire pane of any items/panes. Underlying panes will not be cleared.
     *
     * @since 3.0.0
     */
    public abstract void clear();

    /**
     * Set the consumer that should be called whenever this pane is clicked in.
     *
     * @param onClick the consumer that gets called
     * @since 3.0.0
     */
    public void setOnClick(final @Nullable Consumer<InventoryClickEvent> onClick) {
        this.onClick = onClick;
    }

    /**
     * Calls the consumer (if it's not null) that was specified using {@link #setOnClick(Consumer)},
     * so the consumer that should be called whenever this pane is clicked in.
     *
     * @param event the event to handle
     * @since 3.0.0
     */
    protected void callOnClick(final @NonNull InventoryClickEvent event) {
        if (onClick == null) {
            return;
        }


        try {
            onClick.accept(event);
        } catch (final Throwable t) {
            throw new RuntimeException(
                "Exception while handling click event in inventory '"
                    + PlainTextComponentSerializer.plainText().serialize(event.getView().title()) + "', slot=" + event.getSlot() + ", for "
                    + getClass().getSimpleName() + ", x=" + getX() + ", y=" + getY()
                    + ", length=" + length + ", height=" + height,
                t
            );
        }
    }

    /**
     * An enum representing the rendering priorities for the panes. Uses a similar system to Bukkit's
     * {@link org.bukkit.event.EventPriority} system
     *
     * @since 3.0.0
     */
    public enum Priority {

        /**
         * The lowest priority, will be rendered first
         *
         * @since 3.0.0
         */
        LOWEST {
            @Override
            public boolean isLessThan(final @NonNull Priority priority) {
                return priority != this;
            }
        },

        /**
         * A low priority, lower than default
         *
         * @since 3.0.0
         */
        LOW {
            @Override
            public boolean isLessThan(final @NonNull Priority priority) {
                return priority != this && priority != LOWEST;
            }
        },

        /**
         * A normal priority, the default
         *
         * @since 3.0.0
         */
        NORMAL {
            @Override
            public boolean isLessThan(final @NonNull Priority priority) {
                return priority != this && priority != LOW && priority != LOWEST;
            }
        },

        /**
         * A higher priority, higher than default
         *
         * @since 3.0.0
         */
        HIGH {
            @Override
            public boolean isLessThan(final @NonNull Priority priority) {
                return priority == HIGHEST || priority == MONITOR;
            }
        },

        /**
         * The highest priority for production use
         *
         * @since 3.0.0
         */
        HIGHEST {
            @Override
            public boolean isLessThan(final @NonNull Priority priority) {
                return priority == MONITOR;
            }
        },

        /**
         * The highest priority, will always be called last, should not be used for production code
         *
         * @since 3.0.0
         */
        MONITOR {
            @Override
            public boolean isLessThan(final @NonNull Priority priority) {
                return false;
            }
        };

        /**
         * Whether this priority is less than the priority specified.
         *
         * @param priority the priority to compare against
         * @return true if this priority is less than the specified priority, false otherwise
         * @since 3.0.0
         */
        @Contract(pure = true)
        public abstract boolean isLessThan(final @NonNull Priority priority);

        /**
         * Whether this priority is greater than the priority specified.
         *
         * @param priority the priority to compare against
         * @return true if this priority is greater than the specified priority, false otherwise
         * @since 3.0.0
         */
        @Contract(pure = true)
        public boolean isGreaterThan(final @NonNull Priority priority) {
            return !isLessThan(priority) && this != priority;
        }
    }

}
