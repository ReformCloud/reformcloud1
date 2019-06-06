/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.permissions;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.command.ConsoleCommandSender;
import systems.reformcloud.ReformCloudAPIBungee;
import systems.reformcloud.player.permissions.group.PermissionGroup;
import systems.reformcloud.player.permissions.player.PermissionHolder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * @author _Klaro | Pasqual K. / created on 06.05.2019
 */

public final class PermissionUtil implements Serializable {

    public static boolean hasPermission(CommandSender commandSender, String permission) {
        if (permission == null) {
            return false;
        }

        if (commandSender instanceof ProxiedPlayer) {
            return hasPermission0((ProxiedPlayer) commandSender, permission);
        }

        return commandSender instanceof ConsoleCommandSender;
    }

    private static boolean hasPermission0(ProxiedPlayer proxiedPlayer, String permission) {
        if (proxiedPlayer == null) {
            return false;
        }

        return hasPermission1(proxiedPlayer.getUniqueId(), permission);
    }

    private static boolean hasPermission1(UUID uniqueID, String permission) {
        if (uniqueID == null) {
            return false;
        }

        PermissionHolder permissionHolder = ReformCloudAPIBungee.getInstance()
            .getCachedPermissionHolders().get(uniqueID);
        return hasPermission2(permissionHolder, permission);
    }

    private static boolean hasPermission2(PermissionHolder permissionHolder, String permission) {
        if (permissionHolder == null) {
            return false;
        }

        List<PermissionGroup> permissionGroups = new ArrayList<>();
        permissionHolder.getPermissionGroups().keySet()
            .stream()
            .filter(permissionHolder::isPermissionGroupPresent)
            .map(g -> ReformCloudAPIBungee.getInstance().getPermissionCache().getGroup(g))
            .filter(Objects::nonNull)
            .forEach(permissionGroups::add);
        return hasPermission3(permissionHolder, permissionGroups, permission);
    }

    private static boolean hasPermission3(PermissionHolder permissionHolder,
        List<PermissionGroup> permissionGroups, String permission) {
        if (permissionGroups == null || permissionGroups.isEmpty()) {
            return false;
        }

        return permissionHolder.hasPermission(permission, permissionGroups);
    }
}
