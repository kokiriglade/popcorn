package dev.kokiriglade.popcorn.entity.mob.creature;

import org.bukkit.Location;
import org.bukkit.entity.Breedable;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * Modifies {@link Breedable} entities
 *
 * @param <B> the builder type
 * @param <T> the entity type
 * @since 2.3.0
 */
@SuppressWarnings({"unchecked", "unused"})
public abstract class AbstractBreedableBuilder<B extends AbstractAgeableBuilder<B, T>, T extends Breedable> extends AbstractAgeableBuilder<B, T> {

    protected @Nullable Boolean breed;
    protected @Nullable Boolean ageLock;

    protected AbstractBreedableBuilder(@NonNull Class<T> entityClass, @NonNull Location location) {
        super(entityClass, location);
        this.consumers.add(breedable -> {
            if (breed != null) {
                breedable.setBreed(breed);
            }
            if (ageLock != null) {
                breedable.setAgeLock(ageLock);
            }
        });
    }

    /**
     * Gets whether the {@code Entity} can breed.
     *
     * @return the ability to breed
     * @since 2.3.0
     */
    public @Nullable Boolean breed() {
        return this.breed;
    }

    /**
     * Sets whether the {@code Entity} can breed. Pass {@code null} to reset.
     *
     * @param breed the ability to breed
     * @return the builder
     * @since 2.3.0
     */
    public @NonNull B breed(@Nullable Boolean breed) {
        this.breed = breed;
        return (B) this;
    }

    /**
     * Gets whether the {@code Entity}'s age is locked.
     *
     * @return the age lock
     * @since 2.3.0
     */
    public @Nullable Boolean ageLock() {
        return this.ageLock;
    }

    /**
     * Sets whether the {@code Entity}'s age is locked. Pass {@code null} to reset.
     *
     * @param lock the age lock
     * @return the builder
     * @since 2.3.0
     */
    public @NonNull B ageLock(@Nullable Boolean lock) {
        this.ageLock = lock;
        return (B) this;
    }
}
