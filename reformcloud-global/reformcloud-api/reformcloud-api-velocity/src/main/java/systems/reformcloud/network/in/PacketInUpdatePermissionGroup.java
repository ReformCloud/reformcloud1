/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.in;

import com.google.gson.reflect.TypeToken;
import com.velocitypowered.api.proxy.Player;
import systems.reformcloud.ReformCloudAPIVelocity;
import systems.reformcloud.bootstrap.VelocityBootstrap;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.events.PermissionHolderUpdateEvent;
import systems.reformcloud.network.interfaces.NetworkInboundHandler;
import systems.reformcloud.permissions.VelocityPermissionFunctionAdapter;
import systems.reformcloud.player.permissions.group.PermissionGroup;
import systems.reformcloud.player.permissions.player.PermissionHolder;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author _Klaro | Pasqual K. / created on 17.05.2019
 */

public final class PacketInUpdatePermissionGroup implements Serializable, NetworkInboundHandler {

    @Override
    public void handle(Configuration configuration) {
        PermissionGroup permissionGroup = configuration
            .getValue("group", new TypeToken<PermissionGroup>() {
            });
        List<PermissionHolder> permissionHolders = ReformCloudAPIVelocity.getInstance()
            .getCachedPermissionHolders()
            .values()
            .stream()
            .filter(e ->
                VelocityBootstrap.getInstance().getProxy().getPlayer(e.getUniqueID()).orElse(null)
                    != null)
            .filter(e -> e.getPermissionGroups().containsKey(permissionGroup.getName()))
            .filter(e -> e.isPermissionGroupPresent(permissionGroup.getName()))
            .collect(Collectors.toList());
        if (permissionHolders.isEmpty()) {
            return;
        }

        permissionHolders.forEach(permissionHolder -> {
            Player player = VelocityBootstrap.getInstance().getProxy()
                .getPlayer(permissionHolder.getUniqueID()).get();
            VelocityBootstrap.getInstance().getProxy().getEventManager()
                .fire(new PermissionHolderUpdateEvent(permissionHolder));
            ReformCloudAPIVelocity.getInstance().getCachedPermissionHolders()
                .put(permissionHolder.getUniqueID(), permissionHolder);
            try {
                Field field = player.getClass().getDeclaredField("permissionFunction");
                field.setAccessible(true);
                field.set(player, new VelocityPermissionFunctionAdapter(player.getUniqueId()));
            } catch (final NoSuchFieldException | IllegalAccessException ignored) {
            }
        });
    }
}
