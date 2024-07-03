package dev.kokiriglade.popcorn.builder.entity.projectile.throwable;

import org.bukkit.Location;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.inventory.meta.PotionMeta;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * Modifies {@link ThrownPotion}s
 *
 * @since 2.2.2
 */
@SuppressWarnings("unused")
public final class ThrownPotionBuilder extends AbstractThrowableProjectileBuilder<ThrownPotionBuilder, ThrownPotion> {

    private @Nullable PotionMeta potionMeta;

    private ThrownPotionBuilder(final @NonNull Location location) {
        super(ThrownPotion.class, location);
        this.consumers.add(thrownPotion -> {
            if (potionMeta != null) {
                thrownPotion.setPotionMeta(potionMeta);
            }
        });
    }

    /**
     * Creates a {@code ThrownPotionBuilder}.
     *
     * @param location the {@code Location} to spawn the ThrownPotion at
     * @return instance of {@code ThrownPotionBuilder}
     * @since 2.2.2
     */
    public static @NonNull ThrownPotionBuilder create(final @NonNull Location location) {
        return new ThrownPotionBuilder(location);
    }

    /**
     * Sets the PotionMeta of this thrown potion.
     *
     * @param potionMeta the PotionMeta to set
     * @return the builder
     * @since 2.2.2
     */
    public @NonNull ThrownPotionBuilder potionMeta(final @Nullable PotionMeta potionMeta) {
        this.potionMeta = potionMeta;
        return this;
    }

    /**
     * Gets the PotionMeta of this thrown potion.
     *
     * @return the builder
     * @since 2.2.2
     */
    public @Nullable PotionMeta potionMeta() {
        return this.potionMeta;
    }

}
