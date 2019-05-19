/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.in;

import com.google.gson.reflect.TypeToken;
import systems.reformcloud.ReformCloudAPIBungee;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.internal.events.PermissionHolderUpdateEvent;
import systems.reformcloud.launcher.BungeecordBootstrap;
import systems.reformcloud.network.interfaces.NetworkInboundHandler;
import systems.reformcloud.player.permissions.group.PermissionGroup;
import systems.reformcloud.player.permissions.player.PermissionHolder;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author _Klaro | Pasqual K. / created on 17.05.2019
 */

public final class PacketInUpdatePermissionGroup implements Serializable, NetworkInboundHandler {
    @Override
    public void handle(Configuration configuration) {
        PermissionGroup permissionGroup = configuration.getValue("group", new TypeToken<PermissionGroup>() {
        });
        List<PermissionHolder> permissionHolders = ReformCloudAPIBungee.getInstance()
                .getCachedPermissionHolders()
                .values()
                .stream()
                .filter(e -> BungeecordBootstrap.getInstance().getProxy().getPlayer(e.getUniqueID()) != null)
                .filter(e -> e.getPermissionGroups().containsKey(permissionGroup.getName()))
                .filter(e -> e.isPermissionGroupPresent(permissionGroup.getName()))
                .collect(Collectors.toList());
        if (permissionHolders.isEmpty())
            return;

        permissionHolders.forEach(permissionHolder -> {
            ReformCloudAPIBungee.getInstance().getCachedPermissionHolders().remove(permissionHolder.getUniqueID());
            ReformCloudAPIBungee.getInstance().getCachedPermissionHolders().put(permissionHolder.getUniqueID(), permissionHolder);
            BungeecordBootstrap.getInstance().getProxy().getPluginManager().callEvent(new PermissionHolderUpdateEvent(permissionHolder));
        });
    }
}
