package fr.zcraft.zsorting;

import org.bukkit.permissions.Permissible;

/**
 * This enumeration list all the permission for the plugin.
 * @author Lucas
 */
public enum Permissions
{
    /**
     * ADMIN permission : Allow the user to use all plugin's commands.
     */
    ADMIN("zsorting.admin");

    private String permission;

    Permissions(String permission)
    {
        this.permission = permission;
    }

    /**
     * Returns the associated permission.
     * @return The permission in String format.
     */
    public String getPermission()
    {
        return permission;
    }

    /**
     * Checks whether a permissible (generaly a player) has the permission.
     * @param permissible - The permissible to check.
     * @return {@code true} if the permissible has the permission, {@code false} if not.
     */
    public boolean grantedTo(Permissible permissible)
    {
        return permissible.hasPermission(permission);
    }
}