/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.event;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author _Klaro | Pasqual K. / created on 16.04.2019
 */

final class EventClass implements Serializable {
    /**
     * The current listener class
     */
    private Object listener;

    /**
     * The current listener method
     */
    private Method method;

    @java.beans.ConstructorProperties({"listener", "method"})
    EventClass(Object listener, Method method) {
        this.listener = listener;
        this.method = method;
    }

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

    private Object getListener() {
        return this.listener;
    }

    private Method getMethod() {
        return this.method;
    }
}
