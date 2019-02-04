/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.serverprocess;

import systems.reformcloud.ReformCloudClient;
import systems.reformcloud.ReformCloudLibraryService;
import systems.reformcloud.meta.startup.ProxyStartupInfo;
import systems.reformcloud.meta.startup.ServerStartupInfo;
import systems.reformcloud.netty.packets.out.PacketOutSendControllerConsoleMessage;
import systems.reformcloud.serverprocess.startup.CloudServerStartupHandler;
import systems.reformcloud.serverprocess.startup.ProxyStartupHandler;
import systems.reformcloud.utility.files.FileUtils;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * @author _Klaro | Pasqual K. / created on 29.10.2018
 */

public class CloudProcessStartupHandler implements Runnable {
    private final Queue<ServerStartupInfo> serverStartupInfo = new ConcurrentLinkedDeque<>();
    private final Queue<ProxyStartupInfo> proxyStartupInfo = new ConcurrentLinkedDeque<>();

    /**
     * Offers a serverProcess
     *
     * @param serverStartupInfo
     */
    public void offerServerProcess(ServerStartupInfo serverStartupInfo) {
        this.serverStartupInfo.add(serverStartupInfo);
    }

    /**
     * Offers a proxyProcess
     *
     * @param proxyStartupInfo
     */
    public void offerProxyProcess(ProxyStartupInfo proxyStartupInfo) {
        this.proxyStartupInfo.add(proxyStartupInfo);
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            while (ReformCloudClient.RUNNING) {
                ReformCloudLibraryService.sleep(Thread.currentThread(), 750L);

                while (!this.serverStartupInfo.isEmpty()) {
                    final ServerStartupInfo serverStartupInfo = this.serverStartupInfo.poll();

                    if (!Files.exists(Paths.get( "reformcloud/templates/" + serverStartupInfo.getServerGroup().getName())))
                        FileUtils.createDirectory(Paths.get("reformcloud/templates/" + serverStartupInfo.getServerGroup().getName() + "/plugins"));

                    if (!ReformCloudClient.getInstance().getInternalCloudNetwork().getServerProcessManager().isNameServerProcessRegistered(serverStartupInfo.getName())
                            && (ReformCloudClient.getInstance().getMemory() + serverStartupInfo.getServerGroup().getMemory()) <
                            ReformCloudClient.getInstance().getCloudConfiguration().getMemory() && (ReformCloudClient.getInstance().getCloudConfiguration().getCpu() == 0D || ReformCloudLibraryService.cpuUsage() <= ReformCloudClient.getInstance().getCloudConfiguration().getCpu())) {
                        this.send(ReformCloudClient.getInstance().getInternalCloudNetwork().getLoaded().getClient_wait_start()
                                .replace("%name%", serverStartupInfo.getName())
                                .replace("%uid%", String.valueOf(serverStartupInfo.getUid()))
                                .replace("%group%", serverStartupInfo.getServerGroup().getName())
                                .replace("%type%", "CloudServer"));

                        if (!new CloudServerStartupHandler(serverStartupInfo).bootstrap())
                            this.serverStartupInfo.add(serverStartupInfo);
                    } else
                        this.serverStartupInfo.add(serverStartupInfo);
                }

                while (!this.proxyStartupInfo.isEmpty()) {
                    final ProxyStartupInfo proxyStartupInfo = this.proxyStartupInfo.poll();

                    if (!Files.exists(Paths.get("reformcloud/templates/" + proxyStartupInfo.getProxyGroup().getName())))
                        FileUtils.createDirectory(Paths.get("reformcloud/templates/" + proxyStartupInfo.getProxyGroup().getName() + "/plugins"));

                    if (!ReformCloudClient.getInstance().getInternalCloudNetwork().getServerProcessManager().isNameProxyProcessRegistered(proxyStartupInfo.getName())
                            && (ReformCloudClient.getInstance().getMemory() + proxyStartupInfo.getProxyGroup().getMemory()) <
                            ReformCloudClient.getInstance().getCloudConfiguration().getMemory() && (ReformCloudClient.getInstance().getCloudConfiguration().getCpu() == 0D || ReformCloudLibraryService.cpuUsage() <= ReformCloudClient.getInstance().getCloudConfiguration().getCpu())) {
                        this.send(ReformCloudClient.getInstance().getInternalCloudNetwork().getLoaded().getClient_wait_start()
                                .replace("%name%", proxyStartupInfo.getName())
                                .replace("%uid%", String.valueOf(proxyStartupInfo.getUid()))
                                .replace("%group%", proxyStartupInfo.getProxyGroup().getName())
                                .replace("%type%", "Proxy"));

                        if (!new ProxyStartupHandler(proxyStartupInfo).bootstrap())
                            this.proxyStartupInfo.add(proxyStartupInfo);
                    } else {
                        this.proxyStartupInfo.add(proxyStartupInfo);
                    }
                }
            }
        }
    }

    /**
     * Sends a message to ReformCloudController and to Client Console
     *
     * @param message
     * @see PacketOutSendControllerConsoleMessage
     */
    private void send(String message) {
        ReformCloudClient.getInstance().getLoggerProvider().info(message);
    }
}
