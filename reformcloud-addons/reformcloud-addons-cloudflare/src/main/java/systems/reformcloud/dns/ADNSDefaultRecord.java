/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.dns;

import com.google.gson.JsonObject;
import java.io.Serializable;
import systems.reformcloud.configurations.Configuration;

/**
 * @author _Klaro | Pasqual K. / created on 09.05.2019
 */

public final class ADNSDefaultRecord extends DNSRecord implements Serializable {

    public ADNSDefaultRecord(String name, String content, JsonObject data) {
        super("A", name, content, 1, false, data);
    }

    public static class SRVRecord extends DNSRecord {

        public SRVRecord(String name, String content, String service,
            String proto, String nameSRV, int priority, int weight, int port, String target) {
            super("SRV", name, content, 1, false, new Configuration()
                .addStringValue("service", service)
                .addStringValue("proto", proto)
                .addStringValue("name", nameSRV)
                .addIntegerValue("priority", priority)
                .addIntegerValue("weight", weight)
                .addIntegerValue("port", port)
                .addStringValue("target", target)
                .getJsonObject()
            );
        }
    }
}
