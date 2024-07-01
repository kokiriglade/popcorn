package dev.kokiriglade.popcorn.builder.entity.mob.creature.animal;

import dev.kokiriglade.popcorn.builder.entity.mob.creature.AbstractBreedableBuilder;
import org.bukkit.Location;
import org.bukkit.entity.Animals;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.UUID;

/**
 * Modifies {@link Animals}
 *
 * @param <B> the builder type
 * @param <T> the entity type
 * @since 2.2.2
 */
@SuppressWarnings({"unchecked", "unused"})
public abstract class AbstractAnimalBuilder<B extends AbstractBreedableBuilder<B, T>, T extends Animals> extends AbstractBreedableBuilder<B, T> {

    protected @Nullable Integer loveModeTicks;
    protected @Nullable UUID breedCause;

    protected AbstractAnimalBuilder(final @NonNull Class<T> entityClass, final @NonNull Location location) {
        super(entityClass, location);
        this.consumers.add(animal -> {
            if (loveModeTicks != null) {
                animal.setLoveModeTicks(loveModeTicks);
            }
            animal.setBreedCause(breedCause);
        });
    }

    /**
     * Gets the ticks this {@code Animal} is in love mode.
     *
     * @return the ticks
     * @since 2.2.2
     */
    public @Nullable Integer loveModeTicks() {
        return this.loveModeTicks;
    }

    /**
     * Sets the ticks this {@code Animal} is in love mode. Pass {@code null} to reset.
     *
     * @param ticks the ticks
     * @return the builder
     * @since 2.2.2
     */
    public @NonNull B loveModeTicks(final @Nullable Integer ticks) {
        this.loveModeTicks = ticks;
        return (B) this;
    }

    /**
     * Gets the UUID of the entity that caused this {@code Animal} to be in love mode.
     *
     * @return the ticks
     * @since 2.2.2
     */
    public @Nullable UUID breedCause() {
        return this.breedCause;
    }

    /**
     * Sets the UUID of the entity that caused this {@code Animal} to be in love mode. Pass {@code null} to reset.
     *
     * @param uuid the uuid
     * @return the builder
     * @since 2.2.2
     */
    public @NonNull B breedCause(final @Nullable UUID uuid) {
        this.breedCause = uuid;
        return (B) this;
    }

}
