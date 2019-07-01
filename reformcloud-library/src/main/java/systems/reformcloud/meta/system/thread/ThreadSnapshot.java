/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.meta.system.thread;

import java.beans.ConstructorProperties;
import java.io.Serializable;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import systems.reformcloud.ReformCloudLibraryService;
import systems.reformcloud.utility.annotiations.NeverNull;

/**
 * @author _Klaro | Pasqual K. / created on 26.06.2019
 */

public final class ThreadSnapshot implements Serializable {

    private List<ThreadSnapshotInfo> threadSnapshotInfos;

    private long[] deadLockedThreads;

    private long[] threadIds;

    private int daemonThreads;

    private int threadCount;

    private int peakThreadCount;

    private ThreadSnapshotInfo currentThreadInfo;

    @ConstructorProperties({"threadSnapshotInfos", "deadLockedThreads", "threadIds",
        "daemonThreads", "threadCount", "peakThreadCount", "currentThreadInfo"})
    private ThreadSnapshot(
        List<ThreadSnapshotInfo> threadSnapshotInfos, long[] deadLockedThreads, long[] threadIds,
        int daemonThreads, int threadCount, int peakThreadCount,
        ThreadSnapshotInfo currentThreadInfo) {
        this.threadSnapshotInfos = threadSnapshotInfos;
        this.deadLockedThreads = deadLockedThreads;
        this.threadIds = threadIds;
        this.daemonThreads = daemonThreads;
        this.threadCount = threadCount;
        this.peakThreadCount = peakThreadCount;
        this.currentThreadInfo = currentThreadInfo;
    }

    @NeverNull
    public static ThreadSnapshot create() {
        ThreadMXBean threadMXBean = ReformCloudLibraryService.getThreadMXBean();
        long currentThreadID = Thread.currentThread().getId();

        List<ThreadSnapshotInfo> snapshotInfos = new LinkedList<>();
        Arrays.stream(threadMXBean.dumpAllThreads(true, true)).forEach(info ->
            snapshotInfos.add(
                new ThreadSnapshotInfo(
                    info.getThreadName(),
                    info.getThreadId(),
                    info.getStackTrace().length,
                    info.isInNative(),
                    info.isSuspended()
                )
            )
        );

        return new ThreadSnapshot(
            snapshotInfos,
            threadMXBean.findDeadlockedThreads(),
            threadMXBean.getAllThreadIds(),
            threadMXBean.getDaemonThreadCount(),
            threadMXBean.getThreadCount(),
            threadMXBean.getPeakThreadCount(),
            currentThreadInfo(threadMXBean, currentThreadID)
        );
    }

    @NeverNull
    private static ThreadSnapshotInfo currentThreadInfo(ThreadMXBean threadMXBean,
        long currentID) {
        ThreadInfo threadInfo = threadMXBean.getThreadInfo(currentID);
        return new ThreadSnapshotInfo(
            threadInfo.getThreadName(),
            threadInfo.getThreadId(),
            threadInfo.getStackTrace().length,
            threadInfo.isInNative(),
            threadInfo.isSuspended()
        );
    }

    public List<ThreadSnapshotInfo> getThreadSnapshotInfos() {
        return threadSnapshotInfos;
    }

    public long[] getDeadLockedThreads() {
        return deadLockedThreads;
    }

    public long[] getThreadIds() {
        return threadIds;
    }

    public int getDaemonThreads() {
        return daemonThreads;
    }

    public int getThreadCount() {
        return threadCount;
    }

    public int getPeakThreadCount() {
        return peakThreadCount;
    }

    public ThreadSnapshotInfo getCurrentThreadInfo() {
        return currentThreadInfo;
    }
}
