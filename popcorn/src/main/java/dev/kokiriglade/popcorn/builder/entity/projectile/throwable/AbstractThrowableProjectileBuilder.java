package dev.kokiriglade.popcorn.builder.entity.projectile.throwable;

import dev.kokiriglade.popcorn.builder.entity.projectile.AbstractProjectileBuilder;
import org.bukkit.Location;
import org.bukkit.entity.ThrowableProjectile;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * Modifies {@link ThrowableProjectile}s
 *
 * @param <B> the builder type
 * @param <T> the entity type
 * @since 2.2.2
 */
@SuppressWarnings({"unchecked", "unused"})
public class AbstractThrowableProjectileBuilder<B extends AbstractProjectileBuilder<B, T>, T extends ThrowableProjectile> extends AbstractProjectileBuilder<B, T> implements ThrowableProjectileBuilder<B> {

    protected @Nullable ItemStack itemStack;

    protected AbstractThrowableProjectileBuilder(final @NonNull Class<T> entityClass, final @NonNull Location location) {
        super(entityClass, location);
        this.consumers.add(throwableProjectile -> {
            if (itemStack != null) {
                throwableProjectile.setItem(itemStack);
            }
        });
    }

    @Override
    public @NonNull B item(final @Nullable ItemStack itemStack) {
        this.itemStack = itemStack;
        return (B) this;
    }

    @Override
    public @Nullable ItemStack item() {
        return this.itemStack;
    }

}
