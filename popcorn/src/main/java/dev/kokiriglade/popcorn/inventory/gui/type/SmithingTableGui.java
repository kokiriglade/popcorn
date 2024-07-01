package dev.kokiriglade.popcorn.inventory.gui.type;

import dev.kokiriglade.popcorn.inventory.HumanEntityCache;
import dev.kokiriglade.popcorn.inventory.gui.InventoryComponent;
import dev.kokiriglade.popcorn.inventory.gui.type.abstraction.SmithingTableInventory;
import dev.kokiriglade.popcorn.inventory.gui.type.impl.SmithingTableInventoryImpl;
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
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

/**
 * Represents a gui in the form of a smithing table.
 *
 * @since 3.0.0
 */
public class SmithingTableGui extends NamedGui implements InventoryBased {

    /**
     * An internal smithing inventory
     */
    private final @NonNull SmithingTableInventory smithingTableInventory = new SmithingTableInventoryImpl(this);
    /**
     * The viewers of this gui
     */
    private final @NonNull Collection<HumanEntity> viewers = new HashSet<>();
    /**
     * Represents the inventory component for the input
     */
    private @NonNull InventoryComponent inputComponent = new InventoryComponent(3, 1);
    /**
     * Represents the inventory component for the result
     */
    private @NonNull InventoryComponent resultComponent = new InventoryComponent(1, 1);
    /**
     * Represents the inventory component for the player inventory
     */
    private @NonNull InventoryComponent playerInventoryComponent = new InventoryComponent(9, 4);

    /**
     * Constructs a new GUI.
     *
     * @param title the title/name of this gui.
     * @since 3.0.0
     */
    public SmithingTableGui(final @NonNull Component title) {
        super(title);
    }

    /**
     * Constructs a new smithing table gui for the given {@code plugin}.
     *
     * @param title  the title/name of this gui.
     * @param plugin the owning plugin of this gui
     * @see #SmithingTableGui(Component)
     * @since 3.0.0
     */
    public SmithingTableGui(final @NonNull Component title, final @NonNull Plugin plugin) {
        super(title, plugin);
    }

    @Override
    public void show(final @NonNull HumanEntity humanEntity) {
        if (!(humanEntity instanceof Player)) {
            throw new IllegalArgumentException("Smithing tables can only be opened by players");
        }

        if (isDirty()) {
            this.inventory = createInventory();
            markChanges();
        }

        getInventory().clear();

        getInputComponent().display(getInventory(), 0);
        getResultComponent().display(getInventory(), 3);
        getPlayerInventoryComponent().display();

        if (getPlayerInventoryComponent().hasItem()) {
            final HumanEntityCache humanEntityCache = getHumanEntityCache();

            if (!humanEntityCache.contains(humanEntity)) {
                humanEntityCache.storeAndClear(humanEntity);
            }

            getPlayerInventoryComponent().placeItems(humanEntity.getInventory(), 0);
        }

        final Inventory inventory = smithingTableInventory.openInventory((Player) humanEntity, getTitle(), getTopItems());

        addInventory(inventory, this);

        this.viewers.add(humanEntity);
    }

    @Contract(pure = true)
    @Override
    public @NonNull SmithingTableGui copy() {
        final SmithingTableGui gui = new SmithingTableGui(getTitle(), super.plugin);

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
    public void click(final @NonNull InventoryClickEvent event) {
        final int rawSlot = event.getRawSlot();

        if (rawSlot >= 0 && rawSlot <= 2) {
            getInputComponent().click(this, event, rawSlot);
        } else if (rawSlot == 3) {
            getResultComponent().click(this, event, 0);
        } else {
            getPlayerInventoryComponent().click(this, event, rawSlot - 4);
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
        return Bukkit.createInventory(this, InventoryType.SMITHING, getTitle());
    }

    /**
     * Handles a human entity closing this gui.
     *
     * @param humanEntity the human entity closing the gui
     * @since 3.0.0
     */
    public void handleClose(final @NonNull HumanEntity humanEntity) {
        this.viewers.remove(humanEntity);
    }

    @Contract(pure = true)
    @Override
    public int getViewerCount() {
        return this.viewers.size();
    }

    @Contract(pure = true)
    @Override
    public @NonNull List<HumanEntity> getViewers() {
        return new ArrayList<>(this.viewers);
    }

    /**
     * Gets the inventory component representing the input items
     *
     * @return the input component
     * @since 3.0.0
     */
    @Contract(pure = true)
    public @NonNull InventoryComponent getInputComponent() {
        return inputComponent;
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
            getInputComponent().getItem(0, 0),
            getInputComponent().getItem(1, 0),
            getInputComponent().getItem(2, 0),
            getResultComponent().getItem(0, 0)
        };
    }

}
