package dev.kokiriglade.popcorn.builder.entity.projectile.projectile;

import dev.kokiriglade.popcorn.builder.entity.AbstractEntityBuilder;
import org.bukkit.Location;
import org.bukkit.entity.Projectile;
import org.bukkit.projectiles.ProjectileSource;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * Modifies {@link Projectile}s
 *
 * @param <B> the builder type
 * @param <T> the entity type
 * @since 2.2.2
 */
@SuppressWarnings({"unchecked", "unused"})
public abstract class AbstractProjectileBuilder<B extends AbstractEntityBuilder<B, T>, T extends Projectile> extends AbstractEntityBuilder<B, T> {

    protected boolean hasBeenShot = false;
    protected boolean hasLeftShooter = true;
    protected @Nullable ProjectileSource projectileSource;

    protected AbstractProjectileBuilder(final @NonNull Class<T> entityClass, final @NonNull Location location) {
        super(entityClass, location);
        this.consumers.add(projectile -> {
            projectile.setHasBeenShot(hasBeenShot);
            projectile.setHasLeftShooter(hasLeftShooter);
            projectile.setShooter(projectileSource);
        });
    }

    /**
     * Gets whether the {@code Projectile} has been shot into the world and has sent a projectile shot game event in the next tick.
     *
     * @return whether the {@code Projectile} has been shot
     * @since 2.2.2
     */
    public boolean hasBeenShot() {
        return hasBeenShot;
    }

    /**
     * Sets whether the {@code Projectile} has been shot into the world and has sent a projectile shot game event in the next tick.
     *
     * @param hasBeenShot whether the {@code Projectile} has been shot
     * @return the builder
     * @since 2.2.2
     */
    public @NonNull B hasBeenShot(final boolean hasBeenShot) {
        this.hasBeenShot = hasBeenShot;
        return (B) this;
    }

    /**
     * Gets whether the {@code Projectile} has left the hitbox of their shooter and can now hit entities.
     *
     * @return whether the {@code Projectile} has left the hitbox of their shooter
     * @since 2.2.2
     */
    public boolean hasLeftShooter() {
        return hasLeftShooter;
    }

    /**
     * Sets whether the {@code Projectile} has left the hitbox of their shooter and can now hit entities.
     *
     * @param hasLeftShooter whether the {@code Projectile} has left the hitbox of their shooter
     * @return the builder
     * @since 2.2.2
     */
    public @NonNull B hasLeftShooter(final boolean hasLeftShooter) {
        this.hasLeftShooter = hasLeftShooter;
        return (B) this;
    }

    /**
     * Gets the shooter of the {@code Projectile}.
     *
     * @return the shooter
     * @since 2.2.2
     */
    public ProjectileSource projectileSource() {
        return projectileSource;
    }

    /**
     * Sets the shooter of the {@code Projectile}. Pass {@code null} to remove.
     *
     * @param projectileSource the shooter
     * @return the builder
     * @since 2.2.2
     */
    public @NonNull B projectileSource(final @Nullable ProjectileSource projectileSource) {
        this.projectileSource = projectileSource;
        return (B) this;
    }

}
