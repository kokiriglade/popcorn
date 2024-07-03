package dev.kokiriglade.popcorn.plugin.itemvault.command.argument;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import dev.kokiriglade.popcorn.Popcorn;
import dev.kokiriglade.popcorn.builder.text.MessageBuilder;
import dev.kokiriglade.popcorn.command.ComponentCommandExceptionType;
import dev.kokiriglade.popcorn.plugin.itemvault.api.RegisteredItem;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.CustomArgumentType;
import net.kyori.adventure.audience.Audience;
import org.bukkit.NamespacedKey;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

/**
 * {@link RegisteredItem} command argument resolver
 *
 * @since 3.2.0
 */
@SuppressWarnings("UnstableApiUsage")
public final class RegisteredItemArgument implements CustomArgumentType.Converted<RegisteredItem, NamespacedKey> {

    static boolean matchesSubStr(final String remaining, final String candidate) {
        for (int i = 0; !candidate.startsWith(remaining, i); ++i) {
            i = candidate.indexOf('_', i);
            if (i < 0) {
                return false;
            }
        }

        return true;
    }

    @Override
    public @NonNull RegisteredItem convert(final @NonNull NamespacedKey nativeType) throws CommandSyntaxException {
        final RegisteredItem registeredItem = Popcorn.get().getItemVault().get(nativeType);
        if (registeredItem == null) {
            throw new ComponentCommandExceptionType(
                MessageBuilder.of(Popcorn.get(), Popcorn.get().getMessageManager().get("argument.item.invalid"), Audience.empty())
                    .set("key", nativeType.asString())
                    .prefix(true)
                    .error(true)
                    .component()
            ).create();
        }
        return registeredItem;
    }

    @Override
    public @NonNull ArgumentType<NamespacedKey> getNativeType() {
        return ArgumentTypes.namespacedKey();
    }

    @Override
    public @NonNull <S> CompletableFuture<Suggestions> listSuggestions(final @NonNull CommandContext<S> context, final @NonNull SuggestionsBuilder builder) {
        final Stream<Map.Entry<NamespacedKey, RegisteredItem>> stream = Popcorn.get().getItemVault().stream();
        final String remaining = builder.getRemaining();
        final boolean containsColon = remaining.indexOf(':') > -1;
        stream.map(Map.Entry::getKey)
            .forEach(key -> {
                final String keyAsString = key.asString();
                if (containsColon) {
                    if (matchesSubStr(remaining, keyAsString)) {
                        builder.suggest(keyAsString);
                    }
                } else if (matchesSubStr(remaining, key.namespace()) || "minecraft".equals(key.namespace()) && matchesSubStr(remaining, key.value())) {
                    builder.suggest(keyAsString);
                }
            });
        return builder.buildFuture();
    }

}
