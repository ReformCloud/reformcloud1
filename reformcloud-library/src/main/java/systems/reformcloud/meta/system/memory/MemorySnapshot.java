/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.meta.system.memory;

import java.beans.ConstructorProperties;
import java.io.Serializable;
import java.lang.management.MemoryMXBean;
import systems.reformcloud.ReformCloudLibraryService;
import systems.reformcloud.utility.annotiations.NeverNull;

/**
 * @author _Klaro | Pasqual K. / created on 26.06.2019
 */

public final class MemorySnapshot implements Serializable {

    private long heapMemoryUsageUsed;

    private long heapMemoryUsageMax;

    private long nonHeapMemoryUsageUtility;

    private long nonHeapMemoryUsageMax;

    private int objectPendingFinalizationCount;

    private boolean verbose;

    @ConstructorProperties({"heapMemoryUsageUsed", "heapMemoryUsageMax",
        "nonHeapMemoryUsageUtility", "nonHeapMemoryUsageMax", "objectPendingFinalizationCount",
        "verbose"})
    private MemorySnapshot(long heapMemoryUsageUsed, long heapMemoryUsageMax,
        long nonHeapMemoryUsageUtility, long nonHeapMemoryUsageMax,
        int objectPendingFinalizationCount,
        boolean verbose) {
        this.heapMemoryUsageUsed = heapMemoryUsageUsed;
        this.heapMemoryUsageMax = heapMemoryUsageMax;
        this.nonHeapMemoryUsageUtility = nonHeapMemoryUsageUtility;
        this.nonHeapMemoryUsageMax = nonHeapMemoryUsageMax;
        this.objectPendingFinalizationCount = objectPendingFinalizationCount;
        this.verbose = verbose;
    }

    @NeverNull
    public static MemorySnapshot create() {
        MemoryMXBean memoryMXBean = ReformCloudLibraryService.getMemoryMXBean();
        return new MemorySnapshot(
            memoryMXBean.getHeapMemoryUsage().getUsed(),
            memoryMXBean.getHeapMemoryUsage().getMax(),
            memoryMXBean.getNonHeapMemoryUsage().getUsed(),
            memoryMXBean.getNonHeapMemoryUsage().getMax(),
            memoryMXBean.getObjectPendingFinalizationCount(),
            memoryMXBean.isVerbose()
        );
    }

    public long getHeapMemoryUsageUsed() {
        return heapMemoryUsageUsed;
    }

    public long getHeapMemoryUsageMax() {
        return heapMemoryUsageMax;
    }

    public long getNonHeapMemoryUsageUtility() {
        return nonHeapMemoryUsageUtility;
    }

    public long getNonHeapMemoryUsageMax() {
        return nonHeapMemoryUsageMax;
    }

    public int getObjectPendingFinalizationCount() {
        return objectPendingFinalizationCount;
    }

    public boolean isVerbose() {
        return verbose;
    }
}
