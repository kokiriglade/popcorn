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
 * Represents a gui in the form of a brewing stand
 *
 * @since 3.0.0
 */
public class BrewingStandGui extends NamedGui implements InventoryBased {

    /**
     * Represents the inventory component for the first bottle
     */
    private @NonNull InventoryComponent firstBottleComponent = new InventoryComponent(1, 1);

    /**
     * Represents the inventory component for the second bottle
     */
    private @NonNull InventoryComponent secondBottleComponent = new InventoryComponent(1, 1);

    /**
     * Represents the inventory component for the third bottle
     */
    private @NonNull InventoryComponent thirdBottleComponent = new InventoryComponent(1, 1);

    /**
     * Represents the inventory component for the potion ingredient
     */
    private @NonNull InventoryComponent potionIngredientComponent = new InventoryComponent(1, 1);

    /**
     * Represents the inventory component for the blaze powder
     */
    private @NonNull InventoryComponent blazePowderComponent = new InventoryComponent(1, 1);

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
    public BrewingStandGui(final @NonNull Component title) {
        super(title);
    }

    /**
     * Constructs a new brewing stand gui for the given {@code plugin}.
     *
     * @param title  the title/name of this gui.
     * @param plugin the owning plugin of this gui
     * @see #BrewingStandGui(Component)
     * @since 3.0.0
     */
    public BrewingStandGui(final @NonNull Component title, final @NonNull Plugin plugin) {
        super(title, plugin);
    }

    @Override
    public void show(final @NonNull HumanEntity humanEntity) {
        if (isDirty()) {
            this.inventory = createInventory();
            markChanges();
        }

        getInventory().clear();

        getFirstBottleComponent().display(getInventory(), 0);
        getSecondBottleComponent().display(getInventory(), 1);
        getThirdBottleComponent().display(getInventory(), 2);
        getPotionIngredientComponent().display(getInventory(), 3);
        getBlazePowderComponent().display(getInventory(), 4);
        getPlayerInventoryComponent().display();

        if (getPlayerInventoryComponent().hasItem()) {
            final HumanEntityCache humanEntityCache = getHumanEntityCache();

            if (!humanEntityCache.contains(humanEntity)) {
                humanEntityCache.storeAndClear(humanEntity);
            }

            getPlayerInventoryComponent().placeItems(humanEntity.getInventory(), 0);
        }

        humanEntity.openInventory(getInventory());
    }

    @Contract(pure = true)
    @Override
    public @NonNull BrewingStandGui copy() {
        final BrewingStandGui gui = new BrewingStandGui(getTitle(), super.plugin);

        gui.firstBottleComponent = firstBottleComponent.copy();
        gui.secondBottleComponent = secondBottleComponent.copy();
        gui.thirdBottleComponent = thirdBottleComponent.copy();
        gui.potionIngredientComponent = potionIngredientComponent.copy();
        gui.blazePowderComponent = blazePowderComponent.copy();
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
            getFirstBottleComponent().click(this, event, 0);
        } else if (rawSlot == 1) {
            getSecondBottleComponent().click(this, event, 0);
        } else if (rawSlot == 2) {
            getThirdBottleComponent().click(this, event, 0);
        } else if (rawSlot == 3) {
            getPotionIngredientComponent().click(this, event, 0);
        } else if (rawSlot == 4) {
            getBlazePowderComponent().click(this, event, 0);
        } else {
            getPlayerInventoryComponent().click(this, event, rawSlot - 5);
        }
    }

    @Contract(pure = true)
    @Override
    public boolean isPlayerInventoryUsed() {
        return getPlayerInventoryComponent().hasItem();
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
    public @NonNull Inventory createInventory() {
        final Inventory inventory = Bukkit.createInventory(this, InventoryType.BREWING, getTitle());

        addInventory(inventory, this);

        return inventory;
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
     * Gets the inventory component representing the first bottle
     *
     * @return the first bottle component
     * @since 3.0.0
     */
    @Contract(pure = true)
    public @NonNull InventoryComponent getFirstBottleComponent() {
        return firstBottleComponent;
    }

    /**
     * Gets the inventory component representing the second bottle
     *
     * @return the second bottle component
     * @since 3.0.0
     */
    @Contract(pure = true)
    public @NonNull InventoryComponent getSecondBottleComponent() {
        return secondBottleComponent;
    }

    /**
     * Gets the inventory component representing the third bottle
     *
     * @return the third bottle component
     * @since 3.0.0
     */
    @Contract(pure = true)
    public @NonNull InventoryComponent getThirdBottleComponent() {
        return thirdBottleComponent;
    }

    /**
     * Gets the inventory component representing the potion ingredient
     *
     * @return the potion ingredient component
     * @since 3.0.0
     */
    @Contract(pure = true)
    public @NonNull InventoryComponent getPotionIngredientComponent() {
        return potionIngredientComponent;
    }

    /**
     * Gets the inventory component representing the blaze powder
     *
     * @return the blaze powder component
     * @since 3.0.0
     */
    @Contract(pure = true)
    public @NonNull InventoryComponent getBlazePowderComponent() {
        return blazePowderComponent;
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
