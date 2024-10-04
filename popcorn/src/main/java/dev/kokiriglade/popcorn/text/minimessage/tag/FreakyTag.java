package dev.kokiriglade.popcorn.text.minimessage.tag;

import dev.kokiriglade.popcorn.text.FreakyFont;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.flattener.ComponentFlattener;
import net.kyori.adventure.text.minimessage.Context;
import net.kyori.adventure.text.minimessage.internal.parser.node.TagNode;
import net.kyori.adventure.text.minimessage.internal.parser.node.ValueNode;
import net.kyori.adventure.text.minimessage.tag.Inserting;
import net.kyori.adventure.text.minimessage.tag.Modifying;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.ArgumentQueue;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.text.minimessage.tree.Node;
import org.bukkit.command.ConsoleCommandSender;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Collections;
import java.util.Locale;
import java.util.Set;

import static net.kyori.adventure.text.Component.text;

/**
 * Replaces text inside the tag with the 𝓯𝓻𝓮𝓪𝓴𝔂 font.
 *
 * @see FreakyFont
 * @since 3.3.5
 */
public final class FreakyTag implements Modifying {

    static final String FREAKY = "freaky";
    static final String FREAK = "freak";
    static final TagResolver RESOLVER = TagResolver.resolver(Set.of(FREAKY, FREAK), FreakyTag::create);
    private static final ComponentFlattener LENGTH_CALCULATOR = ComponentFlattener.builder()
        .mapper(TextComponent.class, TextComponent::content)
        .unknownMapper(x -> "_") // every unknown component gets a single colour
        .build();
    private final boolean console;
    private boolean visited;
    private int size = 0;

    private FreakyTag(final boolean console) {
        this.console = console;
    }

    static Tag create(final ArgumentQueue args, final Context ctx) {
        if (ctx.target() != null) {
            if (ctx.target() instanceof ConsoleCommandSender) {
                return new FreakyTag(true);
            }
        }

        return new FreakyTag(false);
    }

    private int size() {
        return this.size;
    }

    @Override
    public void visit(final @NonNull Node current, final int depth) {
        if (this.visited) {
            throw new IllegalStateException("Freaky tag instances cannot be re-used, return a new one for each resolve");
        }

        if (current instanceof ValueNode) {
            final String value = ((ValueNode) current).value();
            this.size += value.codePointCount(0, value.length());
        } else if (current instanceof TagNode tag) {
            if (tag.tag() instanceof Inserting) {
                // ComponentTransformation.apply() returns the value of the component placeholder
                LENGTH_CALCULATOR.flatten(((Inserting) tag.tag()).value(), s -> this.size += s.codePointCount(0, s.length()));
            }
        }
    }

    @Override
    public void postVisit() {
        this.visited = true;
    }

    @Override
    public Component apply(@NonNull Component current, final int depth) {
        if (current instanceof TextComponent textComponent && !((TextComponent) current).content().isEmpty()) {
            final String content = textComponent.content();

            if (!console) {
                current = text(FreakyFont.convert(content));
            } else {
                current = text(content.toUpperCase(Locale.ROOT));
            }

            return current;
        } else if (!(current instanceof TextComponent)) {
            return current.children(Collections.emptyList());
        }

        return Component.empty().mergeStyle(current);
    }

}
