/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.dns;

import com.google.gson.JsonObject;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 09.05.2019
 */

class DNSRecord implements Serializable {
    private String type;

    private String name;

    private String content;

    private int ttl;

    private boolean proxied;

    private JsonObject data;

    @java.beans.ConstructorProperties({"type", "name", "content", "ttl", "proxied", "data"})
    DNSRecord(String type, String name, String content, int ttl, boolean proxied, JsonObject data) {
        this.type = type;
        this.name = name;
        this.content = content;
        this.ttl = ttl;
        this.proxied = proxied;
        this.data = data;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getContent() {
        return content;
    }

    public int getTtl() {
        return ttl;
    }

    public boolean isProxied() {
        return proxied;
    }

    public JsonObject getData() {
        return data;
    }
}
