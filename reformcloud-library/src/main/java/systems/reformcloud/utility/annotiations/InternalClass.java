/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.utility.annotiations;

import java.lang.annotation.*;

/**
 * This class should signal that the class which is used is only for internal
 * not for external use. The class may change or dont have public api methods
 *
 * @author _Klaro | Pasqual K. / created on 22.06.2019
 */

@Documented
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
public @interface InternalClass {
}
