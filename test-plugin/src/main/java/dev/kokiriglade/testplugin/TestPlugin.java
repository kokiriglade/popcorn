package dev.kokiriglade.testplugin;

import com.mojang.brigadier.Command;
import dev.kokiriglade.popcorn.builder.item.ItemBuilder;
import dev.kokiriglade.popcorn.builder.recipe.crafting.ShapedRecipeBuilder;
import dev.kokiriglade.popcorn.builder.recipe.crafting.ShapelessRecipeBuilder;
import dev.kokiriglade.popcorn.builder.text.MessageBuilder;
import dev.kokiriglade.popcorn.inventory.gui.GuiItem;
import dev.kokiriglade.popcorn.inventory.gui.type.ChestGui;
import dev.kokiriglade.popcorn.inventory.pane.OutlinePane;
import dev.kokiriglade.popcorn.inventory.pane.Pane;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

@SuppressWarnings({"UnstableApiUsage", "unused"})
public final class TestPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, lifecycleEvent -> {
            Commands commands = lifecycleEvent.registrar();

            commands.register(
                Commands.literal("test")
                    .executes(context -> {
                        Player sender = (Player) context.getSource().getSender();

                        sender.sendMessage(MessageBuilder.of(this, "Hello <player>!", sender)
                            .set("player", sender.getName())
                            .component()
                        );

                        return Command.SINGLE_SUCCESS;
                    })
                    .requires(commandSourceStack -> commandSourceStack.getSender() instanceof Player)
                    .build()
            );
        });
    }
}
