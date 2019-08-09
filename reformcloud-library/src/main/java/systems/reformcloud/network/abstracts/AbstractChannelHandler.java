/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.abstracts;

import io.netty.channel.ChannelHandlerContext;
import systems.reformcloud.meta.cluster.NetworkGlobalCluster;
import systems.reformcloud.network.interfaces.NetworkQueryInboundHandler;
import systems.reformcloud.network.packet.Packet;
import systems.reformcloud.network.packet.PacketFuture;
import systems.reformcloud.utility.cloudsystem.ServerProcessManager;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ExecutorService;

/**
 * @author _Klaro | Pasqual K. / created on 18.06.2019
 */

public abstract class AbstractChannelHandler implements Serializable {

    /**
     * Shifts all information about all connected channels to the current instance
     *
     * @return all information about all connected channels to the current instance
     */
    public abstract NetworkGlobalCluster shiftClusterNetworkInformation();

    /**
     * Get a specific channel handler context
     *
     * @param arg1 The name of the channel
     * @return The {@link ChannelHandlerContext} of the given channel name or
     * {@code null} if the channel is unknown
     */
    public abstract ChannelHandlerContext getChannel(String arg1);

    /**
     * Checks if a specific channel is registered
     *
     * @param arg1 The channel name
     * @return If the channel is registered
     */
    public abstract boolean isChannelRegistered(String arg1);

    /**
     * Checks if a specific channel is registered
     *
     * @param arg1 The {@link ChannelHandlerContext} of the channel
     * @return If the channel is registered
     */
    public abstract boolean isChannelRegistered(ChannelHandlerContext arg1);

    /**
     * Gets all names of the registered channels
     *
     * @return all names of the registered channels
     */
    public abstract Set<String> getChannels();

    /**
     * Get all {@link ChannelHandlerContext} of all connected channels
     *
     * @return all connected channels by their channel handler context
     */
    public abstract List<ChannelHandlerContext> getChannelList();

    /**
     * Registers a channel to the cloud network
     *
     * @param arg1 The name of the channel which should be registered
     * @param arg2 The channel handler context of the channel
     */
    public abstract void registerChannel(String arg1, ChannelHandlerContext arg2);

    /**
     * Unregisters a specific channel
     *
     * @param arg1 The name of the channel which should be unregistered
     */
    public abstract void unregisterChannel(String arg1);

    /**
     * Unregisters all channels
     */
    public abstract void clearChannels();

    /**
     * Get the channel name by the {@link ChannelHandlerContext}
     *
     * @param arg1 The {@link ChannelHandlerContext} of the channel
     * @return The name of the channel or {@code null} if the channel is unknown
     */
    public abstract String getChannelNameByValue(ChannelHandlerContext arg1);

    /**
     * Sends a packet into the given channel
     *
     * @param arg1 The channel name
     * @param arg2 The packet which should be sent
     * @return If the operation was successful
     */
    public abstract boolean sendPacketSynchronized(String arg1, Packet arg2);

    /**
     * Sends a packet directly to the receiver
     *
     * @param arg1 The channel name
     * @param arg2 The packet which should be sent
     */
    public abstract void sendDirectPacket(String arg1, Packet arg2);

    /**
     * Sends more than one packet sync to the receiver
     *
     * @param arg1 The channel name where the packet should be sent to
     * @param arg2 The packets which should be sent
     * @return If the operation was successful
     */
    public abstract boolean sendPacketSynchronized(String arg1, Packet... arg2);

    /**
     * Sends a packet async to the receiver
     *
     * @param arg1 The channel name
     * @param arg2 The packet which should be sent
     * @return If the operation was successful
     */
    public abstract boolean sendPacketAsynchronous(String arg1, Packet arg2);

    /**
     * Sends more than one packet async to the receiver
     *
     * @param arg1 The channel name where the packet should be sent to
     * @param arg2 The packets which should be sent
     * @return If the operation was successful
     */
    public abstract boolean sendPacketAsynchronous(String arg1, Packet... arg2);

    /**
     * Sends a packet query sync
     *
     * @param arg1 The name of the channel where the packet should go to
     * @param arg2 The name of the sender
     * @param arg3 The packet which should be sent
     * @param arg4 The handler if the query returned a result
     * @param arg5 The handler if the query is not completed after the sync time
     * @return If the channel is registered and the operation was successful
     */
    public abstract boolean sendPacketQuerySync(String arg1, String arg2, Packet arg3,
                                                NetworkQueryInboundHandler arg4,
                                                NetworkQueryInboundHandler arg5);

    /**
     * Sends a packet query sync
     *
     * @param arg1 The name of the channel where the packet should go to
     * @param arg2 The name of the sender
     * @param arg3 The packet which should be sent
     * @param arg4 The handler if the query returned a result
     * @return If the channel is registered and the operation was successful
     */
    public abstract boolean sendPacketQuerySync(String arg1, String arg2, Packet arg3,
                                                NetworkQueryInboundHandler arg4);

    /**
     * Sends a packet query sync
     *
     * @param arg1 The name of the channel where the packet should go to
     * @param arg2 The name of the sender
     * @param arg3 The packet which should be sent
     * @return The packet future which was created
     */
    public abstract PacketFuture sendPacketQuerySync(String arg1, String arg2,
                                                     Packet arg3);

    /**
     * Sends a packet to all network participants
     *
     * @param arg1 The packet which should be sent
     */
    public abstract void sendToAllSynchronized(Packet arg1);

    /**
     * Sends a packet to all network participants async
     *
     * @param arg1 The packet which should be sent
     */
    public abstract void sendToAllAsynchronous(Packet arg1);

    /**
     * Sends a packet to all network participants directly
     *
     * @param arg1 The packet which should be sent
     */
    public abstract void sendToAllDirect(Packet arg1);

    /**
     * Sends more than one packet to all network participants
     *
     * @param arg1 The packets which should be sent
     */
    public abstract void sendToAllSynchronized(Packet... arg1);

    /**
     * Sends more than one packet to all network participants async
     *
     * @param arg1 The packets which should be sent
     */
    public abstract void sendToAllAsynchronous(Packet... arg1);

    /**
     * Sends a packet to all registered lobby servers
     *
     * @param arg1 The {@link ServerProcessManager} which has all information
     *             about the registered servers
     * @param arg2 The packet which should be sent
     */
    public abstract void sendToAllLobbies(ServerProcessManager arg1,
                                          Packet... arg2);

    /**
     * Sends a packet to all registered lobby servers directly
     *
     * @param arg1 The {@link ServerProcessManager} which has all information
     *             about the registered servers
     * @param arg2 The packet which should be sent
     */
    public abstract void sendToAllLobbiesDirect(ServerProcessManager arg1,
                                                Packet... arg2);

    /**
     * Converts a specific packet to a query packet
     *
     * @param arg1 The packet which should be converted
     * @param arg2 The result uid which should be set
     * @param arg3 The sender of the packet which should be added
     */
    public abstract void toQueryPacket(Packet arg1, UUID arg2, String arg3);

    /**
     * Get all waiting queries
     *
     * @return All waiting queries
     */
    public abstract Map<UUID, PacketFuture> getResults();

    /**
     * Get the executor service
     *
     * @return The executor service
     */
    public abstract ExecutorService getExecutorService();
}
