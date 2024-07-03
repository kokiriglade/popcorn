package dev.kokiriglade.popcorn.command;

import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a part of a Brigadier command
 *
 * @param <T> the type of command (LiteralCommandNode, LiteralArgumentBuilder)
 * @param <P> the plugin
 * @since 3.2.0
 */
public interface Command<T, P> {

    /**
     * Get the type of command
     *
     * @param plugin the plugin
     * @return the command
     * @since 3.2.0
     */
    @NonNull T get(@NonNull P plugin);

}
