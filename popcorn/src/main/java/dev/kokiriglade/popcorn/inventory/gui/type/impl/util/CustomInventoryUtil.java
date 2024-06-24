package dev.kokiriglade.popcorn.inventory.gui.type.impl.util;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.jetbrains.annotations.Contract;

/**
 * A utility class for custom inventories
 *
 * @since 3.0.0
 */
public final class CustomInventoryUtil {

    /**
     * A private constructor to prevent construction.
     */
    private CustomInventoryUtil() {}

    /**
     * Converts an array of Bukkit items into a non-null list of NMS items. The returned list is modifiable. If no items
     * were specified, this returns an empty list.
     *
     * @param items the items to convert
     * @return a list of converted items
     * @since 3.0.0
     */
    @NonNull
    @Contract(pure = true)
    public static NonNullList<ItemStack> convertToNMSItems(@Nullable org.bukkit.inventory.ItemStack @NonNull [] items) {
        NonNullList<ItemStack> nmsItems = NonNullList.create();

        for (org.bukkit.inventory.ItemStack item : items) {
            nmsItems.add(CraftItemStack.asNMSCopy(item));
        }

        return nmsItems;
    }
}
