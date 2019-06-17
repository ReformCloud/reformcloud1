/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.serverprocess;

import systems.reformcloud.ReformCloudClient;
import systems.reformcloud.ReformCloudLibraryService;
import systems.reformcloud.meta.startup.ProxyStartupInfo;
import systems.reformcloud.meta.startup.ServerStartupInfo;
import systems.reformcloud.network.packets.out.PacketOutSendControllerConsoleMessage;
import systems.reformcloud.serverprocess.startup.CloudServerStartupHandler;
import systems.reformcloud.serverprocess.startup.ProxyStartupHandler;
import systems.reformcloud.utility.files.FileUtils;

import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * @author _Klaro | Pasqual K. / created on 29.10.2018
 */

public final class CloudProcessStartupHandler implements Runnable, Serializable {

    private final Queue<ServerStartupInfo> serverStartupInfo = new ConcurrentLinkedDeque<>();

    private final Queue<ProxyStartupInfo> proxyStartupInfo = new ConcurrentLinkedDeque<>();

    /**
     * Offers a serverProcess
     */
    public void offerServerProcess(ServerStartupInfo serverStartupInfo) {
        this.serverStartupInfo.add(serverStartupInfo);
    }

    /**
     * Offers a proxyProcess
     */
    public void offerProxyProcess(ProxyStartupInfo proxyStartupInfo) {
        this.proxyStartupInfo.add(proxyStartupInfo);
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            while (ReformCloudClient.RUNNING) {
                if (!this.serverStartupInfo.isEmpty()) {
                    final ServerStartupInfo serverStartupInfo = this.serverStartupInfo.poll();

                    boolean firstStart = false;
                    if (!Files.exists(Paths.get(
                        "reformcloud/templates/servers/" + serverStartupInfo.getServerGroup()
                            .getName() + "/default"))) {
                        FileUtils.createDirectory(Paths.get(
                            "reformcloud/templates/servers/" + serverStartupInfo.getServerGroup()
                                .getName() + "/default/plugins"));
                        firstStart = true;
                    }

                    if (!ReformCloudClient.getInstance().getInternalCloudNetwork()
                        .getServerProcessManager()
                        .isNameServerProcessRegistered(serverStartupInfo.getName())
                        && (ReformCloudClient.getInstance().getMemory() + serverStartupInfo
                        .getServerGroup().getMemory()) <
                        ReformCloudClient.getInstance().getCloudConfiguration().getMemory()
                        && (ReformCloudClient.getInstance().getCloudConfiguration().getCpu() == 0D
                        || ReformCloudLibraryService.cpuUsage() <= ReformCloudClient.getInstance()
                        .getCloudConfiguration().getCpu())) {
                        this.send(
                            ReformCloudClient.getInstance().getInternalCloudNetwork().getLoaded()
                                .getClient_wait_start()
                                .replace("%name%", serverStartupInfo.getName())
                                .replace("%uid%", String.valueOf(serverStartupInfo.getUid()))
                                .replace("%group%", serverStartupInfo.getServerGroup().getName())
                                .replace("%type%", "CloudServer"));

                        if (!new CloudServerStartupHandler(serverStartupInfo, firstStart)
                            .bootstrap()) {
                            this.serverStartupInfo.add(serverStartupInfo);
                        }
                    } else {
                        this.serverStartupInfo.add(serverStartupInfo);
                    }
                }

                if (!proxyStartupInfo.isEmpty()) {
                    final ProxyStartupInfo proxyStartupInfo = this.proxyStartupInfo.poll();

                    if (!Files.exists(Paths.get(
                        "reformcloud/templates/proxies/" + proxyStartupInfo.getProxyGroup()
                            .getName() + "/default"))) {
                        FileUtils.createDirectory(Paths.get(
                            "reformcloud/templates/proxies/" + proxyStartupInfo.getProxyGroup()
                                .getName() + "/default/plugins"));
                    }

                    if (!ReformCloudClient.getInstance().getInternalCloudNetwork()
                        .getServerProcessManager()
                        .isNameProxyProcessRegistered(proxyStartupInfo.getName())
                        && (ReformCloudClient.getInstance().getMemory() + proxyStartupInfo
                        .getProxyGroup().getMemory()) <
                        ReformCloudClient.getInstance().getCloudConfiguration().getMemory() && (
                        ReformCloudClient.getInstance().getCloudConfiguration().getCpu() == 0D
                            || ReformCloudLibraryService.cpuUsage() <= ReformCloudClient
                            .getInstance().getCloudConfiguration().getCpu())) {
                        this.send(
                            ReformCloudClient.getInstance().getInternalCloudNetwork().getLoaded()
                                .getClient_wait_start()
                                .replace("%name%", proxyStartupInfo.getName())
                                .replace("%uid%", String.valueOf(proxyStartupInfo.getUid()))
                                .replace("%group%", proxyStartupInfo.getProxyGroup().getName())
                                .replace("%type%", "Proxy"));

                        if (!new ProxyStartupHandler(proxyStartupInfo).bootstrap()) {
                            this.proxyStartupInfo.add(proxyStartupInfo);
                        }
                    } else {
                        this.proxyStartupInfo.add(proxyStartupInfo);
                    }
                }

                try {
                    Thread.sleep(500);
                } catch (final InterruptedException ignored) {
                }
            }
        }
    }

    /**
     * Sends a message to client console as {@code info}
     */
    private void send(String message) {
        ReformCloudClient.getInstance().getColouredConsoleProvider().info(message);
    }

    public void removeServerProcess(final String name) {
        this.serverStartupInfo.stream().filter(e -> e.getName().equals(name)).forEach(e -> {
            this.serverStartupInfo.remove(e);
            ReformCloudClient.getInstance().getChannelHandler()
                .sendPacketAsynchronous("ReformCloudController",
                    new PacketOutSendControllerConsoleMessage(
                        "ServerProcess §e" + e.getUid() + "§r was §cremoved§r out of the §3"
                            + ReformCloudClient.getInstance().getCloudConfiguration()
                            .getClientName() + "§r queue"));
        });
    }

    public void removeProxyProcess(final String name) {
        this.proxyStartupInfo.stream().filter(e -> e.getName().equals(name)).forEach(e -> {
            this.proxyStartupInfo.remove(e);
            ReformCloudClient.getInstance().getChannelHandler()
                .sendPacketAsynchronous("ReformCloudController",
                    new PacketOutSendControllerConsoleMessage(
                        "ProxyProcess §e" + e.getUid() + "§r was §cremoved§r out of the §3"
                            + ReformCloudClient.getInstance().getCloudConfiguration()
                            .getClientName() + "§r queue"));
        });
    }

    public Queue<ServerStartupInfo> getServerStartupInfo() {
        return this.serverStartupInfo;
    }

    public Queue<ProxyStartupInfo> getProxyStartupInfo() {
        return this.proxyStartupInfo;
    }
}
