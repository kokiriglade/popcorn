package dev.kokiriglade.popcorn.builder.entity.projectile.fireball;

import dev.kokiriglade.popcorn.builder.entity.ExplosiveEntityBuilder;
import dev.kokiriglade.popcorn.builder.entity.projectile.AbstractProjectileBuilder;
import org.bukkit.Location;
import org.bukkit.entity.Fireball;
import org.bukkit.util.Vector;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * Modifies {@link Fireball}s
 *
 * @param <B> the builder type
 * @param <T> the entity type
 * @since 2.2.2
 */
@SuppressWarnings({"unchecked", "unused"})
public class AbstractFireballBuilder<B extends AbstractProjectileBuilder<B, T>, T extends Fireball> extends AbstractProjectileBuilder<B, T> implements ExplosiveEntityBuilder<B> {

    protected @Nullable Float yield;
    protected @Nullable Boolean isIncendiary;
    protected @Nullable Vector acceleration;

    protected AbstractFireballBuilder(final @NonNull Class<T> entityClass, final @NonNull Location location) {
        super(entityClass, location);
        this.consumers.add(fireball -> {
            if (yield != null) {
                fireball.setYield(yield);
            }
            if (isIncendiary != null) {
                fireball.setIsIncendiary(isIncendiary);
            }
            if (acceleration != null) {
                fireball.setAcceleration(acceleration);
            }
        });
    }

    @Override
    public @Nullable Float yield() {
        return this.yield;
    }

    @Override
    public @NonNull B yield(final @Nullable Float yield) {
        this.yield = yield;
        return (B) this;
    }

    @Override
    public @Nullable Boolean isIncendiary() {
        return this.isIncendiary;
    }

    @Override
    public @NonNull B isIncendiary(final @Nullable Boolean isIncendiary) {
        this.isIncendiary = isIncendiary;
        return (B) this;
    }

    /**
     * Gets the acceleration of the {@code Fireball}.
     *
     * @return the acceleration
     * @since 2.2.2
     */
    public @Nullable Vector acceleration() {
        return this.acceleration;
    }

    /**
     * Sets the acceleration of the {@code Fireball}.
     *
     * @param acceleration the acceleration
     * @return the builder
     * @since 2.2.2
     */
    public @NonNull B acceleration(final @Nullable Vector acceleration) {
        this.acceleration = acceleration;
        return (B) this;
    }

}
