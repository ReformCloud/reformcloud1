/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.event.events.cache;

import systems.reformcloud.cache.Cache;
import systems.reformcloud.event.utility.Event;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 10.08.2019
 */

public abstract class CacheEvent extends Event implements Serializable {

    public CacheEvent(Cache cache) {
        this.cache = cache;
    }

    private final Cache cache;

    public Cache getCache() {
        return cache;
    }
}
