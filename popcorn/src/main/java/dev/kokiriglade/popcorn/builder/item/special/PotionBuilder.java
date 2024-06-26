package dev.kokiriglade.popcorn.builder.item.special;

import dev.kokiriglade.popcorn.builder.item.AbstractItemBuilder;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.List;

/**
 * Modifies {@link ItemStack}s that have an {@code ItemMeta} of {@link PotionMeta}.
 *
 * @since 1.0.0
 */
@SuppressWarnings("unused")
public final class PotionBuilder extends AbstractItemBuilder<PotionBuilder, PotionMeta> {

    private PotionBuilder(final @NonNull ItemStack itemStack, final @NonNull PotionMeta itemMeta) {
        super(itemStack, itemMeta);
    }

    /**
     * Creates a {@code PotionBuilder}.
     *
     * @param itemStack the {@code ItemStack} to base the builder off of
     * @return instance of {@code PotionBuilder}
     * @throws IllegalArgumentException if the {@code itemStack}'s {@code ItemMeta} is not the correct type
     * @since 1.0.0
     */
    public static @NonNull PotionBuilder of(final @NonNull ItemStack itemStack) throws IllegalArgumentException {
        return new PotionBuilder(itemStack, castMeta(itemStack.getItemMeta(), PotionMeta.class));
    }

    /**
     * Creates a {@code PotionBuilder}.
     *
     * @param material the {@code Material} to base the builder off of
     * @return instance of {@code PotionBuilder}
     * @throws IllegalArgumentException if the {@code material} is not an obtainable item,
     *                                  or if the {@code material}'s {@code ItemMeta} is not the correct type
     * @since 1.0.0
     */
    public static @NonNull PotionBuilder ofType(final @NonNull Material material) throws IllegalArgumentException {
        return PotionBuilder.of(getItem(material));
    }

    /**
     * Creates a {@code PotionBuilder} of type {@link Material#POTION}. A convenience method.
     *
     * @return instance of {@code PotionBuilder}
     * @throws IllegalArgumentException if the {@code material} is not an obtainable item,
     *                                  or if the {@code material}'s {@code ItemMeta} is not the correct type
     * @since 1.2.0
     */
    public static @NonNull PotionBuilder ofPotion() throws IllegalArgumentException {
        return ofType(Material.POTION);
    }

    /**
     * Creates a {@code PotionBuilder} of type {@link Material#SPLASH_POTION}. A convenience method.
     *
     * @return instance of {@code PotionBuilder}
     * @throws IllegalArgumentException if the {@code material} is not an obtainable item,
     *                                  or if the {@code material}'s {@code ItemMeta} is not the correct type
     * @since 1.2.0
     */
    public static @NonNull PotionBuilder ofSplashPotion() throws IllegalArgumentException {
        return ofType(Material.SPLASH_POTION);
    }

    /**
     * Creates a {@code PotionBuilder} of type {@link Material#LINGERING_POTION}. A convenience method.
     *
     * @return instance of {@code PotionBuilder}
     * @throws IllegalArgumentException if the {@code material} is not an obtainable item,
     *                                  or if the {@code material}'s {@code ItemMeta} is not the correct type
     * @since 1.2.0
     */
    public static @NonNull PotionBuilder ofLingeringPotion() throws IllegalArgumentException {
        return ofType(Material.LINGERING_POTION);
    }

    /**
     * Gets the custom effects.
     *
     * @return the custom effects
     * @since 1.0.0
     */
    public @NonNull List<@NonNull PotionEffect> customEffects() {
        return this.itemMeta.getCustomEffects();
    }

    /**
     * Sets the custom effects. Pass {@code null} to reset.
     *
     * @param customEffects custom effects
     * @return the builder
     * @since 1.0.0
     */
    public @NonNull PotionBuilder customEffects(final @Nullable List<@NonNull PotionEffect> customEffects) {
        this.itemMeta.clearCustomEffects();
        if (customEffects != null) {
            for (final @NonNull PotionEffect item : customEffects) {
                this.itemMeta.addCustomEffect(item, true);
            }
        }
        return this;
    }

    /**
     * Adds a custom effect.
     *
     * @param customEffect the custom effect to add
     * @param overwrite    whether to overwrite {@code PotionEffect}s of the same type
     * @return the builder
     * @since 1.0.0
     */
    public @NonNull PotionBuilder addCustomEffect(final @NonNull PotionEffect customEffect, final boolean overwrite) {
        this.itemMeta.addCustomEffect(customEffect, overwrite);
        return this;
    }

    /**
     * Removes a custom effect type.
     *
     * @param customEffectType the custom effect type to remove
     * @return the builder
     * @since 1.0.0
     */
    public @NonNull PotionBuilder removeCustomEffect(final @NonNull PotionEffectType... customEffectType) {
        for (final PotionEffectType item : customEffectType) {
            this.itemMeta.removeCustomEffect(item);
        }
        return this;
    }

    /**
     * Gets whether a custom effect type is present.
     *
     * @param customEffectType the custom effect type
     * @return the builder
     * @since 1.0.0
     */
    public boolean hasCustomEffect(final @NonNull PotionEffectType customEffectType) {
        return this.itemMeta.hasCustomEffect(customEffectType);
    }

    /**
     * Gets the {@code Color}.
     *
     * @return the {@code Color}
     * @since 1.0.0
     */
    public @Nullable Color color() {
        return this.itemMeta.getColor();
    }

    /**
     * Sets the {@code Color}. Pass {@code null} to reset.
     *
     * @param color the {@code Color}
     * @return the builder
     * @since 1.0.0
     */
    public @NonNull PotionBuilder color(final @Nullable Color color) {
        this.itemMeta.setColor(color);
        return this;
    }

    /**
     * Gets the base {@code PotionData}.
     *
     * @return the base {@code PotionData}
     * @since 1.0.0
     */
    public @Nullable PotionType basePotionData() {
        return this.itemMeta.getBasePotionType();
    }

    /**
     * Sets the base {@code PotionData}.
     *
     * @param basePotionData the base {@code PotionData}
     * @return the builder
     * @since 1.0.0
     */
    public @NonNull PotionBuilder basePotionData(final @NonNull PotionType basePotionData) {
        this.itemMeta.setBasePotionType(basePotionData);
        return this;
    }

}
