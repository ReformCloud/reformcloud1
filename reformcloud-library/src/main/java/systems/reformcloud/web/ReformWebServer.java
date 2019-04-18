/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.web;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.SelfSignedCertificate;
import lombok.Getter;
import systems.reformcloud.ReformCloudLibraryService;
import systems.reformcloud.ReformCloudLibraryServiceProvider;
import systems.reformcloud.utility.cloudsystem.EthernetAddress;
import systems.reformcloud.web.handler.WebServerHandler;
import systems.reformcloud.web.utils.WebHandlerAdapter;

import javax.net.ssl.SSLException;
import java.io.File;
import java.io.Serializable;
import java.security.cert.CertificateException;

/**
 * @author _Klaro | Pasqual K. / created on 30.10.2018
 */

@Getter
public final class ReformWebServer implements Serializable {
    /**
     * The boss group of the web server
     */
    protected EventLoopGroup bossGroup = ReformCloudLibraryService.eventLoopGroup();

    /**
     * The worker group of the web server
     */
    protected EventLoopGroup workerGroup = ReformCloudLibraryService.eventLoopGroup();

    /**
     * The created server bootstrap
     */
    private ServerBootstrap serverBootstrap;

    /**
     * The created ssl context, will be {@code null} if ssl is disabled
     */
    private SslContext sslContext;

    /**
     * The web handler where all handlers are registered
     */
    private final WebHandlerAdapter webHandlerAdapter = new WebHandlerAdapter();

    /**
     * Will create a new web server instance and bind it to the given host and port
     *
     * @param ethernetAddress           Ip and Port where the WebServer will be bound to
     * @param ssl                       If ssl should be enabled or not
     * @param certFile                  SSL-Certificate file, if this is {@code null} a
     *                                  the server will sign a self signed ssl certificate
     * @param keyFile                   SSL-Certificate key-file if this is {@code null}
     *                                  the server will sign a self signed ssl certificate
     * @throws CertificateException     If the Certificate cant be created
     * @throws SSLException             If the Certificate cant be used
     * @throws InterruptedException     If the Cloud cannot bind the Webservice
     */
    public ReformWebServer(EthernetAddress ethernetAddress, boolean ssl, final File certFile, final File keyFile) throws CertificateException, SSLException, InterruptedException {
        if (ssl) {
            if (certFile == null || keyFile == null) {
                SelfSignedCertificate selfSignedCertificate = new SelfSignedCertificate();
                sslContext = SslContextBuilder.forServer(selfSignedCertificate.key(), selfSignedCertificate.cert()).build();
            } else {
                sslContext = SslContextBuilder.forServer(certFile, keyFile).build();
            }
        }

        serverBootstrap = new ServerBootstrap()
                .group(bossGroup, workerGroup)

                .childOption(ChannelOption.IP_TOS, 24)
                .childOption(ChannelOption.AUTO_READ, true)
                .childOption(ChannelOption.TCP_NODELAY, true)

                .channel(ReformCloudLibraryService.serverSocketChannel())

                .childHandler(new ChannelInitializer<Channel>() {
                    @Override
                    protected void initChannel(Channel channel) {
                        if (sslContext != null && ssl)
                            channel.pipeline().addLast(sslContext.newHandler(channel.alloc()));

                        channel.pipeline().addLast(
                                new HttpServerCodec(),
                                new HttpObjectAggregator(Integer.MAX_VALUE),
                                new WebServerHandler(webHandlerAdapter)
                        );
                    }
                });
        serverBootstrap.bind(ethernetAddress.getHost(), ethernetAddress.getPort()).sync().channel().closeFuture();

        ReformCloudLibraryServiceProvider.getInstance().getLoggerProvider().info(ReformCloudLibraryServiceProvider.getInstance().getLoaded().getWebserver_bound()
                .replace("%ip%", ethernetAddress.getHost())
                .replace("%port%", Integer.toString(ethernetAddress.getPort())));
    }

    /**
     * Closes the WebHandler
     */
    public void shutdown() {
        this.bossGroup.shutdownGracefully();
        this.workerGroup.shutdownGracefully();
    }
}
