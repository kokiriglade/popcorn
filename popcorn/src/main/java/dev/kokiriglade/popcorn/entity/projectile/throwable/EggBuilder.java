package dev.kokiriglade.popcorn.entity.projectile.throwable;

import org.bukkit.Location;
import org.bukkit.entity.Egg;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Modifies {@link Egg}s
 *
 * @since 2.2.2
 */
@SuppressWarnings("unused")
public final class EggBuilder extends AbstractThrowableProjectileBuilder<EggBuilder, Egg> {

    private EggBuilder(final @NonNull Location location) {
        super(Egg.class, location);
    }

    /**
     * Creates an {@code EggBuilder}.
     *
     * @param location the {@code Location} to spawn the Egg at
     * @return instance of {@code EggBuilder}
     * @since 2.2.2
     */
    public static @NonNull EggBuilder create(final @NonNull Location location) {
        return new EggBuilder(location);
    }

}
