/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.utility.time;

import systems.reformcloud.ReformCloudController;
import systems.reformcloud.ReformCloudLibraryService;
import systems.reformcloud.network.sync.out.PacketOutSyncControllerTime;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

/**
 * @author _Klaro | Pasqual K. / created on 10.02.2019
 */

public final class TimeSync implements Serializable, Runnable {
    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            ReformCloudController.getInstance().getLoggerProvider().setControllerTime(System.currentTimeMillis());
            ReformCloudController.getInstance().getChannelHandler().sendToAllSynchronized(new PacketOutSyncControllerTime());

            ReformCloudLibraryService.sleep(TimeUnit.SECONDS, 1);
        }
    }
}
