package dev.kokiriglade.popcorn.config;

import dev.dejvokep.boostedyaml.YamlDocument;
import dev.dejvokep.boostedyaml.dvs.versioning.BasicVersioning;
import dev.dejvokep.boostedyaml.settings.updater.UpdaterSettings;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * Utility methods to make loading config files simpler
 *
 * @since 3.1.0
 */
public final class ConfigUtils {

    /**
     * Load a config file, from the plugin's jar file if needed
     *
     * @param <P>      the plugin instance
     * @param plugin   the plugin instance
     * @param fileName the file name
     * @return the {@code YamlDocument}
     * @since 3.1.0
     */
    public static <P extends Plugin> @NonNull YamlDocument loadFile(final @NonNull P plugin, final @NonNull String fileName) {
        try {
            final InputStream inputStream = plugin.getResource(fileName);
            if (inputStream == null) {
                throw new IllegalStateException(fileName + " InputStream is null");
            }
            final File configFile = new File(plugin.getDataFolder(), fileName);
            final YamlDocument yamlDocument = YamlDocument.create(configFile, inputStream);
            yamlDocument.setSettings(
                UpdaterSettings.builder()
                    .setVersioning(new BasicVersioning("version"))
                    .build()
            );
            yamlDocument.update();
            return yamlDocument;
        } catch (IOException |
                 IllegalStateException exception) {
            handleConfigurationError(plugin, exception, fileName);
        }
        throw new IllegalStateException("Plugin should be disabled, but isn't");
    }

    private static <P extends Plugin> void handleConfigurationError(final @NonNull P plugin, final @NonNull Exception exception, final @NonNull String fileName) {
        plugin.getSLF4JLogger().error("Failed to load configuration file {}: {}", fileName, exception);
        plugin.getServer().getPluginManager().disablePlugin(plugin);
    }

}
