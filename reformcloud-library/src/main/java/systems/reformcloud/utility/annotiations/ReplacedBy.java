/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.utility.annotiations;

import java.lang.annotation.*;

/**
 * @author _Klaro | Pasqual K. / created on 22.05.2019
 */

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.TYPE, ElementType.METHOD})
public @interface ReplacedBy {
    String value() default "";
}
