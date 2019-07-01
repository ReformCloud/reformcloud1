/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.api;

import java.io.Serializable;
import systems.reformcloud.meta.system.RuntimeSnapshot;
import systems.reformcloud.meta.system.classloading.ClassLoadingSnapshot;
import systems.reformcloud.meta.system.memory.MemorySnapshot;
import systems.reformcloud.meta.system.system.SystemRuntimeSnapshot;
import systems.reformcloud.meta.system.thread.ThreadSnapshot;

/**
 * @author _Klaro | Pasqual K. / created on 28.06.2019
 */

public interface SnapshotAble extends Serializable {

    /**
     * Creates the current runtime snapshot
     *
     * @return The current runtime snapshot
     */
    default RuntimeSnapshot current() {
        return new RuntimeSnapshot();
    }

    /**
     * Creates the current class loading snapshot
     *
     * @return The current class loading snapshot
     */
    default ClassLoadingSnapshot currentClassLoadingSnapshot() {
        return ClassLoadingSnapshot.create();
    }

    /**
     * Creates the current memory snapshot
     *
     * @return The current memory snapshot
     */
    default MemorySnapshot currentMemorySnapshot() {
        return MemorySnapshot.create();
    }

    /**
     * Creates the current system runtime snapshot
     *
     * @return The current system runtime snapshot
     */
    default SystemRuntimeSnapshot currentSystemRuntimeSnapshot() {
        return SystemRuntimeSnapshot.create();
    }

    /**
     * Creates the current thread snapshot
     *
     * @return The current thread snapshot
     */
    default ThreadSnapshot currentThreadSnapshot() {
        return ThreadSnapshot.create();
    }
}
