package dev.kokiriglade.popcorn.inventory.gui.type;

import dev.kokiriglade.popcorn.inventory.HumanEntityCache;
import dev.kokiriglade.popcorn.inventory.gui.type.abstraction.AnvilInventory;
import dev.kokiriglade.popcorn.inventory.gui.InventoryComponent;
import dev.kokiriglade.popcorn.inventory.gui.type.impl.AnvilInventoryImpl;
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
import java.util.function.Consumer;
import java.util.logging.Level;

/**
 * Represents a gui in the form of an anvil
 *
 * @since 3.0.0
 */
@SuppressWarnings({"unused"})
public class AnvilGui extends NamedGui implements InventoryBased {

    /**
     * Called whenever the name input is changed.
     */
    @NonNull
    private Consumer<? super String> onNameInputChanged = (name) -> {};

    /**
     * Represents the inventory component for the first item
     */
    @NonNull
    private InventoryComponent firstItemComponent = new InventoryComponent(1, 1);

    /**
     * Represents the inventory component for the second item
     */
    @NonNull
    private InventoryComponent secondItemComponent = new InventoryComponent(1, 1);

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
     * An internal anvil inventory
     */
    @NonNull
    private final AnvilInventory anvilInventory = new AnvilInventoryImpl(this);

    /**
     * The viewers of this gui
     */
    @NonNull
    private final Collection<HumanEntity> viewers = new HashSet<>();

    /**
     * Constructs a new anvil gui
     *
     * @param title the title/name of this gui.
     * @since 3.0.0
     */
    public AnvilGui(@NonNull Component title) {
        super(title);

        this.anvilInventory.subscribeToNameInputChanges(this::callOnRename);
    }

    /**
     * Constructs a new anvil gui for the given {@code plugin}.
     *
     * @param title the title/name of this gui.
     * @param plugin the owning plugin of this gui
     * @see #AnvilGui(Component)
     * @since 3.0.0
     */
    public AnvilGui(@NonNull Component title, @NonNull Plugin plugin) {
        super(title, plugin);

        this.anvilInventory.subscribeToNameInputChanges(this::callOnRename);
    }

    @Override
    public void show(@NonNull HumanEntity humanEntity) {
        if (!(humanEntity instanceof Player)) {
            throw new IllegalArgumentException("Anvils can only be opened by players");
        }

        if (isDirty()) {
            this.inventory = createInventory();
            markChanges();
        }

        getInventory().clear();

        getFirstItemComponent().display(getInventory(), 0);
        getSecondItemComponent().display(getInventory(), 1);
        getResultComponent().display(getInventory(), 2);

        getPlayerInventoryComponent().display();

        if (getPlayerInventoryComponent().hasItem()) {
            HumanEntityCache humanEntityCache = getHumanEntityCache();

            if (!humanEntityCache.contains(humanEntity)) {
                humanEntityCache.storeAndClear(humanEntity);
            }

            getPlayerInventoryComponent().placeItems(humanEntity.getInventory(), 0);
        }

        Inventory inventory = anvilInventory.openInventory((Player) humanEntity, getTitle(), getTopItems());

        addInventory(inventory, this);

        this.viewers.add(humanEntity);
    }

