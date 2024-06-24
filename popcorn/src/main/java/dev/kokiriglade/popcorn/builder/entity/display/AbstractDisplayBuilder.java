package dev.kokiriglade.popcorn.builder.entity.display;

import dev.kokiriglade.popcorn.builder.entity.AbstractEntityBuilder;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.entity.Display;
import org.bukkit.util.Transformation;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.joml.Matrix4f;

/**
 * Modifies {@link Display} entities
 *
 * @param <B> the builder type
 * @param <T> the entity type
 * @since 2.2.2
 */
@SuppressWarnings({"unchecked", "unused"})
public class AbstractDisplayBuilder<B extends AbstractEntityBuilder<B, T>, T extends Display> extends AbstractEntityBuilder<B, T> {

    protected Display.@Nullable Billboard billboard;
    protected Display.@Nullable Brightness brightness;
    protected @Nullable Float displayHeight;
    protected @Nullable Float displayWidth;
    protected @Nullable Color glowColorOverride;
    protected @Nullable Integer interpolationDelay;
    protected @Nullable Integer interpolationDuration;
    protected @Nullable Float shadowRadius;
    protected @Nullable Float shadowStrength;
    protected @Nullable Integer teleportDuration;
    protected @Nullable Transformation transformation;
    protected @Nullable Matrix4f transformationMatrix;
    protected @Nullable Float viewRange;

    protected AbstractDisplayBuilder(@NonNull Class<T> entityClass, @NonNull Location location) {
        super(entityClass, location);
        this.consumers.add(display -> {
            if (billboard != null) {
                display.setBillboard(billboard);
            }
            if (brightness != null) {
                display.setBrightness(brightness);
            }
            if (displayHeight != null) {
                display.setDisplayHeight(displayHeight);
            }
            if (displayWidth != null) {
                display.setDisplayWidth(displayWidth);
            }
            display.setGlowColorOverride(glowColorOverride);
            if (interpolationDelay != null) {
                display.setInterpolationDelay(interpolationDelay);
            }
            if (interpolationDuration != null) {
                display.setInterpolationDuration(interpolationDuration);
            }
            if (shadowRadius != null) {
                display.setShadowRadius(shadowRadius);
            }
            if (shadowStrength != null) {
                display.setShadowStrength(shadowStrength);
            }
            if (teleportDuration != null) {
                display.setTeleportDuration(teleportDuration);
            }
            if (transformation != null) {
                display.setTransformation(transformation);
            }
            if (transformationMatrix != null) {
                display.setTransformationMatrix(transformationMatrix);
            }
            if (viewRange != null) {
                display.setViewRange(viewRange);
            }
        });
    }

    /**
     * Gets the billboard setting of the display entity.
     *
     * @return the billboard setting
     * @since 2.2.2
     */
    public Display.@Nullable Billboard billboard() {
        return billboard;
    }

    /**
     * Sets the billboard setting of the display entity.
     *
     * @param billboard the billboard setting to set
     * @return the builder
     * @since 2.2.2
     */
    public @NonNull B billboard(final Display.@Nullable Billboard billboard) {
        this.billboard = billboard;
        return (B) this;
    }

    /**
     * Gets the brightness setting of the display entity.
     *
     * @return the brightness setting
     * @since 2.2.2
     */
    public Display.@Nullable Brightness brightness() {
        return brightness;
    }

    /**
     * Sets the brightness setting of the display entity.
     *
     * @param brightness the brightness setting to set
     * @return the builder
     * @since 2.2.2
     */
    public @NonNull B brightness(final Display.@Nullable Brightness brightness) {
        this.brightness = brightness;
        return (B) this;
    }

    /**
     * Gets the display height of the display entity.
     *
     * @return the display height
     * @since 2.2.2
     */
    public @Nullable Float displayHeight() {
        return displayHeight;
    }

    /**
     * Sets the display height of the display entity.
     *
     * @param displayHeight the display height to set
     * @return the builder
     * @since 2.2.2
     */
    public @NonNull B displayHeight(final @Nullable Float displayHeight) {
        this.displayHeight = displayHeight;
        return (B) this;
    }

    /**
     * Gets the display width of the display entity.
     *
     * @return the display width
     * @since 2.2.2
     */
    public @Nullable Float displayWidth() {
        return displayWidth;
    }

    /**
     * Sets the display width of the display entity.
     *
     * @param displayWidth the display width to set
     * @return the builder
     * @since 2.2.2
     */
    public @NonNull B displayWidth(final @Nullable Float displayWidth) {
        this.displayWidth = displayWidth;
        return (B) this;
    }

