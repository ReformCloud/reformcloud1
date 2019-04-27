/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.cache;

import systems.reformcloud.ReformCloudLibraryService;

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

    public void register(Cache cache) {
        this.caches.add(cache);
    }
}
