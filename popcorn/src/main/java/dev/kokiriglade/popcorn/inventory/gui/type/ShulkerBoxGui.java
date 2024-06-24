package dev.kokiriglade.popcorn.inventory.gui.type;

import dev.kokiriglade.popcorn.inventory.HumanEntityCache;
import dev.kokiriglade.popcorn.inventory.gui.GuiItem;
import dev.kokiriglade.popcorn.inventory.gui.InventoryComponent;
import dev.kokiriglade.popcorn.inventory.gui.type.util.InventoryBased;
import dev.kokiriglade.popcorn.inventory.gui.type.util.MergedGui;
import dev.kokiriglade.popcorn.inventory.gui.type.util.NamedGui;
import dev.kokiriglade.popcorn.inventory.pane.Pane;
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
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Represents a gui in the form a shulker box
 *
 * @since 3.0.0
 */
public class ShulkerBoxGui extends NamedGui implements MergedGui, InventoryBased {

    /**
     * Represents the inventory component for the entire gui
     */
    @NonNull
    private InventoryComponent inventoryComponent = new InventoryComponent(9, 7);

    /**
     * Constructs a new GUI
     *
     * @param title the title/name of this gui.
     * @since 3.0.0
     */
    public ShulkerBoxGui(@NonNull Component title) {
        super(title);
    }

    /**
     * Constructs a new shulker box gui for the given {@code plugin}.
     *
     * @param title the title/name of this gui.
     * @param plugin the owning plugin of this gui
     * @see #ShulkerBoxGui(Component)
     * @since 3.0.0
     */
    public ShulkerBoxGui(@NonNull Component title, @NonNull Plugin plugin) {
        super(title, plugin);
    }

    @Override
    public void show(@NonNull HumanEntity humanEntity) {
        if (isDirty()) {
            this.inventory = createInventory();
            markChanges();
        }

        getInventory().clear();
        int height = getInventoryComponent().getHeight();

        getInventoryComponent().display();

        InventoryComponent topComponent = getInventoryComponent().excludeRows(height - 4, height - 1);
        InventoryComponent bottomComponent = getInventoryComponent().excludeRows(0, height - 5);

        topComponent.placeItems(getInventory(), 0);

        if (bottomComponent.hasItem()) {
            HumanEntityCache humanEntityCache = getHumanEntityCache();

            if (!humanEntityCache.contains(humanEntity)) {
                humanEntityCache.storeAndClear(humanEntity);
            }

            bottomComponent.placeItems(humanEntity.getInventory(), 0);
        }

        humanEntity.openInventory(getInventory());
    }

    @NonNull
    @Contract(pure = true)
    @Override
    public ShulkerBoxGui copy() {
        ShulkerBoxGui gui = new ShulkerBoxGui(getTitle(), super.plugin);

        gui.inventoryComponent = inventoryComponent.copy();

        gui.setOnTopClick(this.onTopClick);
        gui.setOnBottomClick(this.onBottomClick);
        gui.setOnGlobalClick(this.onGlobalClick);
        gui.setOnOutsideClick(this.onOutsideClick);
        gui.setOnClose(this.onClose);

        return gui;
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
        return getInventoryComponent().excludeRows(0, getInventoryComponent().getHeight() - 5).hasItem();
    }

    @Override
    public void click(@NonNull InventoryClickEvent event) {
        getInventoryComponent().click(this, event, event.getRawSlot());
    }

    @Override
    public void addPane(@NonNull Pane pane) {
        this.inventoryComponent.addPane(pane);
    }

    @NonNull
    @Contract(pure = true)
    @Override
    public List<Pane> getPanes() {
        return this.inventoryComponent.getPanes();
    }

    @NonNull
    @Contract(pure = true)
    @Override
    public Collection<GuiItem> getItems() {
        return getPanes().stream().flatMap(pane -> pane.getItems().stream()).collect(Collectors.toSet());
    }

    @NonNull
    @Contract(pure = true)
    @Override
    public Inventory createInventory() {
        return Bukkit.createInventory(this, InventoryType.SHULKER_BOX, getTitle());
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

    @NonNull
    @Contract(pure = true)
    @Override
    public InventoryComponent getInventoryComponent() {
        return inventoryComponent;
    }

}
