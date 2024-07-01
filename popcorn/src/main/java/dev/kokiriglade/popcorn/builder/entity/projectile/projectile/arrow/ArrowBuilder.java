package dev.kokiriglade.popcorn.builder.entity.projectile.projectile.arrow;

import dev.kokiriglade.popcorn.builder.entity.projectile.projectile.AbstractProjectileBuilder;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.entity.Arrow;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionType;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Modifies {@link Arrow}s
 *
 * @since 2.2.2
 */
@SuppressWarnings("unused")
public final class ArrowBuilder extends AbstractProjectileBuilder<ArrowBuilder, Arrow> {

    private final @NonNull List<PotionEffect> effects = new ArrayList<>();
    private @Nullable Color color;
    private @Nullable PotionType potionType;

    private ArrowBuilder(final @NonNull Location location) {
        super(Arrow.class, location);
        this.consumers.add(arrow -> {
            for (final PotionEffect effect : effects) {
                arrow.addCustomEffect(effect, true);
            }
            arrow.setBasePotionType(potionType);
            arrow.setColor(color);
        });
    }

    /**
     * Creates an {@code ArrowBuilder}.
     *
     * @param location the {@code Location} to spawn the Arrow at
     * @return instance of {@code ArrowBuilder}
     * @since 2.2.2
     */
    public static @NonNull ArrowBuilder create(final @NonNull Location location) {
        return new ArrowBuilder(location);
    }

    /**
     * Adds a potion effect to the arrow.
     *
     * @param effect the potion effect to add
     * @return the builder
     * @since 2.2.2
     */
    public @NonNull ArrowBuilder addEffect(final @NonNull PotionEffect effect) {
        effects.add(effect);
        return this;
    }

    /**
     * Removes a potion effect from the arrow.
     *
     * @param effect the potion effect to remove
     * @return the builder
     * @since 2.2.2
     */
    public @NonNull ArrowBuilder removeEffect(final @NonNull PotionEffect effect) {
        effects.remove(effect);
        return this;
    }

    /**
     * Clears all potion effects from the arrow.
     *
     * @return the builder
     * @since 2.2.2
     */
    public @NonNull ArrowBuilder clearEffects() {
        effects.clear();
        return this;
    }

    /**
     * Gets the color of the arrow.
     *
     * @return the color
     * @since 2.2.2
     */
    public @Nullable Color color() {
        return color;
    }

    /**
     * Sets the color of the arrow.
     *
     * @param color the color to set
     * @return the builder
     * @since 2.2.2
     */
    public @NonNull ArrowBuilder color(final @Nullable Color color) {
        this.color = color;
        return this;
    }

    /**
     * Gets the potion type of the arrow.
     *
     * @return the potion type
     * @since 2.2.2
     */
    public @Nullable PotionType potionType() {
        return potionType;
    }

    /**
     * Sets the potion type of the arrow.
     *
     * @param potionType the potion type to set
     * @return the builder
     * @since 2.2.2
     */
    public @NonNull ArrowBuilder potionType(final @Nullable PotionType potionType) {
        this.potionType = potionType;
        return this;
    }

}
