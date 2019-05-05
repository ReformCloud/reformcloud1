/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.in;

import systems.reformcloud.PermissionsAddon;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.network.interfaces.NetworkInboundHandler;
import systems.reformcloud.player.permissions.player.PermissionHolder;
import systems.reformcloud.utility.TypeTokenAdaptor;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 10.03.2019
 */

public final class PacketInUpdatePermissionHolder implements Serializable, NetworkInboundHandler {
    @Override
    public void handle(Configuration configuration) {
        PermissionHolder permissionHolder = configuration.getValue("holder", TypeTokenAdaptor.getPERMISSION_HOLDER_TYPE());
        if (permissionHolder == null)
            return;

        PermissionsAddon.getInstance().getPermissionDatabase().updatePermissionHolder(permissionHolder);
    }
}
