package dev.kokiriglade.corn.special;

import dev.kokiriglade.corn.AbstractItemBuilder;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.map.MapView;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * Modifies {@link ItemStack}s that have an {@code ItemMeta} of {@link MapMeta}.
 * @since 1.0.0
 */
@SuppressWarnings("unused")
public final class MapBuilder extends AbstractItemBuilder<MapBuilder, MapMeta> {

    private MapBuilder(final @NonNull ItemStack itemStack, final @NonNull MapMeta itemMeta) {
        super(itemStack, itemMeta);
    }

    /**
     * Creates a {@code MapBuilder}.
     *
     * @param itemStack the {@code ItemStack} to base the builder off of
     * @return instance of {@code MapBuilder}
     * @throws IllegalArgumentException if the {@code itemStack}'s {@code ItemMeta} is not the correct type
     * @since 1.0.0
     */
    public static @NonNull MapBuilder of(final @NonNull ItemStack itemStack) throws IllegalArgumentException {
        return new MapBuilder(itemStack, castMeta(itemStack.getItemMeta(), MapMeta.class));
    }

    /**
     * Creates a {@code MapBuilder}.
     *
     * @param material the {@code Material} to base the builder off of
     * @return instance of {@code MapBuilder}
     * @throws IllegalArgumentException if the {@code material} is not an obtainable item,
     *                                  or if the {@code material}'s {@code ItemMeta} is not the correct type
     * @since 1.0.0
     */
    public static @NonNull MapBuilder ofType(final @NonNull Material material) throws IllegalArgumentException {
        return MapBuilder.of(getItem(material));
    }

    /**
     * Creates a {@code MapBuilder} of type {@link Material#FILLED_MAP}. A convenience method.
     *
     * @return instance of {@code MapBuilder}
     * @throws IllegalArgumentException if the {@code material} is not an obtainable item,
     *                                  or if the {@code material}'s {@code ItemMeta} is not the correct type
     * @since 1.2.0
     */
    public static @NonNull MapBuilder ofFilledMap() throws IllegalArgumentException {
        return ofType(Material.FILLED_MAP);
    }

    /**
     * Gets the {@code Color}.
     *
     * @return the {@code Color}
     * @since 1.0.0
     */
    public @Nullable Color color() {
        return this.itemMeta.getColor();
    }

    /**
     * Sets the {@code Color}. Pass {@code null} to reset.
     *
     * @param color the {@code Color}
     * @return the builder
     * @since 1.0.0
     */
    public @NonNull MapBuilder color(final @Nullable Color color) {
        this.itemMeta.setColor(color);
        return this;
    }

    /**
     * Gets the {@code MapView}.
     *
     * @return the {@code MapView}
     * @since 1.0.0
     */
    public @Nullable MapView mapView() {
        return this.itemMeta.getMapView();
    }

    /**
     * Sets the {@code MapView}. Pass {@code null} to reset.
     *
     * @param mapView the {@code MapView}
     * @return the builder
     * @since 1.0.0
     */
    public @NonNull MapBuilder mapView(final @Nullable MapView mapView) {
        this.itemMeta.setMapView(mapView);
        return this;
    }

    /**
     * Gets whether the map is scaling.
     *
     * @return whether the map is scaling
     * @since 1.0.0
     */
    public boolean scaling() {
        return this.itemMeta.isScaling();
    }

    /**
     * Sets whether the map is scaling.
     *
     * @param scaling whether the map is scaling
     * @return the builder
     * @since 1.0.0
     */
    public @NonNull MapBuilder scaling(final boolean scaling) {
        this.itemMeta.setScaling(scaling);
        return this;
    }

}
