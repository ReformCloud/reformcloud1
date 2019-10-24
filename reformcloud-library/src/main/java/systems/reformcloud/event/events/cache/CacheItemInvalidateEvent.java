/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.event.events.cache;

import systems.reformcloud.cache.Cache;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 10.08.2019
 */

public final class CacheItemInvalidateEvent<K> extends CacheEvent implements Serializable {

    public CacheItemInvalidateEvent(Cache cache, K item) {
        super(cache);
        this.item = item;
    }

    private final K item;

    public K getItem() {
        return item;
    }
}
