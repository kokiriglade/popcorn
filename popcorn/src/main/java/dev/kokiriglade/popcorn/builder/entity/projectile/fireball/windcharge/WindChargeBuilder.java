package dev.kokiriglade.popcorn.builder.entity.projectile.fireball.windcharge;

import org.bukkit.Location;
import org.bukkit.entity.WindCharge;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Modifies {@link WindCharge}s
 *
 * @since 2.2.2
 */
@SuppressWarnings({"unused", "UnstableApiUsage"})
public final class WindChargeBuilder extends AbstractWindChargeBuilder<WindChargeBuilder, WindCharge> {

    private WindChargeBuilder(final @NonNull Location location) {
        super(WindCharge.class, location);
    }

    /**
     * Creates a {@code WindChargeBuilder}.
     *
     * @param location the {@code Location} to spawn the Wind Charge at
     * @return instance of {@code WindChargeBuilder}
     * @since 2.2.2
     */
    public static @NonNull WindChargeBuilder create(final @NonNull Location location) {
        return new WindChargeBuilder(location);
    }

}
