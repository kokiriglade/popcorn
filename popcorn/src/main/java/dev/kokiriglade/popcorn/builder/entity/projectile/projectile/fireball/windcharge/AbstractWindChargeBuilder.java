package dev.kokiriglade.popcorn.builder.entity.projectile.projectile.fireball.windcharge;

import dev.kokiriglade.popcorn.builder.entity.projectile.projectile.AbstractProjectileBuilder;
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

    protected AbstractWindChargeBuilder(final @NonNull Class<T> entityClass, final @NonNull Location location) {
        super(entityClass, location);
    }

}
