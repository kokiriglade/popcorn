package dev.kokiriglade.popcorn.recipe.crafting;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapelessRecipe;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.List;

/**
 * Modifies {@link ShapelessRecipe}s
 *
 * @since 2.1.0
 */
@SuppressWarnings("unused")
public class ShapelessRecipeBuilder extends AbstractCraftingRecipeBuilder<ShapelessRecipeBuilder, ShapelessRecipe> {
    /**
     * Create a new ShapelessRecipeBuilder
     *
     * @param key    the {@code NamespacedKey} identifier
     * @param result the {@code ItemStack} result
     * @since 2.1.0
     */
    private ShapelessRecipeBuilder(@NonNull NamespacedKey key, @NonNull ItemStack result) {
        super(new ShapelessRecipe(key, result));
    }

    /**
     * Creates a {@code ShapelessRecipeBuilder}.
     *
     * @param key    the {@code NamespacedKey} to identify the recipe
     * @param result the {@code ItemStack} result of the recipe
     * @return instance of {@code ShapelessRecipeBuilder}
     * @since 2.1.0
     */
    public static @NonNull ShapelessRecipeBuilder create(
        final @NonNull NamespacedKey key,
        final @NonNull ItemStack result
    ) {
        return new ShapelessRecipeBuilder(key, result);
    }

    /**
     * Gets the list of choices for the recipe.
     *
     * @return the list of choices
     * @since 2.1.0
     */
    public @NonNull List<RecipeChoice> choiceList() {
        return recipe.getChoiceList();
    }

    /**
     * Adds an ingredient to the recipe.
     *
     * @param count the quantity
     * @param item  the ingredient
     * @return the builder
     * @since 2.1.0
     */
    public @NonNull ShapelessRecipeBuilder addIngredient(int count, @NonNull ItemStack item) {
        recipe.addIngredient(count, item);
        return this;
    }

    /**
     * Adds an ingredient to the recipe.
     *
     * @param item the ingredient
     * @return the builder
     * @since 2.1.0
     */
    public @NonNull ShapelessRecipeBuilder addIngredient(@NonNull ItemStack item) {
        recipe.addIngredient(item);
        return this;
    }

    /**
     * Adds an ingredient to the recipe.
     *
     * @param ingredient the ingredient
     * @return the builder
     * @since 2.1.0
     */
    public @NonNull ShapelessRecipeBuilder addIngredient(@NonNull Material ingredient) {
        recipe.addIngredient(ingredient);
        return this;
    }

    /**
     * Adds an ingredient to the recipe.
     *
     * @param ingredient the ingredient
     * @return the builder
     * @since 2.1.0
     */
    public @NonNull ShapelessRecipeBuilder addIngredient(@NonNull RecipeChoice ingredient) {
        recipe.addIngredient(ingredient);
        return this;
    }

    /**
     * Removes an ingredient from the recipe.
     *
     * @param ingredient the ingredient
     * @return the builder
     * @since 2.1.0
     */
    public @NonNull ShapelessRecipeBuilder removeIngredient(@NonNull Material ingredient) {
        recipe.removeIngredient(ingredient);
        return this;
    }

    /**
     * Removes an ingredient from the recipe.
     *
     * @param count      the quantity
     * @param ingredient the ingredient
     * @return the builder
     * @since 2.1.0
     */
    public @NonNull ShapelessRecipeBuilder removeIngredient(int count, @NonNull Material ingredient) {
        recipe.removeIngredient(count, ingredient);
        return this;
    }

    /**
     * Removes an ingredient from the recipe.
     *
     * @param count the quantity
     * @param item  the ingredient
     * @return the builder
     * @since 2.1.0
     */
    public @NonNull ShapelessRecipeBuilder removeIngredient(int count, @NonNull ItemStack item) {
        recipe.removeIngredient(count, item);
        return this;
    }

    /**
     * Removes an ingredient from the recipe.
     *
     * @param item the ingredient
     * @return the builder
     * @since 2.1.0
     */
    public @NonNull ShapelessRecipeBuilder removeIngredient(@NonNull ItemStack item) {
        recipe.removeIngredient(item);
        return this;
    }

    /**
     * Removes an ingredient from the recipe.
     *
     * @param ingredient the ingredient
     * @return the builder
     * @since 2.1.0
     */
    public @NonNull ShapelessRecipeBuilder removeIngredient(@NonNull RecipeChoice ingredient) {
        recipe.removeIngredient(ingredient);
        return this;
    }

}
