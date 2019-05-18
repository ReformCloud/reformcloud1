/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.launcher;

import io.netty.util.ResourceLeakDetector;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.plugin.java.JavaPlugin;
import systems.reformcloud.ReformCloudAPISpigot;
import systems.reformcloud.ReformCloudLibraryService;
import systems.reformcloud.listener.CloudAddonsListener;
import systems.reformcloud.listener.PlayerConnectListener;
import systems.reformcloud.network.authentication.enums.AuthenticationType;
import systems.reformcloud.network.packets.PacketOutInternalProcessRemove;
import systems.reformcloud.permissions.ReflectionUtil;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;

/**
 * @author _Klaro | Pasqual K. / created on 09.12.2018
 */

public final class SpigotBootstrap extends JavaPlugin implements Serializable {
    public static SpigotBootstrap instance;

    @Deprecated
    private long start;

    public static SpigotBootstrap getInstance() {
        return SpigotBootstrap.instance;
    }

    @Override
    public void onLoad() {
        this.start = System.currentTimeMillis();
        instance = this;

        ResourceLeakDetector.setLevel(ResourceLeakDetector.Level.DISABLED);
    }

    @Override
    public void onEnable() {
        this.getServer().getPluginManager().registerEvents(new PlayerConnectListener(), this);
        this.getServer().getPluginManager().registerEvents(new CloudAddonsListener(), this);

        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");

        try {
            new ReformCloudAPISpigot();
        } catch (final Throwable throwable) {
            throwable.printStackTrace();
            this.onDisable();
        }
    }

    @Override
    public void onDisable() {
        Bukkit.getScheduler().runTask(this, () -> {
            ReformCloudAPISpigot.getInstance().getTempServerStats().addOnlineTime(this.start);
            ReformCloudAPISpigot.getInstance().updateTempStats();
            ReformCloudLibraryService.sleep(1000);
            ReformCloudAPISpigot.getInstance().getChannelHandler().sendPacketSynchronized("ReformCloudController", new PacketOutInternalProcessRemove(ReformCloudAPISpigot.getInstance().getServerStartupInfo().getUid(), AuthenticationType.SERVER));
            this.getServer().getOnlinePlayers().forEach(player -> player.kickPlayer(this.getServer().getShutdownMessage()));
            ReformCloudLibraryService.sleep(1000);
        });
        ReformCloudLibraryService.sleep(1000000000);
    }

    public CommandMap getCommandMap() {
        CommandMap commandMap;

        try {
            Class<?> clazz = ReflectionUtil.reflectClazz(".CraftServer");

            if (clazz != null)
                commandMap = (CommandMap) clazz.getMethod("getCommandMap").invoke(Bukkit.getServer());
            else
                commandMap = (CommandMap) Class.forName("net.glowstone.GlowServer").getMethod("getCommandMap").invoke(Bukkit.getServer());
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException | ClassNotFoundException e) {
            e.printStackTrace();
            commandMap = null;
        }

        return commandMap;
    }

    @Deprecated
    public long getStart() {
        return this.start;
    }
}
