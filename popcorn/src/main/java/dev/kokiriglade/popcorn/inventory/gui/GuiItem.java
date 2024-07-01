package dev.kokiriglade.popcorn.inventory.gui;

import dev.kokiriglade.popcorn.persistence.UUIDTagType;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.NamespacedKey;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.jetbrains.annotations.Contract;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * An item in an inventory
 *
 * @since 3.0.0
 */
@SuppressWarnings("unused")
public class GuiItem {

    /**
     * The logger to log errors with
     *
     * @since 3.0.0
     */
    private final @NonNull Logger logger;

    /**
     * The {@link NamespacedKey} that specifies the location of the (internal) {@link UUID} in {@link org.bukkit.persistence.PersistentDataContainer}s.
     * The {@link org.bukkit.persistence.PersistentDataType} that should be used is {@link UUIDTagType}.
     *
     * @since 3.0.0
     */
    private final @NonNull NamespacedKey keyUUID;

    /**
     * An action for the inventory
     *
     * @since 3.0.0
     */
    private @Nullable Consumer<InventoryClickEvent> action;

    /**
     * List of item's properties
     *
     * @since 3.0.0
     */
    private @NonNull List<Object> properties;

    /**
     * The items shown
     *
     * @since 3.0.0
     */
    private @NonNull ItemStack item;

    /**
     * Whether this item is visible or not
     *
     * @since 3.0.0
     */
    private boolean visible;

    /**
     * Internal UUID for keeping track of this item
     *
     * @since 3.0.0
     */
    private @NonNull UUID uuid = UUID.randomUUID();

    /**
     * Creates a new gui item based on the item stack and action
     *
     * @param item   the item stack
     * @param action the action called whenever an interaction with this item happens
     * @param plugin the owning plugin of this item
     * @see #GuiItem(ItemStack, Consumer)
     * @since 3.0.0
     */
    public GuiItem(final @NonNull ItemStack item, final @Nullable Consumer<InventoryClickEvent> action, final @NonNull Plugin plugin) {
        this(item, action, plugin.getLogger(), new NamespacedKey(plugin, "IF-uuid"));
    }

    /**
     * Creates a new gui item based on the item stack and action
     *
     * @param item   the item stack
     * @param plugin the owning plugin of this item
     * @see #GuiItem(ItemStack)
     * @since 3.0.0
     */
    public GuiItem(final @NonNull ItemStack item, final @NonNull Plugin plugin) {
        this(item, event -> {
        }, plugin);
    }

    /**
     * Creates a new gui item based on the item stack and action
     *
     * @param item   the item stack
     * @param action the action called whenever an interaction with this item happens
     * @since 3.0.0
     */
    public GuiItem(final @NonNull ItemStack item, final @Nullable Consumer<InventoryClickEvent> action) {
        this(item, action, JavaPlugin.getProvidingPlugin(GuiItem.class));
    }

    /**
     * Creates a new gui item based on the item stack and action
     *
     * @param item the item stack
     * @since 3.0.0
     */
    public GuiItem(final @NonNull ItemStack item) {
        this(item, event -> {
        });
    }

    /**
     * Creates a new gui item based on the given item, action, logger, and key. The logger will be used for logging
     * exceptions and the key is used for identification of this item.
     *
     * @param item   the item stack
     * @param action the action called whenever an interaction with this item happens
     * @param logger the logger used for logging exceptions
     * @param key    the key to identify this item with
     * @since 3.0.0
     */
    private GuiItem(final @NonNull ItemStack item, final @Nullable Consumer<InventoryClickEvent> action, final @NonNull Logger logger,
                    final @NonNull NamespacedKey key) {
        this.logger = logger;
        this.keyUUID = key;
        this.action = action;
        this.visible = true;
        this.properties = new ArrayList<>();
        this.item = item;

        // TODO remove this call after the removal of InventoryComponent#setItem(ItemStack, int, int)
        applyUUID();
    }

