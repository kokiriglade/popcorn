package dev.kokiriglade.popcorn.builder.entity.projectile.projectile.arrow;

import dev.kokiriglade.popcorn.builder.entity.projectile.projectile.throwable.ThrowableProjectileBuilder;
import org.bukkit.Location;
import org.bukkit.entity.ThrowableProjectile;
import org.bukkit.entity.Trident;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * Modifies {@link Trident}s
 *
 * @since 2.2.2
 */
@SuppressWarnings("unused")
public class TridentBuilder extends AbstractArrowBuilder<TridentBuilder, Trident> implements ThrowableProjectileBuilder<TridentBuilder> {

    private @Nullable ItemStack itemStack;
    private @Nullable Boolean glint;
    private @Nullable Integer loyaltyLevel;

    private TridentBuilder(final @NonNull Location location) {
        super(Trident.class, location);
        this.consumers.add(trident -> {
            if (itemStack != null) {
                ((ThrowableProjectile) trident).setItem(itemStack);
            }
            if (glint != null) {
                trident.setGlint(glint);
            }
            if (loyaltyLevel != null) {
                trident.setLoyaltyLevel(loyaltyLevel);
            }
        });
    }

    /**
     * Creates a {@code TridentBuilder}.
     *
     * @param location the {@code Location} to spawn the Trident at
     * @return instance of {@code TridentBuilder}
     * @since 2.2.2
     */
    public static @NonNull TridentBuilder create(final @NonNull Location location) {
        return new TridentBuilder(location);
    }

    @Override
    public @NonNull TridentBuilder item(final @Nullable ItemStack itemStack) {
        this.itemStack = itemStack;
        return this;
    }

    @Override
    public @Nullable ItemStack item() {
        return this.itemStack;
    }

    /**
     * Retrieves the glint status.
     *
     * @return true if the trident has a glint, otherwise false, or null if not set
     * @since 2.2.2
     */
    public @Nullable Boolean glint() {
        return glint;
    }

    /**
     * Sets the glint status for the trident.
     *
     * @param glint true to add a glint, false to remove it, or null to unset
     * @return the builder instance
     * @since 2.2.2
     */
    public @NonNull TridentBuilder glint(final @Nullable Boolean glint) {
        this.glint = glint;
        return this;
    }

    /**
     * Retrieves the loyalty level of the trident.
     *
     * @return the loyalty level, or null if not set
     * @since 2.2.2
     */
    public @Nullable Integer loyaltyLevel() {
        return loyaltyLevel;
    }

    /**
     * Sets the loyalty level for the trident.
     *
     * @param loyaltyLevel the loyalty level to set, or null to unset
     * @return the builder instance
     * @since 2.2.2
     */
    public @NonNull TridentBuilder loyaltyLevel(final @Nullable Integer loyaltyLevel) {
        this.loyaltyLevel = loyaltyLevel;
        return this;
    }
}
