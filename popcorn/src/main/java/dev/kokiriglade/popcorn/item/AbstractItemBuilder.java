package dev.kokiriglade.popcorn.item;

import com.google.common.collect.Multimap;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemRarity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.components.FoodComponent;
import org.bukkit.inventory.meta.components.JukeboxPlayableComponent;
import org.bukkit.persistence.PersistentDataType;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.common.value.qual.IntRange;

import java.util.*;
import java.util.function.Consumer;

/**
 * Modifies {@link ItemStack}s
 *
 * @param <B> the builder type
 * @param <M> the {@link ItemMeta} type
 * @since 1.0.0
 */
@SuppressWarnings({"unchecked", "unused"})
public abstract class AbstractItemBuilder<B extends AbstractItemBuilder<B, M>, M extends ItemMeta> {

    private static final Component DISABLE_ITALICS = Component.empty().decoration(TextDecoration.ITALIC, false);

    /**
     * The {@code ItemMeta} to modify during building. This will be applied to
     * the {@link #itemStack} upon {@link #build()}.
     */
    protected final @NonNull M itemMeta;
    /**
     * The {@code ItemStack} to modify during building. This will be cloned and
     * returned upon {@link #build()}.
     */
    protected @NonNull ItemStack itemStack;

    /**
     * Create a new AbstractItemBuilder
     *
     * @param itemStack the {@code ItemStack}
     * @param itemMeta  the {@code ItemMeta}
     * @since 1.0.0
     */
    protected AbstractItemBuilder(final @NonNull ItemStack itemStack, final @NonNull M itemMeta) {
        this.itemStack = itemStack.clone();
        this.itemMeta = itemMeta;
    }

    /**
     * Attempts to cast {@code meta} to {@code expectedType},
     * and returns the result if successful.
     *
     * @param meta         the meta
     * @param expectedType the class of the expected type
     * @param <T>          the expected type
     * @return {@code meta} casted to {@code expectedType}
     * @throws IllegalArgumentException if {@code} meta is not the type of {@code expectedType}
     * @since 1.0.0
     */
    protected static <T extends ItemMeta> T castMeta(final ItemMeta meta, final Class<T> expectedType)
        throws IllegalArgumentException {
        try {
            return expectedType.cast(meta);
        } catch (final ClassCastException e) {
            throw new IllegalArgumentException("The ItemMeta must be of type %s but received ItemMeta of type %s"
                .formatted(
                    expectedType.getSimpleName(),
                    meta.getClass().getSimpleName()
                )
            );
        }
    }

    /**
     * Returns an {@code ItemStack} of {@code material} if it is an item,
     * else throws an exception.
     *
     * @param material the material
     * @return an {@code ItemStack} of type {@code material}
     * @throws IllegalArgumentException if {@code material} is not an item
     * @since 1.0.0
     */
    protected static @NonNull ItemStack getItem(final @NonNull Material material) throws IllegalArgumentException {
        if (!material.isItem()) {
            throw new IllegalArgumentException("The Material must be an obtainable item.");
        }
        return new ItemStack(material);
    }

    /**
     * Gets the display name.
     *
     * @return the display name
     * @since 1.0.0
     */
    public @Nullable Component displayName() {
        return this.itemMeta.displayName();
    }

    /**
     * Sets the display name. Pass {@code null} to reset.
     *
     * @param name the display name
     * @return the builder
     * @since 1.0.0
     */
    public @NonNull B displayName(final @Nullable Component name) {
        this.itemMeta.displayName(name);
        return (B) this;
    }

    /**
     * Gets the item name.
     *
     * @return the item name
     * @since 1.0.0
     */
    public @Nullable Component itemName() {
        return this.itemMeta.itemName();
    }

    /**
     * Sets the item name. Pass {@code null} to reset.
     *
     * @param name the item name
     * @return the builder
     * @since 1.0.0
     */
    public @NonNull B itemName(final @Nullable Component name) {
        this.itemMeta.itemName(name);

        return (B) this;
    }

    /**
     * Gets the lore.
     *
     * @return the lore
     * @since 1.0.0
     */
    public @Nullable List<Component> lore() {
        return this.itemMeta.lore();
    }

