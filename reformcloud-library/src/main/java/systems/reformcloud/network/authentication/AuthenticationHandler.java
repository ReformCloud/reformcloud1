/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.authentication;

import io.netty.channel.ChannelHandlerContext;
import systems.reformcloud.ReformCloudLibraryService;
import systems.reformcloud.ReformCloudLibraryServiceProvider;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.network.authentication.enums.AuthenticationType;
import systems.reformcloud.network.authentication.interfaces.AuthenticationManager;
import systems.reformcloud.network.channel.ChannelHandler;
import systems.reformcloud.network.packet.Packet;

/**
 * @author _Klaro | Pasqual K. / created on 19.10.2018
 */

public class AuthenticationHandler implements AuthenticationManager {

    @Override
    public void handleAuth(AuthenticationType authenticationType, Packet packet, ChannelHandlerContext channelHandlerContext, ChannelHandler channelHandler) {

        String name = packet.getConfiguration().getStringValue("name");
        switch (authenticationType) {
            case SERVER: {
                if (ReformCloudLibraryService.check(s -> s.equals(ReformCloudLibraryServiceProvider.getInstance().getKey()), packet.getConfiguration().getStringValue("key"))) {
                    channelHandler.closeChannel(name);
                    channelHandler.registerChannel(name, channelHandlerContext);

                    channelHandlerContext.channel().writeAndFlush(new Packet(
                            "InitializeCloudNetwork", new Configuration().addProperty("networkProperties", ReformCloudLibraryServiceProvider.getInstance().getInternalCloudNetwork())
                    ));
                } else {
                    channelHandlerContext.channel().close();
                }
                break;
            }
            case PROXY: {
                if (ReformCloudLibraryServiceProvider.getInstance().getKey().equals(packet.getConfiguration().getStringValue("key"))) {
                    channelHandler.registerChannel(name, channelHandlerContext);

                    channelHandlerContext.channel().writeAndFlush(new Packet(
                            "InitializeCloudNetwork", new Configuration().addProperty("networkProperties", ReformCloudLibraryServiceProvider.getInstance().getInternalCloudNetwork())
                    ));
                } else {
                    channelHandlerContext.channel().close();
                }
                break;
            }
            case INTERNAL: {
                if (ReformCloudLibraryService.check(s -> s.equals(ReformCloudLibraryServiceProvider.getInstance().getKey()), packet.getConfiguration().getStringValue("key"))) {
                    channelHandler.registerChannel(name, channelHandlerContext);

                    channelHandlerContext.channel().writeAndFlush(new Packet(
                            "InitializeCloudNetwork", new Configuration().addProperty("networkProperties", ReformCloudLibraryServiceProvider.getInstance().getInternalCloudNetwork())
                    ));
                } else {
                    channelHandlerContext.channel().close();
                }
                break;
            }
            default: {
                channelHandlerContext.channel().close();
                break;
            }
        }
    }
}