    /**
     * Makes a copy of this gui item and returns it. This makes a deep copy of the gui item. This entails that the
     * underlying item will be copied as per their {@link ItemStack#clone()} and miscellaneous data will be copied in
     * such a way that they are identical. The returned gui item will never be reference equal to the current gui item.
     *
     * @return a copy of the gui item
     * @since 3.0.0
     */
    @Contract(pure = true)
    public @NonNull GuiItem copy() {
        final GuiItem guiItem = new GuiItem(item.clone(), action, this.logger, this.keyUUID);

        guiItem.visible = visible;
        guiItem.uuid = uuid;
        guiItem.properties = new ArrayList<>(properties);
        final ItemMeta meta = guiItem.item.getItemMeta();

        if (meta == null) {
            throw new IllegalArgumentException("item must be able to have ItemMeta (it mustn't be AIR)");
        }

        meta.getPersistentDataContainer().set(keyUUID, UUIDTagType.INSTANCE, guiItem.uuid);
        guiItem.item.setItemMeta(meta);

        return guiItem;
    }

    /**
     * Calls the handler of the {@link InventoryClickEvent}
     * if such a handler was specified in the constructor.
     * Catches and logs all exceptions the handler might throw.
     *
     * @param event the event to handle
     * @since 3.0.0
     */
    public void callAction(final @NonNull InventoryClickEvent event) {
        if (action == null) {
            return;
        }

        try {
            action.accept(event);
        } catch (final Throwable t) {
            this.logger.log(Level.SEVERE, "Exception while handling click event in inventory '"
                + PlainTextComponentSerializer.plainText().serialize(event.getView().title()) + "', slot=" + event.getSlot() + ", item=" + item.getType(), t);
        }
    }

    /**
     * Sets the internal UUID of this gui item onto the underlying item. Previously set UUID will be overwritten by the
     * current UUID. If the underlying item does not have an item meta, this method will silently do nothing.
     *
     * @since 3.0.0
     */
    public void applyUUID() {
        final ItemMeta meta = item.getItemMeta();

        if (meta != null) {
            meta.getPersistentDataContainer().set(this.keyUUID, UUIDTagType.INSTANCE, uuid);
            item.setItemMeta(meta);
        }
    }

    /**
     * Sets the action to be executed when a human entity clicks on this item.
     *
     * @param action the action of this item
     * @since 3.0.0
     */
    public void setAction(final @NonNull Consumer<InventoryClickEvent> action) {
        this.action = action;
    }

    /**
     * Returns the list of properties
     *
     * @return the list of properties that belong to this gui item
     * @since 3.0.0
     */
    @Contract(pure = true)
    public @NonNull List<Object> getProperties() {
        return properties;
    }

    /**
     * Sets the list of properties for this gui item
     *
     * @param properties list of new properties
     * @since 3.0.0
     */
    public void setProperties(final @NonNull List<Object> properties) {
        this.properties = properties;
    }

    /**
     * Returns the item
     *
     * @return the item that belongs to this gui item
     * @since 3.0.0
     */
    @Contract(pure = true)
    public @NonNull ItemStack getItem() {
        return item;
    }

    /**
     * Overwrites the current item with the provided item.
     *
     * @param item the item to set
     * @since 3.0.0
     */
    public void setItem(final @NonNull ItemStack item) {
        this.item = item;
    }

    /**
     * Gets the namespaced key used for this item.
     *
     * @return the namespaced key
     * @since 3.0.0
     */
    @Contract(pure = true)
    public @NonNull NamespacedKey getKey() {
        return keyUUID;
    }

    /**
     * Gets the {@link UUID} associated with this {@link GuiItem}. This is for internal use only, and should not be
     * used.
     *
     * @return the {@link UUID} of this item
     * @since 3.0.0
     */
    @Contract(pure = true)
    public @NonNull UUID getUUID() {
        return uuid;
    }

    /**
     * Returns whether this item is visible
     *
     * @return true if this item is visible, false otherwise
     * @since 3.0.0
     */
    public boolean isVisible() {
        return visible;
    }

    /**
     * Sets the visibility of this item to the new visibility
     *
     * @param visible the new visibility
     * @since 3.0.0
     */
    public void setVisible(final boolean visible) {
        this.visible = visible;
    }

}
