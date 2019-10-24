/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network;

import systems.reformcloud.ReformCloudLibraryService;
import systems.reformcloud.ReformCloudLibraryServiceProvider;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.network.interfaces.NetworkInboundHandler;
import systems.reformcloud.network.interfaces.NetworkQueryInboundHandler;
import systems.reformcloud.network.packet.DefaultPacket;
import systems.reformcloud.utility.StringUtil;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

/**
 * @author _Klaro | Pasqual K. / created on 18.10.2018
 */

public final class NettyHandler implements Serializable {

    /**
     * All registered network handlers
     */
    private Map<String, NetworkInboundHandler> networkInboundHandlerMap = ReformCloudLibraryService
        .concurrentHashMap();

    /**
     * All registered network query handlers
     */
    private Map<String, NetworkQueryInboundHandler> networkQueryInboundHandlerMap = ReformCloudLibraryService
        .concurrentHashMap();

    /**
     * Handles an incoming packet
     *
     * @param type The type of the incoming packet
     * @param configuration The configuration of the incoming packet
     * @return If a handler for the packet could be found
     */
    public boolean handle(long channel, String type,
        Configuration configuration) {
        if (this.networkInboundHandlerMap.containsKey(type)) {
            NetworkInboundHandler networkInboundHandler =
                this.networkInboundHandlerMap.get(type);
            if (networkInboundHandler.handlingChannel() == channel) {
                networkInboundHandler.handle(configuration);
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * Handles an incoming packet
     *
     * @param packet The packet which was sent by the other network participant
     * @return If a handler for the packet could be found
     */
    public boolean handle(DefaultPacket packet) {
        return this.handle(packet.getChannel(), packet.getType(),
            packet.getConfiguration());
    }

    /**
     * Clears all Handlers
     */
    public void clearHandlers() {
        this.networkInboundHandlerMap.clear();
    }

    /**
     * Get a registered network handler
     *
     * @param type The type of the handler which should be found
     * @return The registered handler or {@code null} if the handler could not be found
     */
    public NetworkInboundHandler getHandler(String type) {
        return networkInboundHandlerMap.getOrDefault(type, null);
    }

    /**
     * Registers a new network handler
     *
     * @param type The type of the packet the handler should handle
     * @param networkInboundHandler The handler which should be registered
     * @return The current instance of this class
     */
    public NettyHandler registerHandler(String type, NetworkInboundHandler networkInboundHandler) {
        this.networkInboundHandlerMap.put(type, networkInboundHandler);
        return this;
    }

    /**
     * Registers a new network query handler
     *
     * @param type The type of the query packet the handler should handler
     * @param networkQueryInboundHandler The handler which should be registered
     * @return The current instance of this class
     */
    public NettyHandler registerQueryHandler(String type,
        NetworkQueryInboundHandler networkQueryInboundHandler) {
        this.networkQueryInboundHandlerMap.put(type, networkQueryInboundHandler);
        return this;
    }

    /**
     * Registers a new network handler
     *
     * @param type The type of the packet the handler should handler
     * @param clazz The class of the network handler
     * @return The current instance of this class
     */
    public NettyHandler registerHandler(String type, Class<? extends NetworkInboundHandler> clazz) {
        try {
            this.networkInboundHandlerMap.put(type, clazz.newInstance());
        } catch (final InstantiationException | IllegalAccessException ex) {
            StringUtil.printError(
                ReformCloudLibraryServiceProvider.getInstance().getColouredConsoleProvider(),
                "Error while registering network handler", ex
            );
        }

        return this;
    }

    /**
     * Registers a new network query handler
     *
     * @param type The type of the query packet the handler should handler
     * @param clazz The class of the network handler
     * @return The current instance of this class
     */
    public NettyHandler registerQueryHandler(String type,
        Class<? extends NetworkQueryInboundHandler> clazz) {
        try {
            this.networkQueryInboundHandlerMap.put(type, clazz.newInstance());
        } catch (final InstantiationException | IllegalAccessException ex) {
            StringUtil.printError(
                ReformCloudLibraryServiceProvider.getInstance().getColouredConsoleProvider(),
                "Error while registering query network handler", ex
            );
        }

        return this;
    }

    /**
     * Get if a handler is registered
     *
     * @param type The type of the query packet which should be handled
     * @return If a handler is registered
     */
    public boolean isQueryHandlerRegistered(String type) {
        return this.networkQueryInboundHandlerMap.containsKey(type);
    }

    /**
     * Get if a handler is registered
     *
     * @param type The type of the packet which should be handled
     * @return If a handler is registered
     */
    public boolean isHandlerRegisterd(String type) {
        return this.networkInboundHandlerMap.containsKey(type);
    }


    /**
     * Get a registered query handler
     *
     * @param type The type of the query handler which should be found
     * @return The registered handler or {@code null} if the handler isn't registered
     */
    public NetworkQueryInboundHandler getQueryHandler(String type) {
        return this.networkQueryInboundHandlerMap.getOrDefault(type, null);
    }

    /**
     * Unregisters a specific network handler
     *
     * @param type The type of the handler which should be unregistered
     * @return The current instance of this class
     */
    public NettyHandler unregisterHandler(String type) {
        this.networkInboundHandlerMap.remove(type);
        return this;
    }

    /**
     * Unregisters a specific network query handler
     *
     * @param type The type of the query handler which should be unregistered
     * @return The current instance of this class
     */
    public NettyHandler unregisterQueryHandler(String type) {
        this.networkQueryInboundHandlerMap.remove(type);
        return this;
    }

    /**
     * Get all registered network handlers
     *
     * @return A list containing all registered network handler types
     */
    public Set<String> getHandlers() {
        return this.networkInboundHandlerMap.keySet();
    }
}
