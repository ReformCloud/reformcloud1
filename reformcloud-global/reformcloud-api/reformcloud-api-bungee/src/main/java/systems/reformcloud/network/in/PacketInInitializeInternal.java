/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.in;

import com.google.gson.reflect.TypeToken;
import net.md_5.bungee.api.ProxyServer;
import systems.reformcloud.ReformCloudAPIBungee;
import systems.reformcloud.autoicon.IconManager;
import systems.reformcloud.commands.CommandPermissions;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.internal.events.CloudNetworkInitializeEvent;
import systems.reformcloud.launcher.BungeecordBootstrap;
import systems.reformcloud.meta.enums.ServerModeType;
import systems.reformcloud.meta.proxy.ProxyGroup;
import systems.reformcloud.meta.proxy.settings.ProxySettings;
import systems.reformcloud.network.interfaces.NetworkInboundHandler;
import systems.reformcloud.network.packet.Packet;
import systems.reformcloud.network.packets.PacketOutGetProxySettings;
import systems.reformcloud.network.query.out.PacketOutQueryGetPermissionCache;
import systems.reformcloud.utility.TypeTokenAdaptor;

import java.io.Serializable;
import java.net.InetSocketAddress;
import java.util.Optional;

/**
 * @author _Klaro | Pasqual K. / created on 02.11.2018
 */

public final class PacketInInitializeInternal implements NetworkInboundHandler, Serializable {
    @Override
    public void handle(Configuration configuration) {
        ReformCloudAPIBungee.getInstance().setInternalCloudNetwork(configuration.getValue("networkProperties", TypeTokenAdaptor.getINTERNAL_CLOUD_NETWORK_TYPE()));

        BungeecordBootstrap.getInstance().getProxy().getPluginManager().callEvent(new CloudNetworkInitializeEvent(
                ReformCloudAPIBungee.getInstance().getInternalCloudNetwork())
        );

        final ProxyGroup proxyGroup = ReformCloudAPIBungee.getInstance().getInternalCloudNetwork().getProxyGroups().get(ReformCloudAPIBungee.getInstance().getProxyInfo().getProxyGroup().getName());
        if (proxyGroup == null)
            return;

        new IconManager();

        ReformCloudAPIBungee.getInstance().getChannelHandler().sendPacketAsynchronous("ReformCloudController", new Packet(
                "AuthSuccess", new Configuration().addStringValue("name", ReformCloudAPIBungee.getInstance().getProxyInfo().getCloudProcess().getName())
        ));
        ReformCloudAPIBungee.getInstance().getInternalCloudNetwork().getServerProcessManager().getAllRegisteredServerProcesses().forEach(
                process -> {
                    if (proxyGroup.getDisabledServerGroups().contains(process.getServerGroup().getName()))
                        return;

                    ProxyServer.getInstance().getServers().put(
                            process.getCloudProcess().getName(),
                            ProxyServer.getInstance().constructServerInfo(
                                    process.getCloudProcess().getName(),
                                    new InetSocketAddress(process.getHost(), process.getPort()),
                                    "ReformCloudServer",
                                    false
                            ));

                    if (process.getServerGroup().getServerModeType().equals(ServerModeType.LOBBY)) {
                        ProxyServer.getInstance().getConfig().getListeners().forEach(listener ->
                                listener.getServerPriority().add(process.getCloudProcess().getName())
                        );
                    }
                }
        );

        ReformCloudAPIBungee.getInstance().sendPacketQuery("ReformCloudController",
                new PacketOutQueryGetPermissionCache(), (configuration1, resultID) -> {
                    ReformCloudAPIBungee.getInstance().setPermissionCache(configuration1.getValue("cache",
                            TypeTokenAdaptor.getPERMISSION_CACHE_TYPE())
                    );
                    if (ReformCloudAPIBungee.getInstance().getPermissionCache() != null) {
                        BungeecordBootstrap.getInstance().getProxy().getPluginManager().registerCommand(
                                BungeecordBootstrap.getInstance(), new CommandPermissions()
                        );
                    }
                }
        );

        ReformCloudAPIBungee.getInstance().sendPacketQuery("ReformCloudController",
                new PacketOutGetProxySettings(), (configuration1, resultID) -> {
                    Optional<ProxySettings> proxySettings = configuration1.getValue("settings", new TypeToken<Optional<ProxySettings>>() {
                    }.getType());
                    ReformCloudAPIBungee.getInstance().setProxySettings(proxySettings.orElse(null));
                }
        );
    }
}
