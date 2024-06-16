package dev.kokiriglade.popcorn.recipe.crafting;

import dev.kokiriglade.popcorn.recipe.AbstractRecipeBuilder;
import net.kyori.adventure.key.Key;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.CraftingRecipe;
import org.bukkit.inventory.recipe.CraftingBookCategory;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Modifies {@link CraftingRecipe}s
 *
 * @param <B> the builder type
 * @param <R> the recipe type
 * @since 2.1.0
 */
@SuppressWarnings({"unchecked", "unused"})
public abstract class AbstractCraftingRecipeBuilder<B extends AbstractCraftingRecipeBuilder<B, R>, R extends CraftingRecipe> extends AbstractRecipeBuilder<AbstractCraftingRecipeBuilder<B, R>, R> {

    /**
     * Create a new AbstractCraftingRecipeBuilder
     *
     * @param recipe the {@code Recipe}
     * @since 2.1.0
     */
    protected AbstractCraftingRecipeBuilder(@NonNull R recipe) {
        super(recipe);
    }

    /**
     * Gets the recipe book category.
     *
     * @return the category
     * @since 2.1.0
     */
    public @NonNull CraftingBookCategory category() {
        return recipe.getCategory();
    }

    /**
     * Sets the recipe book category.
     *
     * @param category the category
     * @return the builder
     * @since 2.1.0
     */
    public @NonNull B category(final @NonNull CraftingBookCategory category) {
        recipe.setCategory(category);
        return (B) this;
    }

    /**
     * Gets the group of the recipe. An empty string denotes no group.
     *
     * @return the category
     * @since 2.1.0
     */
    public @NonNull String group() {
        return recipe.getGroup();
    }

    /**
     * Sets the group of the recipe. An empty string denotes no group.
     *
     * @param group the group
     * @return the builder
     * @since 2.1.0
     */
    public @NonNull B group(final @NonNull String group) {
        recipe.setGroup(group);
        return (B) this;
    }

    /**
     * Gets the namespaced identifier of the recipe.
     *
     * @return the namespaced key
     * @since 2.1.0
     */
    public @NonNull NamespacedKey namespacedKey() {
        return recipe.getKey();
    }

    /**
     * Gets the identifier of the recipe.
     *
     * @return the key
     * @since 2.1.0
     */
    public @NonNull Key key() {
        return recipe.key();
    }

}
