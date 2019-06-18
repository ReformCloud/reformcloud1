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

    private String globalChannelName;

    private ChannelHandlerContext channelHandlerContext;

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
