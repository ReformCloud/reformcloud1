/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.meta.startup.stages;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 24.11.2018
 */

public enum ProcessStartupStage implements Serializable {

    /**
     * The current process waits for the bootstrap
     */
    WAITING,

    /**
     * The current process copies the template and the global folder
     */
    COPY,

    /**
     * The process will be prepared (copy necessary files...)
     */
    PREPARING,

    /**
     * The process will be started
     */
    START,

    /**
     * The startup is done and the process ready
     */
    DONE
}
