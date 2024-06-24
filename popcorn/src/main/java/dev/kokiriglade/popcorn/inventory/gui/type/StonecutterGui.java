package dev.kokiriglade.popcorn.inventory.gui.type;

import dev.kokiriglade.popcorn.inventory.HumanEntityCache;
import dev.kokiriglade.popcorn.inventory.gui.InventoryComponent;
import dev.kokiriglade.popcorn.inventory.gui.type.abstraction.StonecutterInventory;
import dev.kokiriglade.popcorn.inventory.gui.type.impl.StonecutterInventoryImpl;
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
 * Represents a gui in the form of a stonecutter
 *
 * @since 3.0.0
 */
public class StonecutterGui extends NamedGui implements InventoryBased {

    /**
     * Represents the inventory component for the input
     */
    @NonNull
    private InventoryComponent inputComponent = new InventoryComponent(1, 1);

    /**
     * Represents the inventory component for the result
     */
    @NonNull
    private InventoryComponent resultComponent = new InventoryComponent(1, 1);

    /**
     * Represents the inventory component for the player inventory
     */
    @NonNull
    private InventoryComponent playerInventoryComponent = new InventoryComponent(9, 4);

    /**
     * An internal stonecutter inventory
     */
    @NonNull
    private final StonecutterInventory stonecutterInventory = new StonecutterInventoryImpl(this);

    /**
     * Constructs a new GUI
     *
     * @param title the title/name of this gui.
     * @since 3.0.0
     */
    public StonecutterGui(@NonNull Component title) {
        super(title);
    }

    /**
     * Constructs a new stonecutter gui for the given {@code plugin}.
     *
     * @param title the title/name of this gui.
     * @param plugin the owning plugin of this gui
     * @see #StonecutterGui(Component)
     * @since 3.0.0
     */
    public StonecutterGui(@NonNull Component title, @NonNull Plugin plugin) {
        super(title, plugin);
    }

    @Override
    public void show(@NonNull HumanEntity humanEntity) {
        if (!(humanEntity instanceof Player)) {
            throw new IllegalArgumentException("Enchanting tables can only be opened by players");
        }

        if (isDirty()) {
            this.inventory = createInventory();
            markChanges();
        }

        getInventory().clear();

        getInputComponent().display(getInventory(), 0);
        getResultComponent().display(getInventory(), 1);
        getPlayerInventoryComponent().display();

        if (getPlayerInventoryComponent().hasItem()) {
            HumanEntityCache humanEntityCache = getHumanEntityCache();

            if (!humanEntityCache.contains(humanEntity)) {
                humanEntityCache.storeAndClear(humanEntity);
            }

            getPlayerInventoryComponent().placeItems(humanEntity.getInventory(), 0);
        }

        //also let Bukkit know that we opened an inventory
        humanEntity.openInventory(getInventory());

        stonecutterInventory.openInventory((Player) humanEntity, getTitle(), getTopItems());
    }

    @NonNull
    @Contract(pure = true)
    @Override
    public StonecutterGui copy() {
        StonecutterGui gui = new StonecutterGui(getTitle(), super.plugin);

        gui.inputComponent = inputComponent.copy();
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
    public void click(@NonNull InventoryClickEvent event) {
        int rawSlot = event.getRawSlot();

        if (rawSlot == 0) {
            getInputComponent().click(this, event, 0);
        } else if (rawSlot == 1) {
            getResultComponent().click(this, event, 0);
        } else {
            getPlayerInventoryComponent().click(this, event, rawSlot - 2);
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
        return Bukkit.createInventory(this, InventoryType.STONECUTTER, getTitle());
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

        if (slot >= 2 && slot <= 37) {
            stonecutterInventory.sendItems(player, getTopItems());
        } else if (slot == 0 || slot == 1) {
            stonecutterInventory.sendItems(player, getTopItems());

            if (event.isCancelled()) {
                stonecutterInventory.clearCursor(player);
            }
        }
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
     * Gets the inventory component representing the input
     *
     * @return the input component
     * @since 3.0.0
     */
    @NonNull
    @Contract(pure = true)
    public InventoryComponent getInputComponent() {
        return inputComponent;
    }

    /**
     * Gets the inventory component representing the result
     *
     * @return the result component
     * @since 3.0.0
     */
    @NonNull
    @Contract(pure = true)
    public InventoryComponent getResultComponent() {
        return resultComponent;
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
     * Get the top items
     *
     * @return the top items
     * @since 3.0.0
     */
    @Nullable
    @Contract(pure = true)
    private ItemStack[] getTopItems() {
        return new ItemStack[] {
            getInputComponent().getItem(0, 0),
            getResultComponent().getItem(0, 0)
        };
    }

}
