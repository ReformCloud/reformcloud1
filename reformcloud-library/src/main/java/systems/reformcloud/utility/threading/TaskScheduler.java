/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.utility.threading;

import systems.reformcloud.ReformCloudLibraryService;
import lombok.Getter;
import lombok.Setter;
import systems.reformcloud.ReformCloudLibraryServiceProvider;
import systems.reformcloud.utility.StringUtil;

import java.util.Collection;
import java.util.Deque;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author _Klaro | Pasqual K. / created on 19.10.2018
 */

@Getter
@Setter
public class TaskScheduler {
    private static final TaskScheduler TASK_SCHEDULER = new TaskScheduler(Runtime.getRuntime().availableProcessors());
    protected final ThreadGroup threadGroup = new ThreadGroup("TaskGroup-" + ReformCloudLibraryService.THREAD_LOCAL_RANDOM.nextLong());
    protected final AtomicLong threadId = new AtomicLong(0);
    protected final String name = threadGroup.getName();
    protected final long sleepThreadSwitch, threadLiveMillis;
    protected final boolean dynamicWorkerCount;
    protected int maxThreads;
    protected Deque<TaskEntry<?>> taskEntries = new ConcurrentLinkedDeque<>();
    protected Collection<Worker> workers = new ConcurrentLinkedQueue<>();

    public TaskScheduler(int maxThreads) {
        this(maxThreads, null);
    }

    public TaskScheduler(int maxThreads, Logger logger) {
        this(maxThreads, null, logger);
    }

    public TaskScheduler(int maxThreads, Collection<TaskEntry<?>> entries, Logger logger) {
        this(maxThreads, entries, logger, 10);
    }

    public TaskScheduler(int maxThreads, Collection<TaskEntry<?>> entries, Logger logger, long sleepThreadSwitch) {
        this(maxThreads, entries, logger, sleepThreadSwitch, false);
    }

    public TaskScheduler(int maxThreads, Collection<TaskEntry<?>> entries, Logger logger, long sleepThreadSwitch, boolean dynamicThreadCount) {
        this(maxThreads, entries, logger, sleepThreadSwitch, dynamicThreadCount, 10000L);
    }

    public TaskScheduler(int maxThreads, Collection<TaskEntry<?>> entries, Logger logger, long sleepThreadSwitch, boolean dynamicThreadCount, long threadLiveMillis) {
        this.sleepThreadSwitch = sleepThreadSwitch;
        this.dynamicWorkerCount = dynamicThreadCount;
        this.threadLiveMillis = threadLiveMillis;

        this.maxThreads = maxThreads <= 0 ? Runtime.getRuntime().availableProcessors() : maxThreads;

        if (entries != null)
            taskEntries.addAll(entries);
    }

    public TaskEntryFuture<Void> schedule(Runnable runnable) {
        return schedule(runnable, null);
    }

    public TaskEntryFuture<Void> schedule(Runnable runnable, Consumer<Void> consumer) {
        return schedule(runnable, consumer, 0);
    }

    public TaskEntryFuture<Void> schedule(Runnable runnable, Consumer<Void> consumer, long delay) {
        return schedule(runnable, consumer, delay, 0);
    }

    public TaskEntryFuture<Void> schedule(Runnable runnable, Consumer<Void> consumer, long delay, long repeats) {
        return schedule(new VoidTaskEntry(runnable, consumer, delay, repeats));
    }

    public <V> TaskEntryFuture<V> schedule(TaskEntry<V> taskEntry) {
        return offerEntry(taskEntry);
    }

    protected void newWorker() {
        Worker worker = new Worker();
        workers.add(worker);

        worker.start();
    }

    private void checkEnoughThreads() {
        Worker worker = hasFreeWorker();
        if (this.workers.size() < maxThreads
                || (dynamicWorkerCount && maxThreads > 1 && taskEntries.size() > this.workers.size() && taskEntries.size() <= (getMaxThreads() * 2)) && worker == null)
            newWorker();
    }

    private Worker hasFreeWorker() {
        for (Worker worker : workers)
            if (worker.isFreeWorker()) return worker;

        return null;
    }

    private <V> TaskEntryFuture<V> offerEntry(TaskEntry<V> entry) {
        this.taskEntries.offer(entry);
        checkEnoughThreads();
        return entry.getFuture();
    }

    public class Worker extends Thread {
        private volatile TaskEntry<?> taskEntry = null;
        private long liveTimeStamp = System.currentTimeMillis();

        Worker() {
            super(threadGroup, threadGroup.getName() + "#" + threadId.addAndGet(1));
            setDaemon(true);
        }

        public boolean isFreeWorker() {
            return taskEntry == null;
        }

        @Override
        public synchronized void run() {
            while ((liveTimeStamp + threadLiveMillis) > System.currentTimeMillis()) {
                execute();
                sleepUninterruptedly(sleepThreadSwitch);
            }

            workers.remove(this);
        }

        public synchronized void execute() {
            while (!taskEntries.isEmpty() && !isInterrupted()) {
                taskEntry = taskEntries.poll();

                if (taskEntry == null || taskEntry.task == null) continue;

                liveTimeStamp = System.currentTimeMillis();

                if (taskEntry.delayTimeOut != 0 && System.currentTimeMillis() < taskEntry.delayTimeOut) {
                    if (maxThreads != 1) {
                        long difference = taskEntry.delayTimeOut - System.currentTimeMillis();

                        if (difference > sleepThreadSwitch) {
                            sleepUninterruptedly(sleepThreadSwitch - 1);
                            offerEntry(taskEntry);
                            continue;

                        } else sleepUninterruptedly(difference);
                    } else {
                        sleepUninterruptedly(sleepThreadSwitch);
                        offerEntry(taskEntry);
                        continue;
                    }
                }

                try {
                    taskEntry.invoke();
                } catch (final Throwable throwable) {
                    StringUtil.printError(ReformCloudLibraryServiceProvider.getInstance().getLoggerProvider(), "Error in TaskScheduler", throwable);
                }

                if (checkEntry())
                    taskEntry = null;
            }
        }

        private void offerEntry(TaskEntry<?> entry) {
            taskEntries.offer(entry);
            taskEntry = null;
        }

        private boolean checkEntry() {
            if (taskEntry.repeat == -1) {
                offerEntry(taskEntry);
                return false;
            }

            if (taskEntry.repeat > 0) {
                offerEntry(taskEntry);
                return false;
            }
            return true;
        }

        private synchronized void sleepUninterruptedly(long millis) {
            try {
                Thread.sleep(millis);
            } catch (final InterruptedException ignored) {
            }
        }
    }

    public static TaskScheduler runtimeScheduler() {
        return TASK_SCHEDULER;
    }

    private final class VoidTaskEntry extends TaskEntry<Void> {
        public VoidTaskEntry(Runnable runnable, Consumer<Void> consumer, long delay, long repeats) {
            super(() -> {
                if (runnable != null)
                    runnable.run();

                return null;
            }, consumer, delay, repeats);
        }
    }
}
