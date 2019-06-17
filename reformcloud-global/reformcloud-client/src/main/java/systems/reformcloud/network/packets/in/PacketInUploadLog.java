/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.packets.in;

import systems.reformcloud.ReformCloudClient;
import systems.reformcloud.ReformCloudLibraryServiceProvider;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.network.interfaces.NetworkInboundHandler;
import systems.reformcloud.network.packets.out.PacketOutGetLog;
import systems.reformcloud.serverprocess.startup.CloudServerStartupHandler;
import systems.reformcloud.serverprocess.startup.ProxyStartupHandler;
import systems.reformcloud.utility.StringUtil;

import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @author _Klaro | Pasqual K. / created on 29.01.2019
 */

public final class PacketInUploadLog implements Serializable, NetworkInboundHandler {

    private static final long serialVersionUID = 4582766821019976794L;

    @Override
    public void handle(Configuration configuration) {
        final String name = configuration.getStringValue("name");
        final String type = configuration.getStringValue("type");

        switch (type) {
            case "bungee": {
                ProxyStartupHandler proxyStartupHandler = ReformCloudClient.getInstance()
                    .getCloudProcessScreenService().getRegisteredProxyHandler(name);
                if (proxyStartupHandler != null) {
                    try {
                        String url = proxyStartupHandler.uploadLog();
                        ReformCloudClient.getInstance().getChannelHandler()
                            .sendPacketAsynchronous("ReformCloudController",
                                new PacketOutGetLog(url, name));
                    } catch (final IOException ex) {
                        StringUtil.printError(
                            ReformCloudLibraryServiceProvider.getInstance().getColouredConsoleProvider(),
                            "Could not upload log", ex);
                    }
                } else {
                    ReformCloudClient.getInstance().getChannelHandler()
                        .sendPacketAsynchronous("ReformCloudController",
                            new PacketOutGetLog(StringUtil.NULL, name));
                }
                break;
            }

            case "spigot": {
                CloudServerStartupHandler cloudServerStartupHandler = ReformCloudClient
                    .getInstance().getCloudProcessScreenService().getRegisteredServerHandler(name);
                if (cloudServerStartupHandler != null) {
                    try {
                        String url = cloudServerStartupHandler.uploadLog();
                        ReformCloudClient.getInstance().getChannelHandler()
                            .sendPacketAsynchronous("ReformCloudController",
                                new PacketOutGetLog(url, name));
                    } catch (final IOException ex) {
                        StringUtil.printError(
                            ReformCloudLibraryServiceProvider.getInstance().getColouredConsoleProvider(),
                            "Could not upload log", ex);
                    }
                } else {
                    ReformCloudClient.getInstance().getChannelHandler()
                        .sendPacketAsynchronous("ReformCloudController",
                            new PacketOutGetLog(StringUtil.NULL, name));
                }
                break;
            }

            case "client": {
                StringBuilder stringBuilder = new StringBuilder();
                try {
                    Files.readAllLines(Paths.get("reformcloud/logs/latest.0"),
                        StandardCharsets.UTF_8)
                        .forEach(s -> stringBuilder.append(s).append("\n"));
                } catch (final IOException ex) {
                    StringUtil.printError(
                        ReformCloudLibraryServiceProvider.getInstance().getColouredConsoleProvider(),
                        "Could not read log", ex);
                }

                final String url = ReformCloudClient.getInstance().getColouredConsoleProvider()
                    .uploadLog(stringBuilder.substring(0));
                ReformCloudClient.getInstance().getChannelHandler()
                    .sendPacketAsynchronous("ReformCloudController",
                        new PacketOutGetLog(url, name));
                break;
            }
        }
    }
}
