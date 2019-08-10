/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.event.events.cache;

import systems.reformcloud.cache.Cache;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 10.08.2019
 */

public final class CacheItemPutEvent<K, V> extends CacheEvent implements Serializable {

    public CacheItemPutEvent(Cache cache, K k, V v) {
        super(cache);
        this.key = k;
        this.value = v;
    }

    private final K key;

    private final V value;

    public K getKey() {
        return key;
    }

    public V getValue() {
        return value;
    }
}
