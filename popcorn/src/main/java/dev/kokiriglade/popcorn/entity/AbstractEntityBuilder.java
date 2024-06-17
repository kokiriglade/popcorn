package dev.kokiriglade.popcorn.entity;

import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Pose;
import org.bukkit.util.Vector;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Modifies {@link Entity Entities}
 *
 * @param <B> the builder type
 * @param <T> the {@link Entity}
 * @since 2.2.2
 */
@SuppressWarnings({"unchecked", "unused"})
public abstract class AbstractEntityBuilder<B extends AbstractEntityBuilder<B, T>, T extends Entity> {

    protected final @NonNull ArrayList<Consumer<T>> consumers = new ArrayList<>();
    protected final @NonNull ArrayList<Entity> passengers = new ArrayList<>();
    private final @NonNull Class<T> entityClass;
    protected @NonNull Location location;
    protected boolean persistent = true;
    protected boolean silent = false;
    protected boolean sneaking = false;
    protected boolean glowing = false;
    protected boolean gravity = true;
    protected boolean invisible = false;
    protected boolean invulnerable = false;
    protected boolean customNameVisible = false;
    protected boolean noPhysics = false;
    protected boolean visibleByDefault = true;
    protected boolean visualFire = false;
    protected boolean lockFreezeTicks = false;
    protected float fallDistance = 0f;
    protected int fireTicks = 0;
    protected int freezeTicks = 0;
    protected @MonotonicNonNull Vector velocity;
    protected @MonotonicNonNull Pose pose;
    protected @Nullable Component customName = null;

    /**
     * Create a new AbstractEntityBuilder
     *
     * @param entityClass the entity
     * @param location    the entity's spawn location
     * @since 2.2.2
     */
    protected AbstractEntityBuilder(
        final @NonNull Class<T> entityClass,
        final @NonNull Location location
    ) {
        this.entityClass = entityClass;
        this.location = location;
        this.consumers.add(entity -> {
            for (Entity passenger : passengers) {
                entity.addPassenger(passenger);
            }
            entity.customName(customName);
            entity.setCustomNameVisible(customNameVisible);
            entity.setFallDistance(fallDistance);
            entity.setFireTicks(fireTicks);
            entity.setFreezeTicks(freezeTicks);
            entity.setGlowing(glowing);
            entity.setGravity(gravity);
            entity.setInvisible(invisible);
            entity.setInvulnerable(invulnerable);
            entity.setNoPhysics(noPhysics);
            entity.setPersistent(persistent);
            if (pose != null) {
                entity.setPose(pose);
            }
            entity.setSilent(silent);
            entity.setSneaking(sneaking);
            if (velocity != null) {
                entity.setVelocity(velocity);
            }
            entity.setVisibleByDefault(visibleByDefault);
            entity.setVisualFire(visualFire);

            entity.lockFreezeTicks(lockFreezeTicks);
        });
    }

    /**
     * Sets the number of freeze ticks for the {@code Entity}.
     *
     * @param ticks the number of freeze ticks
     * @return the builder
     * @since 2.2.2
     */
    public @NonNull B freezeTicks(final int ticks) {
        this.freezeTicks = ticks;
        return (B) this;
    }

    /**
     * Gets the number of freeze ticks for the {@code Entity}.
     *
     * @return the number of freeze ticks
     * @since 2.2.2
     */
    public int freezeTicks() {
        return this.freezeTicks;
    }

    /**
     * Sets the number of fire ticks for the {@code Entity}.
     *
     * @param ticks the number of fire ticks
     * @return the builder
     * @since 2.2.2
     */
    public @NonNull B fireTicks(final int ticks) {
        this.fireTicks = ticks;
        return (B) this;
    }

    /**
     * Gets the number of fire ticks for the {@code Entity}.
     *
     * @return the number of fire ticks
     * @since 2.2.2
     */
    public int fireTicks() {
        return this.fireTicks;
    }

    /**
     * Sets the fall distance for the {@code Entity}.
     *
     * @param distance the fall distance
     * @return the builder
     * @since 2.2.2
     */
    public @NonNull B fallDistance(final float distance) {
        this.fallDistance = distance;
        return (B) this;
    }

    /**
     * Gets the fall distance for the {@code Entity}.
     *
     * @return the fall distance
     * @since 2.2.2
     */
    public float fallDistance() {
        return this.fallDistance;
    }

    /**
     * Sets the pose for the {@code Entity}.
     *
     * @param pose the pose
     * @return the builder
     * @since 2.2.2
     */
    public @NonNull B pose(final @NonNull Pose pose) {
        this.pose = pose;
        return (B) this;
    }

    /**
     * Gets the pose for the {@code Entity}.
     *
     * @return the pose
     * @since 2.2.2
     */
    public @MonotonicNonNull Pose pose() {
        return this.pose;
    }

