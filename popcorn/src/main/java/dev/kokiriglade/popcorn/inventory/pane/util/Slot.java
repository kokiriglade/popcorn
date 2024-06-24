package dev.kokiriglade.popcorn.inventory.pane.util;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.jetbrains.annotations.Contract;

import java.util.Objects;

/**
 * A slot represents a position in some type of container. Implementors of this class represent slots in different ways.
 *
 * @since 3.0.0
 */
@SuppressWarnings("unused")
public interface Slot {

    /**
     * Gets the x coordinate of this slot.
     *
     * @param length the length of the parent container
     * @return the x coordinate of this slot
     * @since 3.0.0
     */
    @Contract(pure = true)
    int getX(int length);

    /**
     * Gets the y coordinate of this slot.
     *
     * @param length the length of the parent container
     * @return the y coordinate of this slot
     * @since 3.0.0
     */
    @Contract(pure = true)
    int getY(int length);

    /**
     * Creates a new slot based on an (x, y) coordinate pair.
     *
     * @param x the x coordinate
     * @param y the y coordinate
     * @return the slot representing this position
     * @since 3.0.0
     */
    @NonNull
    @Contract(value = "_, _ -> new", pure = true)
    static Slot fromXY(int x, int y) {
        return new XY(x, y);
    }

    /**
     * Creates a new slot based on an index. This index is relative to the parent container this slot will be used in.
     *
     * @param index the index
     * @return the slot representing this relative position
     * @since 3.0.0
     */
    @NonNull
    @Contract("_ -> new")
    static Slot fromIndex(int index) {
        return new Indexed(index);
    }

    /**
     * A class representing a slot based on an (x, y) coordinate pair.
     *
     * @since 3.0.0
     */
    class XY implements Slot {

        /**
         * The (x, y) coordinate pair
         */
        private final int x, y;

        /**
         * Creates a new slot based on an (x, y) coordinate pair.
         *
         * @param x the x coordinate
         * @param y the y coordinate
         * @since 3.0.0
         */
        private XY(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public int getX(int length) {
            return this.x;
        }

        @Override
        public int getY(int length) {
            return this.y;
        }

        @Override
        public boolean equals(@Nullable Object object) {
            if (this == object) {
                return true;
            }

            if (object == null || getClass() != object.getClass()) {
                return false;
            }

            XY xy = (XY) object;

            return x == xy.x && y == xy.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }
    }

    /**
     * A class representing a slot based on an index.
     *
     * @since 3.0.0
     */
    class Indexed implements Slot {

        /**
         * The index of this slot.
         */
        private final int index;

        /**
         * Creates a new slot based on an index.
         *
         * @param index the index of this slot
         * @since 3.0.0
         */
        private Indexed(int index) {
            this.index = index;
        }

        /**
         * {@inheritDoc}
         *
         * If {@code length} is zero, this will throw an {@link IllegalArgumentException}.
         *
         * @param length {@inheritDoc}
         * @return {@inheritDoc}
         * @throws IllegalArgumentException when {@code length} is zero
         */
        @Override
        @Contract(pure = true)
        public int getX(int length) {
            if (length == 0) {
                throw new IllegalArgumentException("Length may not be zero");
            }

            return this.index % length;
        }

        /**
         * {@inheritDoc}
         *
         * If {@code length} is zero, this will throw an {@link IllegalArgumentException}.
         *
         * @param length {@inheritDoc}
         * @return {@inheritDoc}
         * @throws IllegalArgumentException when {@code length} is zero
         */
        @Override
        @Contract(pure = true)
        public int getY(int length) {
            if (length == 0) {
                throw new IllegalArgumentException("Length may not be zero");
            }

            return this.index / length;
        }

        @Override
        public boolean equals(@Nullable Object object) {
            if (this == object) {
                return true;
            }

            if (object == null || getClass() != object.getClass()) {
                return false;
            }

            Indexed indexed = (Indexed) object;

            return index == indexed.index;
        }

        @Override
        public int hashCode() {
            return index;
        }
    }

}
