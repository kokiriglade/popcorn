package dev.kokiriglade.popcorn.inventory.pane;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.Contract;

/**
 * An interface for panes that can have different orientations
 *
 * @since 3.0.0
 */
public interface Orientable {

    /**
     * Gets the orientation of this outline pane
     *
     * @return the orientation
     * @since 3.0.0
     */
    @NonNull
    @Contract(pure = true)
    Orientation getOrientation();

    /**
     * Sets the orientation of this outline pane
     *
     * @param orientation the new orientation
     * @since 3.0.0
     */
    void setOrientation(@NonNull Orientation orientation);

    /**
     * An orientation for outline panes
     *
     * @since 3.0.0
     */
    enum Orientation {

        /**
         * A horizontal orientation, will outline every item from the top-left corner going to the right and down
         *
         * @since 3.0.0
         */
        HORIZONTAL,

        /**
         * A vertical orientation, will outline every item from the top-left corner going down and to the right
         *
         * @since 3.0.0
         */
        VERTICAL
    }
}
