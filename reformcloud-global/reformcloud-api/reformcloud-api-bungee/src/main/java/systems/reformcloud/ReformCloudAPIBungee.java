/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud;

import systems.reformcloud.launcher.BungeecordBootstrap;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.event.EventManager;
import systems.reformcloud.listener.CloudConnectListener;
import systems.reformcloud.logging.LoggerProvider;
import systems.reformcloud.meta.info.ProxyInfo;
import systems.reformcloud.meta.proxy.ProxyGroup;
import systems.reformcloud.meta.server.ServerGroup;
import systems.reformcloud.meta.startup.ProxyStartupInfo;
import systems.reformcloud.netty.NettyHandler;
import systems.reformcloud.netty.NettySocketClient;
import systems.reformcloud.netty.channel.ChannelHandler;
import systems.reformcloud.netty.in.*;
import systems.reformcloud.netty.packets.PacketOutStartGameServer;
import systems.reformcloud.netty.packets.PacketOutStartProxy;
import systems.reformcloud.utility.TypeTokenAdaptor;
import systems.reformcloud.utility.cloudsystem.InternalCloudNetwork;
import systems.reformcloud.utility.cloudsystem.EthernetAddress;
import lombok.Getter;
import lombok.Setter;

import javax.management.InstanceAlreadyExistsException;
import java.nio.file.Paths;

/**
 * @author _Klaro | Pasqual K. / created on 01.11.2018
 */

@Setter
@Getter
public class ReformCloudAPIBungee {
    @Getter
    public static ReformCloudAPIBungee instance;

    private final NettySocketClient nettySocketClient;
    private final NettyHandler nettyHandler = new NettyHandler();
    private final ChannelHandler channelHandler = new ChannelHandler();

    private final ProxyStartupInfo proxyStartupInfo;
    private ProxyInfo proxyInfo;
    private InternalCloudNetwork internalCloudNetwork = new InternalCloudNetwork();

    /**
     * Creates a new BungeeCord Plugin instance
     *
     * @throws Throwable
     */
    public ReformCloudAPIBungee() throws Throwable {
        if (instance == null)
            instance = this;
        else
            throw new InstanceAlreadyExistsException();

        ReformCloudLibraryService.sendHeader();

        Configuration configuration = Configuration.loadConfiguration(Paths.get("reformcloud/config.json"));

        final EthernetAddress ethernetAddress = configuration.getValue("address", TypeTokenAdaptor.getEthernetAddressType());
        new ReformCloudLibraryServiceProvider(new LoggerProvider(), configuration.getStringValue("controllerKey"), ethernetAddress.getHost(), new EventManager(), null);

        this.proxyStartupInfo = configuration.getValue("startupInfo", TypeTokenAdaptor.getProxyStartupInfoType());
        this.proxyInfo = configuration.getValue("info", TypeTokenAdaptor.getProxyInfoType());

        BungeecordBootstrap.getInstance().getProxy().getPluginManager().registerListener(BungeecordBootstrap.getInstance(), new CloudConnectListener());

        this.nettyHandler.registerHandler("InitializeCloudNetwork", new PacketInInitializeInternal());
        this.nettyHandler.registerHandler("ProcessAdd", new PacketInProcessAdd());
        this.nettyHandler.registerHandler("ProcessRemove", new PacketInProcessRemove());
        this.nettyHandler.registerHandler("UpdateAll", new PacketInUpdateAll());
        this.nettyHandler.registerHandler("ServerInfoUpdate", new PacketInServerInfoUpdate());

        this.nettySocketClient = new NettySocketClient();
        this.nettySocketClient.connect(
                ethernetAddress, nettyHandler, channelHandler, configuration.getBooleanValue("ssl"),
                configuration.getStringValue("controllerKey"), this.proxyStartupInfo.getName()
        );
    }

    public void startGameServer(final String serverGroupName) {
        this.startGameServer(serverGroupName, new Configuration());
    }

    public void startGameServer(final String serverGroupName, final Configuration preConfig) {
        final ServerGroup serverGroup = this.internalCloudNetwork.getServerGroups().getOrDefault(serverGroupName, null);
        this.startGameServer(serverGroup, preConfig);
    }

    public void startGameServer(final ServerGroup serverGroup) {
        this.startGameServer(serverGroup.getName(), new Configuration());
    }

    public void startGameServer(final ServerGroup serverGroup, final Configuration preConfig) {
        this.channelHandler.sendPacketAsynchronous("ReformCloudController", new PacketOutStartGameServer(serverGroup, preConfig));
    }

    public void startGameServer(final ServerGroup serverGroup, final Configuration preConfig, final String template) {
        this.channelHandler.sendPacketAsynchronous("ReformCloudController", new PacketOutStartGameServer(serverGroup, preConfig, template));
    }

    public void startProxy(final String proxyGroupName) {
        this.startProxy(proxyGroupName, new Configuration());
    }

    public void startProxy(final String proxyGroupName, final Configuration preConfig) {
        final ProxyGroup proxyGroup = this.internalCloudNetwork.getProxyGroups().getOrDefault(proxyGroupName, null);
        this.channelHandler.sendPacketAsynchronous("ReformCloudController", new PacketOutStartProxy(proxyGroup, preConfig));
    }

    public void startProxy(final ProxyGroup proxyGroup) {
        this.startProxy(proxyGroup.getName());
    }

    public void startProxy(final ProxyGroup proxyGroup, final Configuration preConfig) {
        this.startProxy(proxyGroup.getName(), preConfig);
    }

    public void startProxy(final ProxyGroup proxyGroup, final Configuration preConfig, final String template) {
        this.channelHandler.sendPacketAsynchronous("ReformCloudController", new PacketOutStartProxy(proxyGroup, preConfig, template));
    }

    /**
     * @deprecated
     */
    @Deprecated
    public void removeInternalProcess() {
        BungeecordBootstrap.getInstance().onDisable();
    }
}
