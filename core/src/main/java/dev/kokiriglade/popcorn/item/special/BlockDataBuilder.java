package dev.kokiriglade.popcorn.special;

import dev.kokiriglade.popcorn.AbstractItemBuilder;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockDataMeta;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Modifies {@link ItemStack}s that have an {@code ItemMeta} of {@link BlockDataMeta}.
 *
 * @since 1.0.0
 */
@SuppressWarnings("unused")
public final class BlockDataBuilder extends AbstractItemBuilder<BlockDataBuilder, BlockDataMeta> {

    private BlockDataBuilder(final @NonNull ItemStack itemStack, final @NonNull BlockDataMeta itemMeta) {
        super(itemStack, itemMeta);
    }

    /**
     * Creates a {@code BlockDataBuilder}.
     *
     * @param itemStack the {@code ItemStack} to base the builder off of
     * @return instance of {@code BlockDataBuilder}
     * @throws IllegalArgumentException if the {@code itemStack}'s {@code ItemMeta} is not the correct type
     * @since 1.0.0
     */
    public static @NonNull BlockDataBuilder of(final @NonNull ItemStack itemStack) throws IllegalArgumentException {
        return new BlockDataBuilder(itemStack, castMeta(itemStack.getItemMeta(), BlockDataMeta.class));
    }

    /**
     * Creates a {@code BlockDataBuilder}.
     *
     * @param material the {@code Material} to base the builder off of
     * @return instance of {@code BlockDataBuilder}
     * @throws IllegalArgumentException if the {@code material} is not an obtainable item,
     *                                  or if the {@code material}'s {@code ItemMeta} is not the correct type
     * @since 1.0.0
     */
    public static @NonNull BlockDataBuilder ofType(final @NonNull Material material) throws IllegalArgumentException {
        return BlockDataBuilder.of(getItem(material));
    }

    /**
     * Gets a copy of the {@code BlockData}. Creates a new one if it doesn't currently exist.
     *
     * @param material the material the data should be retrieved in the context of
     * @return the {@code BlockData}
     * @since 1.0.0
     */
    public @NonNull BlockData blockData(final @NonNull Material material) {
        return this.itemMeta.getBlockData(material);
    }

    /**
     * Sets the {@code BlockData}.
     *
     * @param blockData the {@code BlockData}
     * @return the builder
     * @since 1.0.0
     */
    public @NonNull BlockDataBuilder blockData(final @NonNull BlockData blockData) {
        this.itemMeta.setBlockData(blockData);
        return this;
    }

    /**
     * Gets whether a {@code BlockData} is currently attached.
     *
     * @return whether a {@code BlockData} is currently attached
     * @since 1.0.0
     */
    public boolean hasBlockData() {
        return this.itemMeta.hasBlockData();
    }

}