package dev.kokiriglade.popcorn.builder.entity.projectile.fireball.windcharge;

import org.bukkit.Location;
import org.bukkit.entity.BreezeWindCharge;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Modifies {@link BreezeWindCharge}s
 *
 * @since 2.2.2
 */
@SuppressWarnings({"unused", "UnstableApiUsage"})
public final class BreezeWindChargeBuilder extends AbstractWindChargeBuilder<BreezeWindChargeBuilder, BreezeWindCharge> {

    private BreezeWindChargeBuilder(final @NonNull Location location) {
        super(BreezeWindCharge.class, location);
    }

    /**
     * Creates a {@code WindChargeBuilder}.
     *
     * @param location the {@code Location} to spawn the Breeze Wind Charge at
     * @return instance of {@code WindChargeBuilder}
     * @since 2.2.2
     */
    public static @NonNull BreezeWindChargeBuilder create(final @NonNull Location location) {
        return new BreezeWindChargeBuilder(location);
    }

}
