/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud;

import systems.reformcloud.launcher.SpigotBootstrap;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.event.EventManager;
import systems.reformcloud.logging.LoggerProvider;
import systems.reformcloud.meta.info.ServerInfo;
import systems.reformcloud.meta.proxy.ProxyGroup;
import systems.reformcloud.meta.server.ServerGroup;
import systems.reformcloud.meta.startup.ServerStartupInfo;
import systems.reformcloud.netty.NettyHandler;
import systems.reformcloud.netty.NettySocketClient;
import systems.reformcloud.netty.channel.ChannelHandler;
import systems.reformcloud.netty.in.*;
import systems.reformcloud.netty.packets.PacketOutServerInfoUpdate;
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
 * @author _Klaro | Pasqual K. / created on 09.12.2018
 */

@Getter
@Setter
public class ReformCloudAPISpigot {
    @Getter
    public static ReformCloudAPISpigot instance;

    private final NettySocketClient nettySocketClient;
    private final NettyHandler nettyHandler = new NettyHandler();
    private final ChannelHandler channelHandler = new ChannelHandler();

    private final ServerStartupInfo serverStartupInfo;
    private ServerInfo serverInfo;
    private InternalCloudNetwork internalCloudNetwork = new InternalCloudNetwork();

    /**
     * Creates a new ReformCloud Spigot instance
     *
     * @throws Throwable
     */
    public ReformCloudAPISpigot() throws Throwable {
        if (instance == null)
            instance = this;
        else
            throw new InstanceAlreadyExistsException();
        ReformCloudLibraryService.sendHeader();

        Configuration configuration = Configuration.loadConfiguration(Paths.get("reformcloud/config.json"));

        final EthernetAddress ethernetAddress = configuration.getValue("address", TypeTokenAdaptor.getEthernetAddressType());
        new ReformCloudLibraryServiceProvider(new LoggerProvider(), configuration.getStringValue("controllerKey"), ethernetAddress.getHost(), new EventManager(), null);

        this.serverStartupInfo = configuration.getValue("startupInfo", TypeTokenAdaptor.getServerStartupInfoType());
        this.serverInfo = configuration.getValue("info", TypeTokenAdaptor.getServerInfoType());

        this.nettyHandler.registerHandler("InitializeCloudNetwork", new PacketInInitializeInternal());
        this.nettyHandler.registerHandler("UpdateAll", new PacketInUpdateAll());
        this.nettyHandler.registerHandler("ProcessAdd", new PacketInProcessAdd());
        this.nettyHandler.registerHandler("ProcessRemove", new PacketInProcessRemove());
        this.nettyHandler.registerHandler("ServerInfoUpdate", new PacketInServerInfoUpdate());
        this.nettyHandler.registerHandler("Signs", new PacketInRequestSigns());
        this.nettyHandler.registerHandler("RemoveSign", new PacketInRemoveSign());
        this.nettyHandler.registerHandler("CreateSign", new PacketInCreateSign());
        this.nettyHandler.registerHandler("PlayerAccepted", new PacketInPlayerAccepted());

        this.nettySocketClient = new NettySocketClient();
        this.nettySocketClient.connect(
                ethernetAddress, nettyHandler, channelHandler, configuration.getBooleanValue("ssl"),
                configuration.getStringValue("controllerKey"), this.serverStartupInfo.getName()
        );
    }

    public void setCloudServerMOTDandSendUpdatePacket(final String newMOTD) {
        this.serverInfo.setMotd(newMOTD);
        this.channelHandler.sendPacketSynchronized("ReformCloudController", new PacketOutServerInfoUpdate(serverInfo));
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
        SpigotBootstrap.getInstance().onDisable();
    }
}
