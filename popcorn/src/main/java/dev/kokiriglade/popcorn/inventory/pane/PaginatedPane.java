package dev.kokiriglade.popcorn.inventory.pane;

import dev.kokiriglade.popcorn.Popcorn;
import dev.kokiriglade.popcorn.builder.item.ItemBuilder;
import dev.kokiriglade.popcorn.inventory.gui.GuiItem;
import dev.kokiriglade.popcorn.inventory.gui.InventoryComponent;
import dev.kokiriglade.popcorn.inventory.gui.type.util.Gui;
import dev.kokiriglade.popcorn.inventory.pane.util.Slot;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.jetbrains.annotations.Contract;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * A pane for panes that should be spread out over multiple pages
 * @since 3.0.0
 */
@SuppressWarnings({"unused"})
public class PaginatedPane extends Pane {

    /**
     * A set of panes for the different pages
     * @since 3.0.0
     */
    private @NonNull Map<Integer, List<Pane>> panes = new HashMap<>();

    /**
     * The current page
     * @since 3.0.0
     */
    private int page;

    /**
     * Creates a new paginated pane
     *
     * @param slot     the slot of the pane
     * @param length   the length of the pane
     * @param height   the height of the pane
     * @param priority the priority of the pane
     * @since 3.0.0
     */
    public PaginatedPane(final @NonNull Slot slot, final int length, final int height, final @NonNull Priority priority) {
        super(slot, length, height, priority);
    }

    /**
     * Creates a new paginated pane
     *
     * @param x        x-axis of slot
     * @param y        y-axis of slot
     * @param length   the length of the pane
     * @param height   the height of the pane
     * @param priority the priority of the pane
     * @since 3.0.0
     */
    public PaginatedPane(final int x, final int y, final int length, final int height, final @NonNull Priority priority) {
        this(Slot.fromXY(x, y), length, height, priority);
    }

    /**
     * Creates a new paginated pane
     *
     * @param slot   the slot of the pane
     * @param length the length of the pane
     * @param height the height of the pane
     * @since 3.0.0
     */
    public PaginatedPane(final @NonNull Slot slot, final int length, final int height) {
        this(slot, length, height, Priority.NORMAL);
    }

    /**
     * Creates a new paginated pane
     *
     * @param x        x-axis of slot
     * @param y        y-axis of slot
     * @param length   the length of the pane
     * @param height   the height of the pane
     * @since 3.0.0
     */
    public PaginatedPane(final int x, final int y, final int length, final int height) {
        super(x, y, length, height);
    }

    /**
     * Creates a new paginated pane
     *
     * @param length   the length of the pane
     * @param height   the height of the pane
     * @since 3.0.0
     */
    public PaginatedPane(final int length, final int height) {
        super(length, height);
    }

    /**
     * Returns the current page
     *
     * @return the current page
     * @since 3.0.0
     */
    public int getPage() {
        return page;
    }

    /**
     * Sets the current displayed page
     *
     * @param page the page
     * @since 3.0.0
     */
    public void setPage(final int page) {
        if (!panes.containsKey(page)) {
            throw new ArrayIndexOutOfBoundsException("page outside range");
        }
        this.page = page;
    }

    /**
     * Returns the amount of pages
     *
     * @return the amount of pages
     * @since 3.0.0
     */
    public int getPages() {
        return panes.size();
    }

    /**
     * Adds the specified pane to a new page. The new page will be at the index one after the highest indexed page
     * currently in this paginated pane. If the highest index pane is {@code Integer.MAX_VALUE}, this method will throw
     * an {@link ArithmeticException}. If this paginated pane has no pages, the index of the newly created page will
     * be zero.
     *
     * @param pane the pane to add to a new page
     * @throws ArithmeticException if the highest indexed page is the maximum value
     * @since 3.0.0
     */
    public void addPage(final @NonNull Pane pane) {
        final List<Pane> list = new ArrayList<>(1);

        list.add(pane);

        if (this.panes.isEmpty()) {
            this.panes.put(0, list);

            return;
        }

        int highest = Integer.MIN_VALUE;

        for (final int page : this.panes.keySet()) {
            if (page > highest) {
                highest = page;
            }
        }

        if (highest == Integer.MAX_VALUE) {
            throw new ArithmeticException("Can't increment page index beyond its maximum value");
        }

        this.panes.put(highest + 1, list);
    }

    /**
     * Assigns a pane to a selected page
     *
     * @param page the page to assign the pane to
     * @param pane the new pane
     * @since 3.0.0
     */
    public void addPane(final int page, final @NonNull Pane pane) {
        if (!this.panes.containsKey(page)) {
            this.panes.put(page, new ArrayList<>());
        }

        this.panes.get(page).add(pane);

        this.panes.get(page).sort(Comparator.comparing(Pane::getPriority));
    }

