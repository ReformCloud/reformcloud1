/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.cache;

import systems.reformcloud.ReformCloudLibraryService;
import systems.reformcloud.utility.annotiations.MayNotBePresent;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

/**
 * @author _Klaro | Pasqual K. / created on 03.04.2019
 */

public final class Cache<K, V> implements Serializable {

    private TreeMap<K, V> cache;

    private long maxSize;

    /**
     * Creates a new instance of the cache class
     *
     * @param maxSize The maximum size of the cache
     */
    public Cache(long maxSize) {
        this.maxSize = maxSize;
        this.cache = new TreeMap<>();
        ReformCloudLibraryService.CACHE_CLEARER.register(this);
    }

    /**
     * Gets a value out of the cache
     *
     * @param k The key of the cached item
     * @return The cached item or {@code null} when the cache don't contains the item
     */
    @MayNotBePresent
    private V get(K k) {
        return cache.get(k);
    }

    /**
     * Removes an item out of the cache
     *
     * @param k The key of the item which should be invalided
     */
    public void invalidate(K k) {
        this.cache.remove(k);
    }

    /**
     * Invalidates all keys of the cache
     */
    public void invalidateAll() {
        this.cache.clear();
    }

    /**
     * Puts an item into the cache, will be invalidated after 15 minutes
     *
     * @param k The key of the value
     * @param v The value which should be added to the cache
     */
    public void add(K k, V v) {
        this.cache.put(k, v);
        if (cache.size() >= maxSize) {
            while (this.cache.size() >= maxSize) {
                this.cache.remove(this.cache.lastKey());
            }
        }
    }

    /**
     * Checks if an item is located in the cache
     *
     * @param k The key of the item
     * @return If the cache contains the item or not
     */
    public boolean contains(K k) {
        return this.cache.containsKey(k);
    }

    /**
     * Gets an item as an optional out of the cache
     *
     * @param k The key of the item
     * @return An optional containing the item or an empty optional
     */
    public Optional<V> getSave(K k) {
        return Optional.ofNullable(get(k));
    }

    /**
     * Gets the cache as map
     *
     * @return An map of all items which are in the cache
     */
    public Map<K, V> asMap() {
        return new HashMap<>(this.cache);
    }

    /**
     * Get the max size of the cache
     *
     * @return The max size of the cache
     */
    public long getMaxSize() {
        return maxSize;
    }
}
