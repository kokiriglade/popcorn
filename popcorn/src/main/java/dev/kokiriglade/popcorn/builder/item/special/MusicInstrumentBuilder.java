package dev.kokiriglade.popcorn.builder.item.special;

import dev.kokiriglade.popcorn.builder.item.AbstractItemBuilder;
import org.bukkit.Material;
import org.bukkit.MusicInstrument;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.MusicInstrumentMeta;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * Modifies {@link ItemStack}s that have an {@code ItemMeta} of {@link MusicInstrumentMeta}.
 *
 * @since 1.0.0
 */
@SuppressWarnings("unused")
public final class MusicInstrumentBuilder extends AbstractItemBuilder<MusicInstrumentBuilder, MusicInstrumentMeta> {

    private MusicInstrumentBuilder(final @NonNull ItemStack itemStack, final @NonNull MusicInstrumentMeta itemMeta) {
        super(itemStack, itemMeta);
    }

    /**
     * Creates a {@code MusicInstrumentBuilder}.
     *
     * @param itemStack the {@code ItemStack} to base the builder off of
     * @return instance of {@code MusicInstrumentBuilder}
     * @throws IllegalArgumentException if the {@code itemStack}'s {@code ItemMeta} is not the correct type
     * @since 1.0.0
     */
    public static @NonNull MusicInstrumentBuilder of(final @NonNull ItemStack itemStack) throws IllegalArgumentException {
        return new MusicInstrumentBuilder(itemStack, castMeta(itemStack.getItemMeta(), MusicInstrumentMeta.class));
    }

    /**
     * Creates a {@code MusicInstrumentBuilder}.
     *
     * @param material the {@code Material} to base the builder off of
     * @return instance of {@code MusicInstrumentBuilder}
     * @throws IllegalArgumentException if the {@code material} is not an obtainable item,
     *                                  or if the {@code material}'s {@code ItemMeta} is not the correct type
     * @since 1.0.0
     */
    public static @NonNull MusicInstrumentBuilder ofType(final @NonNull Material material) throws IllegalArgumentException {
        return MusicInstrumentBuilder.of(getItem(material));
    }

    /**
     * Creates a {@code MusicInstrumentBuilder} of type {@link Material#GOAT_HORN}. A convenience method.
     *
     * @return instance of {@code MusicInstrumentBuilder}
     * @throws IllegalArgumentException if the {@code material} is not an obtainable item,
     *                                  or if the {@code material}'s {@code ItemMeta} is not the correct type
     * @since 1.2.0
     */
    public static @NonNull MusicInstrumentBuilder ofGoatHorn() throws IllegalArgumentException {
        return ofType(Material.GOAT_HORN);
    }

    /**
     * Gets the instrument.
     *
     * @return the instrument
     * @since 1.0.0
     */
    public @Nullable MusicInstrument instrument() {
        return this.itemMeta.getInstrument();
    }

    /**
     * Sets the instrument. Pass {@code null} to remove
     *
     * @param instrument the instrument
     * @return the builder
     * @since 1.0.0
     */
    public @NonNull MusicInstrumentBuilder instrument(final @Nullable MusicInstrument instrument) {
        this.itemMeta.setInstrument(instrument);
        return this;
    }

}
