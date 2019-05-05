/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.in;

import com.velocitypowered.api.proxy.Player;
import systems.reformcloud.ReformCloudAPIVelocity;
import systems.reformcloud.bootstrap.VelocityBootstrap;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.network.interfaces.NetworkInboundHandler;
import systems.reformcloud.permissions.VelocityPermissionFunctionAdapter;
import systems.reformcloud.player.permissions.player.PermissionHolder;
import systems.reformcloud.utility.TypeTokenAdaptor;

import java.io.Serializable;
import java.lang.reflect.Field;

/**
 * @author _Klaro | Pasqual K. / created on 05.05.2019
 */

public final class PacketInUpdatePermissionHolder implements Serializable, NetworkInboundHandler {
    @Override
    public void handle(Configuration configuration) {
        PermissionHolder permissionHolder = configuration.getValue("holder", TypeTokenAdaptor.getPERMISSION_HOLDER_TYPE());
        Player player = VelocityBootstrap.getInstance().getProxy().getPlayer(permissionHolder.getUniqueID()).orElse(null);
        if (player == null)
            return;

        ReformCloudAPIVelocity.getInstance().getCachedPermissionHolders().put(permissionHolder.getUniqueID(), permissionHolder);
        try {
            Field field = player.getClass().getDeclaredField("permissionFunction");
            field.setAccessible(true);
            field.set(player, new VelocityPermissionFunctionAdapter(player.getUniqueId()));
        } catch (final NoSuchFieldException | IllegalAccessException ignored) {
        }
    }
}
