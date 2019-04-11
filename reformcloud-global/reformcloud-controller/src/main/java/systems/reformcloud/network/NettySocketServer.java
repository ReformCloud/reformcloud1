/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.*;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.SelfSignedCertificate;
import lombok.Getter;
import systems.reformcloud.ReformCloudController;
import systems.reformcloud.ReformCloudLibraryService;
import systems.reformcloud.ReformCloudLibraryServiceProvider;
import systems.reformcloud.utility.StringUtil;
import systems.reformcloud.utility.cloudsystem.EthernetAddress;

import java.io.File;
import java.net.InetSocketAddress;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

/**
 * @author _Klaro | Pasqual K. / created on 21.10.2018
 */

@Getter
public class NettySocketServer extends ChannelInitializer<Channel> implements AutoCloseable {
    private SslContext sslContext;
    private EventLoopGroup workerGroup = ReformCloudLibraryService.eventLoopGroup(), bossGroup = ReformCloudLibraryService.eventLoopGroup();

    /**
     * Prepares a socket server by using io.network and binds him using
     * {@link ServerBootstrap} synchronized on the main thread. This
     * is blocking and will await the bind. If ssl is enabled the
     * {@link SslContext} will be prepared first by using an self-
     * signed certificate using an SslContextBuilder
     * {@link SslContextBuilder#forServer(PrivateKey, X509Certificate...)}.
     * Please note all ChannelOptions's below in the main source.
     * If an handler connects the cloud will prepare the channel by using
     * the ReformCloudLibraryService#prepareChannel method.
     * If an exceptions occurs it will be catch by {@link Throwable} below and
     * printed in the console. If an error occurs, you can contact the support.
     *
     * @since 2.0
     *
     * @param ssl                   If this is {@code true} the ssl context
     *                              will be enabled and a self-signed certificate
     *                              will be added to every channel, which tries to
     *                              connect to the ReformCloudController
     * @param ethernetAddress    Main address where the cloud tries to bind
     *                              the socket server to. Please make sure that
     *                              the port is not in use, yet
     */
    public NettySocketServer(boolean ssl, EthernetAddress ethernetAddress, File cert, File key) {
        try {
            if (ssl) {
                if (cert == null || key == null) {
                    SelfSignedCertificate selfSignedCertificate = new SelfSignedCertificate();
                    sslContext = SslContextBuilder.forServer(selfSignedCertificate.key(), selfSignedCertificate.cert()).build();
                } else {
                    sslContext = SslContextBuilder.forServer(cert, key).build();
                }
            }

            ServerBootstrap serverBootstrap = new ServerBootstrap()
                    .group(bossGroup, workerGroup)

                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childOption(ChannelOption.IP_TOS, 24)
                    .childOption(ChannelOption.TCP_NODELAY, true)
                    .childOption(ChannelOption.ALLOCATOR, ByteBufAllocator.DEFAULT)
                    .childOption(ChannelOption.AUTO_READ, true)

                    .childHandler(this)

                    .option(ChannelOption.ALLOCATOR, ByteBufAllocator.DEFAULT)
                    .option(ChannelOption.AUTO_READ, true)

                    .channel(ReformCloudLibraryService.serverSocketChannel());

            ChannelFuture channelFuture = serverBootstrap.bind(ethernetAddress.getHost(), ethernetAddress.getPort())
                    .addListener((handler) -> {
                        if (handler.isSuccess())
                            ReformCloudController.getInstance().getLoggerProvider().info(ReformCloudController.getInstance().getLoadedLanguage().getController_socket_bind_success()
                                    .replace("%ip%", ethernetAddress.getHost())
                                    .replace("%port%", Integer.toString(ethernetAddress.getPort())));
                        else
                            ReformCloudController.getInstance().getLoggerProvider().serve(ReformCloudController.getInstance().getLoadedLanguage().getNetty_server_bound()
                                    .replace("%ip%", ethernetAddress.getHost())
                                    .replace("%port%", Integer.toString(ethernetAddress.getPort())));
                    }).addListener(ChannelFutureListener.CLOSE_ON_FAILURE).addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);

            channelFuture.sync().channel().closeFuture();
        } catch (final Throwable throwable) {
            StringUtil.printError(ReformCloudLibraryServiceProvider.getInstance().getLoggerProvider(), "Error while binding socket server", throwable);
        }
    }

    @Override
    protected void initChannel(Channel channel) {
        final InetSocketAddress inetSocketAddress = ((InetSocketAddress) channel.remoteAddress());

        if (this.isIpAllowed(inetSocketAddress.getAddress().getHostAddress())) {
            if (sslContext != null)
                channel.pipeline().addLast(sslContext.newHandler(channel.alloc()));

            ReformCloudLibraryService.prepareChannel(channel, ReformCloudController.getInstance().getChannelHandler());
            ReformCloudController.getInstance().getLoggerProvider().info(ReformCloudController.getInstance().getLoadedLanguage().getController_channel_connected()
                    .replace("%ip%", inetSocketAddress.getAddress().getHostAddress())
                    .replace("%port%", Integer.toString(inetSocketAddress.getPort())));
        } else {
            channel.close().addListener(ChannelFutureListener.CLOSE_ON_FAILURE).addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
        }
    }

    @Override
    public void close() {
        workerGroup.shutdownGracefully();
        bossGroup.shutdownGracefully();
    }

    private boolean isIpAllowed(String ip) {
        List<String> ips = new ArrayList<>();

        ReformCloudController.getInstance().getInternalCloudNetwork().getClients().values().forEach(client -> ips.add(client.getIp()));
        for (String string : ips)
            if (ReformCloudLibraryService.check(s -> s.equals(string), ip))
                return true;

        return false;
    }
}
