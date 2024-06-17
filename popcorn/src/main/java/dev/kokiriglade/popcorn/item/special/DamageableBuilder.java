package dev.kokiriglade.popcorn.item.special;

import dev.kokiriglade.popcorn.item.AbstractItemBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * Modifies {@link ItemStack}s that have an {@code ItemMeta} of {@link Damageable}.
 *
 * @since 1.0.0
 */
@SuppressWarnings("unused")
public final class DamageableBuilder extends AbstractItemBuilder<DamageableBuilder, Damageable> {

    private DamageableBuilder(final @NonNull ItemStack itemStack, final @NonNull Damageable itemMeta) {
        super(itemStack, itemMeta);
    }

    /**
     * Creates a {@code DamageableBuilder}.
     *
     * @param itemStack the {@code ItemStack} to base the builder off of
     * @return instance of {@code DamageableBuilder}
     * @throws IllegalArgumentException if the {@code itemStack}'s {@code ItemMeta} is not the correct type
     * @since 1.0.0
     */
    public static @NonNull DamageableBuilder of(final @NonNull ItemStack itemStack) throws IllegalArgumentException {
        return new DamageableBuilder(itemStack, castMeta(itemStack.getItemMeta(), Damageable.class));
    }

    /**
     * Creates a {@code DamageableBuilder}.
     *
     * @param material the {@code Material} to base the builder off of
     * @return instance of {@code DamageableBuilder}
     * @throws IllegalArgumentException if the {@code material} is not an obtainable item,
     *                                  or if the {@code material}'s {@code ItemMeta} is not the correct type
     * @since 1.0.0
     */
    public static @NonNull DamageableBuilder ofType(final @NonNull Material material) throws IllegalArgumentException {
        return DamageableBuilder.of(getItem(material));
    }

    /**
     * Gets the damage.
     *
     * @return the damage
     * @since 1.0.0
     */
    public @Nullable Integer damage() {
        if (!this.itemMeta.hasDamage()) {
            return null;
        }
        return this.itemMeta.getDamage();
    }

    /**
     * Sets the damage.
     *
     * @param damage the damage
     * @return the builder
     * @since 1.0.0
     */
    public @NonNull DamageableBuilder damage(final @NonNull Integer damage) {
        this.itemMeta.setDamage(damage);
        return this;
    }

    /**
     * Gets the max damage.
     *
     * @return the max damage
     * @since 1.0.0
     */
    public @Nullable Integer maxDamage() {
        if (!this.itemMeta.hasMaxDamage()) {
            return null;
        }
        return this.itemMeta.getMaxDamage();
    }

    /**
     * Sets the max damage.
     *
     * @param maxDamage the max damage
     * @return the builder
     * @since 1.0.0
     */
    public @NonNull DamageableBuilder maxDamage(final @NonNull Integer maxDamage) {
        this.itemMeta.setMaxDamage(maxDamage);
        return this;
    }

}