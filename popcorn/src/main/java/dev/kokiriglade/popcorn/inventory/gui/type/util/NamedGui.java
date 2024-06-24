package dev.kokiriglade.popcorn.inventory.gui.type.util;

import net.kyori.adventure.text.Component;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.Contract;

public abstract class NamedGui extends Gui {

    /**
     * The title of this gui
     */
    @NonNull
    private Component title;

    /**
     * Whether the title is dirty i.e., has changed
     */
    private boolean dirty = false;

    /**
     * Constructs a new gui with a title
     *
     * @param title the title/name of this gui
     * @since 3.0.0
     */
    public NamedGui(@NonNull Component title) {
        this(title, JavaPlugin.getProvidingPlugin(NamedGui.class));
    }

    /**
     * Constructs a new gui with a title for the given {@code plugin}.
     *
     * @param title the title/name of this gui
     * @param plugin the owning plugin of this gui
     * @see #NamedGui(Component)
     * @since 3.0.0
     */
    public NamedGui(@NonNull Component title, @NonNull Plugin plugin) {
        super(plugin);

        this.title = title;
    }

    /**
     * Sets the title for this inventory.
     *
     * @param title the title
     * @since 3.0.0
     */
    public void setTitle(@NonNull Component title) {
        this.title = title;
        this.dirty = true;
    }

    /**
     * Returns the title of this GUI.
     *
     * @return the title
     * @since 3.0.0
     */
    @NonNull
    @Contract(pure = true)
    public Component getTitle() {
        return title;
    }

    /**
     * Gets whether this title is dirty or not i.e. whether the title has changed.
     *
     * @return whether the title is dirty
     * @since 3.0.0
     */
    @Contract(pure = true)
    public boolean isDirty() {
        return dirty;
    }

    /**
     * Marks that the changes present here have been accepted. This sets dirty to false. If dirty was already false,
     * this will do nothing.
     *
     * @since 3.0.0
     */
    public void markChanges() {
        this.dirty = false;
    }
}
