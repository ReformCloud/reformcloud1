/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.api;

import systems.reformcloud.player.implementations.OnlinePlayer;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiConsumer;

/**
 * @author _Klaro | Pasqual K. / created on 21.04.2019
 */

public interface ICloudService {
    AtomicReference<ICloudService> instance = new AtomicReference<>();

    /**
     * Get a cached online player
     *
     * @param name The name of the player
     * @return The cloud player which will be searched for
     */
    OnlinePlayer getCachedPlayer(String name);

    /**
     * Get a cached online player
     *
     * @param uniqueId The uuid of the player
     * @return The cloud player which will be searched for
     */
    OnlinePlayer getCachedPlayer(UUID uniqueId);

    /**
     * Schedules a specific task
     *
     * @param runnable     The runnable which should be run
     * @param timeInMillis The delay in milliseconds
     */
    void schedule(Runnable runnable, int timeInMillis);

    /**
     * Patches an action async and runs the when completed stage after
     *
     * @param runnable The runnable which should be executed
     * @param andThen  Will be called after completing the action
     */
    void patchAsync(Runnable runnable, BiConsumer<? super Void, ? super Throwable> andThen);
}
