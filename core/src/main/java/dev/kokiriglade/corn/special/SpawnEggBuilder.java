package dev.kokiriglade.corn.special;

import dev.kokiriglade.corn.AbstractItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.EntitySnapshot;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.OminousBottleMeta;
import org.bukkit.inventory.meta.SpawnEggMeta;
import org.bukkit.material.SpawnEgg;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * Modifies {@link ItemStack}s that have an {@code ItemMeta} of {@link SpawnEggMeta}.
 */
@SuppressWarnings("unused")
public final class SpawnEggBuilder extends AbstractItemBuilder<SpawnEggBuilder, SpawnEggMeta> {

    private SpawnEggBuilder(final @NonNull ItemStack itemStack, final @NonNull SpawnEggMeta itemMeta) {
        super(itemStack, itemMeta);
    }

    /**
     * Creates an {@code SpawnEggBuilder}.
     *
     * @param itemStack the {@code ItemStack} to base the builder off of
     * @return instance of {@code SpawnEggBuilder}
     * @throws IllegalArgumentException if the {@code itemStack}'s {@code ItemMeta} is not the correct type
     */
    public static @NonNull SpawnEggBuilder of(final @NonNull ItemStack itemStack) throws IllegalArgumentException {
        return new SpawnEggBuilder(itemStack, castMeta(itemStack.getItemMeta(), SpawnEggMeta.class));
    }

    /**
     * Creates an {@code SpawnEggBuilder}.
     *
     * @param material the {@code Material} to base the builder off of
     * @return instance of {@code SpawnEggBuilder}
     * @throws IllegalArgumentException if the {@code material} is not an obtainable item,
     *                                  or if the {@code material}'s {@code ItemMeta} is not the correct type
     */
    public static @NonNull SpawnEggBuilder ofType(final @NonNull Material material) throws IllegalArgumentException {
        return SpawnEggBuilder.of(getItem(material));
    }

    /**
     * Gets the custom type of entity this egg will spawn.
     *
     * @return the custom type of entity this egg will spawn
     */
    public @Nullable EntityType customSpawnedEntityType() {
        return this.itemMeta.getCustomSpawnedType();
    }

    /**
     * Sets the custom type of entity this egg will spawn
     *
     * @param type the entity type
     * @return the builder
     */
    public SpawnEggBuilder customSpawnedEntityType(final @Nullable EntityType type) {
        this.itemMeta.setCustomSpawnedType(type);
        return this;
    }

    /**
     * Gets the {@link EntitySnapshot} that will be spawned by this egg or null if no entity has been set.
     * @return the {@code EntitySnapshot}, or {@code null}
     */
    public EntitySnapshot spawnedEntity() {
        return this.itemMeta.getSpawnedEntity();
    }

    /**
     * Sets the {@link EntitySnapshot} that will be spawned by this spawn egg
     *
     * @param snapshot the {@code EntitySnapshot} that will be spawned by this spawn egg
     * @return the builder
     */
    public SpawnEggBuilder spawnedEntity(final @NonNull EntitySnapshot snapshot) {
        this.itemMeta.setSpawnedEntity(snapshot);
        return this;
    }

}