    @NonNull
    @Contract(pure = true)
    @Override
    public AnvilGui copy() {
        AnvilGui gui = new AnvilGui(getTitle(), super.plugin);

        gui.firstItemComponent = firstItemComponent.copy();
        gui.secondItemComponent = secondItemComponent.copy();
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
            getFirstItemComponent().click(this, event, 0);
        } else if (rawSlot == 1) {
            getSecondItemComponent().click(this, event, 0);
        } else if (rawSlot == 2) {
            getResultComponent().click(this, event, 0);
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

    /**
     * Sets the enchantment level cost for this anvil gui. Taking the item from the result slot will not actually remove
     * these levels. Having a cost specified does not impede a player's ability to take the item in the result item,
     * even if the player does not have the specified amount of levels. The cost must be a non-negative number.
     *
     * @param cost the cost
     * @since 3.0.0
     * @throws IllegalArgumentException when the cost is less than zero
     */
    public void setCost(short cost) {
        if (cost < 0){
            throw new IllegalArgumentException("Cost must be non-negative");
        }

        this.anvilInventory.setCost(cost);
    }

    @NonNull
    @Contract(pure = true)
    @Override
    public Inventory createInventory() {
        return Bukkit.createInventory(this, InventoryType.ANVIL, getTitle());
    }

    /**
     * Gets the rename text currently specified in the anvil.
     *
     * @return the rename text
     * @since 3.0.0
     * @see org.bukkit.inventory.AnvilInventory#getRenameText()
     */
    @NonNull
    @Contract(pure = true)
    public String getRenameText() {
        return anvilInventory.getRenameText();
    }

    @Contract(pure = true)
    @Override
    public boolean isPlayerInventoryUsed() {
        return getPlayerInventoryComponent().hasItem();
    }

    @Contract(pure = true)
    @Override
    public int getViewerCount() {
        return this.viewers.size();
    }

    @NonNull
    @Contract(pure = true)
    @Override
    public List<HumanEntity> getViewers() {
        return new ArrayList<>(this.viewers);
    }

    /**
     * Handles an incoming inventory click event
     *
     * @param event the event to handle
     * @since 3.0.0
     * @deprecated no longer used internally
     */
    @Deprecated
    public void handleClickEvent(@NonNull InventoryClickEvent event) {
        int slot = event.getRawSlot();
        Player player = (Player) event.getWhoClicked();

        if (slot >= 3 && slot <= 38) {
            anvilInventory.sendItems(player, getTopItems());
        } else if (slot == 0 || slot == 1) {
            if (event.isCancelled()) {
                if (slot == 0) {
                    anvilInventory.sendFirstItem(player, getFirstItemComponent().getItem(0, 0));
                } else {
                    anvilInventory.sendSecondItem(player, getSecondItemComponent().getItem(0, 0));
                }

                anvilInventory.clearCursor(player);
            }

            anvilInventory.sendResultItem(player, getResultComponent().getItem(0, 0));
        } else if (slot == 2 && !event.isCancelled()) {
            anvilInventory.clearResultItem(player);

            ItemStack resultItem = getResultComponent().getItem(0, 0);

            if (resultItem != null) {
                anvilInventory.setCursor(player, resultItem);
            }
        }
    }

    /**
     * Handles a human entity closing this gui.
     *
     * @param humanEntity the human entity closing the gui
     * @since 3.0.0
     */
    public void handleClose(@NonNull HumanEntity humanEntity) {
        this.viewers.remove(humanEntity);
    }

    /**
     * Sets the consumer that should be called whenever the name input is changed. The argument is the new input. When
     * this consumer is invoked, the value as returned by {@link #getRenameText()} will not have updated yet, hence
     * allowing you to see the old value via that.
     *
     * @param onNameInputChanged the consumer to call when the rename input is changed
     * @since 3.0.0
     */
    public void setOnNameInputChanged(@NonNull Consumer<? super String> onNameInputChanged) {
        this.onNameInputChanged = onNameInputChanged;
    }

    /**
     * Calls the consumer that was specified using {@link #setOnNameInputChanged(Consumer)}, so the consumer that should
     * be called whenever the rename input is changed. Catches and logs all exceptions the consumer might throw.
     *
     * @param newInput the new rename input
     * @since 3.0.0
     */
    private void callOnRename(@NonNull String newInput) {
        try {
            this.onNameInputChanged.accept(newInput);
        } catch (Throwable throwable) {
            String message = "Exception while handling onRename, newInput='" + newInput + "'";

            this.plugin.getLogger().log(Level.SEVERE, message, throwable);
        }
    }

    /**
     * Gets the inventory component representing the first item
     *
     * @return the first item component
     * @since 3.0.0
     */
    @NonNull
    @Contract(pure = true)
    public InventoryComponent getFirstItemComponent() {
        return firstItemComponent;
    }

    /**
     * Gets the inventory component representing the second item
     *
     * @return the second item component
     * @since 3.0.0
     */
    @NonNull
    @Contract(pure = true)
    public InventoryComponent getSecondItemComponent() {
        return secondItemComponent;
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
     * Gets the top items
     *
     * @return the top items
     * @since 3.0.0
     */
    @Nullable
    @Contract(pure = true)
    private ItemStack[] getTopItems() {
        return new ItemStack[] {
            getFirstItemComponent().getItem(0, 0),
            getSecondItemComponent().getItem(0, 0),
            getResultComponent().getItem(0, 0)
        };
    }

}
