package systems.reformcloud.netty.in;

import systems.reformcloud.ReformCloudController;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.netty.interfaces.NetworkInboundHandler;
import systems.reformcloud.netty.packet.enums.QueryType;

import java.util.List;

public class PacketInServerDisable implements NetworkInboundHandler {

    @Override
    public void handle(Configuration configuration, List<QueryType> queryTypes) {
        String serverName = configuration.getStringValue("serverName");
        ReformCloudController.getInstance().getChannelHandler().closeChannel(serverName);
    }
}
