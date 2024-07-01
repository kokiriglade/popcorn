package dev.kokiriglade.popcorn.inventory.gui.type;

import dev.kokiriglade.popcorn.inventory.HumanEntityCache;
import dev.kokiriglade.popcorn.inventory.gui.InventoryComponent;
import dev.kokiriglade.popcorn.inventory.gui.type.abstraction.GrindstoneInventory;
import dev.kokiriglade.popcorn.inventory.gui.type.impl.GrindstoneInventoryImpl;
import dev.kokiriglade.popcorn.inventory.gui.type.util.InventoryBased;
import dev.kokiriglade.popcorn.inventory.gui.type.util.NamedGui;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.jetbrains.annotations.Contract;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a gui in the form of a grindstone
 *
 * @since 3.0.0
 */
public class GrindstoneGui extends NamedGui implements InventoryBased {

    /**
     * An internal grindstone inventory
     */
    private final @NonNull GrindstoneInventory grindstoneInventory = new GrindstoneInventoryImpl(this);
    /**
     * Represents the inventory component for the items
     */
    private @NonNull InventoryComponent itemsComponent = new InventoryComponent(1, 2);
    /**
     * Represents the inventory component for the result
     */
    private @NonNull InventoryComponent resultComponent = new InventoryComponent(1, 1);
    /**
     * Represents the inventory component for the player inventory
     */
    private @NonNull InventoryComponent playerInventoryComponent = new InventoryComponent(9, 4);

    /**
     * Constructs a new GUI
     *
     * @param title the title/name of this gui.
     * @since 3.0.0
     */
    public GrindstoneGui(final @NonNull Component title) {
        super(title);
    }

    /**
     * Constructs a new grindstone gui for the given {@code plugin}.
     *
     * @param title  the title/name of this gui.
     * @param plugin the owning plugin of this gui
     * @see #GrindstoneGui(Component)
     * @since 3.0.0
     */
    public GrindstoneGui(final @NonNull Component title, final @NonNull Plugin plugin) {
        super(title, plugin);
    }

    @Override
    public void show(final @NonNull HumanEntity humanEntity) {
        if (!(humanEntity instanceof Player)) {
            throw new IllegalArgumentException("Grindstones can only be opened by players");
        }

        if (isDirty()) {
            this.inventory = createInventory();
            markChanges();
        }

        getInventory().clear();

        getItemsComponent().display(getInventory(), 0);
        getResultComponent().display(getInventory(), 2);
        getPlayerInventoryComponent().display();

        if (getPlayerInventoryComponent().hasItem()) {
            final HumanEntityCache humanEntityCache = getHumanEntityCache();

            if (!humanEntityCache.contains(humanEntity)) {
                humanEntityCache.storeAndClear(humanEntity);
            }

            getPlayerInventoryComponent().placeItems(humanEntity.getInventory(), 0);
        }

        final Inventory inventory = grindstoneInventory.openInventory((Player) humanEntity, getTitle(), getTopItems());

        addInventory(inventory, this);
    }

    @Contract(pure = true)
    @Override
    public @NonNull GrindstoneGui copy() {
        final GrindstoneGui gui = new GrindstoneGui(getTitle(), super.plugin);

        gui.itemsComponent = itemsComponent.copy();
        gui.resultComponent = resultComponent.copy();
        gui.playerInventoryComponent = playerInventoryComponent.copy();

        gui.setOnTopClick(this.onTopClick);
        gui.setOnBottomClick(this.onBottomClick);
        gui.setOnGlobalClick(this.onGlobalClick);
        gui.setOnOutsideClick(this.onOutsideClick);
        gui.setOnClose(this.onClose);

        return gui;
    }

    @Override
    public void click(final @NonNull InventoryClickEvent event) {
        final int rawSlot = event.getRawSlot();

        if (rawSlot >= 0 && rawSlot <= 1) {
            getItemsComponent().click(this, event, rawSlot);
        } else if (rawSlot == 2) {
            getResultComponent().click(this, event, 0);
        } else {
            getPlayerInventoryComponent().click(this, event, rawSlot - 3);
        }
    }

    @Override
    public @NonNull Inventory getInventory() {
        if (this.inventory == null) {
            this.inventory = createInventory();
        }

        return inventory;
    }

    @Contract(pure = true)
    @Override
    public boolean isPlayerInventoryUsed() {
        return getPlayerInventoryComponent().hasItem();
    }

    @Contract(pure = true)
    @Override
    public @NonNull Inventory createInventory() {
        return Bukkit.createInventory(this, InventoryType.GRINDSTONE, getTitle());
    }

    /**
     * Handles an incoming inventory click event
     *
     * @param event the event to handle
     * @since 3.0.0
     * @deprecated no longer used internally
     */
    @SuppressWarnings("DataFlowIssue")
    @Deprecated
    public void handleClickEvent(final @NonNull InventoryClickEvent event) {
        final int slot = event.getRawSlot();
        final Player player = (Player) event.getWhoClicked();

        if (slot >= 3 && slot <= 38) {
            grindstoneInventory.sendItems(player, getTopItems(), event.getCurrentItem());
        } else if (slot >= 0 && slot <= 2) {
            grindstoneInventory.sendItems(player, getTopItems(), event.getCurrentItem());

            if (event.isCancelled()) {
                grindstoneInventory.clearCursor(player);
            }
        }
    }

    @Contract(pure = true)
    @Override
    public int getViewerCount() {
        return getInventory().getViewers().size();
    }

    @Contract(pure = true)
    @Override
    public @NonNull List<HumanEntity> getViewers() {
        return new ArrayList<>(getInventory().getViewers());
    }

    /**
     * Gets the inventory component representing the items
     *
     * @return the items component
     * @since 3.0.0
     */
    @Contract(pure = true)
    public @NonNull InventoryComponent getItemsComponent() {
        return itemsComponent;
    }

    /**
     * Gets the inventory component representing the result
     *
     * @return the result component
     * @since 3.0.0
     */
    @Contract(pure = true)
    public @NonNull InventoryComponent getResultComponent() {
        return resultComponent;
    }

    /**
     * Gets the inventory component representing the player inventory
     *
     * @return the player inventory component
     * @since 3.0.0
     */
    @Contract(pure = true)
    public @NonNull InventoryComponent getPlayerInventoryComponent() {
        return playerInventoryComponent;
    }

    /**
     * Gets the top items
     *
     * @return the top items
     * @since 3.0.0
     */
    @Contract(pure = true)
    private @Nullable ItemStack[] getTopItems() {
        return new ItemStack[]{
            getItemsComponent().getItem(0, 0),
            getItemsComponent().getItem(0, 1),
            getResultComponent().getItem(0, 0)
        };
    }

}
