/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.in;

import com.google.gson.reflect.TypeToken;
import com.velocitypowered.api.proxy.server.ServerInfo;
import systems.reformcloud.ReformCloudAPIVelocity;
import systems.reformcloud.autoicon.IconManager;
import systems.reformcloud.bootstrap.VelocityBootstrap;
import systems.reformcloud.commands.CommandPermissions;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.events.CloudNetworkInitializeEvent;
import systems.reformcloud.meta.enums.ServerModeType;
import systems.reformcloud.meta.proxy.ProxyGroup;
import systems.reformcloud.meta.proxy.settings.ProxySettings;
import systems.reformcloud.network.interfaces.NetworkInboundHandler;
import systems.reformcloud.network.packet.DefaultPacket;
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
        ReformCloudAPIVelocity.getInstance()
            .setInternalCloudNetwork(configuration.getValue("networkProperties",
                TypeTokenAdaptor.getINTERNAL_CLOUD_NETWORK_TYPE()));

        VelocityBootstrap.getInstance().getProxyServer().getEventManager()
            .fire(new CloudNetworkInitializeEvent(
                ReformCloudAPIVelocity.getInstance().getInternalCloudNetwork())
            );

        final ProxyGroup proxyGroup = ReformCloudAPIVelocity.getInstance().getInternalCloudNetwork()
            .getProxyGroups()
            .get(ReformCloudAPIVelocity.getInstance().getProxyInfo().getProxyGroup().getName());
        if (proxyGroup == null) {
            return;
        }

        new IconManager();

        ReformCloudAPIVelocity.getInstance().getChannelHandler()
            .sendPacketAsynchronous("ReformCloudController", new DefaultPacket(
                "AuthSuccess", new Configuration().addStringValue("name",
                ReformCloudAPIVelocity.getInstance().getProxyInfo().getCloudProcess().getName())
            ));
        ReformCloudAPIVelocity.getInstance().getInternalCloudNetwork().getServerProcessManager()
            .getAllRegisteredServerProcesses().forEach(
            process -> {
                if (proxyGroup.getDisabledServerGroups()
                    .contains(process.getServerGroup().getName())) {
                    return;
                }

                VelocityBootstrap.getInstance().getProxyServer().registerServer(
                    new ServerInfo(
                        process.getCloudProcess().getName(),
                        new InetSocketAddress(process.getHost(), process.getPort())
                    ));

                if (process.getServerGroup().getServerModeType().equals(ServerModeType.LOBBY)) {
                    VelocityBootstrap.getInstance().getProxyServer().getConfiguration()
                        .getAttemptConnectionOrder().add(
                        process.getCloudProcess().getName()
                    );
                }
            }
        );

        ReformCloudAPIVelocity.getInstance().sendPacketQuery("ReformCloudController",
            new PacketOutQueryGetPermissionCache(), (configuration1, resultID) -> {
                ReformCloudAPIVelocity.getInstance()
                    .setPermissionCache(configuration1.getValue("cache",
                        TypeTokenAdaptor.getPERMISSION_CACHE_TYPE())
                    );
                if (ReformCloudAPIVelocity.getInstance().getPermissionCache() != null) {
                    VelocityBootstrap.getInstance().getProxy().getCommandManager()
                        .register(new CommandPermissions(), "perms", "permissions", "cloudperms",
                            "cp");
                }
            }
        );

        ReformCloudAPIVelocity.getInstance().sendPacketQuery("ReformCloudController",
            new PacketOutGetProxySettings(), (configuration1, resultID) -> {
                Optional<ProxySettings> proxySettings = configuration1
                    .getValue("settings", new TypeToken<Optional<ProxySettings>>() {
                    }.getType());
                ReformCloudAPIVelocity.getInstance().setProxySettings(proxySettings.orElse(null));
            }
        );

        ReformCloudAPIVelocity.getInstance().getProxyInfo().getProcessStartupInformation()
            .setStartupFinishedTime(System.currentTimeMillis());
        ReformCloudAPIVelocity.getInstance().updateProxyInfo();
    }
}
