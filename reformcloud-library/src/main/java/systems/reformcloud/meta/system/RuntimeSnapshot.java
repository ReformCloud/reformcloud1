/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.meta.system;

import java.beans.ConstructorProperties;
import java.io.Serializable;
import systems.reformcloud.meta.system.classloading.ClassLoadingSnapshot;
import systems.reformcloud.meta.system.memory.MemorySnapshot;
import systems.reformcloud.meta.system.system.SystemRuntimeSnapshot;
import systems.reformcloud.meta.system.thread.ThreadSnapshot;

/**
 * @author _Klaro | Pasqual K. / created on 26.06.2019
 */

public final class RuntimeSnapshot implements Serializable {

    private ClassLoadingSnapshot classLoadingSnapshot;

    private MemorySnapshot memorySnapshot;

    private ThreadSnapshot threadSnapshot;

    private SystemRuntimeSnapshot systemRuntimeSnapshot;

    @ConstructorProperties({})
    public RuntimeSnapshot() {
        this.classLoadingSnapshot = ClassLoadingSnapshot.create();
        this.memorySnapshot = MemorySnapshot.create();
        this.threadSnapshot = ThreadSnapshot.create();
        this.systemRuntimeSnapshot = SystemRuntimeSnapshot.create();
    }

    public ClassLoadingSnapshot getClassLoadingSnapshot() {
        return classLoadingSnapshot;
    }

    public MemorySnapshot getMemorySnapshot() {
        return memorySnapshot;
    }

    public ThreadSnapshot getThreadSnapshot() {
        return threadSnapshot;
    }

    public SystemRuntimeSnapshot getSystemRuntimeSnapshot() {
        return systemRuntimeSnapshot;
    }
}
