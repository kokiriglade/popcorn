package dev.kokiriglade.popcorn.inventory.gui.type.util;

import dev.kokiriglade.popcorn.inventory.HumanEntityCache;
import dev.kokiriglade.popcorn.inventory.gui.GuiListener;
import dev.kokiriglade.popcorn.inventory.pane.Pane;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.jetbrains.annotations.Contract;

import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.function.Consumer;

/**
 * The base class of all GUIs
 * @since 3.0.0
 */
@SuppressWarnings("unused")
public abstract class Gui {

    /**
     * A map containing the relations between inventories and their respective gui. This is needed because Bukkit and
     * Spigot ignore inventory holders for beacons, brewing stands, dispensers, droppers, furnaces and hoppers. The
     * inventory holder for beacons is already being set properly via NMS, but this contains the other inventory types.
     */
    private static final @NonNull Map<Inventory, Gui> GUI_INVENTORIES = new WeakHashMap<>();
    /**
     * Whether listeners have been registered by some gui
     */
    private static boolean hasRegisteredListeners;
    /**
     * The plugin that owns this gui
     */
    protected final @NonNull Plugin plugin;
    /**
     * A player cache for storing player's inventories
     */
    protected final @NonNull HumanEntityCache humanEntityCache = new HumanEntityCache();
    /**
     * The inventory of this gui
     */
    protected Inventory inventory;
    /**
     * The consumer that will be called once a players clicks in the top-half of the gui
     */
    protected @Nullable Consumer<InventoryClickEvent> onTopClick;
    /**
     * The consumer that will be called once a players clicks in the bottom-half of the gui
     */
    protected @Nullable Consumer<InventoryClickEvent> onBottomClick;
    /**
     * The consumer that will be called once a players clicks in the gui or in their inventory
     */
    protected @Nullable Consumer<InventoryClickEvent> onGlobalClick;
    /**
     * The consumer that will be called once a player clicks outside of the gui screen
     */
    protected @Nullable Consumer<InventoryClickEvent> onOutsideClick;
    /**
     * The consumer that will be called once a player drags in the top-half of the gui
     */
    protected @Nullable Consumer<InventoryDragEvent> onTopDrag;
    /**
     * The consumer that will be called once a player drags in the bottom-half of the gui
     */
    protected @Nullable Consumer<InventoryDragEvent> onBottomDrag;
    /**
     * The consumer that will be called once a player drags in the gui or their inventory
     */
    protected @Nullable Consumer<InventoryDragEvent> onGlobalDrag;
    /**
     * The consumer that will be called once a player closes the gui
     */
    protected @Nullable Consumer<InventoryCloseEvent> onClose;
    /**
     * Whether this gui is updating (as invoked by {@link #update()}), true if this is the case, false otherwise. This
     * is used to indicate that inventory close events due to updating should be ignored.
     */
    boolean updating = false;
    /**
     * The parent gui. This gui will be navigated to once a player closes this gui. If this is null, the player will not
     * be redirected to another gui once they close this gui.
     */
    private @Nullable Gui parent;

    /**
     * Constructs a new gui with the provided plugin.
     *
     * @param plugin the plugin
     * @since 3.0.0
     */
    public Gui(final @NonNull Plugin plugin) {
        this.plugin = plugin;

        if (!hasRegisteredListeners) {
            Bukkit.getPluginManager().registerEvents(new GuiListener(plugin), plugin);

            hasRegisteredListeners = true;
        }
    }

    /**
     * Gets a gui from the specified inventory. Only guis of type beacon, brewing stand, dispenser, dropper, furnace and
     * hopper can be retrieved.
     *
     * @param inventory the inventory to get the gui from
     * @return the gui or null if the inventory doesn't have an accompanying gui
     * @since 3.0.0
     */
    @Contract(pure = true)
    public static @Nullable Gui getGui(final @NonNull Inventory inventory) {
        return GUI_INVENTORIES.get(inventory);
    }

    /**
     * Shows a gui to a player
     *
     * @param humanEntity the human entity to show the gui to
     * @since 3.0.0
     */
    public abstract void show(final @NonNull HumanEntity humanEntity);

    /**
     * Makes a copy of this gui and returns it. This makes a deep copy of the gui. This entails that the underlying
     * panes will be copied as per their {@link Pane#copy()} and miscellaneous data will be copied. The copy of this gui,
     * will however have no viewers even if this gui currently has viewers. With this, cache data for viewers will also
     * be non-existent for the copied gui. The original owning plugin of the gui is preserved, but the plugin will not
     * be deeply copied. The returned gui will never be reference equal to the current gui.
     *
     * @return a copy of the gui
     * @since 3.0.0
     */
    @Contract(pure = true)
    public abstract @NonNull Gui copy();

    /**
     * This should delegate the provided inventory click event to the right pane, which can then handle this click event
     * further. This should not call any internal click handlers, since those will already have been activated.
     *
     * @param event the event to delegate
     * @since 3.0.0
     */
    public abstract void click(final @NonNull InventoryClickEvent event);

