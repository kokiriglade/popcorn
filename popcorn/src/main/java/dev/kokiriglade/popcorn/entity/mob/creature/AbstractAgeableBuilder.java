package dev.kokiriglade.popcorn.entity.mob.creature;

import dev.kokiriglade.popcorn.entity.AbstractLivingEntityBuilder;
import org.bukkit.Location;
import org.bukkit.entity.Ageable;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * Modifies {@link Ageable} entities
 *
 * @param <B> the builder type
 * @param <T> the entity type
 * @since 2.2.2
 */
@SuppressWarnings({"unused", "unchecked"})
public abstract class AbstractAgeableBuilder<B extends AbstractLivingEntityBuilder<B, T>, T extends Ageable> extends AbstractLivingEntityBuilder<B, T> {

    protected @Nullable Integer age;
    protected @Nullable Boolean adult;
    protected @Nullable Boolean baby;

    protected AbstractAgeableBuilder(@NonNull Class<T> entityClass, @NonNull Location location) {
        super(entityClass, location);
        this.consumers.add(ageable -> {
            if (age != null) {
                ageable.setAge(age);
            }
            if (adult != null) {
                ageable.setAdult();
            }
            if (baby != null) {
                ageable.setBaby();
            }
        });
    }

    /**
     * Gets the {@code Entity}'s age.
     *
     * @return the age
     * @since 2.2.2
     */
    public @Nullable Integer age() {
        return this.age;
    }

    /**
     * Sets the {@code Entity}'s age. Pass {@code null} to reset.
     *
     * @param age the age
     * @return the builder
     * @since 2.2.2
     */
    public @NonNull B age(@Nullable Integer age) {
        this.age = age;
        return (B) this;
    }

    /**
     * Gets whether the {@code Entity} is an adult.
     *
     * @return the adult state
     * @since 2.2.2
     */
    public @Nullable Boolean adult() {
        return this.adult;
    }

    /**
     * Sets the {@code Entity} to be an adult.
     *
     * @return the adult state
     * @since 2.2.2
     */
    public @NonNull B setAdult() {
        this.adult = true;
        this.baby = false;
        return (B) this;
    }

    /**
     * Gets whether the {@code Entity} is a baby.
     *
     * @return the baby state
     * @since 2.2.2
     */
    public @Nullable Boolean baby() {
        return this.baby;
    }

    /**
     * Sets the {@code Entity} to be a baby.
     *
     * @return the baby state
     * @since 2.2.2
     */
    public @NonNull B setBaby() {
        this.baby = true;
        this.adult = false;
        return (B) this;
    }
}
