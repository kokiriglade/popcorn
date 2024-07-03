package dev.kokiriglade.popcorn;

import dev.kokiriglade.popcorn.config.MessageManager;
import dev.kokiriglade.popcorn.plugin.itemvault.ItemVault;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.ApiStatus;

/**
 * The internal Popcorn plugin. You should use {@link Popcorn} rather than interface with this directly.
 *
 * @since 3.2.0
 */
@ApiStatus.Internal
public final class PopcornPlugin extends JavaPlugin implements Popcorn {

    private MessageManager<PopcornPlugin> messageManager;
    private ItemVault itemVault;

    @Override
    public void onLoad() {
        messageManager = new MessageManager<>(this);
    }

    @Override
    public void onEnable() {
        itemVault = new ItemVault(this);
    }

    @Override
    public @NonNull MessageManager<PopcornPlugin> getMessageManager() {
        return messageManager;
    }

    @Override
    public @NonNull ItemVault getItemVault() {
        return itemVault;
    }

}
