/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.utility.converter;

/**
 * @author _Klaro | Pasqual K. / created on 21.04.2019
 */

public interface Converter<E, V> {

    V convert(E e);
}
