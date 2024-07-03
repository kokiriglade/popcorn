package dev.kokiriglade.popcorn.builder.entity.projectile.throwable;

import org.bukkit.Location;
import org.bukkit.entity.ThrownExpBottle;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Modifies {@link ThrownExpBottle}s
 *
 * @since 2.2.2
 */
@SuppressWarnings("unused")
public final class ThrownExpBottleBuilder extends AbstractThrowableProjectileBuilder<ThrownExpBottleBuilder, ThrownExpBottle> {

    private ThrownExpBottleBuilder(final @NonNull Location location) {
        super(ThrownExpBottle.class, location);
    }

    /**
     * Creates a {@code ThrownExpBottleBuilder}.
     *
     * @param location the {@code Location} to spawn the Experience Bottle at
     * @return instance of {@code ThrownExpBottleBuilder}
     * @since 2.2.2
     */
    public static @NonNull ThrownExpBottleBuilder create(final @NonNull Location location) {
        return new ThrownExpBottleBuilder(location);
    }

}
