package dev.kokiriglade.popcorn.command;

import com.mojang.brigadier.ImmutableStringReader;
import com.mojang.brigadier.Message;
import com.mojang.brigadier.exceptions.CommandExceptionType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.papermc.paper.command.brigadier.MessageComponentSerializer;
import net.kyori.adventure.text.Component;

/**
 * A command exception that displays a {@link Component}
 *
 * @since 3.2.0
 */
@SuppressWarnings("UnstableApiUsage")
public class ComponentCommandExceptionType implements CommandExceptionType {

    private final Message message;

    /**
     * Instantiate the exception
     *
     * @param message the message
     * @see ComponentCommandExceptionType#create()
     * @see ComponentCommandExceptionType#createWithContext(ImmutableStringReader)
     * @since 3.2.0
     */
    public ComponentCommandExceptionType(final Component message) {
        this.message = MessageComponentSerializer.message().serialize(message);
    }

    /**
     * Create the exception
     *
     * @return the exception
     * @since 3.2.0
     */
    public CommandSyntaxException create() {
        return new CommandSyntaxException(this, this.message);
    }

    /**
     * Create the exception with context
     *
     * @param reader the context
     * @return the exception
     * @since 3.2.0
     */
    public CommandSyntaxException createWithContext(final ImmutableStringReader reader) {
        return new CommandSyntaxException(this, this.message, reader.getString(), reader.getCursor());
    }

}
