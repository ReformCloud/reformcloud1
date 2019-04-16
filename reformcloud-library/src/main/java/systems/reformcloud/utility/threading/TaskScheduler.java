/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.utility.threading;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import systems.reformcloud.ReformCloudLibraryService;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

/**
 * @author _Klaro | Pasqual K. / created on 28.03.2019
 */

public final class TaskScheduler implements Serializable {
    /**
     * The scheduler which is used
     */
    private Scheduler scheduler;

    /**
     * Creates a new scheduler instance
     */
    public TaskScheduler() {
        try {
            this.scheduler = new StdSchedulerFactory().getScheduler();
            this.scheduler.start();
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    /**
     * Schedules a job
     *
     * @param clazz    The class of the job which should be scheduled
     * @param timeUnit The time unit in which the job should be executed
     * @param repeat   The repeat time in the given time unit
     * @return The current instance of this class
     */
    public TaskScheduler schedule(Class<? extends Job> clazz, TimeUnit timeUnit, long repeat) {
        try {
            if (scheduler.isInStandbyMode())
                scheduler.start();
        } catch (final SchedulerException ignored) {
        }

        long id = ReformCloudLibraryService.THREAD_LOCAL_RANDOM.nextLong();
        JobDetail jobDetail = JobBuilder.newJob(clazz).withIdentity("ReformCloud-Trigger-Default-" + id).build();
        Trigger trigger = TriggerBuilder
                .newTrigger()
                .withIdentity("ReformCloud-Trigger-Default-" + id)
                .withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInMilliseconds(timeUnit.toMillis(repeat)).repeatForever())
                .build();

        try {
            this.scheduler.scheduleJob(jobDetail, trigger);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }

        return this;
    }

    /**
     * Schedules a job
     *
     * @param clazz         The class of the job which should be scheduled
     * @param repeat        The repeat time in milliseconds
     * @return The current instance of this class
     */
    public TaskScheduler schedule(Class<? extends Job> clazz, long repeat) {
        try {
            if (scheduler.isInStandbyMode())
                scheduler.start();
        } catch (final SchedulerException ignored) {
        }

        long id = ReformCloudLibraryService.THREAD_LOCAL_RANDOM.nextLong();
        JobDetail jobDetail = JobBuilder.newJob(clazz).withIdentity("ReformCloud-Trigger-Default-" + id).build();
        Trigger trigger = TriggerBuilder
                .newTrigger()
                .withIdentity("ReformCloud-Trigger-Default-" + id)
                .withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInMilliseconds(repeat).repeatForever())
                .build();

        try {
            this.scheduler.scheduleJob(jobDetail, trigger);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }

        return this;
    }
}
