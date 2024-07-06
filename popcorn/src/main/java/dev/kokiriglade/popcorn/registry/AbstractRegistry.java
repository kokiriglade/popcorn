package dev.kokiriglade.popcorn.registry;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Spliterator;
import java.util.logging.Logger;
import java.util.stream.Stream;

/**
 * A registry of something that is owned by a plugin
 *
 * @param <P> the plugin type
 * @param <K> the key type
 * @param <T> the value type
 * @since 3.1.0
 */
public abstract class AbstractRegistry<P extends Plugin, K, T> {

    private final @NonNull RegistryLogger<P> logger;
    private final @NonNull Map<@NonNull K, @NonNull T> registry = new HashMap<>();
    private final @NonNull Map<@NonNull K, @NonNull T> unmodifiableRegistry = Collections.unmodifiableMap(registry);

    /**
     * The default constructor. Runs the initializer, waits 5 seconds,
     * and then prints the size of the registry
     *
     * @param plugin the plugin that owns the registry
     * @apiNote <strong>Cannot</strong> be instantiated inside {@link JavaPlugin#onLoad()}
     * @since 3.1.0
     */
    public AbstractRegistry(final @NonNull P plugin) {
        this.logger = new RegistryLogger<>(plugin, this);
        initialize(plugin);
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> logger.info("Registered %d entries.".formatted(registry.size())));
    }

    /**
     * Initialize the registry with its default values
     *
     * @param plugin the plugin that owns the registry
     * @since 3.1.0
     */
    abstract protected void initialize(P plugin);

    /**
     * Registers a value with the given key. If the key is already registered, this method throws an IllegalStateException
     *
     * @param key   the key to register
     * @param value the value to register
     * @since 3.1.0
     */
    public void register(final @NonNull K key, final @NonNull T value) {
        if (registry.containsKey(key)) {
            throw new IllegalStateException("Key is already registered: " + key);
        }
        registry.put(key, value);
    }

    /**
     * Gets the value associated with the given key
     *
     * @param key the key to look up
     * @return the value associated with the key, or null if the key is not registered
     * @since 3.1.0
     */
    public @Nullable T get(final @NonNull K key) {
        return unmodifiableRegistry.get(key);
    }

    /**
     * Retrieves the key associated with the given value
     *
     * @param value the value
     * @return the {@link K key} associated with the value
     * @throws NoSuchElementException if the value isn't registered
     * @since 3.2.5
     */
    public @NonNull K resolveKey(final @NonNull T value) throws NoSuchElementException {
        return stream()
            .filter(entry -> Objects.equals(entry.getValue(), value))
            .map(Map.Entry::getKey)
            .findFirst()
            .orElseThrow(NoSuchElementException::new);
    }

    /**
     * Unregister a value with the given key.
     *
     * @param key the key to unregister
     * @since 3.2.0
     */
    protected void unregister(final @NonNull K key) {
        registry.remove(key);
    }

    /**
     * Returns a stream of the registry entries
     *
     * @return a stream of the registry entries
     * @since 3.1.0
     */
    public @NonNull Stream<Map.@NonNull Entry<@NonNull K, @NonNull T>> stream() {
        return unmodifiableRegistry.entrySet().stream();
    }

    /**
     * Returns a spliterator of the registry entries
     *
     * @return a spliterator of the registry entries
     * @since 3.1.0
     */
    public @NonNull Spliterator<Map.@NonNull Entry<@NonNull K, @NonNull T>> spliterator() {
        return unmodifiableRegistry.entrySet().spliterator();
    }

    /**
     * Returns the logger associated with this registry's logger.
     * The returned logger automatically tags all log messages with
     * the registry's name and the name of the plugin that owns it
     *
     * @return the logger
     * @since 3.1.0
     */
    public @NonNull Logger getLogger() {
        return logger;
    }

}