    /**
     * Populates the PaginatedPane based on the provided list by adding new pages until all items can fit.
     * This can be helpful when dealing with lists of unknown size.
     *
     * @param items  The list to populate the pane with
     * @param plugin the plugin that will be the owner of the items created
     * @see #populateWithItemStacks(List)
     * @since 3.0.0
     */
    public void populateWithItemStacks(final @NonNull List<@NonNull ItemStack> items, final @NonNull Plugin plugin) {
        //Don't do anything if the list is empty
        if (items.isEmpty()) {
            return;
        }

        final int itemsPerPage = this.height * this.length;
        final int pagesNeeded = (int) Math.max(Math.ceil(items.size() / (double) itemsPerPage), 1);

        for (int i = 0; i < pagesNeeded; i++) {
            final OutlinePane page = new OutlinePane(0, 0, this.length, this.height);

            for (int j = 0; j < itemsPerPage; j++) {
                //Check if the loop reached the end of the list
                final int index = i * itemsPerPage + j;

                if (index >= items.size()) {
                    break;
                }

                page.addItem(new GuiItem(items.get(index), plugin));
            }

            this.addPane(i, page);
        }
    }

    /**
     * Populates the PaginatedPane based on the provided list by adding new pages until all items can fit.
     * This can be helpful when dealing with lists of unknown size.
     *
     * @param items The list to populate the pane with
     * @since 3.0.0
     */
    public void populateWithItemStacks(final @NonNull List<ItemStack> items) {
        populateWithItemStacks(items, JavaPlugin.getProvidingPlugin(PaginatedPane.class));
    }

    /**
     * Populates the PaginatedPane based on the provided list by adding new pages until all items can fit.
     * This can be helpful when dealing with lists of unknown size.
     *
     * @param items The list to populate the pane with
     * @since 3.0.0
     */
    @Contract("_ -> fail")
    public void populateWithGuiItems(final @NonNull List<GuiItem> items) {
        //Don't do anything if the list is empty
        if (items.isEmpty()) {
            return;
        }

        final int itemsPerPage = this.height * this.length;
        final int pagesNeeded = (int) Math.max(Math.ceil(items.size() / (double) itemsPerPage), 1);

        for (int i = 0; i < pagesNeeded; i++) {
            final OutlinePane page = new OutlinePane(0, 0, this.length, this.height);

            for (int j = 0; j < itemsPerPage; j++) {
                final int index = i * itemsPerPage + j;

                //Check if the loop reached the end of the list
                if (index >= items.size()) {
                    break;
                }

                page.addItem(items.get(index));
            }

            this.addPane(i, page);
        }
    }

    /**
     * This method creates a list of ItemStacks all with the given {@code material} and the display names.
     * After that it calls {@link #populateWithItemStacks(List)}
     * This method also translates the color char {@code &} for all names.
     *
     * @param displayNames The display names for all the items
     * @param material     The material to use for the {@link org.bukkit.inventory.ItemStack}s
     * @param plugin       the plugin that will be the owner of the created items
     * @see #populateWithNames(List, Material)
     * @since 3.0.0
     */
    public void populateWithNames(final @NonNull List<String> displayNames, final @Nullable Material material, final @NonNull Plugin plugin) {
        if (material == null || material == Material.AIR) {
            return;
        }

        populateWithItemStacks(displayNames.stream().map(name -> ItemBuilder.ofType(material)
            .itemName(Popcorn.miniMessage().deserialize(name))
            .build()).collect(Collectors.toList()), plugin);
    }

    /**
     * This method creates a list of ItemStacks all with the given {@code material} and the display names.
     * After that it calls {@link #populateWithItemStacks(List)}
     * This method also translates the color char {@code &} for all names.
     *
     * @param displayNames The display names for all the items
     * @param material     The material to use for the {@link org.bukkit.inventory.ItemStack}s
     * @since 3.0.0
     */
    public void populateWithNames(final @NonNull List<String> displayNames, final @Nullable Material material) {
        populateWithNames(displayNames, material, JavaPlugin.getProvidingPlugin(PaginatedPane.class));
    }

    @Override
    public void display(final @NonNull InventoryComponent inventoryComponent, final int paneOffsetX, final int paneOffsetY, final int maxLength,
                        final int maxHeight) {
        final List<Pane> panes = this.panes.get(page);

        if (panes == null) {
            return;
        }

        for (final Pane pane : panes) {
            if (!pane.isVisible()) {
                continue;
            }

            final Slot slot = getSlot();

            final int newPaneOffsetX = paneOffsetX + slot.getX(maxLength);
            final int newPaneOffsetY = paneOffsetY + slot.getY(maxLength);
            final int newMaxLength = Math.min(length, maxLength);
            final int newMaxHeight = Math.min(height, maxHeight);

            pane.display(inventoryComponent, newPaneOffsetX, newPaneOffsetY, newMaxLength, newMaxHeight);
        }
    }

