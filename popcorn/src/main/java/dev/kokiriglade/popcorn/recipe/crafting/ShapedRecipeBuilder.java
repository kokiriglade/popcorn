package dev.kokiriglade.popcorn.recipe.crafting;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Map;

/**
 * Modifies {@link ShapedRecipe}s
 *
 * @since 2.1.0
 */
@SuppressWarnings("unused")
public class ShapedRecipeBuilder extends AbstractCraftingRecipeBuilder<ShapedRecipeBuilder, ShapedRecipe> {
    /**
     * Create a new ShapedRecipeBuilder
     *
     * @param key    the {@code NamespacedKey} identifier
     * @param result the {@code ItemStack} result
     * @since 2.1.0
     */
    private ShapedRecipeBuilder(@NonNull NamespacedKey key, @NonNull ItemStack result) {
        super(new ShapedRecipe(key, result));
    }

    /**
     * Creates a {@code ShapedRecipeBuilder}.
     *
     * @param key    the {@code NamespacedKey} to identify the recipe
     * @param result the {@code ItemStack} result of the recipe
     * @return instance of {@code ShapedRecipeBuilder}
     * @since 2.1.0
     */
    public static @NonNull ShapedRecipeBuilder create(
        final @NonNull NamespacedKey key,
        final @NonNull ItemStack result
    ) {
        return new ShapedRecipeBuilder(key, result);
    }

    /**
     * Gets the map of choices for the recipe.
     *
     * @return the map of choices
     * @since 2.1.0
     */
    public @NonNull Map<Character, RecipeChoice> choiceMap() {
        return recipe.getChoiceMap();
    }

    /**
     * Gets the shape for the recipe.
     *
     * @return the shape
     * @since 2.1.0
     */
    public @NonNull String[] shape() {
        return recipe.getShape();
    }

    /**
     * Sets the shape for the recipe.
     *
     * @param shape the shape
     * @return the builder
     * @since 2.1.0
     */
    public @NonNull ShapedRecipeBuilder shape(@NonNull String... shape) {
        recipe.shape(shape);
        return this;
    }

    /**
     * Sets the {@code Material} that a character in the recipe shape refers to
     *
     * @param key        the character
     * @param ingredient the ingredient
     * @return the builder
     * @since 2.1.0
     */
    public @NonNull ShapedRecipeBuilder ingredient(char key, @NonNull Material ingredient) {
        recipe.setIngredient(key, ingredient);
        return this;
    }

    /**
     * Sets the {@code ItemStack} that a character in the recipe shape refers to
     *
     * @param key        the character
     * @param ingredient the ingredient
     * @return the builder
     * @since 2.1.0
     */
    public @NonNull ShapedRecipeBuilder ingredient(char key, @NonNull ItemStack ingredient) {
        recipe.setIngredient(key, ingredient);
        return this;
    }

    /**
     * Sets the {@code RecipeChoice} that a character in the recipe shape refers to
     *
     * @param key        the character
     * @param ingredient the ingredient
     * @return the builder
     * @since 2.1.0
     */
    public @NonNull ShapedRecipeBuilder ingredient(char key, @NonNull RecipeChoice ingredient) {
        recipe.setIngredient(key, ingredient);
        return this;
    }

}
