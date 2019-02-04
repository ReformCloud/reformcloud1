/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.exceptions;

/**
 * @author _Klaro | Pasqual K. / created on 26.12.2018
 */

public class LoadException extends Throwable
{
    public LoadException(final Throwable cause) {
        super("ReformCloud internal error", cause);
    }
}
