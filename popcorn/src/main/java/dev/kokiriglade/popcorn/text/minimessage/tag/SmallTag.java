package dev.kokiriglade.popcorn.text.minimessage.tag;

import dev.kokiriglade.popcorn.text.SmallFont;
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
 * Replaces text inside the tag with the "small caps" font.
 *
 * @see SmallFont
 * @since 3.0.0
 */
public final class SmallTag implements Modifying {

    static final String SMALL = "small";
    static final String SM = "sm";
    static final TagResolver RESOLVER = TagResolver.resolver(Set.of(SMALL, SM), SmallTag::create);
    private static final ComponentFlattener LENGTH_CALCULATOR = ComponentFlattener.builder()
        .mapper(TextComponent.class, TextComponent::content)
        .unknownMapper(x -> "_") // every unknown component gets a single colour
        .build();
    private final boolean console;
    private boolean visited;
    private int size = 0;

    private SmallTag(final boolean console) {
        this.console = console;
    }

    static Tag create(final ArgumentQueue args, final Context ctx) {
        if (ctx.target() != null) {
            if (ctx.target() instanceof ConsoleCommandSender) {
                return new SmallTag(true);
            }
        }

        return new SmallTag(false);
    }

    private int size() {
        return this.size;
    }

    @Override
    public void visit(final @NonNull Node current, final int depth) {
        if (this.visited) {
            throw new IllegalStateException("Small tag instances cannot be re-used, return a new one for each resolve");
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
        if (current instanceof TextComponent textComponent && ((TextComponent) current).content().length() > 0) {
            final String content = textComponent.content();

            if (!console) {
                current = text(SmallFont.convert(content));
            } else {
                current = text(content.toUpperCase(Locale.ROOT));
            }

            return current;
        } else if (!(current instanceof TextComponent)) {
            final Component ret = current.children(Collections.emptyList());
            return ret;
        }

        return Component.empty().mergeStyle(current);
    }

}
