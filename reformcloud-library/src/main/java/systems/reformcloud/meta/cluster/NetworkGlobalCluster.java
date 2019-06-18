/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.meta.cluster;

import systems.reformcloud.meta.cluster.channel.ClusterChannelInformation;
import systems.reformcloud.network.abstracts.AbstractChannelHandler;
import systems.reformcloud.network.packet.Packet;
import systems.reformcloud.utility.cloudsystem.InternalCloudNetwork;

import java.beans.ConstructorProperties;
import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * @author _Klaro | Pasqual K. / created on 18.06.2019
 */

public final class NetworkGlobalCluster implements Serializable {

    private Set<String> connectedChannels;

    private List<ClusterChannelInformation> clusterChannelInformation;

    private AbstractChannelHandler channelHandler;

    private InternalCloudNetwork currentCloudInformation;

    @ConstructorProperties({"connectedChannels", "clusterChannelInformation", "channelHandler", "currentCloudInformation"})
    public NetworkGlobalCluster(Set<String> connectedChannels,
                                List<ClusterChannelInformation> clusterChannelInformation,
                                AbstractChannelHandler channelHandler,
                                InternalCloudNetwork currentCloudInformation) {
        this.connectedChannels = connectedChannels;
        this.clusterChannelInformation = clusterChannelInformation;
        this.channelHandler = channelHandler;
        this.currentCloudInformation = currentCloudInformation;
    }

    public void publishPacket(Packet... packets) {
        for (Packet packet : packets) {
            clusterChannelInformation.forEach(channel -> channel.getChannelHandlerContext().writeAndFlush(packet));
        }
    }

    public Set<String> getConnectedChannels() {
        return connectedChannels;
    }

    public List<ClusterChannelInformation> getClusterChannelInformation() {
        return clusterChannelInformation;
    }

    public AbstractChannelHandler getChannelHandler() {
        return channelHandler;
    }

    public InternalCloudNetwork getCurrentCloudInformation() {
        return currentCloudInformation;
    }
}
