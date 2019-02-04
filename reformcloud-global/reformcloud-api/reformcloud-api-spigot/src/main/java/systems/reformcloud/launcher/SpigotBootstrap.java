/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.launcher;

import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;
import systems.reformcloud.ReformCloudAPISpigot;
import systems.reformcloud.libloader.LibraryLoader;
import systems.reformcloud.listener.PlayerConnectListener;
import systems.reformcloud.netty.authentication.enums.AuthenticationType;
import systems.reformcloud.netty.packets.PacketOutInternalProcessRemove;

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

    @Override
    public void onLoad() {
        new LibraryLoader().loadJarFileAndInjectLibraries();
        instance = this;
    }

    @Override
    public void onEnable() {
        this.getServer().getPluginManager().registerEvents(new PlayerConnectListener(), this);

        this.registerShutdownHook();
        try {
            new ReformCloudAPISpigot();
        } catch (final Throwable throwable) {
            throwable.printStackTrace();
            this.onDisable();
        }
    }

    @Override
    public void onDisable() {
    }

    private void registerShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> ReformCloudAPISpigot.getInstance().getChannelHandler()
                .sendPacketSynchronized("ReformCloudController", new PacketOutInternalProcessRemove(
                        ReformCloudAPISpigot.getInstance().getServerStartupInfo().getUid(), AuthenticationType.SERVER))));
        System.out.println("ServerShutdownHook was added.");
    }
}
