package dev.kokiriglade.testplugin;

import com.mojang.brigadier.Command;
import dev.kokiriglade.popcorn.Popcorn;
import dev.kokiriglade.popcorn.builder.entity.mob.creature.animal.AxolotlBuilder;
import dev.kokiriglade.popcorn.builder.item.ItemBuilder;
import dev.kokiriglade.popcorn.builder.recipe.crafting.ShapedRecipeBuilder;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Axolotl;
import org.bukkit.entity.Player;
import org.bukkit.inventory.*;
import org.bukkit.plugin.java.JavaPlugin;

@SuppressWarnings({"UnstableApiUsage", "unused"})
public final class TestPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, event -> {
            Commands commands = event.registrar();

            commands.register(
                Commands.literal("test")
                    .executes(context -> {
                        context.getSource().getSender().sendMessage(
                            Popcorn.miniMessage().deserialize("Test: <sm>Hello World <red>I am red</red></sm>")
                        );
                        context.getSource().getSender().sendMessage(
                            Popcorn.miniMessage().deserialize("Yo, check out <a:'https://www.youtube.com/watch?v=dQw4w9WgXcQ'>this cool video I just found</a>")
                        );

                            return Command.SINGLE_SUCCESS;
                    })
                    .build()
            );
        });
    }
}
