/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.serverprocess.screen;

import systems.reformcloud.ReformCloudLibraryService;
import systems.reformcloud.serverprocess.startup.CloudServerStartupHandler;
import systems.reformcloud.serverprocess.startup.ProxyStartupHandler;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author _Klaro | Pasqual K. / created on 30.10.2018
 */

public class CloudProcessScreenService implements Runnable {

    private Map<String, ProxyStartupHandler> proxyStartupHandlerMap = ReformCloudLibraryService
        .concurrentHashMap();

    private Map<String, CloudServerStartupHandler> cloudServerStartupHandlerMap = ReformCloudLibraryService
        .concurrentHashMap();

    /**
     * Registers a ProxyProcess
     */
    public void registerProxyProcess(final String name,
        final ProxyStartupHandler proxyStartupHandler) {
        this.proxyStartupHandlerMap.put(name, proxyStartupHandler);
    }

    /**
     * Registers a ServerProcess
     */
    public void registerServerProcess(final String name,
        final CloudServerStartupHandler cloudServerStartupHandler) {
        this.cloudServerStartupHandlerMap.put(name, cloudServerStartupHandler);
    }

    /**
     * Unregisters a ProxyProcess
     */
    public void unregisterProxyProcess(final String name) {
        this.proxyStartupHandlerMap.remove(name);
    }

    /**
     * Unregisters a ServerProcess
     */
    public void unregisterServerProcess(final String name) {
        this.cloudServerStartupHandlerMap.remove(name);
    }

    /**
     * Get all CloudServerStartupHandlers
     *
     * @return a list of all registered CloudServerStartupHandlers
     */
    public List<CloudServerStartupHandler> getRegisteredServerProcesses() {
        return new ArrayList<>(this.cloudServerStartupHandlerMap.values());
    }

    /**
     * Get all ProxyStartupHandlers
     *
     * @return a list of all registered ProxyStartupHandlers
     */
    public List<ProxyStartupHandler> getRegisteredProxyProcesses() {
        return new ArrayList<>(this.proxyStartupHandlerMap.values());
    }

    /**
     * Gets a specific handler
     *
     * @return {@link ProxyStartupHandler}
     */
    public ProxyStartupHandler getRegisteredProxyHandler(final String name) {
        return this.proxyStartupHandlerMap.getOrDefault(name, null);
    }

    /**
     * Gets a specific handler
     *
     * @return {@link CloudServerStartupHandler}
     */
    public CloudServerStartupHandler getRegisteredServerHandler(final String name) {
        return this.cloudServerStartupHandlerMap.getOrDefault(name, null);
    }

    private final StringBuffer stringBuffer = new StringBuffer();

    private final byte[] buffer = new byte[1024];

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            for (CloudServerStartupHandler cloudServerStartupHandler : this.cloudServerStartupHandlerMap
                .values()) {
                this.readLog(cloudServerStartupHandler);
            }

            for (ProxyStartupHandler proxyStartupHandler : this.proxyStartupHandlerMap.values()) {
                this.readLog(proxyStartupHandler);
            }

            ReformCloudLibraryService.sleep(500);
        }
    }

    private synchronized void readLog(final CloudServerStartupHandler cloudServerStartupHandler) {
        if (!cloudServerStartupHandler.isAlive()
            || cloudServerStartupHandler.getProcess().getInputStream() == null) {
            return;
        }

        InputStream inputStream = cloudServerStartupHandler.getProcess().getInputStream();

        try {
            int len;
            while (inputStream.available() > 0
                && (len = inputStream.read(buffer, 0, buffer.length)) != -1) {
                stringBuffer.append(new String(buffer, 0, len, StandardCharsets.UTF_8));
            }

            String stringText = stringBuffer.toString();
            if (!stringText.contains("\n") && !stringText.contains("\r")) {
                stringBuffer.setLength(0);
                return;
            }

            for (String input : stringText.split("\r")) {
                for (String text : input.split("\n")) {
                    if (!text.trim().isEmpty()) {
                        cloudServerStartupHandler.getScreenHandler().addScreenLine(text);
                    }
                }
            }

            stringBuffer.setLength(0);
        } catch (final Throwable ignored) {
            stringBuffer.setLength(0);
        }
    }

    private synchronized void readLog(final ProxyStartupHandler proxyStartupHandler) {
        if (!proxyStartupHandler.isAlive()
            || proxyStartupHandler.getProcess().getInputStream() == null) {
            return;
        }

        InputStream inputStream = proxyStartupHandler.getProcess().getInputStream();

        try {
            int len;
            while (inputStream.available() > 0
                && (len = inputStream.read(buffer, 0, buffer.length)) != -1) {
                stringBuffer.append(new String(buffer, 0, len, StandardCharsets.UTF_8));
            }

            String stringText = stringBuffer.toString();
            if (!stringText.contains("\n") && !stringText.contains("\r")) {
                return;
            }

            for (String input : stringText.split("\r")) {
                for (String text : input.split("\n")) {
                    proxyStartupHandler.getScreenHandler().addScreenLine(text);
                }
            }

            stringBuffer.setLength(0);
        } catch (final Throwable ignored) {
            stringBuffer.setLength(0);
        }
    }
}
