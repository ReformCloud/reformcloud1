/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.api;

import io.netty.channel.ChannelHandlerContext;
import systems.reformcloud.ReformCloudClient;
import systems.reformcloud.ReformCloudLibraryService;
import systems.reformcloud.configurations.Configuration;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 23.02.2019
 */

public final class EventAdapter implements Serializable, IEventHandler {
    public EventAdapter() {
        this.instance.set(this);
    }

    @Override
    public void handleCustomPacket(String channel, String targetType, Configuration configuration) {
    }

    @Override
    public void handleReload() {
        ReformCloudClient.getInstance().reloadAll();
    }

    @Override
    public void channelConnected(ChannelHandlerContext channelHandlerContext) {
    }

    @Override
    public void channelDisconnected(ChannelHandlerContext channelHandlerContext) {
        ReformCloudClient.getInstance().getCloudProcessScreenService().getRegisteredProxyProcesses()
                .forEach(proxyStartupHandler -> {
                    proxyStartupHandler.executeCommand("alert Your current client lost the connection to the controller");
                    ReformCloudLibraryService.sleep(5000);
                    proxyStartupHandler.shutdown(null, false);
                });

        ReformCloudClient.getInstance().getCloudProcessScreenService().getRegisteredServerProcesses()
                .forEach(cloudServerStartupHandler -> cloudServerStartupHandler.shutdown(false));
    }

    @Override
    public void channelExceptionCaught(ChannelHandlerContext channelHandlerContext, Throwable cause) {
    }
}
