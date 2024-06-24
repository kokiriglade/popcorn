package dev.kokiriglade.popcorn.builder.item.special;

import dev.kokiriglade.popcorn.builder.item.AbstractItemBuilder;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ColorableArmorMeta;
import org.bukkit.inventory.meta.trim.ArmorTrim;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * Modifies {@link ItemStack}s that have an {@code ItemMeta} of {@link ColorableArmorMeta}.
 *
 * @since 1.0.0
 */
@SuppressWarnings("unused")
public final class ColorableArmorBuilder extends AbstractItemBuilder<ColorableArmorBuilder, ColorableArmorMeta> {

    private ColorableArmorBuilder(final @NonNull ItemStack itemStack, final @NonNull ColorableArmorMeta itemMeta) {
        super(itemStack, itemMeta);
    }

    /**
     * Creates a {@code ColorableArmorBuilder}.
     *
     * @param itemStack the {@code ItemStack} to base the builder off of
     * @return instance of {@code ColorableArmorBuilder}
     * @throws IllegalArgumentException if the {@code itemStack}'s {@code ItemMeta} is not the correct type
     * @since 1.0.0
     */
    public static @NonNull ColorableArmorBuilder of(final @NonNull ItemStack itemStack) throws IllegalArgumentException {
        return new ColorableArmorBuilder(itemStack, castMeta(itemStack.getItemMeta(), ColorableArmorMeta.class));
    }

    /**
     * Creates a {@code ColorableArmorBuilder}.
     *
     * @param material the {@code Material} to base the builder off of
     * @return instance of {@code ColorableArmorBuilder}
     * @throws IllegalArgumentException if the {@code material} is not an obtainable item,
     *                                  or if the {@code material}'s {@code ItemMeta} is not the correct type
     * @since 1.0.0
     */
    public static @NonNull ColorableArmorBuilder ofType(final @NonNull Material material) throws IllegalArgumentException {
        return ColorableArmorBuilder.of(getItem(material));
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
    public @NonNull ColorableArmorBuilder trim(final @Nullable ArmorTrim trim) {
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

    /**
     * Gets the color.
     *
     * @return the color
     * @since 1.0.0
     */
    public @NonNull Color color() {
        return this.itemMeta.getColor();
    }

    /**
     * Sets the color. Pass {@code null} to reset.
     *
     * @param color the color
     * @return the builder
     * @since 1.0.0
     */
    public @NonNull ColorableArmorBuilder color(final @Nullable Color color) {
        this.itemMeta.setColor(color);
        return this;
    }

}
