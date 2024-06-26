package dev.kokiriglade.popcorn.builder.item.special;

import dev.kokiriglade.popcorn.builder.item.AbstractItemBuilder;
import io.papermc.paper.potion.SuspiciousEffectEntry;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SuspiciousStewMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.List;

/**
 * Modifies {@link ItemStack}s that have an {@code ItemMeta} of {@link SuspiciousStewMeta}.
 *
 * @since 1.0.0
 */
@SuppressWarnings("unused")
public final class SuspiciousStewBuilder extends AbstractItemBuilder<SuspiciousStewBuilder, SuspiciousStewMeta> {

    private SuspiciousStewBuilder(final @NonNull ItemStack itemStack, final @NonNull SuspiciousStewMeta itemMeta) {
        super(itemStack, itemMeta);
    }

    /**
     * Creates a {@code SuspiciousStewBuilder}.
     *
     * @param itemStack the {@code ItemStack} to base the builder off of
     * @return instance of {@code SuspiciousStewBuilder}
     * @throws IllegalArgumentException if the {@code itemStack}'s {@code ItemMeta} is not the correct type
     * @since 1.0.0
     */
    public static @NonNull SuspiciousStewBuilder of(final @NonNull ItemStack itemStack) throws IllegalArgumentException {
        return new SuspiciousStewBuilder(itemStack, castMeta(itemStack.getItemMeta(), SuspiciousStewMeta.class));
    }

    /**
     * Creates a {@code SuspiciousStewBuilder}.
     *
     * @param material the {@code Material} to base the builder off of
     * @return instance of {@code SuspiciousStewBuilder}
     * @throws IllegalArgumentException if the {@code material} is not an obtainable item,
     *                                  or if the {@code material}'s {@code ItemMeta} is not the correct type
     * @since 1.0.0
     */
    public static @NonNull SuspiciousStewBuilder ofType(final @NonNull Material material) throws IllegalArgumentException {
        return SuspiciousStewBuilder.of(getItem(material));
    }

    /**
     * Creates a {@code SuspiciousStewBuilder} of type {@link Material#SUSPICIOUS_STEW}. A convenience method.
     *
     * @return instance of {@code SuspiciousStewBuilder}
     * @throws IllegalArgumentException if the {@code material} is not an obtainable item,
     *                                  or if the {@code material}'s {@code ItemMeta} is not the correct type
     * @since 1.0.0
     */
    public static @NonNull SuspiciousStewBuilder ofSuspiciousStew() throws IllegalArgumentException {
        return ofType(Material.SUSPICIOUS_STEW);
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
    public @NonNull SuspiciousStewBuilder customEffects(final @Nullable List<@NonNull SuspiciousEffectEntry> customEffects) {
        this.itemMeta.clearCustomEffects();
        if (customEffects != null) {
            for (final @NonNull SuspiciousEffectEntry item : customEffects) {
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
    public @NonNull SuspiciousStewBuilder addCustomEffect(final @NonNull SuspiciousEffectEntry customEffect, final boolean overwrite) {
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
    public @NonNull SuspiciousStewBuilder removeCustomEffect(final @NonNull PotionEffectType... customEffectType) {
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

}
