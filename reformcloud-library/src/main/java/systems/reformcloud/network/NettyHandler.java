/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network;

import systems.reformcloud.ReformCloudLibraryService;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.network.channel.ChannelReader;
import systems.reformcloud.network.interfaces.NetworkInboundHandler;

import java.util.Map;
import java.util.Set;

/**
 * @author _Klaro | Pasqual K. / created on 18.10.2018
 */

public class NettyHandler {
    private Map<String, NetworkInboundHandler> networkInboundHandlerMap = ReformCloudLibraryService.concurrentHashMap();

    /**
     * Handel the incoming packet by calling the registered {@link NetworkInboundHandler}
     * default call by {@link ChannelReader}
     *
     * @param type
     * @param configuration
     * @return if the Handler is registered
     */
    public boolean handle(String type, Configuration configuration) {
        if (this.networkInboundHandlerMap.containsKey(type)) {
            this.networkInboundHandlerMap.get(type).handle(configuration);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Clears all Handlers
     */
    public void clearHandlers() {
        this.networkInboundHandlerMap.clear();
    }

    /**
     * Returns the registered {@link NetworkInboundHandler}
     *
     * @param type
     * @return the registered {@link NetworkInboundHandler} or null if the Handler isn't registered
     */
    public NetworkInboundHandler getHandler(String type) {
        return networkInboundHandlerMap.getOrDefault(type, null);
    }

    /**
     * Registers a new {@link NetworkInboundHandler}
     *
     * @param type
     * @param networkInboundHandler
     */
    public NettyHandler registerHandler(String type, NetworkInboundHandler networkInboundHandler) {
        this.networkInboundHandlerMap.put(type, networkInboundHandler);
        return this;
    }

    /**
     * Unregisters a specific {@link NetworkInboundHandler}
     *
     * @param type
     */
    public NettyHandler unregisterHandler(String type) {
        this.networkInboundHandlerMap.remove(type);
        return this;
    }

    /**
     * Get all names of all registered {@link NetworkInboundHandler}
     *
     * @return {@link Set<String>} with HandleName of all {@link NetworkInboundHandler}
     */
    public Set<String> getHandlers() {
        return this.networkInboundHandlerMap.keySet();
    }

    /**
     * Returns if a specific {@link NetworkInboundHandler} is registered by name
     *
     * @param name
     * @return if the handler is registered
     */
    public boolean isHandlerRegistered(final String name) {
        return this.networkInboundHandlerMap.containsKey(name);
    }
}
