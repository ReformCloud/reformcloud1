/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.utility.checkable;

/**
 * @author _Klaro | Pasqual K. / created on 28.01.2019
 */

public interface Checkable<E> {
    boolean isChecked(E e);
}
