/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.web.utils;

import lombok.Getter;
import systems.reformcloud.ReformCloudLibraryService;

import java.util.Map;

/**
 * @author _Klaro | Pasqual K. / created on 24.11.2018
 */

public class WebHandlerAdapter {
    @Getter
    private Map<String, WebHandler> webHandlerMap = ReformCloudLibraryService.concurrentHashMap();

    /**
     * Registers a web Handler
     *
     * @param path
     * @param webHandler
     * @return this
     */
    public WebHandlerAdapter registerHandler(final String path, final WebHandler webHandler) {
        this.webHandlerMap.put(path, webHandler);
        return this;
    }

    /**
     * Unregisters a web Handler
     *
     * @param path
     * @return this
     */
    public WebHandlerAdapter unregisterHandler(final String path) {
        this.webHandlerMap.remove(path);
        return this;
    }

    /**
     * Deletes all handler
     *
     * @return this
     */
    public WebHandlerAdapter clearHandlers() {
        this.webHandlerMap.clear();
        return this;
    }

    /**
     * Checks if a handler is registered
     *
     * @param path
     * @return if a handler id registered
     */
    public boolean isHandlerRegistered(final String path) {
        return this.webHandlerMap.containsKey(path);
    }

    /**
     * Gets a specific web Handler by the given name
     *
     * @param path
     * @return a specific web Handler by the given name
     */
    public WebHandler getHandler(final String path) {
        return this.webHandlerMap.getOrDefault(path, null);
    }
}
