package dev.kokiriglade.corn.special;

import dev.kokiriglade.corn.AbstractItemBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ArmorMeta;
import org.bukkit.inventory.meta.trim.ArmorTrim;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * Modifies {@link ItemStack}s that have an {@code ItemMeta} of {@link ArmorMeta}.
 * @since 1.0.0
 */
@SuppressWarnings("unused")
public final class ArmorBuilder extends AbstractItemBuilder<ArmorBuilder, ArmorMeta> {

    private ArmorBuilder(final @NonNull ItemStack itemStack, final @NonNull ArmorMeta itemMeta) {
        super(itemStack, itemMeta);
    }

    /**
     * Creates an {@code ArmorMetaBuilder}.
     *
     * @param itemStack the {@code ItemStack} to base the builder off of
     * @return instance of {@code ArmorMetaBuilder}
     * @throws IllegalArgumentException if the {@code itemStack}'s {@code ItemMeta} is not the correct type
     * @since 1.0.0
     */
    public static @NonNull ArmorBuilder of(final @NonNull ItemStack itemStack) throws IllegalArgumentException {
        return new ArmorBuilder(itemStack, castMeta(itemStack.getItemMeta(), ArmorMeta.class));
    }

    /**
     * Creates an {@code ArmorMetaBuilder}.
     *
     * @param material the {@code Material} to base the builder off of
     * @return instance of {@code ArmorMetaBuilder}
     * @throws IllegalArgumentException if the {@code material} is not an obtainable item,
     *                                  or if the {@code material}'s {@code ItemMeta} is not the correct type
     * @since 1.0.0
     */
    public static @NonNull ArmorBuilder ofType(final @NonNull Material material) throws IllegalArgumentException {
        return ArmorBuilder.of(getItem(material));
    }

    /**
     * Gets the trim.
     *
     * @return the trim
     * @since 1.0.0
     */
    public @Nullable ArmorTrim trim() {
        return this.itemMeta.getTrim();
    }

    /**
     * Sets the trim. Pass {@code null} to remove
     *
     * @param trim the trim
     * @return the builder
     * @since 1.0.0
     */
    public @NonNull ArmorBuilder trim(final @Nullable ArmorTrim trim) {
        this.itemMeta.setTrim(trim);
        return this;
    }

    /**
     * Gets whether a trim tag exists.
     * If true, a specific trim will be applied.
     *
     * @return whether a trim tag exists
     * @since 1.0.0
     */
    public boolean hasTrim() {
        return this.itemMeta.hasTrim();
    }

}
