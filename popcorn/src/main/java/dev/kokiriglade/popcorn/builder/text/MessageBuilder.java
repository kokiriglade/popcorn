package dev.kokiriglade.popcorn.builder.text;

import dev.kokiriglade.popcorn.Popcorn;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.HashMap;
import java.util.Map;

import static net.kyori.adventure.text.Component.translatable;

/**
 * A builder for text messages.
 *
 * @since 3.1.0
 */
@SuppressWarnings("unused")
public final class MessageBuilder {

    private static final @NonNull Map<Plugin, String> PREFIXES = new HashMap<>();
    private static final @NonNull Map<Plugin, String> ERRORS = new HashMap<>();
    private final @NonNull Plugin plugin;
    private final @NonNull String message;
    private final @NonNull Audience audience;
    private final @NonNull Map<String, String> placeholders = new HashMap<>();
    private boolean prefix = false;
    private boolean error = false;

    private MessageBuilder(final @NonNull Plugin plugin, final @NonNull String message, final @NonNull Audience audience) {
        this.plugin = plugin;
        this.message = message;
        this.audience = audience;
    }

    /**
     * Reload prefix and error tags
     *
     * @param plugin the plugin setting its prefix and error strings
     * @param prefix prefix tag
     * @param error  error tag
     * @since 3.1.0
     */
    public static void reload(final @NonNull Plugin plugin, final @NonNull String prefix, final @NonNull String error) {
        PREFIXES.put(plugin, prefix);
        ERRORS.put(plugin, error);
    }

    /**
     * Creates a new MessageBuilder instance
     *
     * @param plugin   The plugin building the message
     * @param message  The message
     * @param audience The intended recipient of the message
     * @return The created MessageBuilder instance
     * @since 3.1.0
     */
    public static @NonNull MessageBuilder of(final @NonNull Plugin plugin, final @NonNull String message, final @NonNull Audience audience) {
        return new MessageBuilder(plugin, message, audience);
    }

    /**
     * Creates a new MessageBuilder instance
     *
     * @param plugin  The plugin building the message
     * @param message The message
     * @return The created MessageBuilder instance
     * @since 3.1.0
     */
    public static @NonNull MessageBuilder of(final @NonNull Plugin plugin, final @NonNull String message) {
        return new MessageBuilder(plugin, message, Audience.empty());
    }

    /**
     * Sets a placeholder value in the message
     *
     * @param placeholder The placeholder key
     * @param string      The value to replace the placeholder with
     * @return The builder instance
     * @since 3.2.0
     */
    public @NonNull MessageBuilder set(final @NonNull String placeholder, final @NonNull String string) {
        placeholders.put(placeholder, new MessageBuilder(plugin, string, audience).string());
        return this;
    }

    /**
     * Sets a placeholder value in the message
     *
     * @param placeholder The placeholder key
     * @param component   The value to replace the placeholder with
     * @return The builder instance
     * @since 3.2.0
     */
    public @NonNull MessageBuilder set(final @NonNull String placeholder, final @NonNull Component component) {
        return set(placeholder, Popcorn.miniMessage().serialize(component));
    }

    /**
     * Sets a placeholder value in the message
     *
     * @param placeholder The placeholder key
     * @param itemStack   The value to replace the placeholder with
     * @return The builder instance
     * @since 3.2.0
     */
    public @NonNull MessageBuilder set(final @NonNull String placeholder, final @NonNull ItemStack itemStack) {
        return set(placeholder, translatable("chat.square_brackets", itemStack.getItemMeta().itemName()).hoverEvent(itemStack.asHoverEvent()));
    }

    /**
     * Sets a placeholder value in the message
     *
     * @param placeholder The placeholder key
     * @param entity      The value to replace the placeholder with
     * @return The builder instance
     * @since 3.2.0
     */
    public @NonNull MessageBuilder set(final @NonNull String placeholder, final @NonNull Entity entity) {
        return set(placeholder, entity.name().hoverEvent(entity.asHoverEvent()));
    }

    /**
     * Sets a placeholder value in the message.
     *
     * @param <T>         The type.
     * @param placeholder The placeholder key.
     * @param value       The value to replace the placeholder with.
     * @return The builder instance.
     * @since 3.1.0
     */
    public <T> @NonNull MessageBuilder set(final @NonNull String placeholder, final @NonNull T value) {
        return set(placeholder, String.valueOf(value));
    }

    /**
     * Sets whether to include a prefix in the message.
     *
     * @param flag True to include the prefix, false otherwise.
     * @return The builder instance.
     * @since 3.1.0
     */
    public @NonNull MessageBuilder prefix(final boolean flag) {
        prefix = flag;
        return this;
    }

    /**
     * Sets whether the builder will render as an error message.
     *
     * @param flag True to render as an error message, false otherwise.
     * @return The builder instance.
     * @since 3.1.0
     */
    public @NonNull MessageBuilder error(final boolean flag) {
        this.error = flag;
        return this;
    }

    /**
     * Constructs the message string with placeholders replaced
     *
     * @return The formatted message string
     * @since 3.1.0
     */
    public String string() {
        return formatMessage();
    }

    /**
     * Constructs a Component representing the formatted message using {@link MiniMessage}
     *
     * @return The Component representing the formatted message
     * @since 3.1.0
     */
    public Component component() {
        return Popcorn.miniMessage().deserialize(string());
    }

    /**
     * Formats the message string with placeholders replaced.
     *
     * @return The formatted message string.
     * @since 3.1.0
     */
    private String formatMessage() {
        String formatted = message;

        for (final Map.Entry<String, String> entry : placeholders.entrySet()) {
            if (entry.getValue() != null) {
                formatted = formatted.replaceAll("%%%s%%".formatted(entry.getKey()), entry.getValue());
            }
        }

        if (error) {
            formatted = new MessageBuilder(plugin, ERRORS.getOrDefault(plugin, "error %message%"), audience)
                .set("message", formatted)
                .string();
        }

        if (prefix) {
            formatted = new MessageBuilder(plugin, PREFIXES.getOrDefault(plugin, "prefix %message%"), audience)
                .set("message", formatted)
                .string();
        }

        return formatted;
    }

}
