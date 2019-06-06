/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.permissions.permissible;

import org.bukkit.entity.Player;
import org.bukkit.permissions.*;
import org.bukkit.plugin.Plugin;
import systems.reformcloud.ReformCloudAPISpigot;
import systems.reformcloud.player.permissions.group.PermissionGroup;
import systems.reformcloud.player.permissions.player.PermissionHolder;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author _Klaro | Pasqual K. / created on 10.03.2019
 */

public final class Permissible extends PermissibleBase implements Serializable {

    private PermissionHolder permissionHolder;

    private List<PermissionGroup> permissionGroups;

    private Set<PermissionAttachmentInfo> permissionAttachmentInfos;

    public Permissible(Player player, PermissionHolder permissionHolder) {
        super(player);
        player.setOp(false);

        this.permissionHolder = permissionHolder;
    }

    public Permissible(ServerOperator serverOperator, PermissionHolder permissionHolder) {
        this((Player) serverOperator, permissionHolder);
    }

    @Override
    public boolean isOp() {
        return false;
    }

    @Override
    public void setOp(boolean value) {
    }

    @Override
    public boolean hasPermission(String inName) {
        checkAvailable();
        return this.permissionHolder.hasPermission(inName, this.getPermissionGroups());
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
        return this.getPermissionAttachmentInfos();
    }

    @Override
    public void recalculatePermissions() {
        if (this.permissionHolder == null) {
            return;
        }

        this.recalculatePermissions0();
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin, String name, boolean value) {
        return new PermissionAttachment(plugin, this);
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin) {
        return new PermissionAttachment(plugin, this);
    }

    @Override
    public void removeAttachment(PermissionAttachment attachment) {
    }

    @Override
    public synchronized void clearPermissions() {
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin, String name, boolean value,
        int ticks) {
        return new PermissionAttachment(plugin, this);
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin, int ticks) {
        return new PermissionAttachment(plugin, this);
    }

    private void recalculatePermissions0() {
        if (this.permissionGroups != null && !this.permissionGroups.isEmpty()) {
            this.permissionGroups.clear();
        }

        this.recalculatePermissions1();
    }

    private void recalculatePermissions1() {
        this.permissionGroups = permissionHolder
            .getAllPermissionGroups(ReformCloudAPISpigot.getInstance().getPermissionCache());
    }

    private void getPermissionAttachmentInfos0() {
        if (this.permissionAttachmentInfos != null && !this.permissionAttachmentInfos.isEmpty()) {
            this.permissionAttachmentInfos.clear();
        }

        this.getPermissionAttachmentInfos1();
    }

    private void getPermissionAttachmentInfos1() {
        Set<PermissionAttachmentInfo> infos = new HashSet<>();
        final Map<String, Boolean> playerDirectPermissions = this.getPermissionHolder()
            .getPlayerPermissions();
        playerDirectPermissions.forEach((k, v) -> {
            PermissionAttachmentInfo permissionAttachmentInfo = new PermissionAttachmentInfo(this,
                k, null, v);
            infos.add(permissionAttachmentInfo);
        });

        this.getPermissionGroups()
            .stream()
            .filter(e -> permissionHolder.isPermissionGroupPresent(e.getName()))
            .forEach(group ->
                group.getPermissions().forEach((k, v) -> {
                    PermissionAttachmentInfo permissionAttachmentInfo = new PermissionAttachmentInfo(
                        this, k, null, v);
                    infos.add(permissionAttachmentInfo);
                })
            );

        this.permissionAttachmentInfos = infos;
    }

    private void checkAvailable() {
        if (!isAvailable()) {
            throw new IllegalStateException("PermissionHolder cannot be null");
        }
    }

    private boolean isAvailable() {
        return permissionHolder != null;
    }

    private PermissionHolder getPermissionHolder() {
        checkAvailable();
        return permissionHolder;
    }

    private Set<PermissionAttachmentInfo> getPermissionAttachmentInfos() {
        if (this.permissionAttachmentInfos == null) {
            this.getPermissionAttachmentInfos0();
        }

        return permissionAttachmentInfos;
    }

    private List<PermissionGroup> getPermissionGroups() {
        if (this.permissionGroups == null) {
            this.recalculatePermissions();
        }

        return permissionGroups;
    }
}
