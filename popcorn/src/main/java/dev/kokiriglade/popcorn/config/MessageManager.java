package dev.kokiriglade.popcorn.config;

import dev.kokiriglade.popcorn.builder.text.MessageBuilder;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Manages the messages configuration file
 *
 * @param <P> the plugin instance
 * @since 3.1.0
 */
public class MessageManager<P extends JavaPlugin> extends AbstractConfigurationManager<P> {

    /**
     * Read the config file
     *
     * @param plugin plugin instance
     * @since 3.1.0
     */
    public MessageManager(final @NonNull P plugin) {
        super(plugin, "messages.yml");
        reload();
    }

    @Override
    public void reload() {
        super.reload();
        MessageBuilder.reload(plugin, get("prefix"), get("error"));
    }

    /**
     * Get a string from the file
     *
     * @param key the key
     * @return the string
     * @since 3.1.0
     */
    public @NonNull String get(final @NonNull String key) {
        return this.get(key, key);
    }

    /**
     * Get a string from the file
     *
     * @param key the key
     * @param def the default if the string isn't found
     * @return the string
     * @since 3.1.0
     */
    public @NonNull String get(final @NonNull String key, final @NonNull String def) {
        return getDocument().getString(key, def);
    }

}
