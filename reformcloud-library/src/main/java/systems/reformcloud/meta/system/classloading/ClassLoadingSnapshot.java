/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.meta.system.classloading;

import java.beans.ConstructorProperties;
import java.io.Serializable;
import java.lang.management.ClassLoadingMXBean;
import systems.reformcloud.ReformCloudLibraryService;
import systems.reformcloud.utility.annotiations.NeverNull;

/**
 * @author _Klaro | Pasqual K. / created on 26.06.2019
 */

public final class ClassLoadingSnapshot implements Serializable {

    private int currentTotalLoadedClasses;

    private long everTotalLoadedClasses;

    private long everUnloadedClasses;

    private boolean isVerbose;

    @ConstructorProperties({"currentTotalLoadedClasses", "everTotalLoadedClasses",
        "everUnloadedClasses", "isVerbose"})
    private ClassLoadingSnapshot(int currentTotalLoadedClasses,
        long everTotalLoadedClasses,
        long everUnloadedClasses, boolean isVerbose) {
        this.currentTotalLoadedClasses = currentTotalLoadedClasses;
        this.everTotalLoadedClasses = everTotalLoadedClasses;
        this.everUnloadedClasses = everUnloadedClasses;
        this.isVerbose = isVerbose;
    }

    @NeverNull
    public static ClassLoadingSnapshot create() {
        ClassLoadingMXBean classLoadingMXBean =
            ReformCloudLibraryService.getClassLoadingMXBean();
        return new ClassLoadingSnapshot(
            classLoadingMXBean.getLoadedClassCount(),
            classLoadingMXBean.getTotalLoadedClassCount(),
            classLoadingMXBean.getUnloadedClassCount(),
            classLoadingMXBean.isVerbose()
        );
    }

    public int getCurrentTotalLoadedClasses() {
        return currentTotalLoadedClasses;
    }

    public long getEverTotalLoadedClasses() {
        return everTotalLoadedClasses;
    }

    public long getEverUnloadedClasses() {
        return everUnloadedClasses;
    }

    public boolean isVerbose() {
        return isVerbose;
    }
}
