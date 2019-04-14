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
import lombok.RequiredArgsConstructor;
import systems.reformcloud.ReformCloudLibraryServiceProvider;
import systems.reformcloud.web.utils.WebHandler;
import systems.reformcloud.web.utils.WebHandlerAdapter;

import java.net.InetSocketAddress;
import java.net.URI;

/**
 * @author _Klaro | Pasqual K. / created on 30.10.2018
 */

@RequiredArgsConstructor
public class WebServerHandler extends ChannelInboundHandlerAdapter {
    private final WebHandlerAdapter webHandlerAdapter;

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        InetSocketAddress inetSocketAddress = (InetSocketAddress) ctx.channel().remoteAddress();
        ReformCloudLibraryServiceProvider.getInstance().getLoggerProvider().debug("Receiving message in channel " +
                inetSocketAddress.getAddress().getHostAddress());

        if (!(msg instanceof HttpRequest))
            return;
        HttpRequest httpRequest = (HttpRequest) msg;

        String requestUri = new URI(httpRequest.uri()).getRawPath();

        if (requestUri == null)
            requestUri = "/";

        final WebHandler webHandler = this.webHandlerAdapter.getHandler(requestUri);

        ReformCloudLibraryServiceProvider.getInstance().getLoggerProvider().debug(webHandler == null ?
                "No web handler found, sending default response" : "Handler found, handling");

        FullHttpResponse fullHttpResponse = null;
        if (webHandler != null)
            fullHttpResponse = webHandler.handleRequest(ctx, httpRequest);

        if (webHandler != null && fullHttpResponse == null)
            ReformCloudLibraryServiceProvider.getInstance().getLoggerProvider().debug("Web handler got request, " +
                    "but returned nothing (sending default response)");

        if (fullHttpResponse == null)
            fullHttpResponse = new DefaultFullHttpResponse(httpRequest.protocolVersion(), HttpResponseStatus.NOT_FOUND, Unpooled.wrappedBuffer("404 Page is not available!".getBytes()));

        fullHttpResponse.headers().set("Access-Control-Allow-Origin", "*");
        ctx.channel().writeAndFlush(fullHttpResponse).addListener(ChannelFutureListener.CLOSE).channel().close().syncUninterruptibly();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        if (!ctx.channel().isActive() && !ctx.channel().isOpen() && !ctx.channel().isWritable())
            ctx.channel().close().addListener(ChannelFutureListener.CLOSE_ON_FAILURE).addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
    }
}
