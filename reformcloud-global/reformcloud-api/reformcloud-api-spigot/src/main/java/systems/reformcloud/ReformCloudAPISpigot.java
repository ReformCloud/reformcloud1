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
import systems.reformcloud.api.IAPIService;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.event.EventManager;
import systems.reformcloud.exceptions.InstanceAlreadyExistsException;
import systems.reformcloud.launcher.SpigotBootstrap;
import systems.reformcloud.logging.LoggerProvider;
import systems.reformcloud.meta.client.Client;
import systems.reformcloud.meta.info.ClientInfo;
import systems.reformcloud.meta.info.ProxyInfo;
import systems.reformcloud.meta.info.ServerInfo;
import systems.reformcloud.meta.proxy.ProxyGroup;
import systems.reformcloud.meta.server.ServerGroup;
import systems.reformcloud.meta.server.stats.TempServerStats;
import systems.reformcloud.meta.startup.ServerStartupInfo;
import systems.reformcloud.network.NettyHandler;
import systems.reformcloud.network.NettySocketClient;
import systems.reformcloud.network.channel.ChannelHandler;
import systems.reformcloud.network.in.*;
import systems.reformcloud.network.packet.Packet;
import systems.reformcloud.network.packets.PacketOutServerInfoUpdate;
import systems.reformcloud.network.packets.PacketOutStartGameServer;
import systems.reformcloud.network.packets.PacketOutStartProxy;
import systems.reformcloud.network.packets.PacketOutUpdateServerTempStats;
import systems.reformcloud.utility.TypeTokenAdaptor;
import systems.reformcloud.utility.cloudsystem.EthernetAddress;
import systems.reformcloud.utility.cloudsystem.InternalCloudNetwork;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author _Klaro | Pasqual K. / created on 09.12.2018
 */

@Data
public class ReformCloudAPISpigot implements Listener, IAPIService {
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

        final EthernetAddress ethernetAddress = configuration.getValue("address", TypeTokenAdaptor.getETHERNET_ADDRESS_TYPE());
        new ReformCloudLibraryServiceProvider(new LoggerProvider(), configuration.getStringValue("controllerKey"), ethernetAddress.getHost(), new EventManager(), null);

        this.serverStartupInfo = configuration.getValue("startupInfo", TypeTokenAdaptor.getSERVER_STARTUP_INFO_TYPE());
        this.serverInfo = configuration.getValue("info", TypeTokenAdaptor.getSERVER_INFO_TYPE());

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

        IAPIService.instance.set(this);
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

    @Override
    public void startGameServer(final String serverGroupName) {
        this.startGameServer(serverGroupName, new Configuration());
    }

    @Override
    public void startGameServer(final String serverGroupName, final Configuration preConfig) {
        final ServerGroup serverGroup = this.internalCloudNetwork.getServerGroups().getOrDefault(serverGroupName, null);
        this.startGameServer(serverGroup, preConfig);
    }

    @Override
    public void startGameServer(final ServerGroup serverGroup) {
        this.startGameServer(serverGroup.getName(), new Configuration());
    }

    @Override
    public void startGameServer(final ServerGroup serverGroup, final Configuration preConfig) {
        this.channelHandler.sendPacketAsynchronous("ReformCloudController", new PacketOutStartGameServer(serverGroup, preConfig));
    }

    @Override
    public void startGameServer(final ServerGroup serverGroup, final Configuration preConfig, final String template) {
        this.channelHandler.sendPacketAsynchronous("ReformCloudController", new PacketOutStartGameServer(serverGroup, preConfig, template));
    }

    @Override
    public void startProxy(final String proxyGroupName) {
        this.startProxy(proxyGroupName, new Configuration());
    }

    @Override
    public void startProxy(final String proxyGroupName, final Configuration preConfig) {
        final ProxyGroup proxyGroup = this.internalCloudNetwork.getProxyGroups().getOrDefault(proxyGroupName, null);
        this.channelHandler.sendPacketAsynchronous("ReformCloudController", new PacketOutStartProxy(proxyGroup, preConfig));
    }

