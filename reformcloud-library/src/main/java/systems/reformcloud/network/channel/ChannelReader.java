/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.channel;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import java.io.IOException;
import java.io.Serializable;
import java.net.InetSocketAddress;
import java.util.Objects;
import systems.reformcloud.ReformCloudLibraryService;
import systems.reformcloud.ReformCloudLibraryServiceProvider;
import systems.reformcloud.api.EventHandler;
import systems.reformcloud.event.events.IncomingPacketEvent;
import systems.reformcloud.network.authentication.AuthenticationHandler;
import systems.reformcloud.network.interfaces.NetworkQueryInboundHandler;
import systems.reformcloud.network.packet.Packet;
import systems.reformcloud.network.packet.constants.ChannelConstants;
import systems.reformcloud.utility.StringUtil;
import systems.reformcloud.utility.TypeTokenAdaptor;

/**
 * @author _Klaro | Pasqual K. / created on 18.10.2018
 */

public final class ChannelReader extends SimpleChannelInboundHandler implements Serializable {

    /**
     * The channel handler instance
     */
    private ChannelHandler channelHandler;

    @java.beans.ConstructorProperties({"channelHandler"})
    public ChannelReader(ChannelHandler channelHandler) {
        this.channelHandler = channelHandler;
    }

    @Override
    protected void channelRead0(final ChannelHandlerContext channelHandlerContext,
        final Object object) {
        if (!(object instanceof Packet)) {
            return;
        }

        Packet packet = (Packet) object;

        ReformCloudLibraryServiceProvider.getInstance().getLoggerProvider()
            .debug("Handling incoming packet " +
                "[Type=" + packet.getType() + "/Query=" + (packet.getResult() != null) + "/Message="
                + packet.getConfiguration());

        IncomingPacketEvent incomingPacketEvent = new IncomingPacketEvent(packet,
            channelHandlerContext);
        ReformCloudLibraryServiceProvider.getInstance().getEventManager().fire(incomingPacketEvent);
        if (incomingPacketEvent.isCancelled()) {
            return;
        }

        final String address = ((InetSocketAddress) channelHandlerContext.channel().remoteAddress())
            .getAddress().getHostAddress();

        if (packet.getType().equalsIgnoreCase("InitializeCloudNetwork")
            && ReformCloudLibraryServiceProvider.getInstance().getControllerIP() != null
            && ReformCloudLibraryService
            .check(s -> s.equals(ReformCloudLibraryServiceProvider.getInstance().getControllerIP()),
                address)) {
            ReformCloudLibraryServiceProvider.getInstance().getLoggerProvider()
                .debug("Incoming packet sender is controller, " +
                    "channel was registered");
            channelHandler.registerChannel("ReformCloudController", channelHandlerContext);
        }

        if (!channelHandler.getChannelList().contains(channelHandlerContext)) {
            if (!packet.getType().equalsIgnoreCase("Auth") || !packet.getConfiguration()
                .contains("AuthenticationType")
                || packet.getChannel() != ChannelConstants.REFORMCLOUD_AUTHENTICATION_CHANNEL) {
                channelHandlerContext.channel().close();
                return;
            }

            ReformCloudLibraryServiceProvider.getInstance().getLoggerProvider()
                .debug("Trying to handle authentication of " +
                    "channel [IP=" + address + "]");
            new AuthenticationHandler().handleAuth(Objects.requireNonNull(
                packet.getConfiguration()
                    .getValue("AuthenticationType", TypeTokenAdaptor.getAUTHENTICATION_TYPE())),
                packet, channelHandlerContext, channelHandler);
        }

        if (packet.getResult() != null) {
            if (ReformCloudLibraryServiceProvider.getInstance().getNettyHandler()
                .isQueryHandlerRegistered(packet.getType())) {
                NetworkQueryInboundHandler networkQueryInboundHandler =
                    ReformCloudLibraryServiceProvider.getInstance().getNettyHandler()
                        .getQueryHandler(packet.getType());
                if (networkQueryInboundHandler.handlingChannel() == packet.getChannel()) {
                    networkQueryInboundHandler
                        .handle(packet.getConfiguration(), packet.getResult());
                    ReformCloudLibraryServiceProvider.getInstance().getLoggerProvider()
                        .debug("Query packet handler " +
                            "found, trying to handle packet");
                }
            } else if (channelHandler.getResults().containsKey(packet.getResult())) {
                channelHandler.getResults().get(packet.getResult()).handleIncoming(packet);
                ReformCloudLibraryServiceProvider.getInstance().getLoggerProvider()
                    .debug("QueryPacket was send by " +
                        "this instance, handling");
            }

            return;
        }

        if (packet.getConfiguration().contains("from")) {
            EventHandler.instance.get()
                .handleCustomPacket(packet.getConfiguration().getStringValue("from"),
                    packet.getType(), packet.getConfiguration());
            ReformCloudLibraryServiceProvider.getInstance().getLoggerProvider()
                .debug("Handling custom packet from " +
                    "[ServiceName=" + packet.getConfiguration().getStringValue("from") + "/Channel="
                    + address + "]");
        } else {
            EventHandler.instance.get()
                .handleCustomPacket("unknown", packet.getType(), packet.getConfiguration());
        }

        if (ReformCloudLibraryServiceProvider.getInstance().getNettyHandler()
            .handle(packet.getChannel(), packet.getType(),
                packet.getConfiguration())) {
            ReformCloudLibraryServiceProvider.getInstance().getLoggerProvider()
                .debug("Successfully handled incoming packet " +
                    "with packet handler");
        } else {
            ReformCloudLibraryServiceProvider.getInstance().getLoggerProvider()
                .debug("Cannot handle incoming packet, no handler found");
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        if (!this.channelHandler.isChannelRegistered(ctx)
            && ctx.channel().isActive()
            && ctx.channel().isOpen()
            && ctx.channel().isWritable()) {
            EventHandler.instance.get().channelConnected(ctx);
        }
    }

    @Override
    public void channelInactive(final ChannelHandlerContext ctx) {
        final InetSocketAddress inetSocketAddress = ((InetSocketAddress) ctx.channel()
            .remoteAddress());
        if (!ctx.channel().isActive() && !ctx.channel().isOpen() && !ctx.channel().isWritable()
            && inetSocketAddress != null) {
            final String serviceName = channelHandler.getChannelNameByValue(ctx);
            ReformCloudLibraryServiceProvider.getInstance().getLoggerProvider().info(
                ReformCloudLibraryServiceProvider.getInstance().getLoaded()
                    .getChannel_global_disconnected()
                    .replace("%ip%", inetSocketAddress.getAddress().getHostAddress())
                    .replace("%name%", serviceName != null ? serviceName : "Not found")
                    .replace("%port%", Integer.toString(inetSocketAddress.getPort())));
            ctx.channel().close();
            EventHandler.instance.get().channelDisconnected(ctx);
        }
    }

    @Override
    public void exceptionCaught(final ChannelHandlerContext ctx, final Throwable cause) {
        EventHandler.instance.get().channelExceptionCaught(ctx, cause);
        if (!(cause instanceof IOException)) {
            StringUtil.printError(
                ReformCloudLibraryServiceProvider.getInstance().getLoggerProvider(),
                "An exceptionCaught() event was fired, and it reached at the " +
                    "tail of the pipeline. It usually means the last handler in the " +
                    "pipeline did not handle the exception.",
                cause
            );
        } else if (ReformCloudLibraryServiceProvider.getInstance().getLoggerProvider().isDebug()) {
            StringUtil.printError(
                ReformCloudLibraryServiceProvider.getInstance().getLoggerProvider(),
                "An exceptionCaught() event was fired, and it reached at the " +
                    "tail of the pipeline. It usually means the last handler in the " +
                    "pipeline did not handle the exception.",
                cause
            );
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }
}
