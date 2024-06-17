package dev.kokiriglade.popcorn.entity;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * Modifies {@link LivingEntity LivingEntities}
 *
 * @param <B> the builder type
 * @param <T> the {@link LivingEntity}
 * @since 2.3.0
 */
@SuppressWarnings({"unchecked", "unused"})
public abstract class AbstractLivingEntityBuilder<B extends AbstractLivingEntityBuilder<B, T>, T extends LivingEntity> extends AbstractEntityBuilder<B, T> {

    protected int arrowsInBody = 0;
    protected int beeStingersInBody = 0;
    protected @Nullable Integer maxAirTicks;
    protected @Nullable Integer remainingAirTicks;
    protected @Nullable Integer maxNoDamageTicks;
    protected @Nullable Integer noDamageTicks;
    protected @Nullable Float bodyYaw;
    protected boolean ai = true;
    protected boolean canPickupItems = true;
    protected boolean collidable = true;
    protected boolean gliding = false;
    protected boolean jumping = false;
    protected @Nullable Entity leashHolder;

    protected AbstractLivingEntityBuilder(
        final @NonNull Class<T> entityClass,
        final @NonNull Location location
    ) {
        super(entityClass, location);
        this.consumers.add(entity -> {
            entity.setAI(ai);
            entity.setArrowsInBody(arrowsInBody, false);
            entity.setBeeStingersInBody(beeStingersInBody);
            if (bodyYaw != null) {
                entity.setBodyYaw(bodyYaw);
            }
            entity.setCanPickupItems(canPickupItems);
            entity.setCollidable(collidable);
            entity.setGliding(gliding);
            entity.setJumping(jumping);
            entity.setLeashHolder(leashHolder);
            if (maxAirTicks != null) {
                entity.setMaximumAir(maxAirTicks);
            }
            if (maxNoDamageTicks != null) {
                entity.setMaximumNoDamageTicks(maxNoDamageTicks);
            }
        });
    }

    /**
     * Gets the {@code LivingEntity}'s remaining air ticks.
     *
     * @return the remaining air ticks
     * @since 2.3.0
     */
    public @Nullable Integer remainingAirTicks() {
        return remainingAirTicks;
    }

    /**
     * Sets the {@code LivingEntity}'s remaining air ticks.
     *
     * @param remainingAirTicks the remaining air ticks
     * @return the builder
     * @since 2.3.0
     */
    public @NonNull B remainingAirTicks(final @Nullable Integer remainingAirTicks) {
        this.remainingAirTicks = remainingAirTicks;
        return (B) this;
    }

    /**
     * Gets the {@code LivingEntity}'s no damage ticks.
     *
     * @return the no damage ticks
     * @since 2.3.0
     */
    public @Nullable Integer noDamageTicks() {
        return noDamageTicks;
    }

    /**
     * Sets the {@code LivingEntity}'s no damage ticks.
     *
     * @param noDamageTicks the no damage ticks
     * @return the builder
     * @since 2.3.0
     */
    public @NonNull B noDamageTicks(final @Nullable Integer noDamageTicks) {
        this.noDamageTicks = noDamageTicks;
        return (B) this;
    }

    /**
     * Gets the {@code LivingEntity}'s max no damage ticks.
     *
     * @return the max no damage ticks
     * @since 2.3.0
     */
    public @Nullable Integer maxNoDamageTicks() {
        return maxNoDamageTicks;
    }

    /**
     * Sets the {@code LivingEntity}'s max no damage ticks. Pass {@code null} to reset.
     *
     * @param maxNoDamageTicks the max no damage ticks
     * @return the builder
     * @since 2.3.0
     */
    public B maxNoDamageTicks(final @Nullable Integer maxNoDamageTicks) {
        this.maxNoDamageTicks = maxNoDamageTicks;
        return (B) this;
    }

    /**
     * Gets the {@code LivingEntity}'s max air ticks.
     *
     * @return the max air ticks
     * @since 2.3.0
     */
    public @Nullable Integer maxAirTicks() {
        return maxAirTicks;
    }

    /**
     * Sets the {@code LivingEntity}'s max air ticks. Pass {@code null} to reset.
     *
     * @param maxAirTicks the max air ticks
     * @return the builder
     * @since 2.3.0
     */
    public B maxAirTicks(final @Nullable Integer maxAirTicks) {
        this.maxAirTicks = maxAirTicks;
        return (B) this;
    }

    /**
     * Gets the {@code LivingEntity}'s leash holder.
     *
     * @return the leash holder
     * @since 2.3.0
     */
    public @Nullable Entity leashHolder() {
        return leashHolder;
    }

