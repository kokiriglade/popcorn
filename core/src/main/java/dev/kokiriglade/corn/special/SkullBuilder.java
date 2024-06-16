package dev.kokiriglade.corn.special;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import dev.kokiriglade.corn.AbstractItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Modifies {@link ItemStack}s that have an {@code ItemMeta} of {@link SkullMeta}.
 * @since 1.0.0
 */
@SuppressWarnings("unused")
public final class SkullBuilder extends AbstractItemBuilder<SkullBuilder, SkullMeta> {

    private SkullBuilder(final @NonNull ItemStack itemStack, final @NonNull SkullMeta itemMeta) {
        super(itemStack, itemMeta);
    }

    /**
     * Creates a {@code SkullBuilder}.
     *
     * @param itemStack the {@code ItemStack} to base the builder off of
     * @return instance of {@code SkullBuilder}
     * @throws IllegalArgumentException if the {@code itemStack}'s {@code ItemMeta} is not the correct type
     * @since 1.0.0
     */
    public static @NonNull SkullBuilder of(final @NonNull ItemStack itemStack) throws IllegalArgumentException {
        return new SkullBuilder(itemStack, castMeta(itemStack.getItemMeta(), SkullMeta.class));
    }

    /**
     * Creates a {@code SkullBuilder}.
     *
     * @param material the {@code Material} to base the builder off of
     * @return instance of {@code SkullBuilder}
     * @throws IllegalArgumentException if the {@code material} is not an obtainable item,
     *                                  or if the {@code material}'s {@code ItemMeta} is not the correct type
     * @since 1.0.0
     */
    public static @NonNull SkullBuilder ofType(final @NonNull Material material) throws IllegalArgumentException {
        return SkullBuilder.of(getItem(material));
    }

    /**
     * Creates a {@code SkullBuilder} of type {@link Material#PLAYER_HEAD}. A convenience method.
     *
     * @return instance of {@code SkullBuilder}
     * @throws IllegalArgumentException if the {@code material} is not an obtainable item,
     *                                  or if the {@code material}'s {@code ItemMeta} is not the correct type
     * @since 1.0.0
     */
    public static @NonNull SkullBuilder ofPlayerHead() throws IllegalArgumentException {
        return ofType(Material.PLAYER_HEAD);
    }

    /**
     * Gets the textures.
     *
     * @return the textures
     * @since 1.0.0
     */
    public @NonNull List<@NonNull ProfileProperty> textures() {
        final @Nullable PlayerProfile profile = this.itemMeta.getPlayerProfile();
        if (profile == null) {
            return List.of();
        }

        final @NonNull List<@NonNull ProfileProperty> textures = new ArrayList<>();
        for (final @NonNull ProfileProperty property : profile.getProperties()) {
            if (property.getName().equals("textures")) {
                textures.add(property);
            }
        }

        return textures;
    }

    /**
     * Sets the textures.
     *
     * @param data the textures
     * @return the builder
     * @since 1.0.0
     */
    public @NonNull SkullBuilder textures(final @NonNull String data) {
        final @NonNull PlayerProfile profile = Optional
            .ofNullable(this.itemMeta.getPlayerProfile())
            .orElse(Bukkit.createProfile(UUID.randomUUID()));

        profile.setProperty(new ProfileProperty("textures", data));

        this.itemMeta.setPlayerProfile(profile);
        return this;
    }

    /**
     * Gets the {@code PlayerProfile}.
     *
     * @return the {@code PlayerProfile}
     * @since 1.0.0
     */
    public @Nullable PlayerProfile playerProfile() {
        return this.itemMeta.getPlayerProfile();
    }

    /**
     * Sets the {@code PlayerProfile}. Pass {@code null} to reset.
     *
     * @param playerProfile the {@code PlayerProfile}
     * @return the builder
     * @since 1.0.0
     */
    public @NonNull SkullBuilder playerProfile(final @Nullable PlayerProfile playerProfile) {
        this.itemMeta.setPlayerProfile(playerProfile);
        return this;
    }

    /**
     * Gets the owning player.
     *
     * @return the owning player
     * @since 1.0.0
     */
    public @Nullable OfflinePlayer owningPlayer() {
        return this.itemMeta.getOwningPlayer();
    }

    /**
     * Sets the owning player. Pass {@code null} to reset.
     *
     * @param owningPlayer the owning player
     * @return the builder
     * @since 1.0.0
     */
    public @NonNull SkullBuilder owningPlayer(final @Nullable OfflinePlayer owningPlayer) {
        this.itemMeta.setOwningPlayer(owningPlayer);
        return this;
    }

    /**
     * Gets the note block sound.
     *
     * @return the note block sound
     * @since 1.0.0
     */
    public NamespacedKey noteBlockSound() {
        return this.itemMeta.getNoteBlockSound();
    }

    /**
     * Sets the note block sound.
     *
     * @param noteBlockSound the note block sound
     * @return the builder
     * @since 1.0.0
     */
    public SkullBuilder noteBlockSound(final NamespacedKey noteBlockSound) {
        this.itemMeta.setNoteBlockSound(noteBlockSound);
        return this;
    }

}