/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.permissions.permissible;

import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissibleBase;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachmentInfo;
import systems.reformcloud.ReformCloudAPISpigot;
import systems.reformcloud.player.permissions.group.PermissionGroup;
import systems.reformcloud.player.permissions.player.PermissionHolder;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author _Klaro | Pasqual K. / created on 10.03.2019
 */

public final class Permissible extends PermissibleBase implements Serializable {
    private PermissionHolder permissionHolder;
    private List<PermissionGroup> inGroups;

    public Permissible(Player player, PermissionHolder permissionHolder) {
        super(player);

        this.permissionHolder = permissionHolder;
        inGroups = ReformCloudAPISpigot.getInstance()
                .getPermissionCache().getAllRegisteredGroups().stream().filter(e -> permissionHolder
                        .getPermissionGroups().containsKey(e.getName()))
                .collect(Collectors.toList());
        if (permissionHolder.getPermissionGroups().containsKey(ReformCloudAPISpigot.getInstance()
                .getPermissionCache().getDefaultGroup().getName())) {
            inGroups.add(ReformCloudAPISpigot.getInstance().getPermissionCache().getDefaultGroup());
        }
    }

    @Override
    public boolean isOp() {
        return super.isOp();
    }

    @Override
    public void setOp(boolean value) {
        super.setOp(value);
    }

    @Override
    public boolean hasPermission(String inName) {
        if (this.isOp() && !inName.toLowerCase().equals("reformcloud.commands.mobs") && !inName.toLowerCase().equals("reformcloud.command.selectors"))
            return true;

        return this.permissionHolder.hasPermission(inName, inGroups);
    }

    @Override
    public boolean isPermissionSet(String name) {
        return this.hasPermission(name);
    }

    @Override
    public boolean isPermissionSet(Permission perm) {
        return this.hasPermission(perm);
    }

    @Override
    public boolean hasPermission(Permission perm) {
        return this.hasPermission(perm.getName());
    }

    @Override
    public Set<PermissionAttachmentInfo> getEffectivePermissions() {
        Map<String, Boolean> permissions = new HashMap<>(this.permissionHolder.getPlayerPermissions());
        this.inGroups.forEach(permissionGroup -> permissions.putAll(permissionGroup.getPermissions()));

        Set<PermissionAttachmentInfo> permissionAttachmentInfos = new HashSet<>();
        for (Map.Entry<String, Boolean> perms : permissions.entrySet())
            permissionAttachmentInfos.add(new PermissionAttachmentInfo(this, perms.getKey(), null, perms.getValue()));

        return permissionAttachmentInfos;
    }

    private void checkAvailable() {
        if (permissionHolder == null)
            throw new IllegalStateException("PermissionHolder cannot be null");
    }

    public PermissionHolder getPermissionHolder() {
        return permissionHolder;
    }

    public List<PermissionGroup> getInGroups() {
        return inGroups;
    }
}
