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
     * Represents the inventory component for the map
     */
    @NonNull
    private InventoryComponent mapComponent = new InventoryComponent(1, 1);

    /**
     * Represents the inventory component for the paper
     */
    @NonNull
    private InventoryComponent paperComponent = new InventoryComponent(1, 1);

    /**
     * Represents the inventory component for the output
     */
    @NonNull
    private InventoryComponent outputComponent = new InventoryComponent(1, 1);

    /**
     * Represents the inventory component for the player inventory
     */
    @NonNull
    private InventoryComponent playerInventoryComponent = new InventoryComponent(9, 4);

    /**
     * An internal cartography table inventory
     */
    @NonNull
    private final CartographyTableInventory cartographyTableInventory = new CartographyTableInventoryImpl(this);

    /**
     * Constructs a new GUI
     *
     * @param title the title/name of this gui.
     * @since 3.0.0
     */
    public CartographyTableGui(@NonNull Component title) {
        super(title);
    }

    /**
     * Constructs a new cartography table gui for the given {@code plugin}.
     *
     * @param title the title/name of this gui.
     * @param plugin the owning plugin of this gui
     * @see #CartographyTableGui(Component)
     * @since 3.0.0
     */
    public CartographyTableGui(@NonNull Component title, @NonNull Plugin plugin) {
        super(title, plugin);
    }

    @Override
    public void show(@NonNull HumanEntity humanEntity) {
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
            HumanEntityCache humanEntityCache = getHumanEntityCache();

            if (humanEntityCache.contains(humanEntity)) {
                humanEntityCache.storeAndClear(humanEntity);
            }

            getPlayerInventoryComponent().placeItems(humanEntity.getInventory(), 0);
        }

        //also let Bukkit know that we opened an inventory
        humanEntity.openInventory(getInventory());

        cartographyTableInventory.openInventory((Player) humanEntity, getTitle(), getTopItems());
    }

    @NonNull
    @Contract(pure = true)
    @Override
    public CartographyTableGui copy() {
        CartographyTableGui gui = new CartographyTableGui(getTitle(), super.plugin);

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
    public void click(@NonNull InventoryClickEvent event) {
        int rawSlot = event.getRawSlot();

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

    @NonNull
    @Override
    public Inventory getInventory() {
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

    @NonNull
    @Contract(pure = true)
    @Override
    public Inventory createInventory() {
        return Bukkit.createInventory(this, InventoryType.CARTOGRAPHY, getTitle());
    }

    @Contract(pure = true)
    @Override
    public int getViewerCount() {
        return getInventory().getViewers().size();
    }

    @NonNull
    @Contract(pure = true)
    @Override
    public List<HumanEntity> getViewers() {
        return new ArrayList<>(getInventory().getViewers());
    }

    /**
     * Handles an incoming inventory click event
     *
     * @param event the event to handle
     * @since 3.0.0
     */
    public void handleClickEvent(@NonNull InventoryClickEvent event) {
        int slot = event.getRawSlot();
        Player player = (Player) event.getWhoClicked();

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
    @NonNull
    @Contract(pure = true)
    public InventoryComponent getMapComponent() {
        return mapComponent;
    }

    /**
     * Gets the inventory component representing the paper
     *
     * @return the paper component
     * @since 3.0.0
     */
    @NonNull
    @Contract(pure = true)
    public InventoryComponent getPaperComponent() {
        return paperComponent;
    }

    /**
     * Gets the inventory component representing the output
     *
     * @return the output component
     * @since 3.0.0
     */
    @NonNull
    @Contract(pure = true)
    public InventoryComponent getOutputComponent() {
        return outputComponent;
    }

    /**
     * Gets the inventory component representing the player inventory
     *
     * @return the player inventory component
     * @since 3.0.0
     */
    @NonNull
    @Contract(pure = true)
    public InventoryComponent getPlayerInventoryComponent() {
        return playerInventoryComponent;
    }

    /**
     * Gets the top items
     *
     * @return the top items
     * @since 3.0.0
     */
    @Nullable
    @Contract(pure = true)
    private ItemStack[] getTopItems() {
        return new ItemStack[] {
            getMapComponent().getItem(0, 0),
            getPaperComponent().getItem(0, 0),
            getOutputComponent().getItem(0, 0)
        };
    }

}
