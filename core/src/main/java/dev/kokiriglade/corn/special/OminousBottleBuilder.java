package dev.kokiriglade.corn.special;

import dev.kokiriglade.corn.AbstractItemBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.OminousBottleMeta;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Modifies {@link ItemStack}s that have an {@code ItemMeta} of {@link OminousBottleMeta}.
 * @since 1.0.0
 */
@SuppressWarnings("unused")
public final class OminousBottleBuilder extends AbstractItemBuilder<OminousBottleBuilder, OminousBottleMeta> {

    private OminousBottleBuilder(final @NonNull ItemStack itemStack, final @NonNull OminousBottleMeta itemMeta) {
        super(itemStack, itemMeta);
    }

    /**
     * Creates an {@code OminousBottleBuilder}.
     *
     * @param itemStack the {@code ItemStack} to base the builder off of
     * @return instance of {@code OminousBottleBuilder}
     * @throws IllegalArgumentException if the {@code itemStack}'s {@code ItemMeta} is not the correct type
     * @since 1.0.0
     */
    public static @NonNull OminousBottleBuilder of(final @NonNull ItemStack itemStack) throws IllegalArgumentException {
        return new OminousBottleBuilder(itemStack, castMeta(itemStack.getItemMeta(), OminousBottleMeta.class));
    }

    /**
     * Creates an {@code OminousBottleBuilder}.
     *
     * @param material the {@code Material} to base the builder off of
     * @return instance of {@code OminousBottleBuilder}
     * @throws IllegalArgumentException if the {@code material} is not an obtainable item,
     *                                  or if the {@code material}'s {@code ItemMeta} is not the correct type
     * @since 1.0.0
     */
    public static @NonNull OminousBottleBuilder ofType(final @NonNull Material material) throws IllegalArgumentException {
        return OminousBottleBuilder.of(getItem(material));
    }

    /**
     * Creates a {@code OminousBottleBuilder} of type {@link Material#OMINOUS_BOTTLE}. A convenience method.
     *
     * @return instance of {@code OminousBottleBuilder}
     * @throws IllegalArgumentException if the {@code material} is not an obtainable item,
     *                                  or if the {@code material}'s {@code ItemMeta} is not the correct type
     * @since 1.2.0
     */
    public static @NonNull OminousBottleBuilder ofOminousBottle() throws IllegalArgumentException {
        return ofType(Material.OMINOUS_BOTTLE);
    }

    /**
     * Gets the amplifier.
     *
     * @return the amplifier
     * @since 1.0.0
     */
    public int amplifier() {
        return this.itemMeta.getAmplifier();
    }

    /**
     * Sets the amplifier for the Ominous Bottle's bad omen effect.
     *
     * @param amplifier the amplifier
     * @return the builder
     * @since 1.0.0
     */
    public @NonNull OminousBottleBuilder amplifier(final int amplifier) {
        this.itemMeta.setAmplifier(amplifier);
        return this;
    }

    /**
     * Gets whether an amplifier exists.
     *
     * @return whether an amplifier exists
     * @since 1.0.0
     */
    public boolean hasAmplifier() {
        return this.itemMeta.hasAmplifier();
    }

}
