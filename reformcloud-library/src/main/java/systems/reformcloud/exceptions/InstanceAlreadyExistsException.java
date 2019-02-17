/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.exceptions;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 17.02.2019
 */

public final class InstanceAlreadyExistsException extends Throwable implements Serializable {
    public InstanceAlreadyExistsException() {
        super();
    }
}
