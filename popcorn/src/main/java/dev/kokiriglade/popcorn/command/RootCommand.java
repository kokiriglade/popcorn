package dev.kokiriglade.popcorn.command;

import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;

/**
 * Represents the root of a Brigadier command
 *
 * @param <P> the plugin
 * @since 3.2.0
 */
@SuppressWarnings("UnstableApiUsage")
public interface RootCommand<P> extends Command<LiteralCommandNode<CommandSourceStack>, P> {
}