    /**
     * Sets the velocity for the {@code Entity}.
     *
     * @param velocity the velocity
     * @return the builder
     * @since 2.2.2
     */
    public @NonNull B velocity(final @NonNull Vector velocity) {
        this.velocity = velocity;
        return (B) this;
    }

    /**
     * Gets the velocity for the {@code Entity}.
     *
     * @return the velocity
     * @since 2.2.2
     */
    public @MonotonicNonNull Vector velocity() {
        return this.velocity;
    }

    /**
     * Sets whether the {@code Entity} is visible by default.
     *
     * @param flag whether the {@code Entity} is visible by default
     * @return the builder
     * @since 2.2.2
     */
    public @NonNull B visibleByDefault(boolean flag) {
        this.visibleByDefault = flag;
        return (B) this;
    }

    /**
     * Gets whether the {@code Entity} is visible by default.
     *
     * @return whether the {@code Entity} is visible by default
     * @since 2.2.2
     */
    public boolean visibleByDefault() {
        return this.visibleByDefault;
    }

    /**
     * Sets whether the {@code Entity}'s freeze ticks are locked.
     *
     * @param locked whether the freeze ticks are locked
     * @return the builder
     * @since 2.2.2
     */
    public @NonNull B lockFreezeTicks(boolean locked) {
        this.lockFreezeTicks = locked;
        return (B) this;
    }

    /**
     * Gets whether the {@code Entity}'s freeze ticks are locked.
     *
     * @return whether the freeze ticks are locked
     * @since 2.2.2
     */
    public boolean lockFreezeTicks() {
        return this.lockFreezeTicks;
    }

    /**
     * Sets whether the {@code Entity} has no physics.
     *
     * @param flag whether the {@code Entity} has no physics
     * @return the builder
     * @since 2.2.2
     */
    public @NonNull B noPhysics(boolean flag) {
        this.noPhysics = flag;
        return (B) this;
    }

    /**
     * Gets whether the {@code Entity} has no physics.
     *
     * @return whether the {@code Entity} has no physics
     * @since 2.2.2
     */
    public boolean noPhysics() {
        return this.noPhysics;
    }

    /**
     * Sets whether the {@code Entity} has visual fire.
     *
     * @param flag whether the {@code Entity} has visual fire
     * @return the builder
     * @since 2.2.2
     */
    public @NonNull B visualFire(boolean flag) {
        this.visualFire = flag;
        return (B) this;
    }

    /**
     * Gets whether the {@code Entity} has visual fire.
     *
     * @return whether the {@code Entity} has visual fire
     * @since 2.2.2
     */
    public boolean visualFire() {
        return this.visualFire;
    }

    /**
     * Sets whether the {@code Entity}'s custom name is visible.
     *
     * @param flag whether the custom name is visible
     * @return the builder
     * @since 2.2.2
     */
    public @NonNull B customNameVisible(boolean flag) {
        this.customNameVisible = flag;
        return (B) this;
    }

    /**
     * Gets whether the {@code Entity}'s custom name is visible.
     *
     * @return whether the custom name is visible
     * @since 2.2.2
     */
    public boolean customNameVisible() {
        return this.customNameVisible;
    }

    /**
     * Sets whether the {@code Entity} is glowing.
     *
     * @param flag whether the {@code Entity} is glowing
     * @return the builder
     * @since 2.2.2
     */
    public @NonNull B glowing(boolean flag) {
        this.glowing = flag;
        return (B) this;
    }

    /**
     * Gets whether the {@code Entity} is glowing.
     *
     * @return whether the {@code Entity} is glowing
     * @since 2.2.2
     */
    public boolean glowing() {
        return this.glowing;
    }

    /**
     * Sets whether the {@code Entity} is invisible.
     *
     * @param flag whether the {@code Entity} is invisible
     * @return the builder
     * @since 2.2.2
     */
    public @NonNull B invisible(boolean flag) {
        this.invisible = flag;
        return (B) this;
    }

    /**
     * Gets whether the {@code Entity} is invisible.
     *
     * @return whether the {@code Entity} is invisible
     * @since 2.2.2
     */
    public boolean invisible() {
        return this.invisible;
    }

    /**
     * Sets whether the {@code Entity} is invulnerable.
     *
     * @param flag whether the {@code Entity} is invulnerable
     * @return the builder
     * @since 2.2.2
     */
    public @NonNull B invulnerable(boolean flag) {
        this.invulnerable = flag;
        return (B) this;
    }

    /**
     * Gets whether the {@code Entity} is invulnerable.
     *
     * @return whether the {@code Entity} is invulnerable
     * @since 2.2.2
     */
    public boolean invulnerable() {
        return this.invulnerable;
    }

