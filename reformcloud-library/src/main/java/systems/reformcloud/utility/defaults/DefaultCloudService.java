/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.utility.defaults;

import systems.reformcloud.ReformCloudLibraryService;
import systems.reformcloud.api.APIService;
import systems.reformcloud.api.CloudService;
import systems.reformcloud.player.implementations.OnlinePlayer;
import systems.reformcloud.utility.Require;

import java.io.Serializable;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

/**
 * @author _Klaro | Pasqual K. / created on 21.04.2019
 */

public final class DefaultCloudService implements Serializable, CloudService {

    public DefaultCloudService(APIService instance) {
        Require.requireNotNull(instance);
        CloudService.instance.set(this);
        this.fallback = instance;
    }

    private APIService fallback;

    @Override
    public OnlinePlayer getCachedPlayer(String name) {
        if (APIService.instance.get() == null) {
            return fallback.getOnlinePlayer(name);
        } else {
            return APIService.instance.get().getOnlinePlayer(name);
        }
    }

    @Override
    public OnlinePlayer getCachedPlayer(UUID uniqueId) {
        if (APIService.instance.get() == null) {
            return fallback.getOnlinePlayer(uniqueId);
        } else {
            return APIService.instance.get().getOnlinePlayer(uniqueId);
        }
    }

    @Override
    public void schedule(Runnable runnable, int timeInMillis) {
        Runnable execute = () -> {
            runnable.run();
            ReformCloudLibraryService.sleep((long) timeInMillis);
        };
        ReformCloudLibraryService.EXECUTOR_SERVICE.execute(execute);
    }

    @Override
    public void patchAsync(Runnable runnable, BiConsumer<? super Void, ? super Throwable> andThen) {
        CompletableFuture
            .runAsync(runnable)
            .whenCompleteAsync(andThen);
    }
}
