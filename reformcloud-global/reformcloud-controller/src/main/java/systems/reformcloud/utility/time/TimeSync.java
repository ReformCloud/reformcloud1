/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.utility.time;

import systems.reformcloud.ReformCloudController;
import systems.reformcloud.netty.sync.out.PacketOutSyncControllerTime;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 10.02.2019
 */

public final class TimeSync implements Serializable, Runnable {
    @Override
    public void run() {
        ReformCloudController.getInstance().getChannelHandler().sendToAllAsynchronous(new PacketOutSyncControllerTime());
        ReformCloudController.getInstance().getLoggerProvider().err("testTime");
    }
}
