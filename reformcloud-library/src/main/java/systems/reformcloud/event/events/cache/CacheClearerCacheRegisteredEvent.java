/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.event.events.cache;

import systems.reformcloud.cache.Cache;
import systems.reformcloud.cache.CacheClearer;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 10.08.2019
 */

public final class CacheClearerCacheRegisteredEvent extends CacheEvent implements Serializable {

    public CacheClearerCacheRegisteredEvent(Cache cache, CacheClearer cacheClearer) {
        super(cache);
        this.cacheClearer = cacheClearer;
    }

    private final CacheClearer cacheClearer;

    public CacheClearer getCacheClearer() {
        return cacheClearer;
    }
}
