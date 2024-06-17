package dev.kokiriglade.popcorn.entity.projectile.throwable;

import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * Modifies {@link org.bukkit.entity.ThrowableProjectile}s
 *
 * @param <B> the builder type
 * @since 2.2.2
 */
@SuppressWarnings("unused")
public interface ThrowableProjectileBuilder<B> {

    /**
     * Sets the item stack to be used by this {@code Projectile}.
     *
     * @param itemStack the ItemStack to set, can be null
     * @return the builder
     * @since 2.2.2
     */
    @NonNull B item(final @Nullable ItemStack itemStack);

    /**
     * Retrieves the item stack used by this {@code Projectile}.
     *
     * @return the ItemStack, or {@code null} if not set
     * @since 2.2.2
     */
    @Nullable ItemStack item();


}
