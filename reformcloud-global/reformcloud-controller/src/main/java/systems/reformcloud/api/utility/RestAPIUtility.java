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
import java.util.Map;

/**
 * @author _Klaro | Pasqual K. / created on 08.02.2019
 */

public final class RestAPIUtility implements Serializable {

    private static final long serialVersionUID = -1253171241896584068L;

    public static FullHttpResponse createFullHttpResponse(final HttpVersion httpVersion) {
        DefaultFullHttpResponse defaultFullHttpResponse = new DefaultFullHttpResponse(httpVersion,
            HttpResponseStatus.UNAUTHORIZED);
        defaultFullHttpResponse.headers().set("Content-Type", "application/json");
        defaultFullHttpResponse.headers().set("Access-Control-Allow-Origin", "*");

        return defaultFullHttpResponse;
    }

    public static Configuration createDefaultAnswer() {
        return new Configuration().addBooleanValue("success", false)
            .addValue("response", new ArrayList<>());
    }

    public static boolean hasPermission(final WebUser webUser, final String permission) {
        return hasPermission(permission, webUser.getPermissions());
    }

    private static boolean hasPermission(String permission, Map<String, Boolean> permissions) {
        if (permission == null || permissions == null) {
            return false;
        }

        permission = permission.toLowerCase();
        return checkPermission(permissions, permission);
    }

    private static boolean checkPermission(Map<String, Boolean> permissions, String permission) {
        for (Map.Entry<String, Boolean> entry : permissions.entrySet()) {
            if (entry.getKey().endsWith("*") && entry.getKey().length() > 1
                && permission
                .startsWith(entry.getKey().substring(0, entry.getKey().length() - 1))) {
                return entry.getValue();
            }

            if (entry.getKey().equalsIgnoreCase(permission)) {
                return entry.getValue();
            }
        }

        return permissions.containsKey("*") && permissions.get("*");
    }
}