    /**
     * Sets the {@code LivingEntity}'s leash holder. Pass {@code null} to remove.
     *
     * @param leashHolder the leash holder
     * @return the builder
     * @since 2.3.0
     */
    public B leashHolder(final @Nullable Entity leashHolder) {
        this.leashHolder = leashHolder;
        return (B) this;
    }

    /**
     * Gets whether the {@code LivingEntity} is jumping.
     *
     * @return whether the {@code LivingEntity} is jumping
     * @since 2.3.0
     */
    public boolean jumping() {
        return this.gliding;
    }

    /**
     * Sets whether the {@code LivingEntity} is jumping.
     *
     * @param flag whether the {@code LivingEntity} is jumping
     * @return the builder
     * @since 2.3.0
     */
    public @NonNull B jumping(final boolean flag) {
        this.jumping = flag;
        return (B) this;
    }

    /**
     * Gets whether the {@code LivingEntity} is gliding.
     *
     * @return whether the {@code LivingEntity} is gliding
     * @since 2.3.0
     */
    public boolean gliding() {
        return this.gliding;
    }

    /**
     * Sets whether the {@code LivingEntity} is gliding.
     *
     * @param flag whether the {@code LivingEntity} is gliding
     * @return the builder
     * @since 2.3.0
     */
    public @NonNull B gliding(final boolean flag) {
        this.gliding = flag;
        return (B) this;
    }

    /**
     * Gets whether the {@code LivingEntity} is collidable.
     *
     * @return whether the {@code LivingEntity} is collidable
     * @since 2.3.0
     */
    public boolean collidable() {
        return this.collidable;
    }

    /**
     * Sets whether the {@code LivingEntity} is collidable.
     *
     * @param flag whether the {@code LivingEntity} is collidable
     * @return the builder
     * @since 2.3.0
     */
    public @NonNull B collidable(final boolean flag) {
        this.collidable = flag;
        return (B) this;
    }

    /**
     * Gets whether the {@code LivingEntity} can pick up items.
     *
     * @return whether the {@code LivingEntity} can pick up items
     * @since 2.3.0
     */
    public boolean canPickupItems() {
        return this.canPickupItems;
    }

    /**
     * Sets whether the {@code LivingEntity} can pick up items.
     *
     * @param flag whether the {@code LivingEntity} can pick up items
     * @return the builder
     * @since 2.3.0
     */
    public @NonNull B canPickupItems(final boolean flag) {
        this.canPickupItems = flag;
        return (B) this;
    }

    /**
     * Gets whether the {@code LivingEntity} will have AI.
     *
     * @return whether the {@code LivingEntity} will have AI
     * @since 2.3.0
     */
    public boolean ai() {
        return this.ai;
    }

    /**
     * Sets whether the {@code LivingEntity} will have AI.
     *
     * @param flag whether the {@code LivingEntity} will have AI
     * @return the builder
     * @since 2.3.0
     */
    public @NonNull B ai(final boolean flag) {
        this.ai = flag;
        return (B) this;
    }

    /**
     * Gets how many arrows will be in the {@code LivingEntity}'s body.
     *
     * @return quantity of arrows
     * @since 2.3.0
     */
    public int arrowsInBody() {
        return this.arrowsInBody;
    }

    /**
     * Sets how many bee stingers will be in the {@code LivingEntity}'s body.
     *
     * @param quantity how many bee stingers
     * @return the builder
     * @since 2.3.0
     */
    public @NonNull B beeStingersInBody(final int quantity) {
        this.beeStingersInBody = quantity;
        return (B) this;
    }

    /**
     * Gets how many bee stingers will be in the {@code LivingEntity}'s body.
     *
     * @return quantity of bee stingers
     * @since 2.3.0
     */
    public int beeStingersInBody() {
        return this.beeStingersInBody;
    }

    /**
     * Gets the {@code LivingEntity}'s body yaw.
     *
     * @return the body yaw
     * @since 2.3.0
     */
    public @Nullable Float bodyYaw() {
        return bodyYaw;
    }

    /**
     * Sets the {@code LivingEntity}'s body yaw. Pass {@code null} to reset.
     *
     * @param bodyYaw the body yaw
     * @return the builder
     * @since 2.3.0
     */
    public @NonNull B bodyYaw(final @Nullable Float bodyYaw) {
        this.bodyYaw = bodyYaw;
        return (B) this;
    }

    /**
     * Sets how many arrows will be in the {@code LivingEntity}'s body.
     *
     * @param quantity how many arrows
     * @return the builder
     * @since 2.3.0
     */
    public @NonNull B arrowsInBody(final int quantity) {
        this.arrowsInBody = quantity;
        return (B) this;
    }

}
