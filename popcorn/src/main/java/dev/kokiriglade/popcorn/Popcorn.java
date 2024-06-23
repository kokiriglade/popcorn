package dev.kokiriglade.popcorn;

import dev.kokiriglade.popcorn.text.minimessage.tag.PopcornTags;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Popcorn is a general purpose utility library for PaperMC.
 *
 * <p>This singleton class provided useful constants and methods to use</p>
 *
 * @since 3.0.0
 */
public final class Popcorn {

    /**
     * Gets a MiniMessage instance with additional tags provided by popcorn.
     *
     * @return a MiniMessage instance with additional tags provided by popcorn.
     * @since 3.0.0
     */
    public static @NonNull MiniMessage miniMessage() {
        return MiniMessage.builder()
            .tags(PopcornTags.defaults())
            .build();
    }

}
