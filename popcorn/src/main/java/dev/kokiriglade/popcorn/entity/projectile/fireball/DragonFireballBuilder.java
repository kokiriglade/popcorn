package dev.kokiriglade.popcorn.entity.projectile.fireball;

import org.bukkit.Location;
import org.bukkit.entity.DragonFireball;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Modifies {@link DragonFireball}s
 *
 * @since 2.2.2
 */
@SuppressWarnings({"unused"})
public final class DragonFireballBuilder extends AbstractFireballBuilder<DragonFireballBuilder, DragonFireball> {

    private DragonFireballBuilder(final @NonNull Location location) {
        super(DragonFireball.class, location);
    }

    /**
     * Creates a {@code DragonFireballBuilder}.
     *
     * @param location the {@code Location} to spawn the Dragon Fireball at
     * @return instance of {@code DragonFireballBuilder}
     * @since 2.2.2
     */
    public static @NonNull DragonFireballBuilder create(final @NonNull Location location) {
        return new DragonFireballBuilder(location);
    }
}
