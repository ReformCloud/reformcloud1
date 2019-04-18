/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.test;

import org.junit.Test;
import systems.reformcloud.ReformCloudLibraryService;
import systems.reformcloud.cache.Cache;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 15.04.2019
 */

public final class CacheTest implements Serializable {
    @Test
    public void cacheTest() {
        Cache<String, String> cache = ReformCloudLibraryService.newCache(5);
        for (short i = 0; i != 7; i++) {
            cache.add(Short.toString(i), Short.toString(i));
            System.out.println("CurrentSize: " + cache.asMap().size() + " Added number: " + i);
        }

        System.out.println("Final size: " + cache.asMap().size());

        if (cache.asMap().size() != 5)
            throw new IllegalStateException("Cache test failed, too many items in cache");
        else
            System.out.println("Test successful, removed all expected items");
    }
}
