package dev.kokiriglade.popcorn.inventory.gui.type.abstraction;

import dev.kokiriglade.popcorn.inventory.gui.type.abstraction.util.ObservableValue;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.jetbrains.annotations.Contract;

import java.util.function.Consumer;

/**
 * An anvil inventory
 *
 * @since 3.0.0
 */
@SuppressWarnings("unused")
public abstract class AnvilInventory {

    /**
     * The name input text.
     */
    protected final @NonNull ObservableValue<@NonNull String> observableText = new ObservableValue<>("");
    /**
     * The inventory holder
     */
    protected @NonNull InventoryHolder inventoryHolder;
    /**
     * The enchantment cost displayed
     */
    protected short cost;

    /**
     * Creates a new anvil inventory for the specified inventory holder
     *
     * @param inventoryHolder the inventory holder
     * @since 3.0.0
     */
    public AnvilInventory(final @NonNull InventoryHolder inventoryHolder) {
        this.inventoryHolder = inventoryHolder;
    }

    /**
     * Sets the enchantment level cost for this anvil gui. Taking the item from the result slot will not actually remove
     * these levels. Having a cost specified does not impede a player's ability to take the item in the result item,
     * even if the player does not have the specified amount of levels. The cost must be a non-negative number.
     *
     * @param cost the cost
     * @throws IllegalArgumentException when the cost is less than zero
     * @since 3.0.0
     */
    public void setCost(final short cost) {
        if (cost < 0) {
            throw new IllegalArgumentException("Cost must be non-negative");
        }

        this.cost = cost;
    }

    /**
     * Opens the inventory for the specified player
     *
     * @param player the player to open the inventory for
     * @param title  the title of the inventory
     * @param items  the items to show
     * @since 3.0.0
     */
    public abstract Inventory openInventory(final @NonNull Player player, final @NonNull Component title, final @Nullable ItemStack @NonNull [] items);

    /**
     * Sends the top items to the inventory for the specified player.
     *
     * @param player the player for which to open the anvil
     * @param items  the items to send
     * @since 3.0.0
     * @deprecated no longer used internally
     */
    @Deprecated
    public abstract void sendItems(final @NonNull Player player, final @Nullable ItemStack @NonNull [] items);

    /**
     * Sends the result item to the specified player
     *
     * @param player the player to send the item to
     * @param item   the item to send
     * @since 3.0.0
     * @deprecated no longer used internally
     */
    @Deprecated
    public abstract void sendResultItem(final @NonNull Player player, final @Nullable ItemStack item);

    /**
     * Sends the first item to the specified player
     *
     * @param player the player to send the item to
     * @param item   the item to send
     * @since 3.0.0
     * @deprecated no longer used internally
     */
    @Deprecated
    public abstract void sendFirstItem(final @NonNull Player player, final @Nullable ItemStack item);

    /**
     * Sends the second item to the specified player
     *
     * @param player the player to send the item to
     * @param item   the item to send
     * @since 3.0.0
     * @deprecated no longer used internally
     */
    @Deprecated
    public abstract void sendSecondItem(final @NonNull Player player, final @Nullable ItemStack item);

    /**
     * Sets the cursor of the given player
     *
     * @param player the player to set the cursor
     * @param item   the item to set the cursor to
     * @since 3.0.0
     * @deprecated no longer used internally
     */
    @Deprecated
    public abstract void setCursor(final @NonNull Player player, final @NonNull ItemStack item);

    /**
     * Clears the cursor of the specified player
     *
     * @param player the player to clear the cursor of
     * @since 3.0.0
     * @deprecated no longer used internally
     */
    @Deprecated
    public abstract void clearCursor(final @NonNull Player player);

    /**
     * Clears the result item for the specified player
     *
     * @param player the player to clear the result item of
     * @since 3.0.0
     * @deprecated no longer used internally
     */
    @Deprecated
    public abstract void clearResultItem(final @NonNull Player player);

    /**
     * Gets the text shown in the rename slot of the anvil
     *
     * @return the rename text
     * @since 3.0.0
     */
    @Contract(pure = true)
    public @NonNull String getRenameText() {
        final String text = observableText.get();

        if (text == null) {
            throw new IllegalStateException("Rename text is null");
        }

        return text;
    }

    /**
     * Subscribes to changes of the name input.
     *
     * @param onNameInputChanged the consumer to call when the name input changes
     * @since 3.0.0
     */
    public void subscribeToNameInputChanges(final @NonNull Consumer<? super String> onNameInputChanged) {
        this.observableText.subscribe(onNameInputChanged);
    }

}
