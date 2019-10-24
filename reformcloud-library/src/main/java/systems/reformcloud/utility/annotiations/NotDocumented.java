/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.utility.annotiations;

import java.lang.annotation.*;

/**
 * This class marks methods, constructors aor variables which are not
 * documented because they are internal and nor for external or api use
 *
 * @author _Klaro | Pasqual K. / created on 30.07.2019
 */

@Documented
@Target({ElementType.METHOD, ElementType.CONSTRUCTOR, ElementType.LOCAL_VARIABLE})
@Retention(RetentionPolicy.CLASS)
public @interface NotDocumented {
}
