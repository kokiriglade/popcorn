package dev.kokiriglade.popcorn;

import com.mojang.brigadier.arguments.ArgumentType;
import dev.kokiriglade.popcorn.config.MessageManager;
import dev.kokiriglade.popcorn.plugin.itemvault.ItemVault;
import dev.kokiriglade.popcorn.plugin.itemvault.api.RegisteredItem;
import dev.kokiriglade.popcorn.plugin.itemvault.command.argument.RegisteredItemArgument;
import dev.kokiriglade.popcorn.text.minimessage.tag.PopcornTags;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * Popcorn is a utility library for Paper
 *
 * @since 3.0.0
 */
public interface Popcorn extends Plugin {

    /**
     * Gets an instance of the Popcorn plugin
     *
     * @return the popcorn instance
     * @since 3.2.0
     */
    static @NonNull Popcorn get() {
        final PluginManager pluginManager = Bukkit.getPluginManager();

        if (pluginManager.isPluginEnabled("popcorn")) {
            final @Nullable Plugin popcornPlugin = pluginManager.getPlugin("popcorn");
            assert popcornPlugin != null;
            return (Popcorn) popcornPlugin;
        }

        throw new NullPointerException("Plugin is not enabled");
    }

    /**
     * Gets a MiniMessage instance with additional tags provided by popcorn
     *
     * @return a MiniMessage instance with additional tags provided by popcorn
     * @since 3.0.0
     */
    static @NonNull MiniMessage miniMessage() {
        return MiniMessage.builder()
            .tags(PopcornTags.defaults())
            .build();
    }

    /**
     * A {@link RegisteredItem} command argument
     *
     * @return argument
     * @since 3.2.0
     */
    static @NonNull ArgumentType<RegisteredItem> registeredItemArgument() {
        return new RegisteredItemArgument();
    }

    /**
     * Gets the {@code ItemVault} instance
     *
     * @return the {@code ItemVault} instance
     * @since 3.2.0
     */
    @NonNull ItemVault getItemVault();

    /**
     * Gets the {@code MessageManager} instance owned by Popcorn
     *
     * @return the {@code MessageManager} instance
     * @since 3.2.0
     */
    @NonNull MessageManager<?> getMessageManager();

}
