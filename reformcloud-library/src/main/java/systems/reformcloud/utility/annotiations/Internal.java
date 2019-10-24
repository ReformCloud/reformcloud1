/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.utility.annotiations;

import java.lang.annotation.*;

/**
 * This annotation marks documented methods, classes... which are for
 * internal use and not for external or as api use
 *
 * @author _Klaro | Pasqual K. / created on 30.07.2019
 */

@Documented
@Retention(RetentionPolicy.CLASS)
@Target(
    {
        ElementType.METHOD,
        ElementType.FIELD,
        ElementType.PARAMETER,
        ElementType.LOCAL_VARIABLE,
        ElementType.CONSTRUCTOR,
        ElementType.TYPE
    }
)
public @interface Internal {
}
