package dev.kokiriglade.popcorn.event;

import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * An event listener that registers itself by default
 * @param <T> the plugin registering the events
 * @since 3.2.6
 */
public abstract class AbstractEventListener<T extends Plugin> implements Listener {

    protected final @NonNull T plugin;

    /**
     * Register the events
     * @param plugin the plugin registering the events
     * @since 3.2.6
     */
    public AbstractEventListener(final @NonNull T plugin) {
        this.plugin = plugin;
        this.plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

}
