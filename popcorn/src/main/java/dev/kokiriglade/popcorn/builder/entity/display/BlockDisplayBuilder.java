package dev.kokiriglade.popcorn.builder.entity.display;

import org.bukkit.Location;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.BlockDisplay;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * Modifies {@link BlockDisplay}s
 *
 * @since 2.2.2
 */
@SuppressWarnings("unused")
public final class BlockDisplayBuilder extends AbstractDisplayBuilder<BlockDisplayBuilder, BlockDisplay> {

    private @Nullable BlockData block;

    private BlockDisplayBuilder(final @NonNull Location location) {
        super(BlockDisplay.class, location);
        this.consumers.add(blockDisplay -> {
            if (block != null) {
                blockDisplay.setBlock(block);
            }
        });
    }

    /**
     * Creates a {@code BlockDisplayBuilder}.
     *
     * @param location the {@code Location} to spawn the Block Display at
     * @return instance of {@code BlockDisplayBuilder}
     * @since 2.2.2
     */
    public static @NonNull BlockDisplayBuilder create(final @NonNull Location location) {
        return new BlockDisplayBuilder(location);
    }

    /**
     * Gets the {@code BlockData} for the {@code BlockDisplay}.
     *
     * @return the block data
     * @since 2.2.2
     */
    public @Nullable BlockData block() {
        return block;
    }

    /**
     * Sets the {@code BlockData} for the {@code BlockDisplay}.
     *
     * @param block the block data
     * @return the builder
     * @since 2.2.2
     */
    public @NonNull BlockDisplayBuilder block(final @Nullable BlockData block) {
        this.block = block;
        return this;
    }
}
