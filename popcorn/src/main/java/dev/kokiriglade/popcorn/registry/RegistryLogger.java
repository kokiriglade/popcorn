package dev.kokiriglade.popcorn.registry;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

/**
 * The RegistryLogger class is a modified {@link Logger} that prepends all
 * logging calls with the name of the plugin that owns the registry and the
 * registry itself. The API for RegistryLogger is exactly the same as {@link Logger}.
 *
 * @param <P> the plugin that owns the registry
 * @see Logger
 * @since 3.1.0
 */
@SuppressWarnings("UnstableApiUsage")
public final class RegistryLogger<P extends Plugin> extends Logger {

    private final String loggerName;

    /**
     * Creates a new RegistryLogger that extracts the name from the plugin
     * that owns the registry and the registry itself.
     *
     * @param plugin   the plugin that owns the registry
     * @param registry the registry that the logger is being made for
     * @since 3.1.0
     */
    public RegistryLogger(final @NonNull P plugin, final @NonNull AbstractRegistry<P, ?, ?> registry) {
        super(plugin.getPluginMeta().getLoggerPrefix() != null ? plugin.getPluginMeta().getLoggerPrefix() : plugin.getPluginMeta().getName(), null);
        loggerName = "[%s] ".formatted(registry.getClass().getSimpleName());

        setParent(plugin.getLogger());
        setLevel(Level.ALL);
    }

    @Override
    public void log(final @NonNull LogRecord logRecord) {
        logRecord.setMessage(loggerName + logRecord.getMessage());
        super.log(logRecord);
    }

}
