package dev.kokiriglade.testplugin;

import com.mojang.brigadier.Command;
import dev.kokiriglade.popcorn.builder.item.ItemBuilder;
import dev.kokiriglade.popcorn.builder.recipe.crafting.ShapedRecipeBuilder;
import dev.kokiriglade.popcorn.builder.recipe.crafting.ShapelessRecipeBuilder;
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

            Bukkit.addRecipe(
                ShapedRecipeBuilder.create(new NamespacedKey(this, "poo"), ItemBuilder.ofType(Material.BROWN_DYE).itemName(Component.text("Poo")).build())
                    .shape("DM", "MD")
                    .ingredient('D', Material.DIRT)
                    .ingredient('M', Material.MUD)
                    .build()
            );

            commands.register(
                Commands.literal("test")
                    .executes(context -> {
                        Player sender = (Player) context.getSource().getSender();

                        ChestGui gui = new ChestGui(3, Component.text("Navigator"));

                        gui.setOnGlobalClick(event -> event.setCancelled(true));

                        OutlinePane background = new OutlinePane(0, 0, 9, 3, Pane.Priority.LOWEST);
                        background.addItem(new GuiItem(new ItemStack(Material.BLACK_STAINED_GLASS_PANE)));
                        background.setRepeat(true);

                        gui.addPane(background);

                        OutlinePane navigationPane = new OutlinePane(3, 1, 3, 1);

                        ItemStack shop = new ItemStack(Material.CHEST);
                        ItemMeta shopMeta = shop.getItemMeta();
                        shopMeta.displayName(Component.text("Shop"));
                        shop.setItemMeta(shopMeta);

                        navigationPane.addItem(new GuiItem(shop, event -> {
                            //navigate to the shop
                        }));

                        ItemStack beacon = new ItemStack(Material.BEACON);
                        ItemMeta beaconMeta = beacon.getItemMeta();
                        beaconMeta.displayName(Component.text("Spawn"));
                        beacon.setItemMeta(beaconMeta);

                        navigationPane.addItem(new GuiItem(beacon, event -> {
                            //navigate to spawn
                        }));

                        ItemStack bed = new ItemStack(Material.RED_BED);
                        ItemMeta bedMeta = bed.getItemMeta();
                        bedMeta.displayName(Component.text("Home"));
                        bed.setItemMeta(bedMeta);

                        navigationPane.addItem(new GuiItem(bed, event -> {
                            //navigate to home
                        }));

                        gui.addPane(navigationPane);

                        gui.show(sender);

                        return Command.SINGLE_SUCCESS;
                    })
                    .requires(commandSourceStack -> commandSourceStack.getSender() instanceof Player)
                    .build()
            );
        });
    }
}