    /**
     * Sets the lore. Pass {@code null} to reset.
     * <p>
     * Each component passed in is appended to an empty component decorated with
     * italicization set to false. This effectively bypasses the default,
     * italicized text formatting, resulting in the text being only the component
     * that is passed in.
     *
     * @param lines the lines of the lore
     * @return the builder
     * @since 1.0.0
     */
    public @NonNull B lore(final @Nullable List<Component> lines) {
        if (lines == null) {
            this.itemMeta.lore(null);
            return (B) this;
        }

        // sidestep default formatting by creating a dummy component and appending the component to that
        final @NonNull List<Component> toAdd = new ArrayList<>(lines);
        toAdd.replaceAll(DISABLE_ITALICS::append);

        this.itemMeta.lore(toAdd);
        return (B) this;
    }

    /**
     * A utility method that converts the provided {@code lines} into a
     * {@code List} using {@link List#of(Object[])}, and calls
     * {@link #lore(List)} using the new {@code List} as the argument.
     *
     * @param lines the lines of the lore
     * @return the builder
     * @since 1.0.0
     */
    public @NonNull B loreList(final @NonNull Component... lines) {
        return this.lore(List.of(lines));
    }

    /**
     * Directly modifies the lore with a {@link Consumer}.
     * If the item has no lore, an empty {@code List} will
     * be supplied to the {@code Consumer} instead.
     *
     * @param consumer the {@code Consumer} to modify the lore with
     * @return the builder
     * @since 1.0.0
     */
    public @NonNull B loreModifier(final @NonNull Consumer<@NonNull List<Component>> consumer) {
        final @NonNull List<Component> lore = Optional
            .ofNullable(this.itemMeta.lore())
            .orElse(new ArrayList<>());

        consumer.accept(lore);

        this.itemMeta.lore(lore);
        return (B) this;
    }

    /**
     * Gets the {@code Material}.
     *
     * @return the {@code Material}
     * @since 1.0.0
     */
    public @NonNull Material material() {
        return this.itemStack.getType();
    }

    /**
     * Sets the {@code Material}.
     *
     * @param material the {@code Material}
     * @return the builder
     * @since 1.0.0
     */
    public @NonNull B material(final @NonNull Material material) {
        this.itemStack = this.itemStack.withType(material);
        return (B) this;
    }

    /**
     * Gets the quantity.
     *
     * @return the quantity
     * @since 1.0.0
     */
    public int amount() {
        return this.itemStack.getAmount();
    }

    /**
     * Sets the quantity.
     *
     * @param amount the quantity
     * @return the builder
     * @since 1.0.0
     */
    public @NonNull B amount(final int amount) {
        this.itemStack.setAmount(amount);
        return (B) this;
    }

    /**
     * Gets data from the {@code ItemStack}'s {@link org.bukkit.persistence.PersistentDataContainer}.
     *
     * @param key  the {@code NamespacedKey} to use
     * @param type the {@code PersistentDataType to use}
     * @param <T>  the primary object type of the data
     * @param <Z>  the retrieve object type of the data
     * @return the data
     * @since 1.0.0
     */
    public <T, Z> @Nullable Z getData(
        final @NonNull NamespacedKey key,
        final @NonNull PersistentDataType<T, Z> type
    ) {
        return this.itemMeta.getPersistentDataContainer().get(key, type);
    }

    /**
     * Checks if the {@code ItemStack}'s {@link org.bukkit.persistence.PersistentDataContainer}
     * contains data for the given {@code NamespacedKey} and {@code PersistentDataType}.
     *
     * @param key  the {@code NamespacedKey} to check
     * @param type the {@code PersistentDataType} to check
     * @param <T>  the primary object type of the data
     * @param <Z>  the retrieve object type of the data
     * @return {@code true} if the data exists, {@code false} otherwise
     * @since 1.0.0
     */
    public <T, Z> boolean hasData(
        final @NonNull NamespacedKey key,
        final @NonNull PersistentDataType<T, Z> type
    ) {
        return this.itemMeta.getPersistentDataContainer().has(key, type);
    }

