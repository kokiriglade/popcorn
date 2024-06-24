package dev.kokiriglade.popcorn.inventory.pane;

import org.jetbrains.annotations.Contract;

/**
 * An interface for panes that can be flipped
 *
 * @since 3.0.0
 */
public interface Flippable {

    /**
     * Sets whether this pane should flip its items horizontally
     *
     * @param flipHorizontally whether the pane should flip items horizontally
     * @since 3.0.0
     */
    void flipHorizontally(boolean flipHorizontally);

    /**
     * Sets whether this pane should flip its items vertically
     *
     * @param flipVertically whether the pane should flip items vertically
     * @since 3.0.0
     */
    void flipVertically(boolean flipVertically);

    /**
     * Gets whether this pane's items are flipped horizontally
     *
     * @return true if the items are flipped horizontally, false otherwise
     * @since 3.0.0
     */
    @Contract(pure = true)
    boolean isFlippedHorizontally();

    /**
     * Gets whether this pane's items are flipped vertically
     *
     * @return true if the items are flipped vertically, false otherwise
     * @since 3.0.0
     */
    @Contract(pure = true)
    boolean isFlippedVertically();

}
