package dev.kokiriglade.popcorn.builder.entity.display;

import net.kyori.adventure.text.Component;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.entity.TextDisplay;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * Modifies {@link TextDisplay}s
 *
 * @since 2.2.2
 */
@SuppressWarnings("unused")
public final class TextDisplayBuilder extends AbstractDisplayBuilder<TextDisplayBuilder, TextDisplay> {

    private @Nullable Component text;
    private TextDisplay.@Nullable TextAlignment textAlignment;
    private @Nullable Integer lineWidth;
    private @Nullable Boolean seeThrough;
    private @Nullable Boolean shadowed;
    private @Nullable Byte textOpacity;
    private @Nullable Color backgroundColor;
    private @Nullable Boolean defaultBackground;

    private TextDisplayBuilder(final @NonNull Location location) {
        super(TextDisplay.class, location);
        this.consumers.add(textDisplay -> {
            textDisplay.text(text);
            if (textAlignment != null) {
                textDisplay.setAlignment(textAlignment);
            }
            if (lineWidth != null) {
                textDisplay.setLineWidth(lineWidth);
            }
            if (seeThrough != null) {
                textDisplay.setSeeThrough(seeThrough);
            }
            if (shadowed != null) {
                textDisplay.setShadowed(shadowed);
            }
            if (textOpacity != null) {
                textDisplay.setTextOpacity(textOpacity);
            }
            textDisplay.setBackgroundColor(backgroundColor);
            if (defaultBackground != null) {
                textDisplay.setDefaultBackground(defaultBackground);
            }
        });
    }

    /**
     * Creates a {@code TextDisplayBuilder}.
     *
     * @param location the {@code Location} to spawn the Text Display at
     * @return instance of {@code TextDisplayBuilder}
     * @since 2.2.2
     */
    public static @NonNull TextDisplayBuilder create(final @NonNull Location location) {
        return new TextDisplayBuilder(location);
    }

    /**
     * Gets the text for the {@code TextDisplay}.
     *
     * @return the text
     * @since 2.2.2
     */
    public @Nullable Component text() {
        return text;
    }

    /**
     * Sets the text for the {@code TextDisplay}.
     *
     * @param text the text
     * @return the builder
     * @since 2.2.2
     */
    public @NonNull TextDisplayBuilder text(final @Nullable Component text) {
        this.text = text;
        return this;
    }

    /**
     * Gets the text alignment for the {@code TextDisplay}.
     *
     * @return the text alignment
     * @since 2.2.2
     */
    public TextDisplay.@Nullable TextAlignment textAlignment() {
        return textAlignment;
    }

    /**
     * Sets the text alignment for the {@code TextDisplay}.
     *
     * @param textAlignment the text alignment
     * @return the builder
     * @since 2.2.2
     */
    public @NonNull TextDisplayBuilder textAlignment(final TextDisplay.@Nullable TextAlignment textAlignment) {
        this.textAlignment = textAlignment;
        return this;
    }

    /**
     * Gets the line width for the {@code TextDisplay}.
     *
     * @return the line width
     * @since 2.2.2
     */
    public @Nullable Integer lineWidth() {
        return lineWidth;
    }

    /**
     * Sets the line width for the {@code TextDisplay}.
     *
     * @param lineWidth the line width
     * @return the builder
     * @since 2.2.2
     */
    public @NonNull TextDisplayBuilder lineWidth(final @Nullable Integer lineWidth) {
        this.lineWidth = lineWidth;
        return this;
    }

    /**
     * Gets whether the text is see-through for the {@code TextDisplay}.
     *
     * @return true if the text is see-through, false otherwise
     * @since 2.2.2
     */
    public @Nullable Boolean seeThrough() {
        return seeThrough;
    }

    /**
     * Sets whether the text is see-through for the {@code TextDisplay}.
     *
     * @param seeThrough true if the text is see-through, false otherwise
     * @return the builder
     * @since 2.2.2
     */
    public @NonNull TextDisplayBuilder seeThrough(final @Nullable Boolean seeThrough) {
        this.seeThrough = seeThrough;
        return this;
    }

    /**
     * Gets whether the text is shadowed for the {@code TextDisplay}.
     *
     * @return true if the text is shadowed, false otherwise
     * @since 2.2.2
     */
    public @Nullable Boolean shadowed() {
        return shadowed;
    }

    /**
     * Sets whether the text is shadowed for the {@code TextDisplay}.
     *
     * @param shadowed true if the text is shadowed, false otherwise
     * @return the builder
     * @since 2.2.2
     */
    public @NonNull TextDisplayBuilder shadowed(final @Nullable Boolean shadowed) {
        this.shadowed = shadowed;
        return this;
    }

    /**
     * Gets the text opacity for the {@code TextDisplay}.
     *
     * @return the text opacity
     * @since 2.2.2
     */
    public @Nullable Byte textOpacity() {
        return textOpacity;
    }

    /**
     * Sets the text opacity for the {@code TextDisplay}.
     *
     * @param textOpacity the text opacity
     * @return the builder
     * @since 2.2.2
     */
    public @NonNull TextDisplayBuilder textOpacity(final @Nullable Byte textOpacity) {
        this.textOpacity = textOpacity;
        return this;
    }

    /**
     * Gets the background color for the {@code TextDisplay}.
     *
     * @return the background color
     * @since 2.2.2
     */
    public @Nullable Color backgroundColor() {
        return backgroundColor;
    }

    /**
     * Sets the background color for the {@code TextDisplay}.
     *
     * @param backgroundColor the background color
     * @return the builder
     * @since 2.2.2
     */
    public @NonNull TextDisplayBuilder backgroundColor(final @Nullable Color backgroundColor) {
        this.backgroundColor = backgroundColor;
        return this;
    }

    /**
     * Gets whether the text has its default background for the {@code TextDisplay}.
     *
     * @return true if the text has its default background, false otherwise
     * @since 2.2.2
     */
    public @Nullable Boolean defaultBackground() {
        return defaultBackground;
    }

    /**
     * Sets whether the text has its default background for the {@code TextDisplay}.
     *
     * @param defaultBackground true if the text has its default background, false otherwise
     * @return the builder
     * @since 2.2.2
     */
    public @NonNull TextDisplayBuilder defaultBackground(final @Nullable Boolean defaultBackground) {
        this.defaultBackground = defaultBackground;
        return this;
    }
}
