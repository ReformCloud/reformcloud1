package systems.reformcloud.netty.in;

import systems.reformcloud.ReformCloudController;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.netty.interfaces.NetworkInboundHandler;

public class PacketInServerDisable implements NetworkInboundHandler {

    @Override
    public void handle(Configuration configuration) {
        String serverName = configuration.getStringValue("serverName");
        ReformCloudController.getInstance().getChannelHandler().closeChannel(serverName);
    }
}