    /**
     * Adds data to the {@code ItemStack}'s {@link org.bukkit.persistence.PersistentDataContainer}.
     *
     * @param key    the {@code NamespacedKey} to use
     * @param type   the {@code PersistentDataType} to use
     * @param object the data to set
     * @param <T>    the primary object type of the data
     * @param <Z>    the retrieve object type of the data
     * @return the builder
     * @since 1.0.0
     */
    public <T, Z> @NonNull B setData(
        final @NonNull NamespacedKey key,
        final @NonNull PersistentDataType<T, Z> type,
        final @NonNull Z object
    ) {
        this.itemMeta.getPersistentDataContainer().set(key, type, object);
        return (B) this;
    }

    /**
     * Removes data from the {@code ItemStack}'s {@link org.bukkit.persistence.PersistentDataContainer}.
     *
     * @param key the {@code NamespacedKey} to use
     * @return the builder
     * @since 1.0.0
     */
    public @NonNull B removeData(
        final @NonNull NamespacedKey key
    ) {
        this.itemMeta.getPersistentDataContainer().remove(key);
        return (B) this;
    }

    /**
     * Gets the {@code ItemFlag}s.
     *
     * @return the {@code ItemFlag}s
     * @since 1.0.0
     */
    public @NonNull Set<ItemFlag> flags() {
        return this.itemMeta.getItemFlags();
    }

    /**
     * Sets the {@code ItemFlag}s. Pass {@code null} to reset.
     *
     * @param flags the {@code ItemFlag}s
     * @return the builder
     * @since 1.0.0
     */
    public @NonNull B flags(final @Nullable List<ItemFlag> flags) {
        this.clearFlags();
        if (flags != null) {
            this.itemMeta.addItemFlags(flags.toArray(new ItemFlag[0]));
        }
        return (B) this;
    }

    private void clearFlags() {
        this.itemMeta.removeItemFlags(this.itemMeta.getItemFlags().toArray(new ItemFlag[0]));
    }

    /**
     * Add an {@code ItemFlag}.
     *
     * @param flag the {@code ItemFlag} to add
     * @return the builder
     * @since 1.0.0
     */
    public @NonNull B addFlag(final @NonNull ItemFlag... flag) {
        this.itemMeta.addItemFlags(flag);
        return (B) this;
    }

    /**
     * Remove an {@code ItemFlag}.
     *
     * @param flag the {@code ItemFlag} to remove
     * @return the builder
     * @since 1.0.0
     */
    public @NonNull B removeFlag(final @NonNull ItemFlag... flag) {
        this.itemMeta.removeItemFlags(flag);
        return (B) this;
    }

    /**
     * Gets the {@code Enchantment}s.
     *
     * @return the {@code Enchantment}s
     * @since 1.0.0
     */
    public @NonNull Map<Enchantment, Integer> enchants() {
        return new HashMap<>(this.itemStack.getEnchantments());
    }

    /**
     * Sets the {@code Enchantment}s. Pass {@code null} to reset.
     *
     * @param enchants the {@code Enchantment}s
     * @return the builder
     * @since 1.0.0
     */
    public @NonNull B enchants(final @Nullable Map<Enchantment, Integer> enchants) {
        this.clearEnchants();
        if (enchants != null) {
            this.itemStack.addEnchantments(enchants);
        }
        return (B) this;
    }

    private void clearEnchants() {
        for (final Enchantment enchantment : this.itemMeta.getEnchants().keySet()) {
            this.itemMeta.removeEnchant(enchantment);
        }
    }

    /**
     * Adds an {@code Enchantment}.
     *
     * @param enchantment the {@code Enchantment} to add
     * @param level       the level of the {@code Enchantment}
     * @return the builder
     * @since 1.0.0
     */
    public @NonNull B addEnchant(final @NonNull Enchantment enchantment, final int level) {
        this.itemMeta.addEnchant(enchantment, level, true);
        return (B) this;
    }

