package dev.kokiriglade.popcorn.inventory.gui.type;

import dev.kokiriglade.popcorn.inventory.HumanEntityCache;
import dev.kokiriglade.popcorn.inventory.gui.InventoryComponent;
import dev.kokiriglade.popcorn.inventory.gui.type.abstraction.MerchantInventory;
import dev.kokiriglade.popcorn.inventory.gui.type.impl.MerchantInventoryImpl;
import dev.kokiriglade.popcorn.inventory.gui.type.util.Gui;
import dev.kokiriglade.popcorn.inventory.gui.type.util.NamedGui;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.TradeSelectEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Merchant;
import org.bukkit.inventory.MerchantRecipe;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.jetbrains.annotations.Contract;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Represents a gui in the form of a merchant.
 *
 * @since 3.0.0
 */
public class MerchantGui extends NamedGui {

    /**
     * The human entities viewing this gui
     */
    private final @NonNull List<HumanEntity> viewers = new ArrayList<>();
    /**
     * The trades of this merchant with their price differences. The differences are the difference between the new
     * price and the original price.
     */
    private final @NonNull List<Map.Entry<MerchantRecipe, Integer>> trades = new ArrayList<>();
    /**
     * The internal merchant inventory
     */
    private final @NonNull MerchantInventory merchantInventory = new MerchantInventoryImpl();
    /**
     * The consumer that will be called once a players selects a trade listed
     * on the left side of the gui
     */
    private Consumer<? super TradeSelectEvent> onTradeSelect;
    /**
     * Represents the inventory component for the input
     */
    private @NonNull InventoryComponent inputComponent = new InventoryComponent(2, 1);
    /**
     * Represents the inventory component for the player inventory
     */
    private @NonNull InventoryComponent playerInventoryComponent = new InventoryComponent(9, 4);
    /**
     * The merchant holding the trades and inventory
     */
    private @NonNull Merchant merchant;
    /**
     * The experience of this merchant. Values below zero indicate that the experience should be hidden.
     */
    private int experience = -1;
    /**
     * The level of this merchant. A value of zero indicates this villager doesn't have a level.
     */
    private int level = 0;

    /**
     * Creates a merchant gui with the given title.
     *
     * @param title the title
     * @since 3.0.0
     */
    public MerchantGui(final @NonNull Component title) {
        this(title, JavaPlugin.getProvidingPlugin(MerchantGui.class));
    }

    /**
     * Constructs a new merchant gui for the given {@code plugin}.
     *
     * @param title  the title/name of this gui.
     * @param plugin the owning plugin of this gui
     * @see #MerchantGui(Component)
     * @since 3.0.0
     */
    public MerchantGui(final @NonNull Component title, final @NonNull Plugin plugin) {
        super(title, plugin);

        this.merchant = Bukkit.createMerchant(getTitle());
    }

    /**
     * Set the consumer that should be called whenever a trade is selected
     * in this gui.
     *
     * @param onTradeSelect the consumer that gets called
     * @since 3.0.0
     */
    public void setOnTradeSelect(final @Nullable Consumer<? super TradeSelectEvent> onTradeSelect) {
        this.onTradeSelect = onTradeSelect;
    }

    /**
     * Calls the consumer (if it's not null) that was specified using {@link #setOnTradeSelect(Consumer)},
     * so the consumer that should be called whenever a trade is selected in this gui.
     * Catches and logs all exceptions the consumer might throw.
     *
     * @param event the event to handle
     * @since 3.0.0
     */
    public void callOnTradeSelect(final @NonNull TradeSelectEvent event) {
        callCallback(onTradeSelect, event, "onTradeSelect");
    }

    @Override
    public void show(final @NonNull HumanEntity humanEntity) {
        if (!(humanEntity instanceof Player player)) {
            throw new IllegalArgumentException("Merchants can only be opened by players");
        }

        if (isDirty()) {
            this.merchant = Bukkit.createMerchant(getTitle());
            markChanges();
        }

        final InventoryView view = humanEntity.openMerchant(merchant, true);

        if (view == null) {
            throw new IllegalStateException("Merchant could not be opened");
        }

        final Inventory inventory = view.getTopInventory();

        addInventory(inventory, this);

        inventory.clear();

        getInputComponent().display(inventory, 0);
        getPlayerInventoryComponent().display();

        if (getPlayerInventoryComponent().hasItem()) {
            final HumanEntityCache humanEntityCache = getHumanEntityCache();

            if (!humanEntityCache.contains(humanEntity)) {
                humanEntityCache.storeAndClear(humanEntity);
            }

            getPlayerInventoryComponent().placeItems(humanEntity.getInventory(), 0);
        }

        this.viewers.add(humanEntity);

        if (this.experience >= 0 || this.level > 0) {
            this.merchantInventory.sendMerchantOffers(player, this.trades, this.level, this.experience);

            return;
        }

        for (final Map.Entry<MerchantRecipe, Integer> trade : this.trades) {
            if (trade.getValue() != 0) {
                this.merchantInventory.sendMerchantOffers(player, this.trades, this.level, this.experience);

                break;
            }
        }
    }

