package dev.kokiriglade.popcorn.entity.projectile.arrow;

import dev.kokiriglade.popcorn.entity.projectile.AbstractProjectileBuilder;
import org.bukkit.Location;
import org.bukkit.entity.SpectralArrow;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * Modifies {@link SpectralArrow}s
 *
 * @since 2.2.2
 */
@SuppressWarnings("unused")
public final class SpectralArrowBuilder extends AbstractProjectileBuilder<SpectralArrowBuilder, SpectralArrow> {

    private @Nullable Integer glowingTicks;

    private SpectralArrowBuilder(final @NonNull Location location) {
        super(SpectralArrow.class, location);
        this.consumers.add(spectralArrow -> {
            if (glowingTicks != null) {
                spectralArrow.setGlowingTicks(glowingTicks);
            }
        });
    }

    /**
     * Creates a {@code SpectralArrowBuilder}.
     *
     * @param location the {@code Location} to spawn the Spectral Arrow at
     * @return instance of {@code SpectralArrowBuilder}
     * @since 2.2.2
     */
    public static @NonNull SpectralArrowBuilder create(final @NonNull Location location) {
        return new SpectralArrowBuilder(location);
    }

    /**
     * Gets the amount of time that this arrow will apply the glowing effect for.
     *
     * @return the ticks
     * @since 2.2.2
     */
    public @Nullable Integer glowingTicks() {
        return glowingTicks;
    }

    /**
     * Sets the amount of time that this arrow will apply the glowing effect for.
     *
     * @param glowingTicks the ticks
     * @return the builder
     * @since 2.2.2
     */
    public @NonNull SpectralArrowBuilder glowingTicks(final @Nullable Integer glowingTicks) {
        this.glowingTicks = glowingTicks;
        return this;
    }


}
