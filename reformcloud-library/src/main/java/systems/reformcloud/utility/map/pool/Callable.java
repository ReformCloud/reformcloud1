/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.utility.map.pool;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 17.06.2019
 */

public interface Callable<T> extends Serializable {

    void call(T t);
}
