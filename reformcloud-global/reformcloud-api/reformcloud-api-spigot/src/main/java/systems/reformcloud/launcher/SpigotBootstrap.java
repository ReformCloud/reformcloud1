/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.launcher;

import io.netty.util.ResourceLeakDetector;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.plugin.java.JavaPlugin;
import systems.reformcloud.ReformCloudAPISpigot;
import systems.reformcloud.ReformCloudLibraryService;
import systems.reformcloud.libloader.LibraryLoader;
import systems.reformcloud.listener.PlayerConnectListener;
import systems.reformcloud.network.authentication.enums.AuthenticationType;
import systems.reformcloud.network.packets.PacketOutInternalProcessRemove;
import systems.reformcloud.permissions.ReflectionUtil;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author _Klaro | Pasqual K. / created on 09.12.2018
 */

@Getter
public class SpigotBootstrap extends JavaPlugin {
    @Getter
    public static SpigotBootstrap instance;

    private List<UUID> acceptedPlayers = new ArrayList<>();

    @Deprecated
    private long start;

    @Override
    public void onLoad() {
        this.start = System.currentTimeMillis();
        new LibraryLoader().loadJarFileAndInjectLibraries();
        instance = this;

        ResourceLeakDetector.setLevel(ResourceLeakDetector.Level.DISABLED);
    }

    @Override
    public void onEnable() {
        this.getServer().getPluginManager().registerEvents(new PlayerConnectListener(), this);

        try {
            new ReformCloudAPISpigot();
        } catch (final Throwable throwable) {
            throwable.printStackTrace();
            this.onDisable();
        }
    }

    @Override
    public void onDisable() {
        ReformCloudAPISpigot.getInstance().getTempServerStats().addOnlineTime(this.start);
        ReformCloudAPISpigot.getInstance().updateTempStats();
        ReformCloudLibraryService.sleep(1000);
        ReformCloudAPISpigot.getInstance().getChannelHandler().sendPacketSynchronized("ReformCloudController", new PacketOutInternalProcessRemove(ReformCloudAPISpigot.getInstance().getServerStartupInfo().getUid(), AuthenticationType.SERVER));
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
}
