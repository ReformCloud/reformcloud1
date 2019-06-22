/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.exceptions;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 26.12.2018
 */

public final class LoadException extends Throwable implements Serializable {

    /**
     * The exception will be thrown if any error occurs while loading the cloud system
     *
     * @param cause The cause which occurs
     */
    public LoadException(final Throwable cause) {
        super("ReformCloud internal error", cause);
    }
}
