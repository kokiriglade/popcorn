package dev.kokiriglade.popcorn.inventory.gui.type;

import dev.kokiriglade.popcorn.inventory.HumanEntityCache;
import dev.kokiriglade.popcorn.inventory.gui.InventoryComponent;
import dev.kokiriglade.popcorn.inventory.gui.type.util.InventoryBased;
import dev.kokiriglade.popcorn.inventory.gui.type.util.NamedGui;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.Plugin;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.Contract;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a gui in the form of a hopper
 *
 * @since 3.0.0
 */
public class HopperGui extends NamedGui implements InventoryBased {

    /**
     * Represents the inventory component for the slots
     */
    @NonNull
    private InventoryComponent slotsComponent = new InventoryComponent(5, 1);

    /**
     * Represents the inventory component for the player inventory
     */
    @NonNull
    private InventoryComponent playerInventoryComponent = new InventoryComponent(9, 4);

    /**
     * Constructs a new GUI
     *
     * @param title the title/name of this gui.
     * @since 3.0.0
     */
    public HopperGui(@NonNull Component title) {
        super(title);
    }

    /**
     * Constructs a new hopper gui for the given {@code plugin}.
     *
     * @param title the title/name of this gui.
     * @param plugin the owning plugin of this gui
     * @see #HopperGui(Component)
     * @since 3.0.0
     */
    public HopperGui(@NonNull Component title, @NonNull Plugin plugin) {
        super(title, plugin);
    }

    @Override
    public void show(@NonNull HumanEntity humanEntity) {
        if (isDirty()) {
            this.inventory = createInventory();
            markChanges();
        }

        getInventory().clear();

        getSlotsComponent().display(getInventory(), 0);
        getPlayerInventoryComponent().display();

        if (getPlayerInventoryComponent().hasItem()) {
            HumanEntityCache humanEntityCache = getHumanEntityCache();

            if (!humanEntityCache.contains(humanEntity)) {
                humanEntityCache.storeAndClear(humanEntity);
            }

            getPlayerInventoryComponent().placeItems(humanEntity.getInventory(), 0);
        }

        humanEntity.openInventory(getInventory());
    }

    @NonNull
    @Contract(pure = true)
    @Override
    public HopperGui copy() {
        HopperGui gui = new HopperGui(getTitle(), super.plugin);

        gui.slotsComponent = slotsComponent.copy();
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

        if (rawSlot >= 0 && rawSlot <= 4) {
            getSlotsComponent().click(this, event, rawSlot);
        } else {
            getPlayerInventoryComponent().click(this, event, rawSlot - 5);
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
        Inventory inventory = Bukkit.createInventory(this, InventoryType.HOPPER, getTitle());

        addInventory(inventory, this);

        return inventory;
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
     * Gets the inventory component for the slots
     *
     * @return the slots component
     * @since 3.0.0
     */
    @NonNull
    @Contract(pure = true)
    public InventoryComponent getSlotsComponent() {
        return slotsComponent;
    }

    /**
     * Gets the inventory component for the player inventory
     *
     * @return the player inventory component
     * @since 3.0.0
     */
    @NonNull
    @Contract(pure = true)
    public InventoryComponent getPlayerInventoryComponent() {
        return playerInventoryComponent;
    }

}
