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

public interface CloudService {

    AtomicReference<CloudService> instance = new AtomicReference<>();

    /**
     * Get a cached online player
     *
     * @param arg1 The name of the player
     * @return The cloud player which will be searched for
     */
    OnlinePlayer getCachedPlayer(String arg1);

    /**
     * Get a cached online player
     *
     * @param arg1 The uuid of the player
     * @return The cloud player which will be searched for
     */
    OnlinePlayer getCachedPlayer(UUID arg1);

    /**
     * Schedules a specific task
     *
     * @param arg1 The runnable which should be run
     * @param arg2 The delay in milliseconds
     */
    void schedule(Runnable arg1, int arg2);

    /**
     * Patches an action async and runs the when completed stage after
     *
     * @param arg1 The runnable which should be executed
     * @param arg2 Will be called after completing the action
     */
    void patchAsync(Runnable arg1,
                    BiConsumer<? super Void, ? super Throwable> arg2);
}
