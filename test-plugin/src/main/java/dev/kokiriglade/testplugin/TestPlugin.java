package dev.kokiriglade.testplugin;

import com.mojang.brigadier.Command;
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
        Bukkit.addRecipe(
            ShapedRecipeBuilder
                .create(
                    new NamespacedKey(this, "shrink_wand"),
                    ItemBuilder.ofType(Material.BREEZE_ROD)
                        .itemName(Component.text("Wand of Shrinking"))
                        .addFlag(ItemFlag.HIDE_ATTRIBUTES)
                        .rarity(ItemRarity.RARE)
                        .addAttributeModifier(
                            Attribute.GENERIC_SCALE,
                            new AttributeModifier(
                                new NamespacedKey(this, "shrink_wand/shrink"),
                                -0.5d,
                                AttributeModifier.Operation.ADD_NUMBER,
                                EquipmentSlotGroup.HAND
                            )
                        )
                        .build()
                )
                .shape(" IE", " BI", "B  ")
                .ingredient('B', new RecipeChoice.MaterialChoice(Material.BLAZE_ROD, Material.BREEZE_ROD))
                .ingredient('E', new RecipeChoice.MaterialChoice(Material.ENDER_PEARL, Material.ENDER_EYE))
                .ingredient('I', new RecipeChoice.MaterialChoice(Material.IRON_INGOT))
                .build()
        );

        getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, event -> {
            Commands commands = event.registrar();

            commands.register(
                Commands.literal("spawn")
                    .then(Commands.literal("axolotl")
                        .executes(context -> {
                            Player player = (Player) context.getSource().getSender();

                            Axolotl axolotl = AxolotlBuilder
                                .create(player.getLocation().add(0, 1, 0))
                                .build();

                            player.sendRichMessage("<green>spawned an axolotl</green>");

                            return Command.SINGLE_SUCCESS;
                        })
                    )
                    .requires(commandSourceStack -> commandSourceStack.getSender().isOp() && commandSourceStack.getSender() instanceof Player)
                    .build()
            );

            commands.register(
                Commands.literal("get")
                    .then(Commands.literal("dirt")
                        .executes(context -> {
                            Player player = (Player) context.getSource().getSender();
                            ItemStack itemStack = ItemBuilder.ofType(Material.DIRT)
                                .itemName(Component.text("Epic Dirt"))
                                .rarity(ItemRarity.EPIC)
                                .addEnchant(Enchantment.UNBREAKING, 1)
                                .addAttributeModifier(
                                    Attribute.GENERIC_SCALE,
                                    new AttributeModifier(
                                        new NamespacedKey(this, "shrink"),
                                        -0.5d,
                                        AttributeModifier.Operation.ADD_NUMBER,
                                        EquipmentSlotGroup.ANY
                                    )
                                )
                                .build();

                            player.getInventory().addItem(itemStack);
                            player.sendRichMessage("Gave you [%s]".formatted(
                                MiniMessage.miniMessage().serialize(
                                    itemStack.getItemMeta().itemName()
                                        .hoverEvent(itemStack)
                                )
                            ));
                            return Command.SINGLE_SUCCESS;
                        })
                    )
                    .requires(commandSourceStack -> commandSourceStack.getSender().isOp() && commandSourceStack.getSender() instanceof Player)
                    .build()
            );
        });
    }
}