    @Override
    public void startProxy(final ProxyGroup proxyGroup) {
        this.startProxy(proxyGroup.getName());
    }

    @Override
    public void startProxy(final ProxyGroup proxyGroup, final Configuration preConfig) {
        this.startProxy(proxyGroup.getName(), preConfig);
    }

    @Override
    public void startProxy(final ProxyGroup proxyGroup, final Configuration preConfig, final String template) {
        this.channelHandler.sendPacketAsynchronous("ReformCloudController", new PacketOutStartProxy(proxyGroup, preConfig, template));
    }

    @Override
    public int getMaxPlayers() {
        return this.serverInfo.getServerGroup().getMaxPlayers();
    }

    @Override
    public int getOnlineCount() {
        return this.serverInfo.getOnline();
    }

    @Override
    public List<Client> getAllClients() {
        return new ArrayList<>(this.internalCloudNetwork.getClients().values());
    }

    @Override
    public List<Client> getAllConnectedClients() {
        return this.internalCloudNetwork
                .getClients()
                .values()
                .stream()
                .filter(e -> e.getClientInfo() != null)
                .collect(Collectors.toList());
    }

    @Override
    public List<ServerGroup> getAllServerGroups() {
        return new ArrayList<>(this.internalCloudNetwork.getServerGroups().values());
    }

    @Override
    public List<ProxyGroup> getAllProxyGroups() {
        return new ArrayList<>(this.internalCloudNetwork.getProxyGroups().values());
    }

    @Override
    public List<ServerInfo> getAllRegisteredServers() {
        return this.internalCloudNetwork.getServerProcessManager().getAllRegisteredServerProcesses();
    }

    @Override
    public List<ProxyInfo> getAllRegisteredProxies() {
        return this.internalCloudNetwork.getServerProcessManager().getAllRegisteredProxyProcesses();
    }

    @Override
    public List<ServerInfo> getAllRegisteredServers(String groupName) {
        return null;
    }

    @Override
    public List<ProxyInfo> getAllRegisteredProxies(String groupName) {
        return this.internalCloudNetwork
                .getServerProcessManager()
                .getAllRegisteredProxyProcesses()
                .stream()
                .filter(e -> e.getProxyGroup().getName().equals(groupName))
                .collect(Collectors.toList());
    }

    @Override
    public boolean sendPacket(String subChannel, Packet packet) {
        return this.channelHandler.sendPacketAsynchronous(subChannel, packet);
    }

    @Override
    public boolean sendPacketSync(String subChannel, Packet packet) {
        return this.channelHandler.sendPacketSynchronized(subChannel, packet);
    }

    @Override
    public void sendPacketToAll(Packet packet) {
        this.channelHandler.sendToAllAsynchronous(packet);
    }

    @Override
    public void sendPacketToAllSync(Packet packet) {
        this.channelHandler.sendToAllSynchronized(packet);
    }

    @Override
    public Client getClient(String name) {
        return this.internalCloudNetwork.getClients().getOrDefault(name, null);
    }

    @Override
    public ClientInfo getConnectedClient(String name) {
        Client client = this.internalCloudNetwork
                .getClients()
                .values()
                .stream()
                .filter(e -> e.getName().equals(name) && e.getClientInfo() != null)
                .findFirst()
                .orElse(null);

        if (client == null)
            return null;

        return client.getClientInfo();
    }

    @Override
    public ServerGroup getServerGroup(String name) {
        return this.internalCloudNetwork.getServerGroups().getOrDefault(name, null);
    }

    @Override
    public ProxyGroup getProxyGroup(String name) {
        return this.internalCloudNetwork.getProxyGroups().getOrDefault(name, null);
    }

    /**
     * @deprecated
     */
    @Deprecated
    @Override
    public void removeInternalProcess() {
        SpigotBootstrap.getInstance().onDisable();
    }

    @Override
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
