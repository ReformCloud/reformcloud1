/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.serverprocess.shutdown;

import systems.reformcloud.ReformCloudClient;
import systems.reformcloud.meta.startup.stages.ProcessStartupStage;
import systems.reformcloud.serverprocess.screen.CloudProcessScreenService;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 04.02.2019
 */

public final class ShutdownWorker implements Serializable, Runnable {
    private static final long serialVersionUID = 8124418130754401586L;

    @Override
    public void run() {
        final CloudProcessScreenService cloudProcessScreenService = ReformCloudClient.getInstance().getCloudProcessScreenService();

        cloudProcessScreenService.getRegisteredServerProcesses().forEach(handler -> {
            if (handler.getProcessStartupStage().equals(ProcessStartupStage.DONE) && !handler.isAlive())
                handler.shutdown(true);
        });

        cloudProcessScreenService.getRegisteredProxyProcesses().forEach(handler -> {
            if (handler.getProcessStartupStage().equals(ProcessStartupStage.DONE) && !handler.isAlive())
                handler.shutdown(null, true);
        });
    }
}
