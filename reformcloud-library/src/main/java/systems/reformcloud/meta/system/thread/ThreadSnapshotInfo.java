/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.meta.system.thread;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 26.06.2019
 */

public final class ThreadSnapshotInfo implements Serializable {

    private String name;

    private long id;

    private int stackTraces;

    private boolean inNative;

    private boolean suspended;

    ThreadSnapshotInfo(String name, long id, int stackTraces, boolean inNative,
        boolean suspended) {
        this.name = name;
        this.id = id;
        this.stackTraces = stackTraces;
        this.inNative = inNative;
        this.suspended = suspended;
    }

    public String getName() {
        return name;
    }

    public long getId() {
        return id;
    }

    public int getStackTraces() {
        return stackTraces;
    }

    public boolean isInNative() {
        return inNative;
    }

    public boolean isSuspended() {
        return suspended;
    }
}