    @Override
    public boolean click(final @NonNull Gui gui, final @NonNull InventoryComponent inventoryComponent,
                         final @NonNull InventoryClickEvent event, final int slot, final int paneOffsetX, final int paneOffsetY, final int maxLength,
                         final int maxHeight) {
        final int length = Math.min(this.length, maxLength);
        final int height = Math.min(this.height, maxHeight);

        final Slot paneSlot = getSlot();

        final int xPosition = paneSlot.getX(maxLength);
        final int yPosition = paneSlot.getY(maxLength);

        final int totalLength = inventoryComponent.getLength();

        final int adjustedSlot = slot - (xPosition + paneOffsetX) - totalLength * (yPosition + paneOffsetY);

        final int x = adjustedSlot % totalLength;
        final int y = adjustedSlot / totalLength;

        //this isn't our item
        if (x < 0 || x >= length || y < 0 || y >= height) {
            return false;
        }

        callOnClick(event);

        boolean success = false;

        for (final Pane pane : new ArrayList<>(this.panes.getOrDefault(page, Collections.emptyList()))) {
            if (!pane.isVisible()) {
                continue;
            }

            success = success || pane.click(gui, inventoryComponent, event, slot, paneOffsetX + xPosition,
                paneOffsetY + yPosition, length, height);
        }

        return success;
    }

    @Contract(pure = true)
    @Override
    public @NonNull PaginatedPane copy() {
        final PaginatedPane paginatedPane = new PaginatedPane(getSlot(), length, height, getPriority());

        for (final Map.Entry<Integer, List<Pane>> entry : panes.entrySet()) {
            for (final Pane pane : entry.getValue()) {
                paginatedPane.addPane(entry.getKey(), pane.copy());
            }
        }

        paginatedPane.setVisible(isVisible());
        paginatedPane.onClick = onClick;

        paginatedPane.uuid = uuid;

        paginatedPane.page = page;

        return paginatedPane;
    }

    /**
     * Deletes a page and all its associated panes from this paginated pane. It also decrements the indexes of all pages
     * beyond the specified page by one. For example, given a sequence of pages 0, 1, 2, 3, 4, upon removing page 2, the
     * new sequence of pages will be 0, 1, 2, 3. If the specified page does not exist, then this method will silently do
     * nothing.
     *
     * @param page the page to delete
     * @since 3.0.0
     */
    public void deletePage(final int page) {
        if (this.panes.remove(page) == null) {
            return;
        }

        final Map<Integer, List<Pane>> newPanes = new HashMap<>();

        for (final Map.Entry<Integer, List<Pane>> entry : this.panes.entrySet()) {
            final int index = entry.getKey();
            final List<Pane> panes = entry.getValue();

            if (index > page) {
                newPanes.put(index - 1, panes);
            } else {
                newPanes.put(index, panes);
            }
        }

        this.panes = newPanes;
    }

    @Contract(pure = true)
    @Override
    public @NonNull Collection<Pane> getPanes() {
        final Collection<Pane> panes = new HashSet<>();

        this.panes.forEach((integer, p) -> {
            p.forEach(pane -> panes.addAll(pane.getPanes()));
            panes.addAll(p);
        });

        return panes;
    }

    /**
     * Gets all the panes from inside the specified page of this pane. If the specified page is not existent, this
     * method will throw an {@link IllegalArgumentException}. If the specified page is existent, but doesn't
     * have any panes, the returned collection will be empty. The returned collection is unmodifiable. The returned
     * collection is not synchronized and no guarantees should be made as to the safety of concurrently accessing the
     * returned collection. If synchronized behaviour should be allowed, the returned collection must be synchronized
     * externally.
     *
     * @param page the panes of this page will be returned
     * @return a collection of panes belonging to the specified page
     * @throws IllegalArgumentException if the page does not exist
     * @since 3.0.0
     */
    @Contract(pure = true)
    public @NonNull Collection<Pane> getPanes(final int page) {
        final Collection<Pane> panes = this.panes.get(page);

        if (panes == null) {
            throw new IllegalArgumentException("Invalid page");
        }

        return Collections.unmodifiableCollection(panes);
    }

    @Contract(pure = true)
    @Override
    public @NonNull Collection<GuiItem> getItems() {
        return getPanes().stream().flatMap(pane -> pane.getItems().stream()).collect(Collectors.toList());
    }

    @Override
    public void clear() {
        panes.clear();
    }

}
