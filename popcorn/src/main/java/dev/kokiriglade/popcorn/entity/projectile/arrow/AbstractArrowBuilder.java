package dev.kokiriglade.popcorn.entity.projectile.arrow;

import dev.kokiriglade.popcorn.entity.projectile.AbstractProjectileBuilder;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.AbstractArrow;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.jetbrains.annotations.ApiStatus;

/**
 * Modifies {@link AbstractArrow}s
 *
 * @param <B> the builder type
 * @param <T> the entity type
 * @since 2.2.2
 */
@SuppressWarnings({"unused", "UnstableApiUsage", "unchecked"})
public abstract class AbstractArrowBuilder<B extends AbstractProjectileBuilder<B, T>, T extends AbstractArrow> extends AbstractProjectileBuilder<B, T> {

    protected @Nullable Boolean critical;
    protected @Nullable Double damage;
    protected @Nullable Sound hitSound;
    protected @Nullable ItemStack itemStack;
    protected AbstractArrow.@Nullable PickupStatus pickupStatus;
    protected @Nullable Integer pierceLevel;
    protected @ApiStatus.Experimental
    @Nullable ItemStack weapon;

    protected AbstractArrowBuilder(@NonNull Class<T> entityClass, @NonNull Location location) {
        super(entityClass, location);
        this.consumers.add(abstractArrow -> {
            if (critical != null) {
                abstractArrow.setCritical(critical);
            }
            if (damage != null) {
                abstractArrow.setDamage(damage);
            }
            if (hitSound != null) {
                abstractArrow.setHitSound(hitSound);
            }
            if (itemStack != null) {
                abstractArrow.setItemStack(itemStack);
            }
            if (pickupStatus != null) {
                abstractArrow.setPickupStatus(pickupStatus);
            }
            if (pierceLevel != null) {
                abstractArrow.setPierceLevel(pierceLevel);
            }
            if (weapon != null) {
                abstractArrow.setWeapon(weapon);
            }
        });
    }

    /**
     * Gets whether the arrow is critical.
     *
     * @return whether the arrow is critical
     * @since 2.2.2
     */
    public @Nullable Boolean critical() {
        return critical;
    }

    /**
     * Sets whether the arrow is critical.
     *
     * @param critical whether the arrow is critical
     * @return the builder
     * @since 2.2.2
     */
    public @NonNull B critical(final @Nullable Boolean critical) {
        this.critical = critical;
        return (B) this;
    }

    /**
     * Gets the arrow's damage.
     *
     * @return the damage
     * @since 2.2.2
     */
    public @Nullable Double damage() {
        return damage;
    }

    /**
     * Sets the arrow's damage.
     *
     * @param damage the damage
     * @return the builder
     * @since 2.2.2
     */
    public @NonNull B damage(final @Nullable Double damage) {
        this.damage = damage;
        return (B) this;
    }

    /**
     * Gets the sound played when the arrow hits.
     *
     * @return the hit sound
     * @since 2.2.2
     */
    public @Nullable Sound hitSound() {
        return hitSound;
    }

    /**
     * Sets the sound to be played when the arrow hits.
     *
     * @param hitSound the hit sound
     * @return the builder
     * @since 2.2.2
     */
    public @NonNull B hitSound(final @Nullable Sound hitSound) {
        this.hitSound = hitSound;
        return (B) this;
    }

    /**
     * Gets the item stack for the arrow.
     *
     * @return the item stack
     * @since 2.2.2
     */
    public @Nullable ItemStack itemStack() {
        return itemStack;
    }

    /**
     * Sets the item stack for the arrow.
     *
     * @param itemStack the item stack
     * @return the builder
     * @since 2.2.2
     */
    public @NonNull B itemStack(final @Nullable ItemStack itemStack) {
        this.itemStack = itemStack;
        return (B) this;
    }

    /**
     * Gets the pickup status of the arrow.
     *
     * @return the pickup status
     * @since 2.2.2
     */
    public AbstractArrow.@Nullable PickupStatus pickupStatus() {
        return pickupStatus;
    }

    /**
     * Sets the pickup status of the arrow.
     *
     * @param pickupStatus the pickup status
     * @return the builder
     * @since 2.2.2
     */
    public @NonNull B pickupStatus(final AbstractArrow.@Nullable PickupStatus pickupStatus) {
        this.pickupStatus = pickupStatus;
        return (B) this;
    }

    /**
     * Gets the pierce level of the arrow.
     *
     * @return the pierce level
     * @since 2.2.2
     */
    public @Nullable Integer pierceLevel() {
        return pierceLevel;
    }

    /**
     * Sets the pierce level of the arrow.
     *
     * @param pierceLevel the pierce level
     * @return the builder
     * @since 2.2.2
     */
    public @NonNull B pierceLevel(final @Nullable Integer pierceLevel) {
        this.pierceLevel = pierceLevel;
        return (B) this;
    }

    /**
     * Gets the weapon associated with the arrow.
     *
     * @return the weapon
     * @since 2.2.2
     */
    @ApiStatus.Experimental
    public @Nullable ItemStack weapon() {
        return weapon;
    }

    /**
     * Sets the weapon associated with the arrow.
     *
     * @param weapon the weapon
     * @return the builder
     * @since 2.2.2
     */
    @ApiStatus.Experimental
    public @NonNull B weapon(final @Nullable ItemStack weapon) {
        this.weapon = weapon;
        return (B) this;
    }

}
