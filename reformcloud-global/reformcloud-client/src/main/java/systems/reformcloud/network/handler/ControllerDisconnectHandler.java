/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.handler;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import systems.reformcloud.ReformCloudClient;
import systems.reformcloud.ReformCloudLibraryService;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author _Klaro | Pasqual K. / created on 03.02.2019
 */

public final class ControllerDisconnectHandler extends ChannelDuplexHandler implements Serializable {
    private static final long serialVersionUID = 4902179984306421185L;

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        if (!ctx.channel().isActive() && !ctx.channel().isOpen() && !ctx.channel().isWritable()
                && Objects.equals(ReformCloudClient.getInstance().getChannelHandler().getChannelNameByValue(ctx), "ReformCloudController")
                && ReformCloudClient.getInstance().getNettySocketClient().getConnections() == -1
                && ReformCloudClient.RUNNING) {
            ReformCloudClient.getInstance().getNettySocketClient().close();
            ReformCloudClient.getInstance().getCloudProcessScreenService().getRegisteredProxyProcesses().forEach(proxyProcess -> {
                proxyProcess.shutdown(null, false);
                ReformCloudClient.getInstance().getLoggerProvider().info(ReformCloudClient.getInstance().getInternalCloudNetwork().getLoaded().getClient_shutdown_process()
                        .replace("%name%", proxyProcess.getProxyStartupInfo().getName()));
                ReformCloudLibraryService.sleep(1000);
            });
            ReformCloudClient.getInstance().getCloudProcessScreenService().getRegisteredServerProcesses().forEach(serverProcess -> {
                serverProcess.shutdown(false);
                ReformCloudClient.getInstance().getLoggerProvider().info(ReformCloudClient.getInstance().getInternalCloudNetwork().getLoaded().getClient_shutdown_process()
                        .replace("%name%", serverProcess.getServerStartupInfo().getName()));
                ReformCloudLibraryService.sleep(1000);
            });
            ReformCloudLibraryService.sleep(1000);
            ReformCloudClient.getInstance().connect(ReformCloudClient.getInstance().isSsl());
        }
    }
}
