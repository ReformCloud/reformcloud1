/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.event.EventManager;
import systems.reformcloud.launcher.SpigotBootstrap;
import systems.reformcloud.logging.LoggerProvider;
import systems.reformcloud.meta.info.ServerInfo;
import systems.reformcloud.meta.proxy.ProxyGroup;
import systems.reformcloud.meta.server.ServerGroup;
import systems.reformcloud.meta.server.stats.TempServerStats;
import systems.reformcloud.meta.startup.ServerStartupInfo;
import systems.reformcloud.network.NettyHandler;
import systems.reformcloud.network.NettySocketClient;
import systems.reformcloud.network.channel.ChannelHandler;
import systems.reformcloud.network.in.*;
import systems.reformcloud.network.packets.PacketOutServerInfoUpdate;
import systems.reformcloud.network.packets.PacketOutStartGameServer;
import systems.reformcloud.network.packets.PacketOutStartProxy;
import systems.reformcloud.network.packets.PacketOutUpdateServerTempStats;
import systems.reformcloud.utility.TypeTokenAdaptor;
import systems.reformcloud.utility.cloudsystem.EthernetAddress;
import systems.reformcloud.utility.cloudsystem.InternalCloudNetwork;

import javax.management.InstanceAlreadyExistsException;
import java.nio.file.Paths;

/**
 * @author _Klaro | Pasqual K. / created on 09.12.2018
 */

@Data
public class ReformCloudAPISpigot implements Listener {
    @Getter
    @Setter
    @Deprecated
    public static ReformCloudAPISpigot instance;

    private final NettySocketClient nettySocketClient;
    private final ChannelHandler channelHandler = new ChannelHandler();

    private final ServerStartupInfo serverStartupInfo;
    private ServerInfo serverInfo;
    private InternalCloudNetwork internalCloudNetwork = new InternalCloudNetwork();

    private final TempServerStats tempServerStats = new TempServerStats();

    @Setter
    private long internalTime = System.currentTimeMillis();

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

        SpigotBootstrap.getInstance().getServer().getPluginManager().registerEvents(this, SpigotBootstrap.getInstance());

        Configuration configuration = Configuration.loadConfiguration(Paths.get("reformcloud/config.json"));

        final EthernetAddress ethernetAddress = configuration.getValue("address", TypeTokenAdaptor.getEthernetAddressType());
        new ReformCloudLibraryServiceProvider(new LoggerProvider(), configuration.getStringValue("controllerKey"), ethernetAddress.getHost(), new EventManager(), null);

        this.serverStartupInfo = configuration.getValue("startupInfo", TypeTokenAdaptor.getServerStartupInfoType());
        this.serverInfo = configuration.getValue("info", TypeTokenAdaptor.getServerInfoType());

        this.getNettyHandler().registerHandler("InitializeCloudNetwork", new PacketInInitializeInternal())
                .registerHandler("UpdateAll", new PacketInUpdateAll())
                .registerHandler("ProcessAdd", new PacketInProcessAdd())
                .registerHandler("ProcessRemove", new PacketInProcessRemove())
                .registerHandler("ServerInfoUpdate", new PacketInServerInfoUpdate())
                .registerHandler("Signs", new PacketInRequestSigns())
                .registerHandler("RemoveSign", new PacketInRemoveSign())
                .registerHandler("CreateSign", new PacketInCreateSign())
                .registerHandler("SyncControllerTime", new PacketInSyncControllerTime())
                .registerHandler("PlayerAccepted", new PacketInPlayerAccepted());

        this.nettySocketClient = new NettySocketClient();
        this.nettySocketClient.connect(
                ethernetAddress, channelHandler, configuration.getBooleanValue("ssl"),
                configuration.getStringValue("controllerKey"), this.serverStartupInfo.getName()
        );
    }

    public void updateTempStats() {
        if (this.tempServerStats.hasChanges()) {
            this.channelHandler.sendPacketAsynchronous("ReformCloudController", new PacketOutUpdateServerTempStats(this.tempServerStats));
            this.tempServerStats.reset();
        }
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

    public NettyHandler getNettyHandler() {
        return ReformCloudLibraryServiceProvider.getInstance().getNettyHandler();
    }

    @EventHandler
    public void handle(final PlayerMoveEvent event) {
        if (!event.isCancelled())
            this.tempServerStats.addWalkedDistance(event.getFrom().distance(event.getTo()));
    }

    @EventHandler
    public void handle(final BlockPlaceEvent event) {
        if (!event.isCancelled())
            this.tempServerStats.addPlacedBlock();
    }
}
