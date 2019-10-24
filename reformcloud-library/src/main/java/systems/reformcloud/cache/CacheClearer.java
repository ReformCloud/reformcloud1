/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.cache;

import systems.reformcloud.ReformCloudLibraryService;
import systems.reformcloud.ReformCloudLibraryServiceProvider;
import systems.reformcloud.event.events.cache.CacheClearerCacheRegisteredEvent;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author _Klaro | Pasqual K. / created on 27.04.2019
 */

public final class CacheClearer implements Serializable {

    private List<Cache> caches = new ArrayList<>();

    public CacheClearer() {
        ReformCloudLibraryService.EXECUTOR_SERVICE.execute(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                this.caches.forEach(Cache::invalidateAll);
                ReformCloudLibraryService.sleep(TimeUnit.MINUTES, 15);
            }
        });
    }

    void register(Cache cache) {
        ReformCloudLibraryServiceProvider.getInstance().getEventManager().fire(new CacheClearerCacheRegisteredEvent(cache, this));
        this.caches.add(cache);
    }
}
