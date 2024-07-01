package dev.kokiriglade.popcorn.builder.entity.projectile.projectile.fireball.sized;

import dev.kokiriglade.popcorn.builder.entity.projectile.projectile.fireball.AbstractFireballBuilder;
import org.bukkit.Location;
import org.bukkit.entity.SizedFireball;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * Modifies {@link SizedFireball}s
 *
 * @since 2.2.2
 */
@SuppressWarnings({"unused", "unchecked"})
public class AbstractSizedFireballBuilder<B extends AbstractFireballBuilder<B, T>, T extends SizedFireball> extends AbstractFireballBuilder<B, T> {

    protected @Nullable ItemStack displayItem;

    protected AbstractSizedFireballBuilder(final @NonNull Class<T> entityClass, final @NonNull Location location) {
        super(entityClass, location);
        this.consumers.add(sizedFireball -> {
            if (displayItem != null) {
                sizedFireball.setDisplayItem(displayItem);
            }
        });
    }

    /**
     * Retrieves display {@code ItemStack} for the fireball.
     *
     * @return the display {@code ItemStack}
     * @since 2.2.2
     */
    public @Nullable ItemStack displayItem() {
        return displayItem;
    }

    /**
     * Sets the display {@code ItemStack} for the fireball.
     *
     * @param displayItem the display {@code ItemStack}
     * @return the builder
     * @since 2.2.2
     */
    public @NonNull B setDisplayItem(final @Nullable ItemStack displayItem) {
        this.displayItem = displayItem;
        return (B) this;
    }

}
