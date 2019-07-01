/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.utility.screen.defaults;

import systems.reformcloud.ReformCloudController;
import systems.reformcloud.meta.client.Client;
import systems.reformcloud.meta.info.ProxyInfo;
import systems.reformcloud.meta.info.ServerInfo;
import systems.reformcloud.network.out.PacketOutExecuteClientCommand;
import systems.reformcloud.network.out.PacketOutExecuteCommand;
import systems.reformcloud.utility.screen.ScreenHandler;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 05.02.2019
 */

public final class DefaultScreenHandler extends ScreenHandler implements Serializable {

    private static final long serialVersionUID = -908658424519877940L;

    private final String name;

    private final String type;

    public DefaultScreenHandler(final String client, final String type, final String name) {
        super(client);
        this.name = name;
        this.type = type;
    }

    @Override
    public void sendLine(String in) {
        ReformCloudController.getInstance().getColouredConsoleProvider()
            .coloured("[§eSCREEN §3" + name + "§r/§3" + type.toUpperCase() + "§r] " + in);
    }

    @Override
    public void executeCommand(String cmd) {
        switch (type.toLowerCase()) {
            case "proxy": {
                final ProxyInfo proxyInfo = ReformCloudController.getInstance()
                    .getInternalCloudNetwork()
                    .getServerProcessManager()
                    .getRegisteredProxyByName(name);
                if (proxyInfo == null) {
                    return;
                }

                ReformCloudController.getInstance().getChannelHandler()
                    .sendPacketAsynchronous(proxyInfo.getCloudProcess().getClient(),
                        new PacketOutExecuteCommand(cmd, "proxy", name));
                break;
            }
            case "server": {
                final ServerInfo serverInfo = ReformCloudController.getInstance()
                    .getInternalCloudNetwork()
                    .getServerProcessManager()
                    .getRegisteredServerByName(name);
                if (serverInfo == null) {
                    return;
                }

                ReformCloudController.getInstance().getChannelHandler()
                    .sendPacketAsynchronous(serverInfo.getCloudProcess().getClient(),
                        new PacketOutExecuteCommand(cmd, "server", name));
                break;
            }
            case "client": {
                final Client client = ReformCloudController.getInstance().getInternalCloudNetwork()
                    .getClients().get(name);
                if (client == null || client.getClientInfo() == null) {
                    return;
                }

                ReformCloudController.getInstance().getChannelHandler()
                    .sendPacketAsynchronous(client.getName(),
                        new PacketOutExecuteClientCommand(cmd)
                    );
            }
            default:
                break;
        }
    }
}
