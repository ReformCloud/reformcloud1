/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.database;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import systems.reformcloud.ReformCloudController;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 28.03.2019
 */

public final class DatabaseSaver implements Serializable, Job {
    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
        ReformCloudController.getInstance().getDatabaseProviders().forEach(databaseProvider -> databaseProvider.save());
        ReformCloudController.getInstance().getLoggerProvider().info("Task-1 success");
    }
}
