/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.utility.time;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import systems.reformcloud.ReformCloudController;
import systems.reformcloud.network.sync.out.PacketOutSyncControllerTime;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 10.02.2019
 */

public final class TimeSync implements Serializable, Job {
    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
        ReformCloudController.getInstance().getLoggerProvider().setControllerTime(System.currentTimeMillis());
        ReformCloudController.getInstance().getChannelHandler().sendToAllSynchronized(new PacketOutSyncControllerTime());
    }
}