    /**
     * Gets the glow color override of the display entity.
     *
     * @return the glow color override
     * @since 2.2.2
     */
    public @Nullable Color glowColorOverride() {
        return glowColorOverride;
    }

    /**
     * Sets the glow color override of the display entity.
     *
     * @param glowColorOverride the glow color override to set
     * @return the builder
     * @since 2.2.2
     */
    public @NonNull B glowColorOverride(final @Nullable Color glowColorOverride) {
        this.glowColorOverride = glowColorOverride;
        return (B) this;
    }

    /**
     * Gets the interpolation delay of the display entity.
     *
     * @return the interpolation delay
     * @since 2.2.2
     */
    public @Nullable Integer interpolationDelay() {
        return interpolationDelay;
    }

    /**
     * Sets the interpolation delay of the display entity.
     *
     * @param interpolationDelay the interpolation delay to set
     * @return the builder
     * @since 2.2.2
     */
    public @NonNull B interpolationDelay(final @Nullable Integer interpolationDelay) {
        this.interpolationDelay = interpolationDelay;
        return (B) this;
    }

    /**
     * Gets the interpolation duration of the display entity.
     *
     * @return the interpolation duration
     * @since 2.2.2
     */
    public @Nullable Integer interpolationDuration() {
        return interpolationDuration;
    }

    /**
     * Sets the interpolation duration of the display entity.
     *
     * @param interpolationDuration the interpolation duration to set
     * @return the builder
     * @since 2.2.2
     */
    public @NonNull B interpolationDuration(final @Nullable Integer interpolationDuration) {
        this.interpolationDuration = interpolationDuration;
        return (B) this;
    }

    /**
     * Gets the shadow radius of the display entity.
     *
     * @return the shadow radius
     * @since 2.2.2
     */
    public @Nullable Float shadowRadius() {
        return shadowRadius;
    }

    /**
     * Sets the shadow radius of the display entity.
     *
     * @param shadowRadius the shadow radius to set
     * @return the builder
     * @since 2.2.2
     */
    public @NonNull B shadowRadius(final @Nullable Float shadowRadius) {
        this.shadowRadius = shadowRadius;
        return (B) this;
    }

    /**
     * Gets the shadow strength of the display entity.
     *
     * @return the shadow strength
     * @since 2.2.2
     */
    public @Nullable Float shadowStrength() {
        return shadowStrength;
    }

    /**
     * Sets the shadow strength of the display entity.
     *
     * @param shadowStrength the shadow strength to set
     * @return the builder
     * @since 2.2.2
     */
    public @NonNull B shadowStrength(final @Nullable Float shadowStrength) {
        this.shadowStrength = shadowStrength;
        return (B) this;
    }

    /**
     * Gets the teleport duration of the display entity.
     *
     * @return the teleport duration
     * @since 2.2.2
     */
    public @Nullable Integer teleportDuration() {
        return teleportDuration;
    }

    /**
     * Sets the teleport duration of the display entity.
     *
     * @param teleportDuration the teleport duration to set
     * @return the builder
     * @since 2.2.2
     */
    public @NonNull B teleportDuration(final @Nullable Integer teleportDuration) {
        this.teleportDuration = teleportDuration;
        return (B) this;
    }

    /**
     * Gets the transformation of the display entity.
     *
     * @return the transformation
     * @since 2.2.2
     */
    public @Nullable Transformation transformation() {
        return transformation;
    }

    /**
     * Sets the transformation of the display entity.
     *
     * @param transformation the transformation to set
     * @return the builder
     * @since 2.2.2
     */
    public @NonNull B transformation(final @Nullable Transformation transformation) {
        this.transformation = transformation;
        return (B) this;
    }

    /**
     * Gets the transformation matrix of the display entity.
     *
     * @return the transformation matrix
     * @since 2.2.2
     */
    public @Nullable Matrix4f transformationMatrix() {
        return transformationMatrix;
    }

    /**
     * Sets the transformation matrix of the display entity.
     *
     * @param transformationMatrix the transformation matrix to set
     * @return the builder
     * @since 2.2.2
     */
    public @NonNull B transformationMatrix(final @Nullable Matrix4f transformationMatrix) {
        this.transformationMatrix = transformationMatrix;
        return (B) this;
    }

    /**
     * Gets the view range of the display entity.
     *
     * @return the view range
     * @since 2.2.2
     */
    public @Nullable Float viewRange() {
        return viewRange;
    }

    /**
     * Sets the view range of the display entity.
     *
     * @param viewRange the view range to set
     * @return the builder
     * @since 2.2.2
     */
    public @NonNull B viewRange(final @Nullable Float viewRange) {
        this.viewRange = viewRange;
        return (B) this;
    }

}
