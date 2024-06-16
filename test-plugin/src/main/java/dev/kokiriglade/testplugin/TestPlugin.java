package dev.kokiriglade.testplugin;

import com.mojang.brigadier.Command;
import dev.kokiriglade.corn.ItemBuilder;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemRarity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

@SuppressWarnings("UnstableApiUsage")
public final class TestPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, event -> {
            Commands commands = event.registrar();

            commands.register(
                Commands.literal("dirt")
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
                    .requires(commandSourceStack -> commandSourceStack.getSender().isOp() && commandSourceStack.getSender() instanceof Player)
                    .build()
            );
        });
    }
}
