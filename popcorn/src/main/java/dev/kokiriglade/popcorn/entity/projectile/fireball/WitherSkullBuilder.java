package dev.kokiriglade.popcorn.entity.projectile.fireball;

import org.bukkit.Location;
import org.bukkit.entity.WitherSkull;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * Modifies {@link WitherSkull}s
 *
 * @since 2.2.2
 */
@SuppressWarnings({"unused"})
public final class WitherSkullBuilder extends AbstractFireballBuilder<WitherSkullBuilder, WitherSkull> {

    private @Nullable Boolean charged;

    private WitherSkullBuilder(final @NonNull Location location) {
        super(WitherSkull.class, location);
        this.consumers.add(witherSkull -> {
            if (charged != null) {
                witherSkull.setCharged(charged);
            }
        });
    }

    /**
     * Creates a {@code WitherSkullBuilder}.
     *
     * @param location the {@code Location} to spawn the Wither Skull at
     * @return instance of {@code WitherSkullBuilder}
     * @since 2.2.2
     */
    public static @NonNull WitherSkullBuilder create(final @NonNull Location location) {
        return new WitherSkullBuilder(location);
    }

    /**
     * Retrieves the charged status.
     *
     * @return true if the wither skull is charged, otherwise false, or null if not set
     * @since 2.2.2
     */
    public @Nullable Boolean charged() {
        return charged;
    }

    /**
     * Sets the charged status for the wither skull.
     *
     * @param charged true to set the wither skull as charged, false to unset, or null to unset
     * @return the builder
     * @since 2.2.2
     */
    public @NonNull WitherSkullBuilder charged(final @Nullable Boolean charged) {
        this.charged = charged;
        return this;
    }
}
