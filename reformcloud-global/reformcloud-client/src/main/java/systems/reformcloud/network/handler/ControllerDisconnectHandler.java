/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import systems.reformcloud.ReformCloudClient;
import systems.reformcloud.ReformCloudLibraryService;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 03.02.2019
 */

public final class ControllerDisconnectHandler extends ChannelInboundHandlerAdapter implements Serializable {
    private static final long serialVersionUID = 4902179984306421185L;

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        if (!ctx.channel().isActive() && !ctx.channel().isOpen() && !ctx.channel().isWritable()
                && ReformCloudClient.getInstance().getNettySocketClient().getConnections() == -1
                && ReformCloudClient.RUNNING) {
            ReformCloudClient.getInstance().getNettySocketClient().close();
            ReformCloudLibraryService.sleep(1000);
            ReformCloudClient.getInstance().connect(ReformCloudClient.getInstance().isSsl());
        }
    }
}
