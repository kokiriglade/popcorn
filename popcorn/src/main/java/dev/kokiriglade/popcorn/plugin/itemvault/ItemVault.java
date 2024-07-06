package dev.kokiriglade.popcorn.plugin.itemvault;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import dev.kokiriglade.popcorn.Popcorn;
import dev.kokiriglade.popcorn.PopcornPlugin;
import dev.kokiriglade.popcorn.builder.text.MessageBuilder;
import dev.kokiriglade.popcorn.plugin.itemvault.api.RegisteredItem;
import dev.kokiriglade.popcorn.registry.AbstractRegistry;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.PlayerSelectorArgumentResolver;
import io.papermc.paper.event.server.ServerResourcesReloadedEvent;
import io.papermc.paper.plugin.lifecycle.event.LifecycleEventManager;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.bukkit.Bukkit;
import org.bukkit.Keyed;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.plugin.Plugin;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.ApiStatus;

import java.util.Iterator;
import java.util.List;

/**
 * A custom item vault. Items can be obtained via the {@code /iv} command
 *
 * @since 3.2.0
 */
@SuppressWarnings("UnstableApiUsage")
public class ItemVault extends AbstractRegistry<PopcornPlugin, NamespacedKey, RegisteredItem> implements Listener {

    /**
     * Creates an ItemVault instance
     *
     * @param plugin the internal popcorn plugin
     * @since 3.2.0
     */
    @ApiStatus.Internal
    public ItemVault(final @NonNull PopcornPlugin plugin) {
        super(plugin);
    }

    @ApiStatus.Internal
    @Override
    protected void initialize(final @NonNull PopcornPlugin plugin) {
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, this::registerCustomRecipes);
        Bukkit.getServer().getPluginManager().registerEvents(this, plugin);

        final LifecycleEventManager<Plugin> manager = plugin.getLifecycleManager();
        manager.registerEventHandler(LifecycleEvents.COMMANDS, event -> {
            final Commands commands = event.registrar();
            commands.register(
                Commands.literal("iv")
                    .then(Commands.argument("player", ArgumentTypes.player())
                        .then(Commands.argument("item", Popcorn.registeredItemArgument())
                            .then(Commands.argument("amount", IntegerArgumentType.integer(1, 99))
                                .executes(context -> {
                                    final RegisteredItem registeredItem = context.getArgument("item", RegisteredItem.class);
                                    final Player player = context.getArgument("player", PlayerSelectorArgumentResolver.class).resolve(context.getSource()).getFirst();
                                    int amount = context.getArgument("amount", Integer.class);


                                    final ItemStack itemStack = registeredItem.apply(player);
                                    itemStack.setAmount(Math.min(itemStack.getMaxStackSize(), amount));
                                    amount = itemStack.getAmount();

                                    player.getInventory().addItem(itemStack);

                                    context.getSource().getSender().sendMessage(
                                        MessageBuilder.of(Popcorn.get(), Popcorn.get().getMessageManager().get("command.iv.give"))
                                            .set("amount", amount)
                                            .set("item", itemStack)
                                            .set("player", player)
                                            .prefix(true)
                                            .component()
                                    );

                                    return Command.SINGLE_SUCCESS;
                                })
                            )
                        )
                    )
                    .requires(commandSourceStack -> commandSourceStack.getExecutor() instanceof Player && commandSourceStack.getSender().hasPermission("popcorn.itemvault"))
                    .build(),
                List.of("itemvault")
            );
        });
    }

    @EventHandler
    void resourceReload(final ServerResourcesReloadedEvent event) {
        registerCustomRecipes();
    }

    private void registerCustomRecipes() {
        for (final @NonNull Iterator<Recipe> it = Bukkit.recipeIterator(); it.hasNext(); ) {
            final Recipe recipe = it.next();

            if (recipe instanceof Keyed keyed) {
                if (!keyed.getKey().getNamespace().equalsIgnoreCase(NamespacedKey.MINECRAFT_NAMESPACE)) {
                    unregister(keyed.getKey());
                    register(keyed.getKey(), player -> recipe.getResult());
                }
            }
        }
    }

}
