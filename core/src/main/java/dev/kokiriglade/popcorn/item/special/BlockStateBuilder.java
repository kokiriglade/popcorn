package dev.kokiriglade.popcorn.special;

import dev.kokiriglade.popcorn.AbstractItemBuilder;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * Modifies {@link ItemStack}s that have an {@code ItemMeta} of {@link BlockStateMeta}.
 *
 * @since 1.0.0
 */
@SuppressWarnings("unused")
public final class BlockStateBuilder extends AbstractItemBuilder<BlockStateBuilder, BlockStateMeta> {

    private BlockStateBuilder(final @NonNull ItemStack itemStack, final @NonNull BlockStateMeta itemMeta) {
        super(itemStack, itemMeta);
    }

    /**
     * Creates a {@code BlockStateBuilder}.
     *
     * @param itemStack the {@code ItemStack} to base the builder off of
     * @return instance of {@code BlockStateBuilder}
     * @throws IllegalArgumentException if the {@code itemStack}'s {@code ItemMeta} is not the correct type
     * @since 1.0.0
     */
    public static @NonNull BlockStateBuilder of(final @NonNull ItemStack itemStack) throws IllegalArgumentException {
        return new BlockStateBuilder(itemStack, castMeta(itemStack.getItemMeta(), BlockStateMeta.class));
    }

    /**
     * Creates a {@code BlockStateBuilder}.
     *
     * @param material the {@code Material} to base the builder off of
     * @return instance of {@code BlockStateBuilder}
     * @throws IllegalArgumentException if the {@code material} is not an obtainable item,
     *                                  or if the {@code material}'s {@code ItemMeta} is not the correct type
     * @since 1.0.0
     */
    public static @NonNull BlockStateBuilder ofType(final @NonNull Material material) throws IllegalArgumentException {
        return BlockStateBuilder.of(getItem(material));
    }

    /**
     * Gets a copy of {@code BlockState}. Creates a new one if it doesn't currently exist.
     *
     * @return the {@code BlockState}
     * @since 1.0.0
     */
    public @NonNull BlockState blockState() {
        return this.itemMeta.getBlockState();
    }

    /**
     * Sets the {@code BlockState}. Pass {@code null} to remove
     *
     * @param blockState the {@code BlockState}
     * @return the builder
     * @since 1.0.0
     */
    public @NonNull BlockStateBuilder blockState(final @Nullable BlockState blockState) {
        if (blockState == null) {
            this.itemMeta.clearBlockState();
        } else {
            this.itemMeta.setBlockState(blockState);
        }
        return this;
    }

    /**
     * Gets whether a {@code BlockState} is currently attached.
     *
     * @return whether a {@code BlockState} is currently attached
     * @since 1.0.0
     */
    public boolean hasBlockState() {
        return this.itemMeta.hasBlockState();
    }

}