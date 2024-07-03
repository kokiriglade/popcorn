package dev.kokiriglade.popcorn.builder.entity.projectile.throwable;

import org.bukkit.Location;
import org.bukkit.entity.Snowball;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Modifies {@link Snowball}s
 *
 * @since 2.2.2
 */
@SuppressWarnings("unused")
public final class SnowballBuilder extends AbstractThrowableProjectileBuilder<SnowballBuilder, Snowball> {

    private SnowballBuilder(final @NonNull Location location) {
        super(Snowball.class, location);
    }

    /**
     * Creates a {@code SnowballBuilder}.
     *
     * @param location the {@code Location} to spawn the Snowball at
     * @return instance of {@code SnowballBuilder}
     * @since 2.2.2
     */
    public static @NonNull SnowballBuilder create(final @NonNull Location location) {
        return new SnowballBuilder(location);
    }

}
