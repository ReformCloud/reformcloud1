/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.packets.in;

import systems.reformcloud.ReformCloudClient;
import systems.reformcloud.ReformCloudLibraryService;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.meta.enums.ServerModeType;
import systems.reformcloud.meta.info.ServerInfo;
import systems.reformcloud.network.interfaces.NetworkInboundHandler;
import systems.reformcloud.serverprocess.startup.CloudServerStartupHandler;
import systems.reformcloud.serverprocess.startup.ProxyStartupHandler;
import systems.reformcloud.utility.files.FileUtils;

import java.nio.file.Paths;

/**
 * @author _Klaro | Pasqual K. / created on 16.12.2018
 */

public final class PacketInCopyServerIntoTemplate implements NetworkInboundHandler {
    @Override
    public void handle(Configuration configuration) {
        switch (configuration.getStringValue("type").toLowerCase()) {
            case "server": {
                ServerInfo serverInfo = ReformCloudClient.getInstance().getInternalCloudNetwork()
                        .getServerProcessManager().getRegisteredServerByName(configuration.getStringValue("name"));
                CloudServerStartupHandler cloudServerStartupHandler = ReformCloudClient.getInstance()
                        .getCloudProcessScreenService().getRegisteredServerHandler(configuration.getStringValue("serverName"));
                if (serverInfo.getServerGroup().getServerModeType().equals(ServerModeType.DYNAMIC)
                        || serverInfo.getServerGroup().getServerModeType().equals(ServerModeType.LOBBY)) {
                    cloudServerStartupHandler.executeCommand("save-all");
                    ReformCloudLibraryService.sleep(2000);
                    FileUtils.deleteFullDirectory(Paths.get("reformcloud/templates/servers/" + configuration.getStringValue("group") + "/" +
                            cloudServerStartupHandler.getLoaded().getName()));
                    FileUtils.createDirectory(Paths.get("reformcloud/templates/servers/" + configuration.getStringValue("group") + "/" +
                            cloudServerStartupHandler.getLoaded().getName()));
                    FileUtils.copyAllFiles(Paths.get("reformcloud/temp/servers/" + configuration.getStringValue("name")),
                            "reformcloud/templates/servers/" + configuration.getStringValue("group") + "/" +
                                    cloudServerStartupHandler.getLoaded().getName(), "spigot.jar");
                } else {
                    cloudServerStartupHandler.executeCommand("save-all");
                    ReformCloudLibraryService.sleep(2000);
                    FileUtils.deleteFullDirectory(Paths.get("reformcloud/templates/servers/" + configuration.getStringValue("group") + "/" +
                            cloudServerStartupHandler.getLoaded().getName()));
                    FileUtils.createDirectory(Paths.get("reformcloud/templates/servers/" + configuration.getStringValue("group") + "/" +
                            cloudServerStartupHandler.getLoaded().getName()));
                    FileUtils.copyAllFiles(Paths.get("reformcloud/static/servers/" + configuration.getStringValue("name")),
                            "reformcloud/templates/servers/" + configuration.getStringValue("group") + "/" +
                                    cloudServerStartupHandler.getLoaded().getName(), "spigot.jar");
                }

                break;
            }
            case "proxy": {
                ProxyStartupHandler proxyStartupHandler = ReformCloudClient.getInstance()
                        .getCloudProcessScreenService().getRegisteredProxyHandler(configuration.getStringValue("serverName"));
                FileUtils.copyAllFiles(Paths.get("reformcloud/temp/proxies/" + configuration.getStringValue("name")),
                        "reformcloud/templates/proxies/" + configuration.getStringValue("group") + "/" +
                                proxyStartupHandler.getTemplate().getName(), "BungeeCord.jar");
                break;
            }
            default:
                break;
        }
    }
}
