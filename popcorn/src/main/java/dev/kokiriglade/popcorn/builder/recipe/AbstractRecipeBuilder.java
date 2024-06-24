package dev.kokiriglade.popcorn.builder.recipe;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Modifies {@link Recipe}s
 *
 * @param <B> the builder type
 * @param <R> the recipe type
 * @since 2.1.0
 */
@SuppressWarnings({"unused"})
public abstract class AbstractRecipeBuilder<B extends AbstractRecipeBuilder<B, R>, R extends Recipe> {

    /**
     * The {@code Recipe} to modify during building.
     */
    protected final @NonNull R recipe;

    /**
     * Create a new AbstractRecipeBuilder
     *
     * @param recipe the {@code Recipe}
     * @since 2.1.0
     */
    protected AbstractRecipeBuilder(final @NonNull R recipe) {
        this.recipe = recipe;
    }

    /**
     * Attempts to cast {@code meta} to {@code expectedType},
     * and returns the result if successful.
     *
     * @param meta         the meta
     * @param expectedType the class of the expected type
     * @param <T>          the expected type
     * @return {@code meta} casted to {@code expectedType}
     * @throws IllegalArgumentException if {@code} meta is not the type of {@code expectedType}
     * @since 1.0.0
     */
    protected static <T extends Recipe> T castRecipe(final Recipe meta, final Class<T> expectedType)
        throws IllegalArgumentException {
        try {
            return expectedType.cast(meta);
        } catch (final ClassCastException e) {
            throw new IllegalArgumentException("The Recipe must be of type %s but received Recipe of type %s"
                .formatted(
                    expectedType.getSimpleName(),
                    meta.getClass().getSimpleName()
                )
            );
        }
    }

    /**
     * Gets the result of the recipe.
     *
     * @return the result
     * @since 2.1.0
     */
    public @NonNull ItemStack result() {
        return recipe.getResult();
    }

    /**
     * Builds the {@code Recipe} from the set properties.
     *
     * @return the built {@code Recipe}
     * @since 2.1.0
     */
    public @NonNull R build() {
        return this.recipe;
    }

}
