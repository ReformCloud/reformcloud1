/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.web.utils;

import lombok.Getter;
import systems.reformcloud.ReformCloudLibraryService;
import systems.reformcloud.utility.Require;

import java.util.Map;

/**
 * @author _Klaro | Pasqual K. / created on 24.11.2018
 */

public class WebHandlerAdapter {
    /**
     * The map of all web handler
     */
    @Getter
    private Map<String, WebHandler> webHandlerMap = ReformCloudLibraryService.concurrentHashMap();

    /**
     * Registers a web Handler
     *
     * @param path          The path where the web handler should be called from
     * @param webHandler    The web handler which should handle the requests
     * @return The current class instance
     */
    public WebHandlerAdapter registerHandler(final String path, final WebHandler webHandler) {
        Require.requireNotNull(path);
        Require.requireNotNull(webHandler);
        this.webHandlerMap.put(path, webHandler);
        return this;
    }

    /**
     * Unregisters a web Handler
     *
     * @param path      The path where the web handler is called from
     * @return The current class instance
     */
    public WebHandlerAdapter unregisterHandler(final String path) {
        Require.requireNotNull(path);
        this.webHandlerMap.remove(path);
        return this;
    }

    /**
     * Deletes all handler
     *
     * @return The current class instance
     */
    public WebHandlerAdapter clearHandlers() {
        this.webHandlerMap.clear();
        return this;
    }

    /**
     * Checks if a handler is registered
     *
     * @param path          The path where the web handler should be called from
     * @return if a handler id registered
     */
    public boolean isHandlerRegistered(final String path) {
        Require.requireNotNull(path);
        return this.webHandlerMap.containsKey(path);
    }

    /**
     * Gets a specific web Handler by the given name
     *
     * @param path          The path where the web handler should be called from
     * @return a specific web Handler by the given name
     */
    public WebHandler getHandler(final String path) {
        Require.requireNotNull(path);
        return this.webHandlerMap.getOrDefault(path, null);
    }
}
