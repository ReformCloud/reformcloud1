/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.serverprocess.shutdown;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import systems.reformcloud.ReformCloudClient;
import systems.reformcloud.meta.startup.stages.ProcessStartupStage;
import systems.reformcloud.serverprocess.screen.CloudProcessScreenService;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

/**
 * @author _Klaro | Pasqual K. / created on 04.02.2019
 */

public final class ShutdownWorker implements Serializable, Job {
    private static final long serialVersionUID = 8124418130754401586L;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
        if (!ReformCloudClient.RUNNING || ReformCloudClient.getInstance().isShutdown())
            return;

        final CloudProcessScreenService cloudProcessScreenService = ReformCloudClient.getInstance().getCloudProcessScreenService();

        cloudProcessScreenService.getRegisteredServerProcesses().forEach(handler -> {
            if (handler.getProcessStartupStage().equals(ProcessStartupStage.DONE) && !handler.isAlive() && !handler.isToShutdown()) {
                long shutdown = handler.getStartupTime() + TimeUnit.MINUTES.toMillis(2);
                if (shutdown <= System.currentTimeMillis()) {
                    handler.shutdown(true);
                }
            }
        });

        cloudProcessScreenService.getRegisteredProxyProcesses().forEach(handler -> {
            if (handler.getProcessStartupStage().equals(ProcessStartupStage.DONE) && !handler.isAlive() && !handler.isToShutdown())
                handler.shutdown(null, true);
        });
    }
}
