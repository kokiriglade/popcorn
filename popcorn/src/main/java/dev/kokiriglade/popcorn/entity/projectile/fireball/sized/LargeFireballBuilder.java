package dev.kokiriglade.popcorn.entity.projectile.fireball.sized;

import dev.kokiriglade.popcorn.entity.projectile.fireball.sized.AbstractSizedFireballBuilder;
import org.bukkit.Location;
import org.bukkit.entity.LargeFireball;
import org.bukkit.entity.WindCharge;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Modifies {@link LargeFireball}s
 *
 * @since 2.2.2
 */
@SuppressWarnings({"unused"})
public final class LargeFireballBuilder extends AbstractSizedFireballBuilder<LargeFireballBuilder, LargeFireball> {

    private LargeFireballBuilder(final @NonNull Location location) {
        super(LargeFireball.class, location);
    }

    /**
     * Creates a {@code LargeFireballBuilder}.
     *
     * @param location the {@code Location} to spawn the Large Fireball at
     * @return instance of {@code LargeFireballBuilder}
     * @since 2.2.2
     */
    public static @NonNull LargeFireballBuilder create(final @NonNull Location location) {
        return new LargeFireballBuilder(location);
    }

}
