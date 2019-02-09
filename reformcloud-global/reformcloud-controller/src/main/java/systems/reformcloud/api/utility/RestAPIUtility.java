/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.api.utility;

import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.meta.web.WebUser;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author _Klaro | Pasqual K. / created on 08.02.2019
 */

public final class RestAPIUtility implements Serializable {
    private static final long serialVersionUID = -1253171241896584068L;

    public static FullHttpResponse createFullHttpResponse(final HttpVersion httpVersion) {
        DefaultFullHttpResponse defaultFullHttpResponse = new DefaultFullHttpResponse(httpVersion, HttpResponseStatus.UNAUTHORIZED);
        defaultFullHttpResponse.headers().set("Content-Type", "application/json");
        defaultFullHttpResponse.headers().set("Access-Control-Allow-Origin", "*");

        return defaultFullHttpResponse;
    }

    public static Configuration createDefaultAnswer() {
        return new Configuration().addBooleanProperty("success", false).addProperty("response", new ArrayList<>());
    }

    public static boolean hasPermission(final WebUser webUser, final String permission) {
        Map<String, Boolean> permissions = webUser.getPermissions();
        if (permissions.containsKey("*") && permissions.get("*"))
            return true;

        return permissions.containsKey(permission) && permissions.get(permission);
    }
}
