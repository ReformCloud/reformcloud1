/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.netty.authentication;

import systems.reformcloud.ReformCloudLibraryService;
import systems.reformcloud.ReformCloudLibraryServiceProvider;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.netty.authentication.enums.AuthenticationType;
import systems.reformcloud.netty.authentication.interfaces.AuthenticationManager;
import systems.reformcloud.netty.channel.ChannelHandler;
import systems.reformcloud.netty.packet.Packet;
import systems.reformcloud.netty.packet.enums.PacketSender;
import systems.reformcloud.netty.packet.enums.QueryType;
import io.netty.channel.ChannelHandlerContext;

import java.util.Arrays;

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
                            "InitializeCloudNetwork", new Configuration().addProperty("networkProperties", ReformCloudLibraryServiceProvider.getInstance().getInternalCloudNetwork()), Arrays.asList(QueryType.COMPLETE, QueryType.RESULT), PacketSender.CONTROLLER
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
                            "InitializeCloudNetwork", new Configuration().addProperty("networkProperties", ReformCloudLibraryServiceProvider.getInstance().getInternalCloudNetwork()), Arrays.asList(QueryType.COMPLETE, QueryType.RESULT), PacketSender.CONTROLLER
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
                            "InitializeCloudNetwork", new Configuration().addProperty("networkProperties", ReformCloudLibraryServiceProvider.getInstance().getInternalCloudNetwork()), Arrays.asList(QueryType.COMPLETE, QueryType.RESULT), PacketSender.CONTROLLER
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
