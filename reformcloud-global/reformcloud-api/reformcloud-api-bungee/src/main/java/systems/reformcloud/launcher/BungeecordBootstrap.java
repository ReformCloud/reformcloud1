/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.launcher;

import io.netty.util.ResourceLeakDetector;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.plugin.Plugin;
import systems.reformcloud.ReformCloudAPIBungee;
import systems.reformcloud.ReformCloudLibraryService;
import systems.reformcloud.commands.CommandHub;
import systems.reformcloud.commands.CommandJumpto;
import systems.reformcloud.commands.CommandReformCloud;
import systems.reformcloud.commands.CommandWhereIAm;
import systems.reformcloud.listener.CloudAddonsListener;
import systems.reformcloud.listener.CloudConnectListener;
import systems.reformcloud.listener.CloudProcessListener;
import systems.reformcloud.listener.CloudProxyPingListener;
import systems.reformcloud.network.authentication.enums.AuthenticationType;
import systems.reformcloud.network.packets.PacketOutInternalProcessRemove;

import java.io.Serializable;
import java.util.Arrays;

/**
 * @author _Klaro | Pasqual K. / created on 01.11.2018
 */

public final class BungeecordBootstrap extends Plugin implements Serializable {
    public static BungeecordBootstrap instance;

    public static BungeecordBootstrap getInstance() {
        return BungeecordBootstrap.instance;
    }

    @Override
    public void onLoad() {
        instance = this;

        ResourceLeakDetector.setLevel(ResourceLeakDetector.Level.DISABLED);
    }

    @Override
    public void onEnable() {
        this.getProxy().getConfig().getListeners().forEach(listenerInfo -> listenerInfo.getServerPriority().clear());

        Arrays.asList(
                new CloudProxyPingListener(),
                new CloudProcessListener(),
                new CloudAddonsListener(),
                new CloudConnectListener()
        ).forEach(listener -> this.getProxy().getPluginManager().registerListener(this, listener));

        Arrays.asList(
                new CommandJumpto(),
                new CommandHub(),
                new CommandReformCloud(),
                new CommandWhereIAm()
        ).forEach(command -> this.getProxy().getPluginManager().registerCommand(this, command));

        /*
         * Clears the default config servers
         */
        BungeeCord.getInstance().getConfig().getServers().clear();

        try {
            new ReformCloudAPIBungee();
        } catch (final Throwable throwable) {
            throwable.printStackTrace();
            this.onDisable();
        }
    }

    @Override
    public void onDisable() {
        ReformCloudAPIBungee.getInstance().getChannelHandler().sendPacketSynchronized("ReformCloudController", new PacketOutInternalProcessRemove(ReformCloudAPIBungee.getInstance().getProxyStartupInfo().getUid(), AuthenticationType.PROXY));
        ReformCloudLibraryService.sleep(1000000000);
    }
}
