/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.utility.threading;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

/**
 * @author _Klaro | Pasqual K. / created on 28.03.2019
 */

public final class TaskScheduler implements Serializable {
    private Scheduler scheduler;

    public TaskScheduler() {
        try {
            this.scheduler = new StdSchedulerFactory().getScheduler();
            this.scheduler.start();
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    public TaskScheduler schedule(Class<? extends Job> clazz, TimeUnit timeUnit, long repeat) {
        JobDetail jobDetail = JobBuilder.newJob(clazz).withIdentity("ReformCloud-Trigger-Default").build();
        Trigger trigger = TriggerBuilder
                .newTrigger()
                .withIdentity("ReformCloud-Trigger-Default")
                .withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInMilliseconds(timeUnit.toMillis(repeat)).repeatForever())
                .build();

        try {
            this.scheduler.scheduleJob(jobDetail, trigger);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }

        return this;
    }
}
