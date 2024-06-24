package dev.kokiriglade.popcorn.builder.entity.mob.ambient;

import dev.kokiriglade.popcorn.builder.entity.AbstractLivingEntityBuilder;
import org.bukkit.Location;
import org.bukkit.entity.Bat;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * Modifies {@link Bat}s
 *
 * @since 2.2.2
 */
@SuppressWarnings("unused")
public final class BatBuilder extends AbstractLivingEntityBuilder<BatBuilder, Bat> {

    private @Nullable Boolean awake;
    private @Nullable Location targetLocation;

    private BatBuilder(final @NonNull Location location) {
        super(Bat.class, location);
        this.consumers.add(bat -> {
            if (awake != null) {
                bat.setAwake(awake);
            }
            bat.setTargetLocation(targetLocation);
        });
    }

    /**
     * Creates a {@code BatBuilder}.
     *
     * @param location the {@code Location} to spawn the Bat at
     * @return instance of {@code BatBuilder}
     * @since 2.2.2
     */
    public static @NonNull BatBuilder create(final @NonNull Location location) {
        return new BatBuilder(location);
    }

    /**
     * Gets whether the {@code Bat}'s is awake.
     *
     * @return the waking state
     * @since 2.2.2
     */
    public @Nullable Boolean awake() {
        return awake;
    }

    /**
     * Sets whether the {@code Bat}'s is awake.
     *
     * @param awake the waking state
     * @return the builder
     * @since 2.2.2
     */
    public BatBuilder awake(final @Nullable Boolean awake) {
        this.awake = awake;
        return this;
    }

    /**
     * Gets the {@code Location} the {@code Bat} will try to move towards.
     *
     * @return the target location
     * @since 2.2.2
     */
    public @Nullable Location targetLocation() {
        return targetLocation;
    }

    /**
     * Sets the {@code Location} the {@code Bat} will try to move towards.
     *
     * @param targetLocation the target location
     * @return the builder
     * @since 2.2.2
     */
    public @NonNull BatBuilder targetLocation(final @Nullable Location targetLocation) {
        this.targetLocation = targetLocation;
        return this;
    }
}
