package dev.kokiriglade.popcorn.text.minimessage.tag;

import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Additional tag types distributed with popcorn.
 *
 * <p>All standard MiniMessage tags are included in the default tag resolver.</p>
 *
 * @since 3.0.0
 */
public final class PopcornTags {

    private static final TagResolver ALL = TagResolver.builder()
        .resolvers(
            TagResolver.standard(),
            LinkTag.RESOLVER,
            SmallTag.RESOLVER
        )
        .build();

    private PopcornTags() {
    }

    /**
     * Get a resolver for the {@value LinkTag#LINK} tag.
     *
     * @return a resolver for the {@value LinkTag#LINK} tag
     * @since 3.0.0
     */
    public static @NonNull TagResolver link() {
        return LinkTag.RESOLVER;
    }

    /**
     * Get a resolver for the {@value SmallTag#SMALL} tag.
     *
     * @return a resolver for the {@value SmallTag#SMALL} tag
     * @since 3.0.0
     */
    public static @NonNull TagResolver small() {
        return SmallTag.RESOLVER;
    }

    /**
     * Get a resolver that handles all standard MiniMessage tags as well as tags provide by popcorn.
     *
     * <p>This will currently return all standard MiniMessage tags, but in the future MiniMessage
     * may add tags that are not enabled by default, and which must explicitly be opted-in to.</p>
     *
     * @return the resolver for popcorn-provided tags
     * @since 3.0.0
     */
    public static @NonNull TagResolver defaults() {
        return ALL;
    }

}
