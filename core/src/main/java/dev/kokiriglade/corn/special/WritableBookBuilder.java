package dev.kokiriglade.corn.special;

import dev.kokiriglade.corn.AbstractItemBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.WritableBookMeta;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.common.value.qual.IntRange;

import java.util.List;


/**
 * Modifies {@link ItemStack}s that have an {@code ItemMeta} of {@link WritableBookMeta}.
 */
@SuppressWarnings("unused")
public final class WritableBookBuilder extends AbstractItemBuilder<WritableBookBuilder, WritableBookMeta> {

    private WritableBookBuilder(final @NonNull ItemStack itemStack, final @NonNull WritableBookMeta itemMeta) {
        super(itemStack, itemMeta);
    }

    /**
     * Creates a {@code WritableBookBuilder}.
     *
     * @param itemStack the {@code ItemStack} to base the builder off of
     * @return instance of {@code WritableBookBuilder}
     * @throws IllegalArgumentException if the {@code itemStack}'s {@code ItemMeta} is not the correct type
     */
    public static @NonNull WritableBookBuilder of(final @NonNull ItemStack itemStack) throws IllegalArgumentException {
        return new WritableBookBuilder(itemStack, castMeta(itemStack.getItemMeta(), WritableBookMeta.class));
    }

    /**
     * Creates a {@code WritableBookBuilder}.
     *
     * @param material the {@code Material} to base the builder off of
     * @return instance of {@code WritableBookBuilder}
     * @throws IllegalArgumentException if the {@code material} is not an obtainable item,
     *                                  or if the {@code material}'s {@code ItemMeta} is not the correct type
     */
    public static @NonNull WritableBookBuilder ofType(final @NonNull Material material) throws IllegalArgumentException {
        return WritableBookBuilder.of(getItem(material));
    }

    /**
     * Creates a {@code WritableBookBuilder} of type {@link Material#WRITABLE_BOOK}. A convenience method.
     *
     * @return instance of {@code WritableBookBuilder}
     * @throws IllegalArgumentException if the {@code material} is not an obtainable item,
     *                                  or if the {@code material}'s {@code ItemMeta} is not the correct type
     */
    public static @NonNull WritableBookBuilder ofWritableBook() throws IllegalArgumentException {
        return ofType(Material.WRITABLE_BOOK);
    }

    /**
     * Gets the pages.
     *
     * @return the pages
     */
    public @NonNull List<@NonNull String> pages() {
        return this.itemMeta.getPages();
    }

    /**
     * Sets the pages. Pass {@code null} to reset.
     *
     * @param pages the pages
     * @return the builder
     */
    public @NonNull WritableBookBuilder pages(final @Nullable List<@NonNull String> pages) {
        if (pages == null) {
            this.itemMeta.setPages(List.of());
            return this;
        }

        this.itemMeta.setPages(pages);
        return this;
    }

    /**
     * Gets the page at that index.
     *
     * @param index the index (1-indexed)
     * @return the page
     */
    public @NonNull String getPage(final @IntRange(from = 1) int index) {
        return this.itemMeta.getPage(index);
    }

    /**
     * Sets the page at that index.
     *
     * @param index the index (1-indexed)
     * @param page  the page
     * @return the builder
     */
    public @NonNull WritableBookBuilder setPage(final @IntRange(from = 1) int index, final @NonNull String page) {
        this.itemMeta.setPage(index, page);
        return this;
    }

    /**
     * Adds a page.
     *
     * @param page the page to add
     * @return the builder
     */
    public @NonNull WritableBookBuilder addPage(final @NonNull String... page) {
        this.itemMeta.addPage(page);
        return this;
    }

    /**
     * Removes a page.
     *
     * @param index the index of the page to remove (1-indexed)
     * @return the builder
     */
    public @NonNull WritableBookBuilder removePage(final @IntRange(from = 1) int... index) {
        for (final int i : index) {
            this.itemMeta.setPage(i, "");
        }
        return this;
    }

    /**
     * Gets the page count.
     *
     * @return the page count
     */
    public int pageCount() {
        return this.itemMeta.getPageCount();
    }

}
