/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.cache;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.CacheStats;
import com.google.common.cache.LoadingCache;

import java.io.Serializable;
import java.time.Duration;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

/**
 * @author _Klaro | Pasqual K. / created on 03.04.2019
 */

public final class Cache<K, V> implements Serializable {
    private LoadingCache<K, V> cache;

    public Cache(long maxSize) {
        this.cache = CacheBuilder
                .newBuilder()
                .maximumSize(maxSize)
                .expireAfterWrite(Duration.ofMinutes(15))
                .recordStats()
                .build(new CacheLoader<K, V>() {
                    @Override
                    public V load(K k) throws Exception {
                        return get(k);
                    }
                });
    }

    public V get(K k) throws ExecutionException {
        return cache.get(k);
    }

    public void invalidate(K k) {
        this.cache.invalidate(k);
    }

    public void invalidateAll() {
        this.cache.invalidateAll();
    }

    public void add(K k, V v) {
        this.cache.put(k, v);
    }

    public boolean contains(K k) {
        return this.cache.asMap().containsKey(k);
    }

    public Optional<V> getSave(K k) {
        try {
            return Optional.ofNullable(this.get(k));
        } catch (ExecutionException e) {
            return Optional.empty();
        }
    }

    public Map<K, V> asMap() {
        return this.cache.asMap();
    }

    public CacheStats getStats() {
        return cache.stats();
    }
}
