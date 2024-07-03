package dev.kokiriglade.popcorn.builder.entity.projectile.fireball.sized;

import org.bukkit.Location;
import org.bukkit.entity.SmallFireball;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Modifies {@link SmallFireball}s
 *
 * @since 2.2.2
 */
@SuppressWarnings({"unused"})
public final class SmallFireballBuilder extends AbstractSizedFireballBuilder<SmallFireballBuilder, SmallFireball> {

    private SmallFireballBuilder(final @NonNull Location location) {
        super(SmallFireball.class, location);
    }

    /**
     * Creates a {@code SmallFireballBuilder}.
     *
     * @param location the {@code Location} to spawn the Small Fireball at
     * @return instance of {@code SmallFireballBuilder}
     * @since 2.2.2
     */
    public static @NonNull SmallFireballBuilder create(final @NonNull Location location) {
        return new SmallFireballBuilder(location);
    }

}
