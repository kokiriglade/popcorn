package dev.kokiriglade.popcorn.builder.entity.mob.creature.animal;

import org.bukkit.Location;
import org.bukkit.entity.Axolotl;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * Modifies {@link Axolotl}s
 *
 * @since 2.2.2
 */
@SuppressWarnings("unused")
public final class AxolotlBuilder extends AbstractAnimalBuilder<AxolotlBuilder, Axolotl> {

    private @Nullable Boolean playingDead;
    private Axolotl.@Nullable Variant variant;

    private AxolotlBuilder(final @NonNull Location location) {
        super(Axolotl.class, location);
        this.consumers.add(axolotl -> {
            if (playingDead != null) {
                axolotl.setPlayingDead(playingDead);
            }
            if (variant != null) {
                axolotl.setVariant(variant);
            }
        });
    }

    /**
     * Creates an {@code AxolotlBuilder}.
     *
     * @param location the {@code Location} to spawn the Axolotl at
     * @return instance of {@code AxolotlBuilder}
     * @since 2.2.2
     */
    public static @NonNull AxolotlBuilder create(final @NonNull Location location) {
        return new AxolotlBuilder(location);
    }

    /**
     * Gets whether the {@code Axolotl} is playing dead.
     *
     * @return the playing dead state
     * @since 2.2.2
     */
    public @Nullable Boolean playingDead() {
        return playingDead;
    }

    /**
     * Sets whether the {@code Axolotl} is playing dead. Pass {@code null} to reset.
     *
     * @param playingDead the playing dead state
     * @return the builder
     * @since 2.2.2
     */
    public @NonNull AxolotlBuilder playingDead(final @Nullable Boolean playingDead) {
        this.playingDead = playingDead;
        return this;
    }

    /**
     * Gets the {@code Axolotl}'s variant.
     *
     * @return the variant
     * @since 2.2.2
     */
    public Axolotl.@Nullable Variant variant() {
        return variant;
    }

    /**
     * Sets the {@code Axolotl}'s variant. Pass {@code null} to reset.
     *
     * @param variant the variant
     * @return the builder
     * @since 2.2.2
     */
    public @NonNull AxolotlBuilder variant(final Axolotl.@Nullable Variant variant) {
        this.variant = variant;
        return this;
    }

}
