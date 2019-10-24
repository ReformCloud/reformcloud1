/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import systems.reformcloud.ReformCloudClient;
import systems.reformcloud.ReformCloudLibraryService;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.network.abstracts.AbstractChannelHandler;
import systems.reformcloud.network.authentication.enums.AuthenticationType;
import systems.reformcloud.network.packet.DefaultPacket;
import systems.reformcloud.network.packet.constants.ChannelConstants;
import systems.reformcloud.utility.cloudsystem.EthernetAddress;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 24.10.2018
 */

public final class NettySocketClient implements AutoCloseable, Serializable {

    private int connections = 1;

    private SslContext sslContext;

    private EventLoopGroup eventLoopGroup;

    /**
     * Connects to the ReformCloudController
     */
    public void connect(EthernetAddress ethernetAddress,
                        AbstractChannelHandler channelHandler,
                        boolean ssl) {
        if (eventLoopGroup == null) {
            eventLoopGroup = ReformCloudLibraryService.eventLoopGroup(4);
        }

        try {
            ReformCloudClient.getInstance().getColouredConsoleProvider()
                .info("Trying to connect to §3ReformCloudController §e@" +
                    ethernetAddress.getHost() + ":" + ethernetAddress.getPort() + "§r ["
                    + connections + "/7]");

            if (ssl) {
                sslContext = SslContextBuilder.forClient()
                    .trustManager(InsecureTrustManagerFactory.INSTANCE).build();
            }

            Bootstrap bootstrap = new Bootstrap()
                .group(eventLoopGroup)

                .option(ChannelOption.AUTO_READ, true)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.IP_TOS, 24)
                .option(ChannelOption.TCP_NODELAY, true)

                .channel(ReformCloudLibraryService.clientSocketChannel())

                .handler(new ChannelInitializer<Channel>() {
                    @Override
                    protected void initChannel(Channel channel) {
                        if (channelHandler.isChannelRegistered("ReformCloudController")) {
                            channel.close().syncUninterruptibly();
                            return;
                        }

                        if (ssl && sslContext != null) {
                            channel.pipeline().addLast(sslContext.newHandler(channel.alloc(),
                                ethernetAddress.getHost(), ethernetAddress.getPort()));
                        }

                        ReformCloudLibraryService
                            .prepareChannel(channel, channelHandler);
                    }
                });

            bootstrap.connect(ethernetAddress.getHost(), ethernetAddress.getPort()).sync().channel()
                .writeAndFlush(new DefaultPacket("Auth",
                    new Configuration()
                        .addStringValue("key",
                            ReformCloudClient.getInstance().getCloudConfiguration()
                                .getControllerKey())
                        .addStringValue("name",
                            ReformCloudClient.getInstance().getCloudConfiguration().getClientName())
                        .addValue("AuthenticationType",
                            AuthenticationType.INTERNAL),
                    ChannelConstants.REFORMCLOUD_AUTHENTICATION_CHANNEL
                ));

            ReformCloudClient.getInstance().getColouredConsoleProvider()
                .info("ReformCloud is now §aready§r and §aconnected§r to §e" + ethernetAddress
                    .getHost() + ":" + ethernetAddress.getPort());

            connections = -1;
        } catch (final Throwable throwable) {
            connections++;
            ReformCloudClient.getInstance().getColouredConsoleProvider().serve(
                "ReformCloud could not connect to " + ethernetAddress.getHost() + ":"
                    + ethernetAddress.getPort());
            if (throwable.getCause() != null) {
                ReformCloudClient.getInstance().getColouredConsoleProvider()
                    .serve("The following error occurred: " + throwable.getCause().toString());
            }
        }
    }

    @Override
    public void close() {
        if (eventLoopGroup == null) {
            return;
        }

        eventLoopGroup.shutdownGracefully();
        eventLoopGroup = null;
    }

    public int getConnections() {
        return this.connections;
    }

    public void setConnections(int connections) {
        this.connections = connections;
    }
}
