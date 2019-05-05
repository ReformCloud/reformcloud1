/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.event.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author _Klaro | Pasqual K. / created on 16.04.2019
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Handler {
}
