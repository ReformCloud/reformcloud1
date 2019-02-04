/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.netty.channel;

import systems.reformcloud.ReformCloudLibraryService;
import systems.reformcloud.ReformCloudLibraryServiceProvider;
import systems.reformcloud.event.enums.EventTargetType;
import systems.reformcloud.event.events.IncomingPacketEvent;
import systems.reformcloud.event.events.PacketHandleSuccessEvent;
import systems.reformcloud.netty.NettyHandler;
import systems.reformcloud.netty.authentication.AuthenticationHandler;
import systems.reformcloud.netty.packet.Packet;
import systems.reformcloud.netty.packet.enums.QueryType;
import systems.reformcloud.utility.AccessChecker;
import systems.reformcloud.utility.TypeTokenAdaptor;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.AllArgsConstructor;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * @author _Klaro | Pasqual K. / created on 18.10.2018
 */

@AllArgsConstructor
public class ChannelReader extends SimpleChannelInboundHandler {
    private NettyHandler nettyHandler;
    private ChannelHandler channelHandler;

    @Override
    protected void channelRead0(final ChannelHandlerContext channelHandlerContext, final Object object) {
        if (!(object instanceof Packet))
            return;

        Packet packet = (Packet) object;

        IncomingPacketEvent incomingPacketEvent = new IncomingPacketEvent(packet, false);
        ReformCloudLibraryServiceProvider.getInstance().getEventManager().callEvent(EventTargetType.INCOMING_PACKET, incomingPacketEvent);

        if (incomingPacketEvent.isCancelled())
            return;

        if (!packet.getQueryTypes().contains(QueryType.COMPLETE))
            return;

        packet.getQueryTypes().remove(QueryType.COMPLETE);
        packet.getConfiguration().addProperty("responseUUID", packet.getResponseUUID());

        final String address = ((InetSocketAddress) channelHandlerContext.channel().remoteAddress()).getAddress().getHostAddress();

        if (packet.getType().equalsIgnoreCase("InitializeCloudNetwork") && ReformCloudLibraryServiceProvider.getInstance().getControllerIP() != null
                && ReformCloudLibraryService.check(s -> s.equals(ReformCloudLibraryServiceProvider.getInstance().getControllerIP()), address)) {
            channelHandler.registerChannel("ReformCloudController", channelHandlerContext);
        }

        if (!new AccessChecker().checkChannel(channelHandler.getChannelList(), channelHandlerContext).isAccepted()) {
            if (!packet.getType().equalsIgnoreCase("Auth") || !packet.getConfiguration().contains("AuthenticationType")) {
                channelHandlerContext.channel().close();
                return;
            }

            new AuthenticationHandler().handleAuth(packet.getConfiguration().getValue("AuthenticationType", TypeTokenAdaptor.getAuthenticationType()),
                    packet, channelHandlerContext, channelHandler);
        }

        if (nettyHandler.handle(packet.getType(), packet.getConfiguration(), packet.getQueryTypes()))
            ReformCloudLibraryServiceProvider.getInstance().getEventManager().callEvent(EventTargetType.PACKET_HANDLE_SUCCESS, new PacketHandleSuccessEvent(true, packet));
        else
            ReformCloudLibraryServiceProvider.getInstance().getEventManager().callEvent(EventTargetType.PACKET_HANDLE_SUCCESS, new PacketHandleSuccessEvent(false, packet));
    }

    @Override
    public void channelInactive(final ChannelHandlerContext ctx) {
        final InetSocketAddress inetSocketAddress = ((InetSocketAddress) ctx.channel().remoteAddress());
        if (!ctx.channel().isActive() && !ctx.channel().isOpen() && !ctx.channel().isWritable() && inetSocketAddress != null) {
            ReformCloudLibraryServiceProvider.getInstance().getLoggerProvider().info(ReformCloudLibraryServiceProvider.getInstance().getLoaded().getChannel_global_disconnected()
                    .replace("%ip%", inetSocketAddress.getAddress().getHostAddress())
                    .replace("%port%", Integer.toString(inetSocketAddress.getPort())));
        }
    }

    @Override
    public void exceptionCaught(final ChannelHandlerContext ctx, final Throwable cause) {
        if (!(cause instanceof IOException)) {
            ReformCloudLibraryServiceProvider.getInstance().getLoggerProvider().err("An exceptionCaught() event was fired, and it reached at the tail of the pipeline. It usually means the last handler in the pipeline did not handle the exception.");
            cause.printStackTrace();
        }
    }
}
