package dev.kokiriglade.popcorn.entity;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * Modifies {@link org.bukkit.entity.Explosive} entities
 *
 * @param <B> the builder type
 * @since 2.2.2
 */
@SuppressWarnings("unused")
public interface ExplosiveEntityBuilder<B> {

    /**
     * Gets the radius or yield to be used by this {@code Explosive}'s explosion.
     *
     * @return the yield
     * @since 2.2.2
     */
    @Nullable Float yield();

    /**
     * Sets the radius or yield to be used by this {@code Explosive}'s explosion.
     *
     * @param yield the yield
     * @return the builder
     * @since 2.2.2
     */
    @NonNull B yield(final @Nullable Float yield);

    /**
     * Gets whether the {@code Explosive}'s explosion causes fire.
     *
     * @return whether to cause fire
     * @since 2.2.2
     */
    @Nullable Boolean isIncendiary();

    /**
     * Sets whether the {@code Explosive}'s explosion causes fire.
     *
     * @param isIncendiary whether to cause fire
     * @return the builder
     * @since 2.2.2
     */
    @NonNull B isIncendiary(final @Nullable Boolean isIncendiary);

}
