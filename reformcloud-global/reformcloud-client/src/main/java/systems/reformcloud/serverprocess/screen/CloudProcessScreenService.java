/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.serverprocess.screen;

import systems.reformcloud.ReformCloudLibraryService;
import systems.reformcloud.serverprocess.startup.CloudServerStartupHandler;
import systems.reformcloud.serverprocess.startup.ProxyStartupHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author _Klaro | Pasqual K. / created on 30.10.2018
 */

public class CloudProcessScreenService {
    private Map<String, ProxyStartupHandler> proxyStartupHandlerMap = ReformCloudLibraryService.concurrentHashMap();
    private Map<String, CloudServerStartupHandler> cloudServerStartupHandlerMap = ReformCloudLibraryService.concurrentHashMap();

    /**
     * Registers a ProxyProcess
     *
     * @param name
     * @param proxyStartupHandler
     */
    public void registerProxyProcess(final String name, final ProxyStartupHandler proxyStartupHandler) {
        this.proxyStartupHandlerMap.put(name, proxyStartupHandler);
    }

    /**
     * Registers a ServerProcess
     *
     * @param name
     * @param cloudServerStartupHandler
     */
    public void registerServerProcess(final String name, final CloudServerStartupHandler cloudServerStartupHandler) {
        this.cloudServerStartupHandlerMap.put(name, cloudServerStartupHandler);
    }

    /**
     * Unregisters a ProxyProcess
     *
     * @param name
     */
    public void unregisterProxyProcess(final String name) {
        this.proxyStartupHandlerMap.remove(name);
    }

    /**
     * Unregisters a ServerProcess
     *
     * @param name
     */
    public void unregisterServerProcess(final String name) {
        this.cloudServerStartupHandlerMap.remove(name);
    }

    /**
     * Get all CloudServerStartupHandlers
     *
     * @return a {@link List<CloudServerStartupHandler>} of all registered CloudServerStartupHandlers
     */
    public List<CloudServerStartupHandler> getRegisteredServerProcesses() {
        return new ArrayList<>(this.cloudServerStartupHandlerMap.values());
    }

    /**
     * Get all ProxyStartupHandlers
     *
     * @return a {@link List<ProxyStartupHandler>} of all registered ProxyStartupHandlers
     */
    public List<ProxyStartupHandler> getRegisteredProxyProcesses() {
        return new ArrayList<>(this.proxyStartupHandlerMap.values());
    }

    /**
     * Gets a specific handler
     *
     * @param name
     * @return {@link ProxyStartupHandler}
     */
    public ProxyStartupHandler getRegisteredProxyHandler(final String name) {
        return this.proxyStartupHandlerMap.getOrDefault(name, null);
    }

    /**
     * Gets a specific handler
     *
     * @param name
     * @return {@link CloudServerStartupHandler}
     */
    public CloudServerStartupHandler getRegisteredServerHandler(final String name) {
        return this.cloudServerStartupHandlerMap.getOrDefault(name, null);
    }
}
