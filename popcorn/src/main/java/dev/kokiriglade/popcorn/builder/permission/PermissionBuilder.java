package dev.kokiriglade.popcorn.builder.permission;

import org.bukkit.permissions.Permissible;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Map;
import java.util.Set;

/**
 * Modifies {@link Permission}s
 *
 * @since 2.2.0
 */
@SuppressWarnings("unused")
public class PermissionBuilder {

    /**
     * The {@code Permission} to modify during building.
     */
    private final Permission permission;

    /**
     * Create a new PermissionBuilder
     *
     * @since 2.2.0
     */
    private PermissionBuilder(String name) {
        this.permission = new Permission(name);
    }

    /**
     * Creates a {@code PermissionBuilder}.
     *
     * @param name the permission's name
     * @return instance of {@code PermissionBuilder}
     * @since 2.2.0
     */
    public static PermissionBuilder create(final @NonNull String name) {
        return new PermissionBuilder(name);
    }

    /**
     * Gets the name of the permission.
     *
     * @return the name
     * @since 2.2.0
     */
    public @NonNull String getName() {
        return permission.getName();
    }

    /**
     * Gets the description of the permission.
     *
     * @return the description
     * @since 2.2.0
     */
    public @NonNull String description() {
        return permission.getDescription();
    }

    /**
     * Sets the description for the recipe.
     *
     * @param description the description
     * @return the builder
     * @since 2.2.0
     */
    public @NonNull PermissionBuilder description(final @Nullable String description) {
        this.permission.setDescription(description);
        return this;
    }

    /**
     * Gets the children of the permission.
     *
     * @return the children
     * @since 2.2.0
     */
    public @NonNull Map<String, Boolean> children() {
        return this.permission.getChildren();
    }

    /**
     * Removes all children from the permission.
     *
     * @return the builder
     * @since 2.2.0
     */
    public @NonNull PermissionBuilder removeChildren() {
        this.permission.getChildren().clear();
        this.permission.recalculatePermissibles();
        return this;
    }

    /**
     * Add a child to the permission.
     *
     * @param string the child
     * @param value  the value
     * @return the builder
     * @since 2.2.0
     */
    public @NonNull PermissionBuilder addChild(final @NonNull String string, final boolean value) {
        this.permission.getChildren().put(string, value);
        this.permission.recalculatePermissibles();
        return this;
    }

    /**
     * Removes a child to the permission.
     *
     * @param string the child
     * @return the builder
     * @since 2.2.0
     */
    public @NonNull PermissionBuilder removeChild(@NonNull String string) {
        this.permission.getChildren().remove(string);
        this.permission.recalculatePermissibles();
        return this;
    }

    /**
     * Get the default value of the permission.
     *
     * @return the builder
     * @since 2.2.0
     */
    public @NonNull PermissionDefault permissionDefault() {
        return this.permission.getDefault();
    }

    /**
     * Set the default value of the permission.
     *
     * @param defaultValue the default permission value
     * @return the builder
     * @since 2.2.0
     */
    public @NonNull PermissionBuilder permissionDefault(@NonNull PermissionDefault defaultValue) {
        this.permission.setDefault(defaultValue);
        return this;
    }

    /**
     * Get a set containing every {@link Permissible} that has the permission.
     *
     * @return the permissibles
     * @since 2.2.0
     */
    public @NonNull Set<Permissible> permissibles() {
        return this.permission.getPermissibles();
    }

    /**
     * Add the permission to the specified parent permission..
     *
     * @param string the parent
     * @param value  the value
     * @return the builder
     * @since 3.0.1
     */
    public @NonNull PermissionBuilder addParent(final @NonNull String string, final boolean value) {
        this.permission.addParent(string, value);
        this.permission.recalculatePermissibles();
        return this;
    }

    /**
     * Add the permission to the specified parent permission..
     *
     * @param permission the parent
     * @param value  the value
     * @return the builder
     * @since 3.0.1
     */
    public @NonNull PermissionBuilder addParent(final @NonNull Permission permission, final boolean value) {
        this.permission.addParent(permission, value);
        this.permission.recalculatePermissibles();
        return this;
    }

    /**
     * Builds the {@code Permission} from the set properties.
     *
     * @return the built {@code Permission}
     * @since 2.2.0
     */
    public @NonNull Permission build() {
        return permission;
    }

}
