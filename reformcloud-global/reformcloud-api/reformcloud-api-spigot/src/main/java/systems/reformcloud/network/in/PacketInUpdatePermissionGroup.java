/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.in;

import com.google.gson.reflect.TypeToken;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import systems.reformcloud.ReformCloudAPISpigot;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.internal.events.PermissionHolderUpdateEvent;
import systems.reformcloud.launcher.SpigotBootstrap;
import systems.reformcloud.network.interfaces.NetworkInboundHandler;
import systems.reformcloud.permissions.ReflectionUtil;
import systems.reformcloud.permissions.permissible.Permissible;
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
        List<PermissionHolder> permissionHolders = ReformCloudAPISpigot.getInstance()
            .getCachedPermissionHolders()
            .values()
            .stream()
            .filter(e -> Bukkit.getPlayer(e.getUniqueID()) != null)
            .filter(e -> e.getPermissionGroups().containsKey(permissionGroup.getName()))
            .filter(e -> e.isPermissionGroupPresent(permissionGroup.getName()))
            .collect(Collectors.toList());
        if (permissionHolders.isEmpty()) {
            return;
        }

        permissionHolders.forEach(permissionHolder -> {
            Player player = Bukkit.getPlayer(permissionHolder.getUniqueID());
            if (player == null) {
                return;
            }

            ReformCloudAPISpigot.getInstance().getCachedPermissionHolders()
                .put(permissionHolder.getUniqueID(), permissionHolder);
            SpigotBootstrap.getInstance().getServer().getPluginManager()
                .callEvent(new PermissionHolderUpdateEvent(permissionHolder));
            Field field;
            try {
                Class<?> clazz = ReflectionUtil.reflectClazz(".entity.CraftHumanEntity");

                if (clazz != null) {
                    field = clazz.getDeclaredField("perm");
                } else {
                    field = Class.forName("net.glowstone.entity.GlowHumanEntity")
                        .getDeclaredField("permissions");
                }

                field.setAccessible(true);
                field.set(player, new Permissible(player, permissionHolder));
            } catch (final NoSuchFieldException | IllegalAccessException | ClassNotFoundException ex) {
                ex.printStackTrace();
            }
        });
    }
}
