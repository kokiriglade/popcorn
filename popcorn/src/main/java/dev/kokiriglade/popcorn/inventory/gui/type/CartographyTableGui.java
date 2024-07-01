package dev.kokiriglade.popcorn.inventory.gui.type;

import dev.kokiriglade.popcorn.inventory.HumanEntityCache;
import dev.kokiriglade.popcorn.inventory.gui.InventoryComponent;
import dev.kokiriglade.popcorn.inventory.gui.type.abstraction.CartographyTableInventory;
import dev.kokiriglade.popcorn.inventory.gui.type.impl.CartographyTableInventoryImpl;
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
 * Represents a gui in the form of a cartography table
 *
 * @since 3.0.0
 */
public class CartographyTableGui extends NamedGui implements InventoryBased {

    /**
     * An internal cartography table inventory
     */
    private final @NonNull CartographyTableInventory cartographyTableInventory = new CartographyTableInventoryImpl(this);
    /**
     * Represents the inventory component for the map
     */
    private @NonNull InventoryComponent mapComponent = new InventoryComponent(1, 1);
    /**
     * Represents the inventory component for the paper
     */
    private @NonNull InventoryComponent paperComponent = new InventoryComponent(1, 1);
    /**
     * Represents the inventory component for the output
     */
    private @NonNull InventoryComponent outputComponent = new InventoryComponent(1, 1);
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
    public CartographyTableGui(final @NonNull Component title) {
        super(title);
    }

    /**
     * Constructs a new cartography table gui for the given {@code plugin}.
     *
     * @param title  the title/name of this gui.
     * @param plugin the owning plugin of this gui
     * @see #CartographyTableGui(Component)
     * @since 3.0.0
     */
    public CartographyTableGui(final @NonNull Component title, final @NonNull Plugin plugin) {
        super(title, plugin);
    }

    @Override
    public void show(final @NonNull HumanEntity humanEntity) {
        if (!(humanEntity instanceof Player)) {
            throw new IllegalArgumentException("Cartography tables can only be opened by players");
        }

        if (isDirty()) {
            this.inventory = createInventory();
            markChanges();
        }

        getInventory().clear();

        getMapComponent().display(getInventory(), 0);
        getPaperComponent().display(getInventory(), 1);
        getOutputComponent().display(getInventory(), 2);
        getPlayerInventoryComponent().display();

        if (getPlayerInventoryComponent().hasItem()) {
            final HumanEntityCache humanEntityCache = getHumanEntityCache();

            if (humanEntityCache.contains(humanEntity)) {
                humanEntityCache.storeAndClear(humanEntity);
            }

            getPlayerInventoryComponent().placeItems(humanEntity.getInventory(), 0);
        }

        //also let Bukkit know that we opened an inventory
        humanEntity.openInventory(getInventory());

        cartographyTableInventory.openInventory((Player) humanEntity, getTitle(), getTopItems());
    }

    @Contract(pure = true)
    @Override
    public @NonNull CartographyTableGui copy() {
        final CartographyTableGui gui = new CartographyTableGui(getTitle(), super.plugin);

        gui.mapComponent = mapComponent.copy();
        gui.paperComponent = paperComponent.copy();
        gui.outputComponent = outputComponent.copy();
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

        if (rawSlot == 0) {
            getMapComponent().click(this, event, 0);
        } else if (rawSlot == 1) {
            getPaperComponent().click(this, event, 0);
        } else if (rawSlot == 2) {
            getOutputComponent().click(this, event, 0);
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
        return Bukkit.createInventory(this, InventoryType.CARTOGRAPHY, getTitle());
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
     * Handles an incoming inventory click event
     *
     * @param event the event to handle
     * @since 3.0.0
     */
    public void handleClickEvent(final @NonNull InventoryClickEvent event) {
        final int slot = event.getRawSlot();
        final Player player = (Player) event.getWhoClicked();

        if (slot >= 3 && slot <= 38) {
            cartographyTableInventory.sendItems(player, getTopItems());
        } else if (slot >= 0 && slot <= 2) {
            //the client rejects the output item if send immediately
            Bukkit.getScheduler().runTask(super.plugin, () ->
                cartographyTableInventory.sendItems(player, getTopItems()));

            if (event.isCancelled()) {
                cartographyTableInventory.clearCursor(player);
            }
        }
    }

    /**
     * Gets the inventory component representing the map
     *
     * @return the map component
     * @since 3.0.0
     */
    @Contract(pure = true)
    public @NonNull InventoryComponent getMapComponent() {
        return mapComponent;
    }

    /**
     * Gets the inventory component representing the paper
     *
     * @return the paper component
     * @since 3.0.0
     */
    @Contract(pure = true)
    public @NonNull InventoryComponent getPaperComponent() {
        return paperComponent;
    }

    /**
     * Gets the inventory component representing the output
     *
     * @return the output component
     * @since 3.0.0
     */
    @Contract(pure = true)
    public @NonNull InventoryComponent getOutputComponent() {
        return outputComponent;
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
            getMapComponent().getItem(0, 0),
            getPaperComponent().getItem(0, 0),
            getOutputComponent().getItem(0, 0)
        };
    }

}
