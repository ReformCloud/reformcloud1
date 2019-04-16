/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.utility.checkable;

/**
 * @author _Klaro | Pasqual K. / created on 28.01.2019
 */

public interface Checkable<E> {
    /**
     * Checks a value by the given parameters
     *
     * @param e     The value which should be checked
     * @return      If all parameters are passed
     */
    boolean isChecked(E e);
}
