/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.util;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 09.05.2019
 */

public enum RequestMethod implements Serializable {
    POST("POST"),
    DELETE("DELETE"),
    GET("GET");

    private String stringValue;

    RequestMethod(String value) {
        this.stringValue = value;
    }

    public String getStringValue() {
        return stringValue;
    }
}
