package dev.kokiriglade.popcorn.util;

import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataType;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * Modifies objects that implement {@link org.bukkit.persistence.PersistentDataHolder}.
 *
 * @param <B> the builder type
 * @since 2.2.1
 */
@SuppressWarnings("unused")
public interface PersistentDataHolderBuilder<B> {

    /**
     * Gets data from the {@code PersistentDataHolder}'s {@link org.bukkit.persistence.PersistentDataContainer}.
     *
     * @param key  the {@code NamespacedKey} to use
     * @param type the {@code PersistentDataType to use}
     * @param <T>  the primary object type of the data
     * @param <Z>  the retrieve object type of the data
     * @return the data
     * @since 2.2.1
     */
    <T, Z> @Nullable Z getData(
        final @NonNull NamespacedKey key,
        final @NonNull PersistentDataType<T, Z> type
    );

    /**
     * Checks if the {@code PersistentDataHolder}'s {@link org.bukkit.persistence.PersistentDataContainer}
     * contains data for the given {@code NamespacedKey} and {@code PersistentDataType}.
     *
     * @param key  the {@code NamespacedKey} to check
     * @param type the {@code PersistentDataType} to check
     * @param <T>  the primary object type of the data
     * @param <Z>  the retrieve object type of the data
     * @return {@code true} if the data exists, {@code false} otherwise
     * @since 2.2.1
     */
    <T, Z> boolean hasData(
        final @NonNull NamespacedKey key,
        final @NonNull PersistentDataType<T, Z> type
    );

    /**
     * Adds data to the {@code PersistentDataHolder}'s {@link org.bukkit.persistence.PersistentDataContainer}.
     *
     * @param key    the {@code NamespacedKey} to use
     * @param type   the {@code PersistentDataType} to use
     * @param object the data to set
     * @param <T>    the primary object type of the data
     * @param <Z>    the retrieve object type of the data
     * @return the builder
     * @since 2.2.1
     */
    <T, Z> @NonNull B setData(
        final @NonNull NamespacedKey key,
        final @NonNull PersistentDataType<T, Z> type,
        final @NonNull Z object
    );

    /**
     * Removes data from the {@code ItemStack}'s {@link org.bukkit.persistence.PersistentDataContainer}.
     *
     * @param key the {@code NamespacedKey} to use
     * @return the builder
     * @since 2.2.1
     */
    @NonNull B removeData(
        final @NonNull NamespacedKey key
    );

}
