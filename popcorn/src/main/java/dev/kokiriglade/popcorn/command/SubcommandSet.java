package dev.kokiriglade.popcorn.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.papermc.paper.command.brigadier.CommandSourceStack;

import java.util.Set;

/**
 * Represents multiple sub-commands of a Brigadier command
 *
 * @param <P> the plugin
 * @since 3.2.0
 */
@SuppressWarnings("UnstableApiUsage")
public interface SubcommandSet<P> extends Command<Set<LiteralArgumentBuilder<CommandSourceStack>>, P> {

}
