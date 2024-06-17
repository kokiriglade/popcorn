package dev.kokiriglade.popcorn.entity.projectile.throwable;

import org.bukkit.Location;
import org.bukkit.entity.EnderPearl;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Modifies {@link EnderPearl}s
 *
 * @since 2.2.2
 */
@SuppressWarnings("unused")
public final class EnderPearlBuilder extends AbstractThrowableProjectileBuilder<EnderPearlBuilder, EnderPearl> {

    private EnderPearlBuilder(final @NonNull Location location) {
        super(EnderPearl.class, location);
    }

    /**
     * Creates an {@code EnderPearlBuilder}.
     *
     * @param location the {@code Location} to spawn the Ender Pearl at
     * @return instance of {@code EnderPearlBuilder}
     * @since 2.2.2
     */
    public static @NonNull EnderPearlBuilder create(final @NonNull Location location) {
        return new EnderPearlBuilder(location);
    }

}
