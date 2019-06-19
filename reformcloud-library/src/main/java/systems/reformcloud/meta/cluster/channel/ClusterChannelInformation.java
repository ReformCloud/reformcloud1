/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.meta.cluster.channel;

import io.netty.channel.ChannelHandlerContext;

import java.beans.ConstructorProperties;
import java.io.Serializable;
import java.net.InetSocketAddress;

/**
 * @author _Klaro | Pasqual K. / created on 18.06.2019
 */

public final class ClusterChannelInformation implements Serializable {

    /**
     * The channel name to send a packet over
     */
    private String globalChannelName;

    /**
     * The {@link ChannelHandlerContext} of the channel
     */
    private ChannelHandlerContext channelHandlerContext;

    /**
     * The address of the channel
     */
    private InetSocketAddress inetSocketAddress;

    @ConstructorProperties({"globalChannelName", "channelHandlerContext", "inetSocketAddress"})
    public ClusterChannelInformation(String globalChannelName,
                                     ChannelHandlerContext channelHandlerContext,
                                     InetSocketAddress inetSocketAddress) {
        this.globalChannelName = globalChannelName;
        this.channelHandlerContext = channelHandlerContext;
        this.inetSocketAddress = inetSocketAddress;
    }

    public String getGlobalChannelName() {
        return globalChannelName;
    }

    public ChannelHandlerContext getChannelHandlerContext() {
        return channelHandlerContext;
    }

    public InetSocketAddress getInetSocketAddress() {
        return inetSocketAddress;
    }
}
