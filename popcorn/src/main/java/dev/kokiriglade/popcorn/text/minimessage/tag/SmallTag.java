package dev.kokiriglade.popcorn.text.minimessage.tag;

import dev.kokiriglade.popcorn.text.SmallFont;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.Context;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.ArgumentQueue;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.command.ConsoleCommandSender;

import java.util.Set;

/**
 * Replaces text with the "small caps" font.
 *
 * @see SmallFont
 * @since 3.0.0
 */
class SmallTag {

    private static final String SMALL = "small";
    private static final String SM = "sm";

    static final TagResolver RESOLVER = TagResolver.resolver(Set.of(SMALL, SM), SmallTag::create);

    static Tag create(final ArgumentQueue args, final Context ctx) {
        final String text = args.popOr("null").value();

        /*
         * the "small caps" font generally looks like crap in the terminal (or at least
         * it does for me), so avoid using it if we know we're in a terminal environment
         */
        if (ctx.target() != null) {
            if (ctx.target() instanceof ConsoleCommandSender) {
                return Tag.inserting(Component.text(text));
            }
        }

        return Tag.inserting(Component.text(SmallFont.convert(text)));
    }

}
