/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.in;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import systems.reformcloud.ReformCloudAPIBungee;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.internal.events.PermissionHolderUpdateEvent;
import systems.reformcloud.launcher.BungeecordBootstrap;
import systems.reformcloud.network.interfaces.NetworkInboundHandler;
import systems.reformcloud.player.permissions.player.PermissionHolder;
import systems.reformcloud.utility.TypeTokenAdaptor;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 05.05.2019
 */

public final class PacketInUpdatePermissionHolder implements Serializable, NetworkInboundHandler {
    @Override
    public void handle(Configuration configuration) {
        PermissionHolder permissionHolder = configuration.getValue("holder", TypeTokenAdaptor.getPERMISSION_HOLDER_TYPE());
        ProxiedPlayer proxiedPlayer = BungeecordBootstrap.getInstance().getProxy().getPlayer(permissionHolder.getUniqueID());
        if (proxiedPlayer == null)
            return;

        ReformCloudAPIBungee.getInstance().getCachedPermissionHolders().put(permissionHolder.getUniqueID(), permissionHolder);
        BungeecordBootstrap.getInstance().getProxy().getPluginManager().callEvent(new PermissionHolderUpdateEvent(permissionHolder));
    }
}
