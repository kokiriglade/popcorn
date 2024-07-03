package dev.kokiriglade.popcorn.plugin.itemvault.api;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.function.Function;

/**
 * A registered item obtainable via the {@code /iv} command
 *
 * @since 3.2.0
 */
@FunctionalInterface
public interface RegisteredItem extends Function<@Nullable Player, @NonNull ItemStack> {

}
