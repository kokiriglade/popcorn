package dev.kokiriglade.popcorn.builder.item.special;

import dev.kokiriglade.popcorn.builder.item.AbstractItemBuilder;
import org.bukkit.FireworkEffect;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkEffectMeta;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * Modifies {@link ItemStack}s that have an {@code ItemMeta} of {@link FireworkEffectMeta}.
 *
 * @since 1.0.0
 */
@SuppressWarnings("unused")
public final class FireworkEffectBuilder extends AbstractItemBuilder<FireworkEffectBuilder, FireworkEffectMeta> {

    private FireworkEffectBuilder(final @NonNull ItemStack itemStack, final @NonNull FireworkEffectMeta itemMeta) {
        super(itemStack, itemMeta);
    }

    /**
     * Creates a {@code FireworkEffectBuilder}.
     *
     * @param itemStack the {@code ItemStack} to base the builder off of
     * @return instance of {@code FireworkEffectBuilder}
     * @throws IllegalArgumentException if the {@code itemStack}'s {@code ItemMeta} is not the correct type
     * @since 1.0.0
     */
    public static @NonNull FireworkEffectBuilder of(final @NonNull ItemStack itemStack) throws IllegalArgumentException {
        return new FireworkEffectBuilder(itemStack, castMeta(itemStack.getItemMeta(), FireworkEffectMeta.class));
    }

    /**
     * Creates a {@code FireworkEffectBuilder}.
     *
     * @param material the {@code Material} to base the builder off of
     * @return instance of {@code FireworkEffectBuilder}
     * @throws IllegalArgumentException if the {@code material} is not an obtainable item,
     *                                  or if the {@code material}'s {@code ItemMeta} is not the correct type
     * @since 1.0.0
     */
    public static @NonNull FireworkEffectBuilder ofType(final @NonNull Material material) throws IllegalArgumentException {
        return FireworkEffectBuilder.of(getItem(material));
    }

    /**
     * Creates a {@code FireworkEffectBuilder} of type {@link Material#FIREWORK_STAR}. A convenience method.
     *
     * @return instance of {@code FireworkEffectBuilder}
     * @throws IllegalArgumentException if the {@code material} is not an obtainable item,
     *                                  or if the {@code material}'s {@code ItemMeta} is not the correct type
     * @since 1.2.0
     */
    public static @NonNull FireworkEffectBuilder ofFireworkStar() throws IllegalArgumentException {
        return ofType(Material.FIREWORK_STAR);
    }

    /**
     * Gets the {@code FireworkEffect}.
     *
     * @return the {@code FireworkEffect}
     * @since 1.0.0
     */
    public @Nullable FireworkEffect fireworkEffect() {
        return this.itemMeta.getEffect();
    }

    /**
     * Sets the {@code FireworkEffect}. Pass {@code null} to reset.
     *
     * @param fireworkEffect the {@code FireworkEffect}
     * @return the builder
     * @since 1.0.0
     */
    public @NonNull FireworkEffectBuilder fireworkEffect(final @Nullable FireworkEffect fireworkEffect) {
        this.itemMeta.setEffect(fireworkEffect);
        return this;
    }

}