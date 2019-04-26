/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.cache;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.CacheStats;
import com.google.common.cache.LoadingCache;

import javax.annotation.ParametersAreNonnullByDefault;
import java.io.Serializable;
import java.time.Duration;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

/**
 * @author _Klaro | Pasqual K. / created on 03.04.2019
 */

public final class Cache<K, V> implements Serializable {
    /**
     * The cache which is used
     */
    private LoadingCache<K, V> cache;

    /**
     * Creates a new instance of the cache class
     *
     * @param maxSize The maximum size of the cache
     */
    public Cache(long maxSize) {
        this.cache = CacheBuilder
                .newBuilder()
                .maximumSize(maxSize)
                .expireAfterWrite(Duration.ofMinutes(15))
                .recordStats()
                .build(new CacheLoader<K, V>() {
                    @Override
                    @ParametersAreNonnullByDefault
                    public V load(K k) throws Exception {
                        return get(k);
                    }
                });
    }

    /**
     * Gets a value out of the cache
     *
     * @param k                     The key of the cached item
     * @return The cached item or {@code null} when the cache don't contains the item
     * @throws ExecutionException   This exception will be thrown when any exception occurs while loading out of the cache
     */
    public V get(K k) throws ExecutionException {
        return cache.get(k);
    }

    /**
     * Removes an item out of the cache
     *
     * @param k     The key of the item which should be invalided
     */
    public void invalidate(K k) {
        this.cache.invalidate(k);
    }

    /**
     * Invalidates all keys of the cache
     */
    public void invalidateAll() {
        this.cache.invalidateAll();
    }

    /**
     * Puts an item into the cache, will be invalidated after 15 minutes
     *
     * @param k     The key of the value
     * @param v     The value which should be added to the cache
     */
    public void add(K k, V v) {
        this.cache.put(k, v);
    }

    /**
     * Checks if an item is located in the cache
     *
     * @param k     The key of the item
     * @return If the cache contains the item or not
     */
    public boolean contains(K k) {
        return this.cache.asMap().containsKey(k);
    }

    /**
     * Gets an item as an optional out of the cache
     *
     * @param k     The key of the item
     * @return An optional containing the item or an empty optional
     */
    public Optional<V> getSave(K k) {
        try {
            return Optional.ofNullable(this.get(k));
        } catch (ExecutionException e) {
            return Optional.empty();
        }
    }

    /**
     * Gets the cache as map
     *
     * @return An map of all items which are in the cache
     */
    public Map<K, V> asMap() {
        return this.cache.asMap();
    }

    /**
     * Gets the stats of the cache
     *
     * @return The created stats of the cache
     */
    public CacheStats getStats() {
        return cache.stats();
    }
}