    /**
     * Gets whether the player inventory is currently in use. This means whether the player inventory currently has an
     * item in it.
     *
     * @return true if the player inventory is occupied, false otherwise
     * @since 3.0.0
     */
    public abstract boolean isPlayerInventoryUsed();

    /**
     * Gets the count of {@link HumanEntity} instances that are currently viewing this GUI.
     *
     * @return the count of viewers
     * @since 3.0.0
     */
    @Contract(pure = true)
    public abstract int getViewerCount();

    /**
     * Gets a mutable snapshot of the current {@link HumanEntity} viewers of this GUI.
     * This is a snapshot (copy) and not a view, therefore modifications aren't visible.
     *
     * @return a snapshot of the current viewers
     * @see #getViewerCount()
     * @since 3.0.0
     */
    @Contract(pure = true)
    public abstract @NonNull List<HumanEntity> getViewers();

    /**
     * Update the gui for everyone
     * @since 3.0.0
     */
    public void update() {
        updating = true;

        for (final HumanEntity viewer : getViewers()) {
            final ItemStack cursor = viewer.getItemOnCursor();
            viewer.setItemOnCursor(null); // TODO set to air if this fails

            show(viewer);

            viewer.setItemOnCursor(cursor);
        }

        if (!updating) {
            throw new AssertionError("Gui#isUpdating became false before Gui#update finished");
        }

        updating = false;
    }

    /**
     * Adds the specified inventory and gui, so we can properly intercept clicks.
     *
     * @param inventory the inventory for the specified gui
     * @param gui       the gui belonging to the specified inventory
     * @since 3.0.0
     */
    protected void addInventory(final @NonNull Inventory inventory, final @NonNull Gui gui) {
        GUI_INVENTORIES.put(inventory, gui);
    }

    /**
     * Gets the human entity cache used for this gui
     *
     * @return the human entity cache
     * @see HumanEntityCache
     * @since 3.0.0
     */
    @Contract(pure = true)
    public @NonNull HumanEntityCache getHumanEntityCache() {
        return humanEntityCache;
    }

    /**
     * Set the consumer that should be called whenever this gui is clicked in.
     *
     * @param onTopClick the consumer that gets called
     * @since 3.0.0
     */
    public void setOnTopClick(final @Nullable Consumer<InventoryClickEvent> onTopClick) {
        this.onTopClick = onTopClick;
    }

    /**
     * Calls the consumer (if it's not null) that was specified using {@link #setOnTopClick(Consumer)},
     * so the consumer that should be called whenever this gui is clicked in.
     * Catches and logs all exceptions the consumer might throw.
     *
     * @param event the event to handle
     * @since 3.0.0
     */
    public void callOnTopClick(final @NonNull InventoryClickEvent event) {
        callCallback(onTopClick, event, "onTopClick");
    }

    /**
     * Set the consumer that should be called whenever the inventory is clicked in.
     *
     * @param onBottomClick the consumer that gets called
     * @since 3.0.0
     */
    public void setOnBottomClick(final @Nullable Consumer<InventoryClickEvent> onBottomClick) {
        this.onBottomClick = onBottomClick;
    }

    /**
     * Calls the consumer (if it's not null) that was specified using {@link #setOnBottomClick(Consumer)},
     * so the consumer that should be called whenever the inventory is clicked in.
     * Catches and logs all exceptions the consumer might throw.
     *
     * @param event the event to handle
     * @since 3.0.0
     */
    public void callOnBottomClick(final @NonNull InventoryClickEvent event) {
        callCallback(onBottomClick, event, "onBottomClick");
    }

    /**
     * Set the consumer that should be called whenever this gui or inventory is clicked in.
     *
     * @param onGlobalClick the consumer that gets called
     * @since 3.0.0
     */
    public void setOnGlobalClick(final @Nullable Consumer<InventoryClickEvent> onGlobalClick) {
        this.onGlobalClick = onGlobalClick;
    }

    /**
     * Calls the consumer (if it's not null) that was specified using {@link #setOnGlobalClick(Consumer)},
     * so the consumer that should be called whenever this gui or inventory is clicked in.
     * Catches and logs all exceptions the consumer might throw.
     *
     * @param event the event to handle
     * @since 3.0.0
     */
    public void callOnGlobalClick(final @NonNull InventoryClickEvent event) {
        callCallback(onGlobalClick, event, "onGlobalClick");
    }

    /**
     * Set the consumer that should be called whenever a player clicks outside the gui.
     *
     * @param onOutsideClick the consumer that gets called
     * @since 3.0.0
     */
    public void setOnOutsideClick(final @Nullable Consumer<InventoryClickEvent> onOutsideClick) {
        this.onOutsideClick = onOutsideClick;
    }

    /**
     * Calls the consumer (if it's not null) that was specified using {@link #setOnOutsideClick(Consumer)},
     * so the consumer that should be called whenever a player clicks outside the gui.
     * Catches and logs all exceptions the consumer might throw.
     *
     * @param event the event to handle
     * @since 3.0.0
     */
    public void callOnOutsideClick(final @NonNull InventoryClickEvent event) {
        callCallback(onOutsideClick, event, "onOutsideClick");
    }

