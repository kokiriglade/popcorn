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
 * Represents a gui in the form of a crafting table
 *
 * @since 3.0.0
 */
public class CraftingTableGui extends NamedGui implements InventoryBased {

    /**
     * Represents the inventory component for the input
     */
    private @NonNull InventoryComponent inputComponent = new InventoryComponent(3, 3);

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
    public CraftingTableGui(final @NonNull Component title) {
        super(title);
    }

    /**
     * Constructs a new crafting table gui for the given {@code plugin}.
     *
     * @param title  the title/name of this gui.
     * @param plugin the owning plugin of this gui
     * @see #CraftingTableGui(Component)
     * @since 3.0.0
     */
    public CraftingTableGui(final @NonNull Component title, final @NonNull Plugin plugin) {
        super(title, plugin);
    }

    @Override
    public void show(final @NonNull HumanEntity humanEntity) {
        if (isDirty()) {
            this.inventory = createInventory();
            markChanges();
        }

        getInventory().clear();

        getOutputComponent().display(getInventory(), 0);
        getInputComponent().display(getInventory(), 1);
        getPlayerInventoryComponent().display();

        if (getPlayerInventoryComponent().hasItem()) {
            final HumanEntityCache humanEntityCache = getHumanEntityCache();

            if (humanEntityCache.contains(humanEntity)) {
                humanEntityCache.storeAndClear(humanEntity);
            }

            getPlayerInventoryComponent().placeItems(humanEntity.getInventory(), 0);
        }

        humanEntity.openInventory(getInventory());
    }

    @Contract(pure = true)
    @Override
    public @NonNull CraftingTableGui copy() {
        final CraftingTableGui gui = new CraftingTableGui(getTitle(), super.plugin);

        gui.inputComponent = inputComponent.copy();
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
            getOutputComponent().click(this, event, 0);
        } else if (rawSlot >= 1 && rawSlot <= 9) {
            getInputComponent().click(this, event, rawSlot - 1);
        } else {
            getPlayerInventoryComponent().click(this, event, rawSlot - 10);
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
        return Bukkit.createInventory(this, InventoryType.WORKBENCH, getTitle());
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
     * Gets the inventory component representing the input
     *
     * @return the input component
     * @since 3.0.0
     */
    @Contract(pure = true)
    public @NonNull InventoryComponent getInputComponent() {
        return inputComponent;
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

}
