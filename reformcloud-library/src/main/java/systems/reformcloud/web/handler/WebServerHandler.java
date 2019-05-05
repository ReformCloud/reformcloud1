/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.web.handler;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import systems.reformcloud.ReformCloudLibraryServiceProvider;
import systems.reformcloud.logging.AbstractLoggerProvider;
import systems.reformcloud.web.utils.WebHandler;
import systems.reformcloud.web.utils.WebHandlerAdapter;

import java.io.IOException;
import java.io.Serializable;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.function.Consumer;

/**
 * @author _Klaro | Pasqual K. / created on 30.10.2018
 */

public final class WebServerHandler extends ChannelInboundHandlerAdapter implements Serializable {
    /**
     * The web handler of the cloud system
     */
    private final WebHandlerAdapter webHandlerAdapter;

    /**
     * Handles all exceptions which occurs while handling messages
     */
    private Consumer<Throwable> exception = this::handleException;

    @java.beans.ConstructorProperties({"webHandlerAdapter"})
    public WebServerHandler(WebHandlerAdapter webHandlerAdapter) {
        this.webHandlerAdapter = webHandlerAdapter;
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    /**
     * Tries to handle the incoming message
     *
     * @param ctx               The channel handler context of the channel which connects
     * @param msg               The sent message of the network participant
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        InetSocketAddress inetSocketAddress = (InetSocketAddress) ctx.channel().remoteAddress();
        ReformCloudLibraryServiceProvider.getInstance().getLoggerProvider().debug("Receiving message in channel " +
                inetSocketAddress.getAddress().getHostAddress());

        if (!(msg instanceof HttpRequest))
            return;
        HttpRequest httpRequest = (HttpRequest) msg;

        ReformCloudLibraryServiceProvider.getInstance().getLoggerProvider().debug(httpRequest.headers().entries() + "");

        String requestUri;

        try {
            requestUri = new URI(httpRequest.uri()).getRawPath();
        } catch (final URISyntaxException ex) {
            exception.accept(ex);
            ctx.writeAndFlush(
                    new DefaultFullHttpResponse(httpRequest.protocolVersion(),
                            HttpResponseStatus.NOT_FOUND, Unpooled.wrappedBuffer("404 Page is not available!".getBytes()))
            ).addListener(ChannelFutureListener.CLOSE);
            return;
        }

        if (requestUri == null)
            requestUri = "/";

        final WebHandler webHandler = this.webHandlerAdapter.getHandler(requestUri);

        ReformCloudLibraryServiceProvider.getInstance().getLoggerProvider().debug(webHandler == null ?
                "No web handler found, sending default response" : "Handler found, handling");

        FullHttpResponse fullHttpResponse = null;
        if (webHandler != null) {
            try {
                fullHttpResponse = webHandler.handleRequest(ctx, httpRequest);
            } catch (final Exception ex) {
                exception.accept(ex);
                ctx.writeAndFlush(
                        new DefaultFullHttpResponse(httpRequest.protocolVersion(),
                                HttpResponseStatus.NOT_FOUND, Unpooled.wrappedBuffer("404 Page is not available!".getBytes()))
                ).addListener(ChannelFutureListener.CLOSE);
                return;
            }
        }

        if (webHandler != null && fullHttpResponse == null)
            ReformCloudLibraryServiceProvider.getInstance().getLoggerProvider().debug("Web handler got request, " +
                    "but returned nothing (sending default response)");

        if (fullHttpResponse == null)
            fullHttpResponse = new DefaultFullHttpResponse(httpRequest.protocolVersion(),
                    HttpResponseStatus.NOT_FOUND, Unpooled.wrappedBuffer("404 Page is not available!".getBytes()));

        fullHttpResponse.headers().set("Access-Control-Allow-Origin", "*");
        ctx.channel().writeAndFlush(fullHttpResponse).addListener(ChannelFutureListener.CLOSE);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        if (!ctx.channel().isActive() && !ctx.channel().isOpen() && !ctx.channel().isWritable())
            ctx.channel().close().addListener(ChannelFutureListener.CLOSE_ON_FAILURE).addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        exception.accept(cause);
    }

    /**
     * Handles the exceptions and print them
     *
     * @param cause     The exception which occurs
     */
    private void handleException(Throwable cause) {
        if (ReformCloudLibraryServiceProvider.getInstance() == null)
            AbstractLoggerProvider.defaultLogger().exception().accept(cause);

        if (cause instanceof IOException) {
            if (ReformCloudLibraryServiceProvider.getInstance().getLoggerProvider().isDebug())
                ReformCloudLibraryServiceProvider.getInstance().getLoggerProvider().exception().accept(cause);
        } else
            ReformCloudLibraryServiceProvider.getInstance().getLoggerProvider().exception().accept(cause);
    }
}
