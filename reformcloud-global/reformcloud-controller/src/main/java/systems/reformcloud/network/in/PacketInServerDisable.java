package systems.reformcloud.network.in;

import systems.reformcloud.ReformCloudController;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.network.interfaces.NetworkInboundHandler;

public class PacketInServerDisable implements NetworkInboundHandler {

    @Override
    public void handle(Configuration configuration) {
        String serverName = configuration.getStringValue("serverName");
        ReformCloudController.getInstance().getChannelHandler().closeChannel(serverName);
    }
}
