package dev.kokiriglade.popcorn.inventory.gui.type;

import dev.kokiriglade.popcorn.inventory.HumanEntityCache;
import dev.kokiriglade.popcorn.inventory.gui.InventoryComponent;
import dev.kokiriglade.popcorn.inventory.gui.type.abstraction.BeaconInventory;
import dev.kokiriglade.popcorn.inventory.gui.type.impl.BeaconInventoryImpl;
import dev.kokiriglade.popcorn.inventory.gui.type.util.Gui;
import dev.kokiriglade.popcorn.inventory.gui.type.util.InventoryBased;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.Contract;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a gui in the form of a beacon
 *
 * @since 3.0.0
 */
public class BeaconGui extends Gui implements InventoryBased {

    /**
     * Represents the payment item inventory component
     */
    @NonNull
    private InventoryComponent paymentItemComponent = new InventoryComponent(1, 1);

    /**
     * Represents the player inventory component
     */
    @NonNull
    private InventoryComponent playerInventoryComponent = new InventoryComponent(9, 4);

    /**
     * An internal beacon inventory
     */
    @NonNull
    private final BeaconInventory beaconInventory = new BeaconInventoryImpl(this);

    /**
     * Constructs a new beacon gui.
     *
     * @since 3.0.0
     */
    public BeaconGui() {
        this(JavaPlugin.getProvidingPlugin(BeaconGui.class));
    }

    /**
     * Constructs a new beacon gui for the given {@code plugin}.
     *
     * @param plugin the owning plugin of this gui
     * @see #BeaconGui()
     * @since 3.0.0
     */
    public BeaconGui(@NonNull Plugin plugin) {
        super(plugin);
    }

    @Override
    public void show(@NonNull HumanEntity humanEntity) {
        if (!(humanEntity instanceof Player)) {
            throw new IllegalArgumentException("Beacons can only be opened by players");
        }

        getInventory().clear();

        getPaymentItemComponent().display(getInventory(), 0);
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

        beaconInventory.openInventory((Player) humanEntity, getPaymentItemComponent().getItem(0, 0));
    }

    @NonNull
    @Contract(pure = true)
    @Override
    public BeaconGui copy() {
        BeaconGui gui = new BeaconGui(super.plugin);

        gui.paymentItemComponent = paymentItemComponent.copy();
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
            getPaymentItemComponent().click(this, event, 0);
        } else {
            getPlayerInventoryComponent().click(this, event, rawSlot - 1);
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
        return Bukkit.createInventory(this, InventoryType.BEACON);
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

        if (slot >= 1 && slot <= 36) {
            beaconInventory.sendItem(player, getPaymentItemComponent().getItem(0, 0));
        } else if (slot == 0 && event.isCancelled()) {
            beaconInventory.sendItem(player, getPaymentItemComponent().getItem(0, 0));

            beaconInventory.clearCursor(player);
        }
    }

    /**
     * Gets the inventory component representing the payment item
     *
     * @return the payment item component
     * @since 3.0.0
     */
    @NonNull
    @Contract(pure = true)
    public InventoryComponent getPaymentItemComponent() {
        return paymentItemComponent;
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

}
