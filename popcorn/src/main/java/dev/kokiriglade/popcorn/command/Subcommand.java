package dev.kokiriglade.popcorn.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.papermc.paper.command.brigadier.CommandSourceStack;

/**
 * Represents a sub-command of a Brigadier command
 *
 * @param <P> the plugin
 * @since 3.2.0
 */
@SuppressWarnings("UnstableApiUsage")
public interface Subcommand<P> extends Command<LiteralArgumentBuilder<CommandSourceStack>, P> {
}
