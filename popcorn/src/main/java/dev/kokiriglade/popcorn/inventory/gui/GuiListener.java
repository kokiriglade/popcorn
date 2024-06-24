package dev.kokiriglade.popcorn.inventory.gui;

import dev.kokiriglade.popcorn.inventory.gui.type.util.Gui;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.*;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.inventory.*;
import org.bukkit.plugin.Plugin;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.jetbrains.annotations.Contract;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.logging.Logger;

/**
 * Listens to events for {@link Gui}s. Only one instance of this class gets constructed.
 * (One instance per plugin, but plugins are supposed to shade and relocate IF.)
 *
 * @since 3.0.0
 */
public class GuiListener implements Listener {

    /**
     * The owning plugin of this listener.
     */
    @NonNull
    private final Plugin plugin;

    /**
     * A collection of all {@link Gui} instances that have at least one viewer.
     */
    @NonNull
    private final Set<Gui> activeGuiInstances = new HashSet<>();

    /**
     * Creates a new listener for all guis for the provided {@code plugin}.
     *
     * @param plugin the owning plugin of this listener
     * @since 3.0.0
     */
    public GuiListener(@NonNull Plugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Handles clicks in inventories
     *
     * @param event the event fired
     * @since 3.0.0
     */
    @EventHandler(ignoreCancelled = true)
    public void onInventoryClick(@NonNull InventoryClickEvent event) {
        Gui gui = getGui(event.getInventory());

        if (gui == null) {
            return;
        }

        InventoryView view = event.getView();
        Inventory inventory = view.getInventory(event.getRawSlot());

        if (inventory == null) {
            gui.callOnOutsideClick(event);
            return;
        }

        gui.callOnGlobalClick(event);
        if (inventory.equals(view.getTopInventory())) {
            gui.callOnTopClick(event);
        } else {
            gui.callOnBottomClick(event);
        }

        gui.click(event);
    }

    /**
     * Handles users picking up items while their bottom inventory is in use.
     *
     * @param event the event fired when an entity picks up an item
     * @since 3.0.0
     */
    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onEntityPickupItem(@NonNull EntityPickupItemEvent event) {
        if (!(event.getEntity() instanceof HumanEntity)) {
            return;
        }

        Gui gui = getGui(((HumanEntity) event.getEntity()).getOpenInventory().getTopInventory());

        if (gui == null || !gui.isPlayerInventoryUsed()) {
            return;
        }

        int leftOver = gui.getHumanEntityCache().add((HumanEntity) event.getEntity(), event.getItem().getItemStack());

        if (leftOver == 0) {
            event.getItem().remove();
        } else {
            ItemStack itemStack = event.getItem().getItemStack();

            itemStack.setAmount(leftOver);

            event.getItem().setItemStack(itemStack);
        }

        event.setCancelled(true);
    }

    /**
     * Handles drag events
     *
     * @param event the event fired
     * @since 3.0.0
     */
    @EventHandler
    public void onInventoryDrag(@NonNull InventoryDragEvent event) {
        Gui gui = getGui(event.getInventory());

        if (gui == null) {
            return;
        }

        InventoryView view = event.getView();
        Set<Integer> inventorySlots = event.getRawSlots();

        if (inventorySlots.size() > 1) {
            boolean top = false, bottom = false;

            for (int inventorySlot : inventorySlots) {
                Inventory inventory = view.getInventory(inventorySlot);

                if (view.getTopInventory().equals(inventory)) {
                    top = true;
                } else if (view.getBottomInventory().equals(inventory)) {
                    bottom = true;
                }

                if (top && bottom) {
                    break;
                }
            }

            gui.callOnGlobalDrag(event);

            if (top) {
                gui.callOnTopDrag(event);
            }

            if (bottom) {
                gui.callOnBottomDrag(event);
            }
        } else {
            int index = inventorySlots.toArray(new Integer[0])[0];
            InventoryType.SlotType slotType = view.getSlotType(index);

            boolean even = event.getType() == DragType.EVEN;

            ClickType clickType = even ? ClickType.LEFT : ClickType.RIGHT;
            InventoryAction inventoryAction = even ? InventoryAction.PLACE_SOME : InventoryAction.PLACE_ONE;

            ItemStack previousViewCursor = view.getCursor();
            // Overwrite getCursor in inventory click event to mimic real event fired by Bukkit.
            view.setCursor(event.getOldCursor());
            //this is a fake click event, firing this may cause other plugins to function incorrectly, so keep it local
            InventoryClickEvent inventoryClickEvent = new InventoryClickEvent(view, slotType, index, clickType,
                inventoryAction);

            onInventoryClick(inventoryClickEvent);
            // Restore previous cursor only if someone has not changed it manually in onInventoryClick.
            if (Objects.equals(view.getCursor(), event.getOldCursor())) {
                view.setCursor(previousViewCursor);
            }

            event.setCancelled(inventoryClickEvent.isCancelled());
        }
    }

    /**
     * Handles closing in inventories
     *
     * @param event the event fired
     * @since 3.0.0
     */
    @EventHandler(ignoreCancelled = true)
    public void onInventoryClose(@NonNull InventoryCloseEvent event) {
        Gui gui = getGui(event.getInventory());

        if (gui == null) {
            return;
        }

        HumanEntity humanEntity = event.getPlayer();
        PlayerInventory playerInventory = humanEntity.getInventory();

        //due to a client issue off-hand items appear as ghost items, this updates the off-hand correctly client-side
        playerInventory.setItemInOffHand(playerInventory.getItemInOffHand());

        if (!gui.isUpdating()) {
            gui.callOnClose(event);

            event.getInventory().clear(); //clear inventory to prevent items being put back

            gui.getHumanEntityCache().restoreAndForget(humanEntity);

            if (gui.getViewerCount() == 1) {
                activeGuiInstances.remove(gui);
            }

            //Bukkit doesn't like it if you open an inventory while the previous one is being closed
            Bukkit.getScheduler().runTask(this.plugin, () -> gui.navigateToParent(humanEntity));
        }
    }

    /**
     * Registers newly opened inventories
     *
     * @param event the event fired
     * @since 3.0.0
     */
    @EventHandler(ignoreCancelled = true)
    public void onInventoryOpen(@NonNull InventoryOpenEvent event) {
        Gui gui = getGui(event.getInventory());

        if (gui == null) {
            return;
        }

        activeGuiInstances.add(gui);
    }

    /**
     * Handles the disabling of the plugin
     *
     * @param event the event fired
     * @since 3.0.0
     */
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPluginDisable(@NonNull PluginDisableEvent event) {
        if (event.getPlugin() != this.plugin) {
            return;
        }

        int counter = 0; //callbacks might open GUIs, eg. in nested menus
        int maxCount = 10;
        while (!activeGuiInstances.isEmpty() && counter++ < maxCount) {
            for (Gui gui : new ArrayList<>(activeGuiInstances)) {
                for (HumanEntity viewer : gui.getViewers()) {
                    viewer.closeInventory();
                }
            }
        }

        if (counter == maxCount) {
            Logger logger = this.plugin.getLogger();

            logger.warning(
                "Unable to close GUIs on plugin disable: they keep getting opened (tried: " + maxCount + " times)"
            );
        }
    }

    /**
     * Gets the gui from the inventory or null if the inventory isn't a gui
     *
     * @param inventory the inventory to get the gui from
     * @return the gui or null if the inventory doesn't have a gui
     * @since 3.0.0
     */
    @Nullable
    @Contract(pure = true)
    private Gui getGui(@NonNull Inventory inventory) {
        Gui gui = Gui.getGui(inventory);

        if (gui != null) {
            return gui;
        }

        InventoryHolder holder = inventory.getHolder();

        if (holder instanceof Gui guiInstance) {
            return guiInstance;
        }

        return null;
    }

}
