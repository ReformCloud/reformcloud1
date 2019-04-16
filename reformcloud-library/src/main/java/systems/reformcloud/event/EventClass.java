/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.event;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author _Klaro | Pasqual K. / created on 16.04.2019
 */

@AllArgsConstructor(access = AccessLevel.PACKAGE)
@Getter(value = AccessLevel.PRIVATE)
final class EventClass implements Serializable {
    /**
     * The current listener class
     */
    private Object listener;

    /**
     * The current listener method
     */
    private Method method;

    /**
     * Calls the current listener
     *
     * @param event                             The event which should be called
     * @throws InvocationTargetException        If the method can not be invoked
     * @throws IllegalAccessException           If the access to the class failed or the permission are not correct
     */
    void invoke(Object event) throws InvocationTargetException, IllegalAccessException {
        method.invoke(listener, event);
    }
}
