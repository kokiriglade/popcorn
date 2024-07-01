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
     * Creates a new slot based on an (x, y) coordinate pair.
     *
     * @param x the x coordinate
     * @param y the y coordinate
     * @return the slot representing this position
     * @since 3.0.0
     */
    @Contract(value = "_, _ -> new", pure = true)
    static @NonNull Slot fromXY(final int x, final int y) {
        return new XY(x, y);
    }

    /**
     * Creates a new slot based on an index. This index is relative to the parent container this slot will be used in.
     *
     * @param index the index
     * @return the slot representing this relative position
     * @since 3.0.0
     */
    @Contract("_ -> new")
    static @NonNull Slot fromIndex(final int index) {
        return new Indexed(index);
    }

    /**
     * Gets the x coordinate of this slot.
     *
     * @param length the length of the parent container
     * @return the x coordinate of this slot
     * @since 3.0.0
     */
    @Contract(pure = true)
    int getX(final int length);

    /**
     * Gets the y coordinate of this slot.
     *
     * @param length the length of the parent container
     * @return the y coordinate of this slot
     * @since 3.0.0
     */
    @Contract(pure = true)
    int getY(final int length);

    /**
     * A class representing a slot based on an (x, y) coordinate pair.
     *
     * @since 3.0.0
     */
    final class XY implements Slot {

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
        private XY(final int x, final int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public int getX(final int length) {
            return this.x;
        }

        @Override
        public int getY(final int length) {
            return this.y;
        }

        @Override
        public boolean equals(final @Nullable Object object) {
            if (this == object) {
                return true;
            }

            if (object == null || getClass() != object.getClass()) {
                return false;
            }

            final XY xy = (XY) object;

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
    final class Indexed implements Slot {

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
        private Indexed(final int index) {
            this.index = index;
        }

        /**
         * {@inheritDoc}
         * <p>
         * If {@code length} is zero, this will throw an {@link IllegalArgumentException}.
         *
         * @param length {@inheritDoc}
         * @return {@inheritDoc}
         * @throws IllegalArgumentException when {@code length} is zero
         */
        @Override
        @Contract(pure = true)
        public int getX(final int length) {
            if (length == 0) {
                throw new IllegalArgumentException("Length may not be zero");
            }

            return this.index % length;
        }

        /**
         * {@inheritDoc}
         * <p>
         * If {@code length} is zero, this will throw an {@link IllegalArgumentException}.
         *
         * @param length {@inheritDoc}
         * @return {@inheritDoc}
         * @throws IllegalArgumentException when {@code length} is zero
         */
        @Override
        @Contract(pure = true)
        public int getY(final int length) {
            if (length == 0) {
                throw new IllegalArgumentException("Length may not be zero");
            }

            return this.index / length;
        }

        @Override
        public boolean equals(final @Nullable Object object) {
            if (this == object) {
                return true;
            }

            if (object == null || getClass() != object.getClass()) {
                return false;
            }

            final Indexed indexed = (Indexed) object;

            return index == indexed.index;
        }

        @Override
        public int hashCode() {
            return index;
        }

    }

}
