/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.launcher;

import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.ResourceLeakDetector;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandMap;
import org.bukkit.plugin.java.JavaPlugin;
import systems.reformcloud.ReformCloudAPISpigot;
import systems.reformcloud.ReformCloudLibraryService;
import systems.reformcloud.addons.dependency.DependencyLoader;
import systems.reformcloud.addons.dependency.util.DynamicDependency;
import systems.reformcloud.listener.CloudAddonsListener;
import systems.reformcloud.listener.PlayerConnectListener;
import systems.reformcloud.network.authentication.enums.AuthenticationType;
import systems.reformcloud.network.packet.DefaultPacket;
import systems.reformcloud.network.packets.PacketOutInternalProcessRemove;
import systems.reformcloud.permissions.ReflectionUtil;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;

/**
 * @author _Klaro | Pasqual K. / created on 09.12.2018
 */

public final class SpigotBootstrap extends JavaPlugin implements Serializable {

    private static SpigotBootstrap instance;

    @Deprecated
    private long start;

    public static SpigotBootstrap getInstance() {
        return SpigotBootstrap.instance;
    }

    @Override
    public void onLoad() {
        DependencyLoader.loadDependency(new DynamicDependency(null) {
            @Override
            public String getGroupID() {
                return "io.netty";
            }

            @Override
            public String getName() {
                return "netty-all";
            }

            @Override
            public String getVersion() {
                return "4.1.37.Final";
            }
        });
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

        this.getServer().getWorlds().forEach(world -> world.setAutoSave(false));
    }

    @Override
    public void onDisable() {
        this.getServer().getWorlds().forEach(World::save);
        getServer().getScheduler().cancelTasks(this);
        getServer().getMessenger().unregisterOutgoingPluginChannel(this);

        ReformCloudAPISpigot.getInstance().getTempServerStats().addOnlineTime(this.start);
        ReformCloudAPISpigot.getInstance().updateTempStats();

        sendPacketAndClose(new PacketOutInternalProcessRemove(
            ReformCloudAPISpigot.getInstance().getServerStartupInfo().getUid(),
            AuthenticationType.SERVER));

        this.getServer().getOnlinePlayers()
            .forEach(player -> player.kickPlayer(this.getServer().getShutdownMessage()));
        Bukkit.shutdown();
        ReformCloudLibraryService.sleep(1000000000);
    }

    private void sendPacketAndClose(DefaultPacket packet) {
        ChannelHandlerContext channelHandlerContext =
            ReformCloudAPISpigot.getInstance().getChannelHandler()
                .getChannel("ReformCloudController");
        if (channelHandlerContext == null) {
            return;
        }

        channelHandlerContext.channel().writeAndFlush(packet)
            .addListener(ChannelFutureListener.CLOSE).addListener((ChannelFutureListener) channelFuture ->
            ReformCloudAPISpigot.getInstance().getNettyHandler().clearHandlers()
        );
    }

    public CommandMap getCommandMap() {
        CommandMap commandMap;

        try {
            Class<?> clazz = ReflectionUtil.reflectClazz(".CraftServer");

            if (clazz != null) {
                commandMap = (CommandMap) clazz.getMethod("getCommandMap")
                    .invoke(Bukkit.getServer());
            } else {
                commandMap = (CommandMap) Class.forName("net.glowstone.GlowServer")
                    .getMethod("getCommandMap").invoke(Bukkit.getServer());
            }
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