    /**
     * Removes an {@code Enchantment}.
     *
     * @param enchantment the {@code Enchantment} to remove
     * @return the builder
     * @since 1.0.0
     */
    public @NonNull B removeEnchant(final @NonNull Enchantment... enchantment) {
        for (final Enchantment item : enchantment) {
            this.itemMeta.removeEnchant(item);
        }
        return (B) this;
    }

    /**
     * Sets whether the {@code ItemStack} is hiding its tooltip.
     *
     * @param hideTooltip whether the {@code ItemStack} is hiding its tooltip
     * @return the builder
     * @since 1.0.0
     */
    public @NonNull B hideTooltip(final boolean hideTooltip) {
        itemMeta.setHideTooltip(hideTooltip);
        return (B) this;
    }


    /**
     * Gets whether the {@code ItemStack} is hiding its tooltip.
     *
     * @return {@code true} if the item is hiding tooltip, {@code false} otherwise
     * @since 1.0.0
     */
    public boolean hideTooltip() {
        return itemMeta.isHideTooltip();
    }

    /**
     * Get the max stack size.
     *
     * @return the max stack size
     * @since 1.0.0
     */
    public int maxStackSize() {
        return this.itemStack.getMaxStackSize();
    }

    /**
     * Sets the {@code ItemStack}'s rarity.
     *
     * @param rarity the rarity
     * @return the builder
     * @since 1.0.0
     */
    public @NonNull B rarity(final @Nullable ItemRarity rarity) {
        itemMeta.setRarity(rarity);
        return (B) this;
    }

    /**
     * Get the rarity.
     *
     * @return the rarity
     * @since 1.0.0
     */
    public @Nullable ItemRarity rarity() {
        if (!this.itemMeta.hasRarity()) {
            return null;
        }
        return this.itemMeta.getRarity();
    }

    /**
     * Set the max stack size.
     *
     * @param maxStackSize the max stack size
     * @return the builder
     * @since 1.0.0
     */
    public B maxStackSize(@Nullable @IntRange(from = 1, to = 99) Integer maxStackSize) {
        itemMeta.setMaxStackSize(maxStackSize);
        return (B) this;
    }

    /**
     * Gets the custom model data.
     *
     * @return the custom model data
     * @since 1.0.0
     */
    public @Nullable Integer customModelData() {
        // we use the wrapper with null signifying absent for api consistency
        if (!this.itemMeta.hasCustomModelData()) {
            return null;
        }
        return this.itemMeta.getCustomModelData();
    }

    /**
     * Sets the custom model data. Pass {@code null} to reset.
     *
     * @param customModelData the custom model data
     * @return the builder
     * @since 1.0.0
     */
    public @NonNull B customModelData(final @Nullable Integer customModelData) {
        this.itemMeta.setCustomModelData(customModelData);
        return (B) this;
    }

    /**
     * Gets the {@code AttributeModifier}s.
     *
     * @return the {@code AttributeModifier}s
     * @since 1.0.0
     */
    public @Nullable Multimap<Attribute, AttributeModifier> attributeModifiers() {
        return this.itemMeta.getAttributeModifiers();
    }

    /**
     * Sets the {@code AttributeModifier}s. Pass {@code null} to reset.
     *
     * @param attributeModifiers the {@code AttributeModifier}s
     * @return the builder
     * @since 1.0.0
     */
    public @NonNull B attributeModifiers(final @Nullable Multimap<Attribute, AttributeModifier> attributeModifiers) {
        this.itemMeta.setAttributeModifiers(attributeModifiers);
        return (B) this;
    }

    /**
     * Adds an {@code AttributeModifier}.
     *
     * @param attribute         the attribute to modify
     * @param attributeModifier the {@code AttributeModifier} to add
     * @return the builder
     * @since 1.0.0
     */
    public @NonNull B addAttributeModifier(
        final @NonNull Attribute attribute,
        final @NonNull AttributeModifier attributeModifier
    ) {
        this.itemMeta.addAttributeModifier(attribute, attributeModifier);
        return (B) this;
    }