    /**
     * Set the consumer that should be called whenever this gui's top half is dragged in.
     *
     * @param onTopDrag the consumer that gets called
     * @since 3.0.0
     */
    public void setOnTopDrag(final @Nullable Consumer<InventoryDragEvent> onTopDrag) {
        this.onTopDrag = onTopDrag;
    }

    /**
     * Calls the consumer (if it's not null) that was specified using {@link #setOnTopDrag(Consumer)},
     * so the consumer that should be called whenever this gui's top half is dragged in.
     * Catches and logs all exceptions the consumer might throw.
     *
     * @param event the event to handle
     * @since 3.0.0
     */
    public void callOnTopDrag(final @NonNull InventoryDragEvent event) {
        callCallback(onTopDrag, event, "onTopDrag");
    }

    /**
     * Set the consumer that should be called whenever the inventory is dragged in.
     *
     * @param onBottomDrag the consumer that gets called
     * @since 3.0.0
     */
    public void setOnBottomDrag(final @Nullable Consumer<InventoryDragEvent> onBottomDrag) {
        this.onBottomDrag = onBottomDrag;
    }

    /**
     * Calls the consumer (if it's not null) that was specified using {@link #setOnBottomDrag(Consumer)},
     * so the consumer that should be called whenever the inventory is dragged in.
     * Catches and logs all exceptions the consumer might throw.
     *
     * @param event the event to handle
     * @since 3.0.0
     */
    public void callOnBottomDrag(final @NonNull InventoryDragEvent event) {
        callCallback(onBottomDrag, event, "onBottomDrag");
    }

    /**
     * Set the consumer that should be called whenever this gui or inventory is dragged in.
     *
     * @param onGlobalDrag the consumer that gets called
     * @since 3.0.0
     */
    public void setOnGlobalDrag(final @Nullable Consumer<InventoryDragEvent> onGlobalDrag) {
        this.onGlobalDrag = onGlobalDrag;
    }

    /**
     * Calls the consumer (if it's not null) that was specified using {@link #setOnGlobalDrag(Consumer)},
     * so the consumer that should be called whenever this gui or inventory is dragged in.
     * Catches and logs all exceptions the consumer might throw.
     *
     * @param event the event to handle
     * @since 3.0.0
     */
    public void callOnGlobalDrag(final @NonNull InventoryDragEvent event) {
        callCallback(onGlobalDrag, event, "onGlobalDrag");
    }

    /**
     * Set the consumer that should be called whenever this gui is closed.
     *
     * @param onClose the consumer that gets called
     * @since 3.0.0
     */
    public void setOnClose(final @Nullable Consumer<InventoryCloseEvent> onClose) {
        this.onClose = onClose;
    }

    /**
     * Calls the consumer (if it's not null) that was specified using {@link #setOnClose(Consumer)},
     * so the consumer that should be called whenever this gui is closed.
     * Catches and logs all exceptions the consumer might throw.
     *
     * @param event the event to handle
     * @since 3.0.0
     */
    public void callOnClose(final @NonNull InventoryCloseEvent event) {
        callCallback(onClose, event, "onClose");
    }

    /**
     * Calls the specified consumer (if it's not null) with the specified parameter,
     * catching and logging all exceptions it might throw.
     *
     * @param callback     the consumer to call if it isn't null
     * @param event        the value the consumer should accept
     * @param callbackName the name of the action, used for logging
     * @param <T>          the type of the value the consumer is accepting
     * @since 3.0.0
     */
    protected <T extends InventoryEvent> void callCallback(final @Nullable Consumer<? super T> callback,
                                                           final @NonNull T event, final @NonNull String callbackName) {
        if (callback == null) {
            return;
        }

        try {
            callback.accept(event);
        } catch (final Throwable t) {
            String message = "Exception while handling %s".formatted(callbackName);
            if (event instanceof InventoryClickEvent clickEvent) {
                message += ", slot=%s".formatted(clickEvent.getSlot());
            }

            this.plugin.getSLF4JLogger().error(message, t);
        }
    }

    /**
     * The parent gui will be shown to the specified {@link HumanEntity}. If no parent gui is set, then this method will
     * silently do nothing.
     *
     * @param humanEntity the human entity to redirect
     * @since 3.0.0
     */
    public void navigateToParent(final @NonNull HumanEntity humanEntity) {
        if (this.parent == null) {
            return;
        }

        this.parent.show(humanEntity);
    }

    /**
     * Sets the parent gui to the provided gui. This is the gui that a player will be navigated to once they close this
     * gui. The navigation will occur after the close event handler, set by {@link #setOnClose(Consumer)}, is called. If
     * there was already a previous parent set, the provided gui will override the previous one.
     *
     * @param gui the new parent gui
     * @since 3.0.0
     */
    public void setParent(final @NonNull Gui gui) {
        this.parent = gui;
    }

    /**
     * Gets whether this gui is being updated, as invoked by {@link #update()}. This returns true if this is the case
     * and false otherwise.
     *
     * @return whether this gui is being updated
     * @since 3.0.0
     */
    @Contract(pure = true)
    public boolean isUpdating() {
        return updating;
    }

}
