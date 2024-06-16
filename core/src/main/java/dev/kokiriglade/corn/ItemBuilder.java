package dev.kokiriglade.corn;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Objects;

/**
 * Modifies {@link ItemStack}s that have an {@code ItemMeta} of {@link ItemMeta}.
 */
@SuppressWarnings({"unused"})
public class ItemBuilder extends AbstractItemBuilder<ItemBuilder, ItemMeta> {
    private ItemBuilder(final @NonNull ItemStack itemStack, final @Nullable ItemMeta itemMeta) {
        super(itemStack, itemMeta != null
            ? itemMeta
            : Objects.requireNonNull(
            Bukkit.getItemFactory().getItemMeta(itemStack.getType())
        ));
    }

    /**
     * Creates an {@code ItemBuilder}.
     *
     * @param itemStack the {@code ItemStack} to base the builder off of
     * @return instance of {@code ItemBuilder}
     */
    public static @NonNull ItemBuilder of(final @NonNull ItemStack itemStack) {
        return new ItemBuilder(itemStack, itemStack.getItemMeta());
    }

    /**
     * Creates an {@code ItemBuilder}.
     *
     * @param material the {@code Material} to base the builder off of
     * @return instance of {@code ItemBuilder}
     * @throws IllegalArgumentException if the {@code material} is not an obtainable item
     */
    public static @NonNull ItemBuilder ofType(final @NonNull Material material) throws IllegalArgumentException {
        return ItemBuilder.of(getItem(material));
    }
}
