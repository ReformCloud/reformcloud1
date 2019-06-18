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

    public abstract NetworkGlobalCluster shiftClusterNetworkInformation();

    public abstract ChannelHandlerContext getChannel(String arg1);

    public abstract boolean isChannelRegistered(String arg1);

    public abstract boolean isChannelRegistered(ChannelHandlerContext arg1);

    public abstract Set<String> getChannels();

    public abstract List<ChannelHandlerContext> getChannelList();

    public abstract void registerChannel(String arg1, ChannelHandlerContext arg2);

    public abstract void unregisterChannel(String arg1);

    public abstract void clearChannels();

    public abstract String getChannelNameByValue(ChannelHandlerContext arg1);

    public abstract boolean sendPacketSynchronized(String arg1, Packet arg2);

    public abstract void sendDirectPacket(String arg1, Packet arg2);

    public abstract boolean sendPacketSynchronized(String arg1, Packet... arg2);

    public abstract boolean sendPacketAsynchronous(String arg1, Packet arg2);

    public abstract boolean sendPacketAsynchronous(String arg1, Packet... arg2);

    public abstract boolean sendPacketQuerySync(String arg1, String arg2, Packet arg3,
                                                NetworkQueryInboundHandler arg4,
                                                NetworkQueryInboundHandler arg5);

    public abstract boolean sendPacketQuerySync(String arg1, String arg2, Packet arg3,
                                                NetworkQueryInboundHandler arg4);

    public abstract PacketFuture sendPacketQuerySync(String arg1, String arg2,
                                                     Packet arg3);

    public abstract void sendToAllSynchronized(Packet arg1);

    public abstract void sendToAllAsynchronous(Packet arg1);

    public abstract void sendToAllDirect(Packet arg1);

    public abstract void sendToAllSynchronized(Packet... arg1);

    public abstract void sendToAllAsynchronous(Packet... arg1);

    public abstract void sendToAllLobbies(ServerProcessManager arg1,
                                          Packet... arg2);

    public abstract void sendToAllLobbiesDirect(ServerProcessManager arg1,
                                                Packet... arg2);

    public abstract void toQueryPacket(Packet arg1, UUID arg2, String arg3);

    public abstract Map<UUID, PacketFuture> getResults();

    public abstract ExecutorService getExecutorService();
}
