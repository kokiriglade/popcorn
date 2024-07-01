package dev.kokiriglade.popcorn.text.minimessage.tag;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.Context;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.ArgumentQueue;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Set;

/**
 * Creates a clickable link in chat
 *
 * @since 3.0.0
 */
public final class LinkTag {

    static final String LINK = "link";
    static final String A = "a";

    static final TagResolver RESOLVER = TagResolver.resolver(Set.of(LINK, A), LinkTag::create);

    static Tag create(final ArgumentQueue args, final Context ctx) {
        final String link = args.popOr("https://example.com/").value();

        try {
            final URI uri = new URI(link);
            return Tag.styling(
                NamedTextColor.AQUA,
                TextDecoration.UNDERLINED,
                ClickEvent.openUrl(link),
                HoverEvent.showText(Component.text("Open " + uri.getHost() + uri.getRawPath()))
            );
        } catch (final URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

}