    @Override
    public @NonNull Gui copy() {
        final MerchantGui gui = new MerchantGui(getTitle(), super.plugin);

        gui.inputComponent = inputComponent.copy();
        gui.playerInventoryComponent = playerInventoryComponent.copy();

        gui.experience = experience;
        gui.level = level;

        for (final Map.Entry<MerchantRecipe, Integer> trade : trades) {
            final MerchantRecipe originalRecipe = trade.getKey();

            final ItemStack result = originalRecipe.getResult().clone();
            final int uses = originalRecipe.getUses();
            final int maxUses = originalRecipe.getMaxUses();
            final boolean experienceReward = originalRecipe.hasExperienceReward();
            final int villagerExperience = originalRecipe.getVillagerExperience();
            final float priceMultiplier = originalRecipe.getPriceMultiplier();

            final MerchantRecipe recipe = new MerchantRecipe(
                result, uses, maxUses, experienceReward, villagerExperience, priceMultiplier
            );

            for (final ItemStack ingredient : originalRecipe.getIngredients()) {
                recipe.addIngredient(ingredient.clone());
            }

            gui.trades.add(new AbstractMap.SimpleImmutableEntry<>(recipe, trade.getValue()));
        }

        gui.setOnTopClick(this.onTopClick);
        gui.setOnBottomClick(this.onBottomClick);
        gui.setOnGlobalClick(this.onGlobalClick);
        gui.setOnOutsideClick(this.onOutsideClick);
        gui.setOnTradeSelect(this.onTradeSelect);
        gui.setOnClose(this.onClose);

        return gui;
    }

    @Override
    public void click(final @NonNull InventoryClickEvent event) {
        final int rawSlot = event.getRawSlot();

        if (rawSlot >= 0 && rawSlot <= 1) {
            getInputComponent().click(this, event, rawSlot);
        } else if (rawSlot != 2) {
            getPlayerInventoryComponent().click(this, event, rawSlot - 3);
        }
    }

    /**
     * Adds a trade to this gui. The specified discount is the difference between the old price and the new price. For
     * example, if a price was decreased from five to two, the discount would be three.
     *
     * @param recipe   the recipe to add
     * @param discount the discount
     * @since 3.0.0
     */
    public void addTrade(final @NonNull MerchantRecipe recipe, final int discount) {
        this.trades.add(new AbstractMap.SimpleImmutableEntry<>(recipe, -discount));

        final List<MerchantRecipe> recipes = new ArrayList<>(this.merchant.getRecipes());

        recipes.add(recipe);

        this.merchant.setRecipes(recipes);
    }

    /**
     * Sets the experience of this merchant gui. Setting the experience will make the experience bar visible, even if
     * the amount of experience is zero. Note that if the level of this merchant gui has not been set via
     * {@link #setLevel(int)} that the experience will always show as zero even when set to something else. Experience
     * must be greater than or equal to zero. Attempting to set the experience to below zero will throw an
     * {@link IllegalArgumentException}.
     *
     * @param experience the experience to set
     * @throws IllegalArgumentException when the experience is below zero
     * @since 3.0.0
     */
    public void setExperience(final int experience) {
        if (experience < 0) {
            throw new IllegalArgumentException("Experience must be greater than or equal to zero");
        }

        this.experience = experience;
    }

    /**
     * Sets the level of this merchant gui. This is a value between one and five and will visibly change the gui by
     * appending the level of the villager to the title. These are displayed as "Novice", "Apprentice", "Journeyman",
     * "Expert" and "Master" respectively (when the player's locale is set to English). When an argument is supplied
     * that is not within one and five, an {@link IllegalArgumentException} will be thrown.
     *
     * @param level the numeric level
     * @throws IllegalArgumentException when the level is not between one and five
     * @since 3.0.0
     */
    public void setLevel(final int level) {
        if (level < 0 || level > 5) {
            throw new IllegalArgumentException("Level must be between one and five");
        }

        this.level = level;
    }

    /**
     * Adds a trade to this gui. This will not set a discount on the trade. For specifiying discounts, see
     * {@link #addTrade(MerchantRecipe, int)}.
     *
     * @param recipe the recipe to add
     * @since 3.0.0
     */
    public void addTrade(final @NonNull MerchantRecipe recipe) {
        addTrade(recipe, 0);
    }

    /**
     * Handles a human entity closing this gui.
     *
     * @param humanEntity the human entity who's closing this gui
     * @since 3.0.0
     */
    public void handleClose(final @NonNull HumanEntity humanEntity) {
        this.viewers.remove(humanEntity);
    }

    @Override
    public boolean isPlayerInventoryUsed() {
        return getPlayerInventoryComponent().hasItem();
    }

    @Contract(pure = true)
    @Override
    public int getViewerCount() {
        return this.viewers.size();
    }

    @Contract(pure = true)
    @Override
    public @NonNull List<HumanEntity> getViewers() {
        return new ArrayList<>(this.viewers);
    }

    /**
     * Gets the inventory component representing the input
     *
     * @return the input component
     * @since 3.0.0
     */
    @Contract(pure = true)
    public @NonNull InventoryComponent getInputComponent() {
        return inputComponent;
    }

    /**
     * Gets the inventory component representing the player inventory
     *
     * @return the player inventory component
     * @since 3.0.0
     */
    @Contract(pure = true)
    public @NonNull InventoryComponent getPlayerInventoryComponent() {
        return playerInventoryComponent;
    }

}
