package dev.kokiriglade.popcorn.builder.entity;

import org.bukkit.Location;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.FallingBlock;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * Modifies {@link FallingBlock}s
 *
 * @since 2.2.3
 */
@SuppressWarnings("unused")
public final class FallingBlockBuilder extends AbstractEntityBuilder<FallingBlockBuilder, FallingBlock> {

    private @Nullable BlockData blockData;
    private @Nullable BlockState blockState;
    private @Nullable Boolean cancelDrop;
    private @Nullable Float damagePerBlock;
    private @Nullable Boolean dropItem;
    private @Nullable Boolean hurtEntities;
    private @Nullable Integer maxDamage;
    private @Nullable Boolean shouldAutoExpire;

    private FallingBlockBuilder(final @NonNull Location location) {
        super(FallingBlock.class, location);
        this.consumers.add(fallingBlock -> {
            if (blockData != null) {
                fallingBlock.setBlockData(blockData);
            }
            if (blockState != null) {
                fallingBlock.setBlockState(blockState);
            }
            if (cancelDrop != null) {
                fallingBlock.setCancelDrop(cancelDrop);
            }
            if (damagePerBlock != null) {
                fallingBlock.setDamagePerBlock(damagePerBlock);
            }
            if (dropItem != null) {
                fallingBlock.setDropItem(dropItem);
            }
            if (hurtEntities != null) {
                fallingBlock.setHurtEntities(hurtEntities);
            }
            if (maxDamage != null) {
                fallingBlock.setMaxDamage(maxDamage);
            }
            if (shouldAutoExpire != null) {
                fallingBlock.shouldAutoExpire(shouldAutoExpire);
            }
        });
    }

    /**
     * Creates an {@code FallingBlockBuilder}.
     *
     * @param location the {@code Location} to spawn the Falling Block at
     * @return instance of {@code FallingBlockBuilder}
     * @since 2.2.3
     */
    public static @NonNull FallingBlockBuilder create(final @NonNull Location location) {
        return new FallingBlockBuilder(location);
    }

    /**
     * Sets the block data for the falling block.
     *
     * @param blockData the block data
     * @return the builder
     * @since 2.2.3
     */
    public @NonNull FallingBlockBuilder blockData(final @Nullable BlockData blockData) {
        this.blockData = blockData;
        return this;
    }

    /**
     * Sets the block state for the falling block.
     *
     * @param blockState the block state
     * @return the builder
     * @since 2.2.3
     */
    public @NonNull FallingBlockBuilder blockState(final @Nullable BlockState blockState) {
        this.blockState = blockState;
        return this;
    }

    /**
     * Sets if the falling block will cancel drop.
     *
     * @param cancelDrop the cancel drop flag
     * @return the builder
     * @since 2.2.3
     */
    public @NonNull FallingBlockBuilder cancelDrop(final @Nullable Boolean cancelDrop) {
        this.cancelDrop = cancelDrop;
        return this;
    }

    /**
     * Sets the damage per block for the falling block.
     *
     * @param damagePerBlock the damage per block
     * @return the builder
     * @since 2.2.3
     */
    public @NonNull FallingBlockBuilder damagePerBlock(final @Nullable Float damagePerBlock) {
        this.damagePerBlock = damagePerBlock;
        return this;
    }

    /**
     * Sets if the falling block will drop an item if it cannot be placed.
     *
     * @param dropItem the drop item flag
     * @return the builder
     * @since 2.2.3
     */
    public @NonNull FallingBlockBuilder dropItem(final @Nullable Boolean dropItem) {
        this.dropItem = dropItem;
        return this;
    }

    /**
     * Sets if the falling block will hurt entities.
     *
     * @param hurtEntities the hurt entities flag
     * @return the builder
     * @since 2.2.3
     */
    public @NonNull FallingBlockBuilder hurtEntities(final @Nullable Boolean hurtEntities) {
        this.hurtEntities = hurtEntities;
        return this;
    }

    /**
     * Sets the maximum damage the falling block can inflict on entities.
     *
     * @param maxDamage the maximum damage
     * @return the builder
     * @since 2.2.3
     */
    public @NonNull FallingBlockBuilder maxDamage(final @Nullable Integer maxDamage) {
        this.maxDamage = maxDamage;
        return this;
    }

    /**
     * Sets if the falling block should auto expire.
     *
     * @param shouldAutoExpire the auto expire flag
     * @return the builder
     * @since 2.2.3
     */
    public @NonNull FallingBlockBuilder shouldAutoExpire(final @Nullable Boolean shouldAutoExpire) {
        this.shouldAutoExpire = shouldAutoExpire;
        return this;
    }

    /**
     * Gets the block data of the falling block.
     *
     * @return the block data
     * @since 2.2.3
     */
    public @Nullable BlockData blockData() {
        return blockData;
    }

    /**
     * Gets the block state of the falling block.
     *
     * @return the block state
     * @since 2.2.3
     */
    public @Nullable BlockState blockState() {
        return blockState;
    }

    /**
     * Gets the cancel drop flag of the falling block.
     *
     * @return the cancel drop flag
     * @since 2.2.3
     */
    public @Nullable Boolean cancelDrop() {
        return cancelDrop;
    }

    /**
     * Gets the damage per block of the falling block.
     *
     * @return the damage per block
     * @since 2.2.3
     */
    public @Nullable Float damagePerBlock() {
        return damagePerBlock;
    }

    /**
     * Gets the drop item flag of the falling block.
     *
     * @return the drop item flag
     * @since 2.2.3
     */
    public @Nullable Boolean dropItem() {
        return dropItem;
    }

    /**
     * Gets the hurt entities flag of the falling block.
     *
     * @return the hurt entities flag
     * @since 2.2.3
     */
    public @Nullable Boolean hurtEntities() {
        return hurtEntities;
    }

    /**
     * Gets the maximum damage of the falling block.
     *
     * @return the maximum damage
     * @since 2.2.3
     */
    public @Nullable Integer maxDamage() {
        return maxDamage;
    }

    /**
     * Gets the auto expire flag of the falling block.
     *
     * @return the auto expire flag
     * @since 2.2.3
     */
    public @Nullable Boolean shouldAutoExpire() {
        return shouldAutoExpire;
    }

}