    /**
     * Sets whether the {@code Entity} is affected by gravity.
     *
     * @param flag whether the {@code Entity} is affected by gravity
     * @return the builder
     * @since 2.2.2
     */
    public @NonNull B gravity(boolean flag) {
        this.gravity = flag;
        return (B) this;
    }

    /**
     * Gets whether the {@code Entity} is affected by gravity.
     *
     * @return whether the {@code Entity} is affected by gravity
     * @since 2.2.2
     */
    public boolean gravity() {
        return this.gravity;
    }

    /**
     * Sets whether the {@code Entity} is persistent.
     *
     * @param flag whether the {@code Entity} is persistent
     * @return the builder
     * @since 2.2.2
     */
    public @NonNull B persistent(boolean flag) {
        this.persistent = flag;
        return (B) this;
    }

    /**
     * Gets whether the {@code Entity} is persistent.
     *
     * @return whether the {@code Entity} is persistent
     * @since 2.2.2
     */
    public boolean persistent() {
        return this.persistent;
    }

    /**
     * Sets whether the {@code Entity} is silent.
     *
     * @param flag whether the {@code Entity} is silent
     * @return the builder
     * @since 2.2.2
     */
    public @NonNull B silent(boolean flag) {
        this.silent = flag;
        return (B) this;
    }

    /**
     * Gets whether the {@code Entity} is silent.
     *
     * @return whether the {@code Entity} is silent
     * @since 2.2.2
     */
    public boolean silent() {
        return this.silent;
    }

    /**
     * Sets whether the {@code Entity} is sneaking.
     *
     * @param flag whether the {@code Entity} is sneaking
     * @return the builder
     * @since 2.2.2
     */
    public @NonNull B sneaking(boolean flag) {
        this.sneaking = flag;
        return (B) this;
    }

    /**
     * Gets whether the {@code Entity} is sneaking.
     *
     * @return whether the {@code Entity} is sneaking
     * @since 2.2.2
     */
    public boolean sneaking() {
        return this.sneaking;
    }


    /**
     * Adds a passenger to the {@code Entity}.
     *
     * @param entity the passenger
     * @return the builder
     * @since 2.2.2
     */
    public @NonNull B addPassenger(final @NonNull Entity entity) {
        this.passengers.add(entity);
        return (B) this;
    }

    /**
     * Removes a passenger to the {@code Entity}.
     *
     * @param entity the passenger
     * @return the builder
     * @since 2.2.2
     */
    public @NonNull B removePassenger(final @NonNull Entity entity) {
        this.passengers.remove(entity);
        return (B) this;
    }

    /**
     * Gets the passengers for the {@code Entity}.
     *
     * @return the passengers
     * @since 2.2.2
     */
    public @NonNull List<Entity> passengers() {
        return this.passengers;
    }

    /**
     * Sets the spawn location for the {@code Entity}.
     *
     * @param location the location where the {@code Entity} will be spawned
     * @return the builder
     * @since 2.2.2
     */
    public @NonNull B location(final @NonNull Location location) {
        this.location = location;
        return (B) this;
    }

    /**
     * Gets the current spawn {@code Location}.
     *
     * @return the current spawn {@code Location}
     * @since 2.2.2
     */
    public @NonNull Location location() {
        return this.location;
    }

    /**
     * Sets the custom name for the {@code Entity}.
     *
     * @param customName the custom name
     * @return the builder
     * @since 2.2.2
     */
    public @NonNull B customName(final @Nullable Component customName) {
        this.customName = customName;
        return (B) this;
    }

    /**
     * Gets the custom name for the {@code Entity}.
     *
     * @return the custom name
     * @since 2.2.2
     */
    public @Nullable Component customName() {
        return this.customName;
    }

    /**
     * Gets the list of consumers that will be run when the {@code Entity} is built (spawned).
     *
     * @return the consumers
     * @since 2.2.2
     */
    public @NonNull List<Consumer<T>> consumers() {
        return this.consumers;
    }

    /**
     * Adds a consumer that will be run when the {@code Entity} is built (spawned)
     *
     * @param consumer the consumer
     * @return the builder
     * @since 2.2.2
     */
    public @NonNull B addConsumer(@NonNull Consumer<T> consumer) {
        this.consumers.add(consumer);
        return (B) this;
    }

    /**
     * Builds the {@code Entity} from the set properties and spawns it into the world.
     *
     * @return the built {@code Entity}
     * @since 2.2.2
     */
    public T build() {
        Consumer<T> finalConsumer = entity -> {
            for (Consumer<T> consumer : consumers) {
                consumer.accept(entity);
            }
        };

        return this.location.getWorld().spawn(
            location,
            entityClass,
            finalConsumer
        );
    }

}
