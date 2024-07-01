package dev.kokiriglade.popcorn.inventory.gui.type.abstraction;

import org.bukkit.entity.Player;
import org.bukkit.inventory.MerchantRecipe;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.List;
import java.util.Map;

/**
 * A merchant inventory
 *
 * @since 3.0.0
 */
public abstract class MerchantInventory {

    /**
     * Sends the merchant offers to the player, combined with the merchants level and experience.
     *
     * @param player     the player to send this to
     * @param trades     the trades to send
     * @param level      the level of the merchant
     * @param experience the experience of the merchant
     * @since 3.0.0
     */
    public abstract void sendMerchantOffers(@NonNull Player player,
                                            @NonNull List<? extends Map.Entry<? extends MerchantRecipe, ? extends Integer>> trades,
                                            int level, int experience);

}
