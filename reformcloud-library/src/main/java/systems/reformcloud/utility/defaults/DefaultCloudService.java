/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.utility.defaults;

import systems.reformcloud.ReformCloudLibraryService;
import systems.reformcloud.api.IAPIService;
import systems.reformcloud.api.ICloudService;
import systems.reformcloud.player.implementations.OnlinePlayer;
import systems.reformcloud.utility.Require;

import java.io.Serializable;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

/**
 * @author _Klaro | Pasqual K. / created on 21.04.2019
 */

public final class DefaultCloudService implements Serializable, ICloudService {
    public DefaultCloudService(IAPIService instance) {
        Require.requireNotNull(instance);
        ICloudService.instance.set(this);
        this.fallback = instance;
    }

    private IAPIService fallback;

    @Override
    public OnlinePlayer getCachedPlayer(String name) {
        if (IAPIService.instance.get() == null)
            return fallback.getOnlinePlayer(name);
        else
            return IAPIService.instance.get().getOnlinePlayer(name);
    }

    @Override
    public OnlinePlayer getCachedPlayer(UUID uniqueId) {
        if (IAPIService.instance.get() == null)
            return fallback.getOnlinePlayer(uniqueId);
        else
            return IAPIService.instance.get().getOnlinePlayer(uniqueId);
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
