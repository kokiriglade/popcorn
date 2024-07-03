package dev.kokiriglade.popcorn.config;

import dev.dejvokep.boostedyaml.YamlDocument;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * An abstract configuration file manager
 *
 * @param <P> the plugin that owns the manager
 * @since 3.1.0
 */
public class AbstractConfigurationManager<P extends JavaPlugin> {

    protected final @NonNull P plugin;
    protected final @NonNull String fileName;
    private YamlDocument document;

    /**
     * Create the manager. Implementations likely want to call {@link #reload()}
     * after calling the {@code super} constructor
     *
     * @param plugin   the plugin that owns the manager
     * @param fileName the file name
     * @since 3.1.0
     */
    public AbstractConfigurationManager(final @NonNull P plugin, final @NonNull String fileName) {
        this.plugin = plugin;
        this.fileName = fileName;
    }

    /**
     * Load the file from disk
     *
     * @since 3.1.0
     */
    protected void load() {
        document = ConfigUtils.loadFile(plugin, fileName);
    }

    /**
     * Reload the file
     *
     * @since 3.1.0
     */
    public void reload() {
        load();
    }

    /**
     * Get the loaded document
     *
     * @since 3.1.0
     */
    public @NonNull YamlDocument getDocument() {
        return document;
    }

}
