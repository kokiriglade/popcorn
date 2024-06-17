package dev.kokiriglade.popcorn.entity.mob.creature.animal;

import org.bukkit.Location;
import org.bukkit.entity.Armadillo;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Modifies {@link Armadillo}s
 *
 * @since 2.2.2
 */
@SuppressWarnings("unused")
public final class ArmadilloBuilder extends AbstractAnimalBuilder<ArmadilloBuilder, Armadillo> {

    private ArmadilloBuilder(final @NonNull Location location) {
        super(Armadillo.class, location);
    }

    /**
     * Creates an {@code ArmadilloBuilder}.
     *
     * @param location the {@code Location} to spawn the Armadillo at
     * @return instance of {@code ArmadilloBuilder}
     * @since 2.2.2
     */
    public static @NonNull ArmadilloBuilder create(final @NonNull Location location) {
        return new ArmadilloBuilder(location);
    }

}
