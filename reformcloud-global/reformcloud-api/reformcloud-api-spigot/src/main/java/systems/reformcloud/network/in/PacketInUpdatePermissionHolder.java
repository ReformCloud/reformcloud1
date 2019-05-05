/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.in;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import systems.reformcloud.ReformCloudAPISpigot;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.internal.events.PermissionHolderUpdateEvent;
import systems.reformcloud.launcher.SpigotBootstrap;
import systems.reformcloud.network.interfaces.NetworkInboundHandler;
import systems.reformcloud.permissions.ReflectionUtil;
import systems.reformcloud.permissions.permissible.Permissible;
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
        Player player = Bukkit.getPlayer(permissionHolder.getUniqueID());
        if (player == null)
            return;

        ReformCloudAPISpigot.getInstance().getCachedPermissionHolders().put(permissionHolder.getUniqueID(), permissionHolder);
        SpigotBootstrap.getInstance().getServer().getPluginManager().callEvent(new PermissionHolderUpdateEvent(permissionHolder));
        Field field;
        try {
            Class<?> clazz = ReflectionUtil.reflectClazz(".entity.CraftHumanEntity");

            if (clazz != null)
                field = clazz.getDeclaredField("perm");
            else
                field = Class.forName("net.glowstone.entity.GlowHumanEntity").getDeclaredField("permissions");

            field.setAccessible(true);
            field.set(player, new Permissible(player, permissionHolder));
        } catch (final NoSuchFieldException | IllegalAccessException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }
    }
}
