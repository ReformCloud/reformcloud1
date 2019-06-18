/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.meta.process;

import java.beans.ConstructorProperties;
import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 18.06.2019
 */

public final class ProcessStartupInformation implements Serializable {

    /**
     * The time when the process will be prepared by the client
     */
    private long prepareTime;

    /**
     * The time when the client starts the process
     */
    private long bootstrapTime;

    /**
     * The startup finished time (will be set by the server after the
     * controller connect)
     */
    private long startupFinishedTime;


    @ConstructorProperties({"prepareTime", "bootstrapTime", "startupFinishedTime"})
    public ProcessStartupInformation(long prepareTime, long bootstrapTime, long startupFinishedTime) {
        this.prepareTime = prepareTime;
        this.bootstrapTime = bootstrapTime;
        this.startupFinishedTime = startupFinishedTime;
    }

    public long getPrepareTime() {
        return prepareTime;
    }

    public long getBootstrapTime() {
        return bootstrapTime;
    }

    public long getStartupFinishedTime() {
        return startupFinishedTime;
    }

    public void setStartupFinishedTime(long startupFinishedTime) {
        this.startupFinishedTime = startupFinishedTime;
    }
}
