/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.addons.exceptions;

/**
 * @author _Klaro | Pasqual K. / created on 10.12.2018
 */

/**
 * Exception when a Addon couldn't be loaded
 */
public final class AddonLoadException extends Exception {
    public AddonLoadException(final Throwable cause) {
        super("Invalid addon.properties", cause);
    }
}
