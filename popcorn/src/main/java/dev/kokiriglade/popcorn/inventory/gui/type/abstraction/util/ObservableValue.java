package dev.kokiriglade.popcorn.inventory.gui.type.abstraction.util;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.jetbrains.annotations.Contract;

import java.util.Collection;
import java.util.HashSet;
import java.util.function.Consumer;

/**
 * A value whose modifications can be observed.
 *
 * @param <T> the type of value to store
 * @since 3.0.0
 */
public class ObservableValue<T> {

    /**
     * A collection of subscribers that should be notified on updates.
     */
    private final @NonNull Collection<Consumer<? super T>> subscribers = new HashSet<>();

    /**
     * The current value
     */
    private @Nullable T value;

    /**
     * Creates a new observable value with the given default value.
     *
     * @param defaultValue the default value
     * @since 3.0.0
     */
    public ObservableValue(final @Nullable T defaultValue) {
        this.value = defaultValue;
    }

    /**
     * Updates the old value to the given new value. This will notify all the subscribers before updating the new value.
     * Subscribers may observe the old value by using {@link #get()}. This will always notify the subscribers, even if
     * the new value is the same as the old value.
     *
     * @param newValue the new value
     * @since 3.0.0
     */
    public void set(final T newValue) {
        for (final Consumer<? super T> subscriber : this.subscribers) {
            subscriber.accept(newValue);
        }

        this.value = newValue;
    }

    /**
     * Subscribes to modifications of this value. The provided consumer will be called every time this value changes.
     *
     * @param consumer the consumer to call upon updates of this value
     * @since 3.0.0
     */
    public void subscribe(final @NonNull Consumer<? super T> consumer) {
        this.subscribers.add(consumer);
    }

    /**
     * Gets the current value of this item. If this is called from within a subscriber, then this is the value from
     * before the current in-progress update.
     *
     * @return the current value
     * @since 3.0.0
     */
    @Contract(pure = true)
    public @Nullable T get() {
        return this.value;
    }

}
