package dev.kokiriglade.popcorn.persistence;

import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataType;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;
import java.util.UUID;

/**
 * A {@link PersistentDataType} implementation that adds support for {@link UUID}s.
 *
 * @since 0.6.0
 */
public final class UUIDTagType implements PersistentDataType<byte[], UUID> {

    /**
     * The one and only instance of this class.
     * Since this class stores no state information (apart from this field),
     * the usage of a single instance is safe even across multiple threads.
     */
    public static final UUIDTagType INSTANCE = new UUIDTagType();

    /**
     * A private constructor so that only a single instance of this class can exist.
     */
    private UUIDTagType() {
    }


    @Override
    public @NonNull Class<byte[]> getPrimitiveType() {
        return byte[].class;
    }

    @Override
    public @NonNull Class<UUID> getComplexType() {
        return UUID.class;
    }

    @Override
    public byte @NonNull [] toPrimitive(final @NonNull UUID complex, final @NonNull PersistentDataAdapterContext context) {
        final ByteBuffer buffer = ByteBuffer.wrap(new byte[16]);
        buffer.putLong(complex.getMostSignificantBits());
        buffer.putLong(complex.getLeastSignificantBits());
        return buffer.array();
    }

    @Override
    public @NonNull UUID fromPrimitive(final @NonNull byte[] primitive, final @NonNull PersistentDataAdapterContext context) {
        final ByteBuffer buffer = ByteBuffer.wrap(primitive);
        final  long most = buffer.getLong();
        final long least = buffer.getLong();
        return new UUID(most, least);
    }

}
