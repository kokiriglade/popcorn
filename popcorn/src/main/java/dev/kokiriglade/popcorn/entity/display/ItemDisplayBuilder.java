package dev.kokiriglade.popcorn.entity.display;

import org.bukkit.Location;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * Modifies {@link ItemDisplay}s
 *
 * @since 2.2.2
 */
@SuppressWarnings("unused")
public final class ItemDisplayBuilder extends AbstractDisplayBuilder<ItemDisplayBuilder, ItemDisplay> {

    private @Nullable ItemStack itemStack;
    private ItemDisplay.@Nullable ItemDisplayTransform itemDisplayTransform;

    private ItemDisplayBuilder(final @NonNull Location location) {
        super(ItemDisplay.class, location);
        this.consumers.add(itemDisplay -> {
            itemDisplay.setItemStack(itemStack);
            if (itemDisplayTransform != null) {
                itemDisplay.setItemDisplayTransform(itemDisplayTransform);
            }
        });
    }

    /**
     * Creates an {@code ItemDisplayBuilder}.
     *
     * @param location the {@code Location} to spawn the Item Display at
     * @return instance of {@code ItemDisplayBuilder}
     * @since 2.2.2
     */
    public static @NonNull ItemDisplayBuilder create(final @NonNull Location location) {
        return new ItemDisplayBuilder(location);
    }

    /**
     * Gets the {@code ItemStack} for the {@code ItemDisplay}.
     *
     * @return the item stack
     * @since 2.2.2
     */
    public @Nullable ItemStack itemStack() {
        return itemStack;
    }

    /**
     * Sets the {@code ItemStack} for the {@code ItemDisplay}.
     *
     * @param itemStack the item stack
     * @return the builder
     * @since 2.2.2
     */
    public @NonNull ItemDisplayBuilder itemStack(final @Nullable ItemStack itemStack) {
        this.itemStack = itemStack;
        return this;
    }

    /**
     * Gets the {@code ItemDisplayTransform} for the {@code ItemDisplay}.
     *
     * @return the item display transform
     * @since 2.2.2
     */
    public ItemDisplay.@Nullable ItemDisplayTransform itemDisplayTransform() {
        return itemDisplayTransform;
    }

    /**
     * Sets the {@code ItemDisplayTransform} for the {@code ItemDisplay}.
     *
     * @param itemDisplayTransform the item display transform
     * @return the builder
     * @since 2.2.2
     */
    public @NonNull ItemDisplayBuilder itemDisplayTransform(final ItemDisplay.@Nullable ItemDisplayTransform itemDisplayTransform) {
        this.itemDisplayTransform = itemDisplayTransform;
        return this;
    }

}
