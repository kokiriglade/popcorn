package dev.kokiriglade.popcorn.entity.projectile.fireball.windcharge;

import dev.kokiriglade.popcorn.entity.projectile.AbstractProjectileBuilder;
import org.bukkit.Location;
import org.bukkit.entity.AbstractWindCharge;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Modifies {@link AbstractWindCharge}s
 *
 * @since 2.2.2
 */
@SuppressWarnings({"unused", "UnstableApiUsage"})
public class AbstractWindChargeBuilder<B extends AbstractProjectileBuilder<B, T>, T extends AbstractWindCharge> extends AbstractProjectileBuilder<B, T> {

    protected AbstractWindChargeBuilder(@NonNull Class<T> entityClass, @NonNull Location location) {
        super(entityClass, location);
    }

}
