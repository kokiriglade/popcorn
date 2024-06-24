package dev.kokiriglade.popcorn.inventory.gui.type.impl;

import dev.kokiriglade.popcorn.inventory.gui.type.abstraction.MerchantInventory;
import net.minecraft.core.component.DataComponentPredicate;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.trading.ItemCost;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.Contract;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Internal merchant inventory
 *
 * @since 3.0.0
 */
public class MerchantInventoryImpl extends MerchantInventory {

    @Override
    public void sendMerchantOffers(@NonNull Player player,
                                   @NonNull List<? extends Map.Entry<? extends MerchantRecipe, ? extends Integer>> trades,
                                   int level, int experience) {
        MerchantOffers offers = new MerchantOffers();

        for (Map.Entry<? extends MerchantRecipe, ? extends Integer> entry : trades) {
            MerchantRecipe recipe = entry.getKey();
            List<ItemStack> ingredients = recipe.getIngredients();

            if (ingredients.isEmpty()) {
                throw new IllegalStateException("Merchant recipe has no ingredients");
            }

            ItemStack itemA = ingredients.get(0);
            ItemStack itemB = null;

            if (ingredients.size() >= 2) {
                itemB = ingredients.get(1);
            }

            net.minecraft.world.item.ItemStack nmsItemA = CraftItemStack.asNMSCopy(itemA);
            net.minecraft.world.item.ItemStack nmsItemB = net.minecraft.world.item.ItemStack.EMPTY;
            net.minecraft.world.item.ItemStack nmsItemResult = CraftItemStack.asNMSCopy(recipe.getResult());

            if (itemB != null) {
                nmsItemB = CraftItemStack.asNMSCopy(itemB);
            }

            ItemCost itemCostA = convertItemStackToItemCost(nmsItemA);
            ItemCost itemCostB = convertItemStackToItemCost(nmsItemB);

            int uses = recipe.getUses();
            int maxUses = recipe.getMaxUses();
            int exp = recipe.getVillagerExperience();
            float multiplier = recipe.getPriceMultiplier();

            MerchantOffer merchantOffer = new MerchantOffer(
                itemCostA, Optional.of(itemCostB), nmsItemResult, uses, maxUses, exp, multiplier
            );
            merchantOffer.setSpecialPriceDiff(entry.getValue());

            offers.add(merchantOffer);
        }

        ServerPlayer serverPlayer = getServerPlayer(player);
        int containerId = getContainerId(serverPlayer);

        serverPlayer.sendMerchantOffers(containerId, offers, level, experience, true, false);
    }

    /**
     * Converts an NMS item stack to an item cost.
     *
     * @param itemStack the item stack to convert
     * @return the item cost
     * @since 3.0.0
     */
    @NonNull
    @Contract(value = "_ -> new", pure = true)
    private ItemCost convertItemStackToItemCost(net.minecraft.world.item.@NonNull ItemStack itemStack) {
        DataComponentPredicate predicate = DataComponentPredicate.allOf(itemStack.getComponents());

        return new ItemCost(itemStack.getItemHolder(), itemStack.getCount(), predicate, itemStack);
    }

    /**
     * Gets the server player associated to this player
     *
     * @param player the player to get the server player from
     * @return the server player
     * @since 3.0.0
     */
    @NonNull
    @Contract(pure = true)
    private ServerPlayer getServerPlayer(@NonNull Player player) {
        return ((CraftPlayer) player).getHandle();
    }

    /**
     * Gets the containerId id for the inventory view the player currently has open
     *
     * @param nmsPlayer the player to get the containerId id for
     * @return the containerId id
     * @since 3.0.0
     */
    @Contract(pure = true)
    private int getContainerId(net.minecraft.world.entity.player.@NonNull Player nmsPlayer) {
        return nmsPlayer.containerMenu.containerId;
    }
}
