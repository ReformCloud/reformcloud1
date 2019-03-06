/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.channel;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.AllArgsConstructor;
import systems.reformcloud.ReformCloudLibraryService;
import systems.reformcloud.ReformCloudLibraryServiceProvider;
import systems.reformcloud.api.IEventHandler;
import systems.reformcloud.event.enums.EventTargetType;
import systems.reformcloud.event.events.IncomingPacketEvent;
import systems.reformcloud.event.events.PacketHandleSuccessEvent;
import systems.reformcloud.network.authentication.AuthenticationHandler;
import systems.reformcloud.network.packet.Packet;
import systems.reformcloud.utility.TypeTokenAdaptor;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * @author _Klaro | Pasqual K. / created on 18.10.2018
 */

@AllArgsConstructor
public class ChannelReader extends SimpleChannelInboundHandler {
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

        final String address = ((InetSocketAddress) channelHandlerContext.channel().remoteAddress()).getAddress().getHostAddress();

        if (packet.getType().equalsIgnoreCase("InitializeCloudNetwork") && ReformCloudLibraryServiceProvider.getInstance().getControllerIP() != null
                && ReformCloudLibraryService.check(s -> s.equals(ReformCloudLibraryServiceProvider.getInstance().getControllerIP()), address)) {
            channelHandler.registerChannel("ReformCloudController", channelHandlerContext);
        }

        if (!channelHandler.getChannelList().contains(channelHandlerContext)) {
            if (!packet.getType().equalsIgnoreCase("Auth") || !packet.getConfiguration().contains("AuthenticationType")) {
                channelHandlerContext.channel().close();
                return;
            }

            new AuthenticationHandler().handleAuth(packet.getConfiguration().getValue("AuthenticationType", TypeTokenAdaptor.getAUTHENTICATION_TYPE()),
                    packet, channelHandlerContext, channelHandler);
        }

        ReformCloudLibraryServiceProvider.getInstance().getLoggerProvider().serve("Receiving packet |:" + packet.getType());

        if (packet.getResult() != null) {
            ReformCloudLibraryServiceProvider.getInstance().getLoggerProvider().serve("Packet is query packet");
            if (ReformCloudLibraryServiceProvider.getInstance().getNettyHandler().isQueryHandlerRegistered(packet.getType())) {
                ReformCloudLibraryServiceProvider.getInstance().getNettyHandler()
                        .getQueryHandler(packet.getType()).handle(packet.getConfiguration(), packet.getResult());
                ReformCloudLibraryServiceProvider.getInstance().getLoggerProvider().serve("Packet has query handler");
            } else  {
                channelHandler.getResults().get(packet.getResult()).handleIncoming(packet);
                ReformCloudLibraryServiceProvider.getInstance().getLoggerProvider().serve("Packet was query send and handeled");
            }

            return;
        }

        if (packet.getConfiguration().contains("from"))
            IEventHandler.instance.get().handleCustomPacket(packet.getConfiguration().getStringValue("from"),
                    packet.getType(), packet.getConfiguration());
        else
            IEventHandler.instance.get().handleCustomPacket("unknown", packet.getType(), packet.getConfiguration());

        if (ReformCloudLibraryServiceProvider.getInstance().getNettyHandler().handle(packet.getType(), packet.getConfiguration()))
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
            ReformCloudLibraryServiceProvider.getInstance().getLoggerProvider().serve("An exceptionCaught() event was fired, and it reached at the tail of the pipeline. It usually means the last handler in the pipeline did not handle the exception.");
            cause.printStackTrace();
        }
    }
}
