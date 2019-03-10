/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.query.in;

import systems.reformcloud.PermissionsAddon;
import systems.reformcloud.ReformCloudController;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.network.interfaces.NetworkQueryInboundHandler;
import systems.reformcloud.network.query.out.PacketOutQueryGetPermissionHolder;
import systems.reformcloud.player.permissions.player.PermissionHolder;
import systems.reformcloud.utility.TypeTokenAdaptor;

import java.io.Serializable;
import java.util.UUID;

/**
 * @author _Klaro | Pasqual K. / created on 10.03.2019
 */

public final class PacketQueryInGetPermissionHolder implements Serializable, NetworkQueryInboundHandler {
    @Override
    public void handle(Configuration configuration, UUID resultID) {
        PermissionHolder permissionHolder = configuration.getValue("holder", TypeTokenAdaptor.getPERMISSION_HOLDER_TYPE());
        ReformCloudController.getInstance().getChannelHandler().sendPacketSynchronized(configuration.getStringValue("from"),
                new PacketOutQueryGetPermissionHolder(
                        PermissionsAddon.getInstance().getPermissionDatabase().getPermissionHolder(permissionHolder), resultID
                )
        );
    }
}
