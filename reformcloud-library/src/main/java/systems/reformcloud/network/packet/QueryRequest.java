/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.packet;

import java.io.Serializable;
import java.util.function.Consumer;

/**
 * @author _Klaro | Pasqual K. / created on 09.08.2019
 */

public abstract class QueryRequest<T> implements Serializable {

    public abstract void onResult(Consumer<T> consumer);
}
