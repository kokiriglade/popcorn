package dev.kokiriglade.popcorn.inventory.util;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.Contract;

import java.util.AbstractMap;
import java.util.Map;

/**
 * Utility methods for inventory geometry
 * @since 3.0.0
 */
@SuppressWarnings("unused")
public class GeometryUtil {

    /**
     * Calculates a clockwise rotation across a two-dimensional grid
     *
     * @param x        the standard x coordinate
     * @param y        the standard y coordinate
     * @param length   the length of the grid
     * @param height   the height of the grid
     * @param rotation the rotation in degrees
     * @return a pair of new coordinates, with the x coordinate being the key and the y coordinate being the value
     * @since 3.0.0
     */
    @Contract(pure = true)
    public static Map.@NonNull Entry<Integer, Integer> processClockwiseRotation(final int x, final int y, final int length, final int height,
                                                                                final int rotation) {
        int newX = x, newY = y;

        if (rotation == 90) {
            newX = height - 1 - y;
            //noinspection SuspiciousNameCombination
            newY = x;
        } else if (rotation == 180) {
            newX = length - 1 - x;
            newY = height - 1 - y;
        } else if (rotation == 270) {
            //noinspection SuspiciousNameCombination
            newX = y;
            newY = length - 1 - x;
        }

        return new AbstractMap.SimpleEntry<>(newX, newY);
    }

    /**
     * Calculates a counterclockwise rotation across a two-dimensional grid. This is the same as calling
     * {@link #processClockwiseRotation(int, int, int, int, int)} with 360 - rotation as the rotation.
     *
     * @param x        the standard x coordinate
     * @param y        the standard y coordinate
     * @param length   the length of the grid
     * @param height   the height of the grid
     * @param rotation the rotation in degrees
     * @return a pair of new coordinates, with the x coordinate being the key and the y coordinate being the value
     * @since 3.0.0
     */
    @Contract(pure = true)
    public static Map.@NonNull Entry<Integer, Integer> processCounterClockwiseRotation(final int x, final int y, final int length, final int height,
                                                                                       final int rotation) {
        return processClockwiseRotation(x, y, length, height, 360 - rotation);
    }

}