    /**
     * Removes an {@code AttributeModifier}.
     *
     * @param attribute         the attribute to modify
     * @param attributeModifier the {@code AttributeModifier} to remove
     * @return the builder
     * @since 1.0.0
     */
    public @NonNull B removeAttributeModifier(
        final @NonNull Attribute attribute,
        final @NonNull AttributeModifier attributeModifier
    ) {
        this.itemMeta.removeAttributeModifier(attribute, attributeModifier);
        return (B) this;
    }

    /**
     * Removes all {@code AttributeModifier}s for the given {@code attribute}.
     *
     * @param attribute the {@code Attribute}
     * @return the builder
     * @since 1.0.0
     */
    public @NonNull B removeAttributeModifier(final @NonNull Attribute... attribute) {
        for (final @NonNull Attribute item : attribute) {
            this.itemMeta.removeAttributeModifier(item);
        }
        return (B) this;
    }

    /**
     * Gets the enchantment glint override.
     *
     * @return the enchantment glint override
     * @since 1.1.0
     */
    public @NonNull Boolean enchantmentGlintOverride() {
        return this.itemMeta.getEnchantmentGlintOverride();
    }

    /**
     * Sets the enchantment glint override.
     *
     * @param enchantmentGlintOverride the enchantment glint override
     * @return the builder
     * @since 1.1.0
     */
    public @NonNull B enchantmentGlintOverride(final @NonNull Boolean enchantmentGlintOverride) {
        this.itemMeta.setEnchantmentGlintOverride(enchantmentGlintOverride);
        return (B) this;
    }

    /**
     * Gets the jukebox playable component.
     *
     * @return the jukebox playable component
     * @since 1.1.0
     */
    @SuppressWarnings("UnstableApiUsage")
    public @Nullable JukeboxPlayableComponent jukeboxPlayable() {
        return this.itemMeta.getJukeboxPlayable();
    }

    /**
     * Checks if the item has a jukebox playable component.
     *
     * @return {@code true} if the item has a jukebox playable component, {@code false} otherwise
     * @since 1.1.0
     */
    public boolean hasJukeboxPlayable() {
        return this.itemMeta.hasJukeboxPlayable();
    }

    /**
     * Sets the jukebox playable component.
     *
     * @param jukeboxPlayableComponent the jukebox playable component
     * @return the builder
     * @since 1.1.0
     */
    @SuppressWarnings("UnstableApiUsage")
    public @NonNull B jukeboxPlayable(final @Nullable JukeboxPlayableComponent jukeboxPlayableComponent) {
        this.itemMeta.setJukeboxPlayable(jukeboxPlayableComponent);
        return (B) this;
    }

    /**
     * Gets the food component.
     *
     * @return the food component
     * @since 1.1.0
     */
    @SuppressWarnings("UnstableApiUsage")
    public @NonNull FoodComponent food() {
        return this.itemMeta.getFood();
    }

    /**
     * Checks if the item has a food component.
     *
     * @return {@code true} if the item has a food component, {@code false} otherwise
     * @since 1.1.0
     */
    public boolean hasFood() {
        return this.itemMeta.hasFood();
    }

    /**
     * Sets the food component.
     *
     * @param food the food component
     * @return the builder
     * @since 1.1.0
     */
    @SuppressWarnings("UnstableApiUsage")
    public @NonNull B food(final @Nullable FoodComponent food) {
        this.itemMeta.setFood(food);
        return (B) this;
    }

    /**
     * Checks if the item is fire resistant.
     *
     * @return {@code true} if the item is fire resistant, {@code false} otherwise
     * @since 1.1.0
     */
    public boolean fireResistant() {
        return this.itemMeta.isFireResistant();
    }

    /**
     * Sets whether the item is fire resistant.
     *
     * @param fireResistant whether the item is fire resistant
     * @return the builder
     * @since 1.1.0
     */
    public B fireResistant(boolean fireResistant) {
        this.itemMeta.setFireResistant(fireResistant);
        return (B) this;
    }


    /**
     * Builds the {@code ItemStack} from the set properties.
     *
     * @return the built {@code ItemStack}
     * @since 1.0.0
     */
    public @NonNull ItemStack build() {
        this.itemStack.setItemMeta(this.itemMeta);
        return this.itemStack.clone();
    }

}
