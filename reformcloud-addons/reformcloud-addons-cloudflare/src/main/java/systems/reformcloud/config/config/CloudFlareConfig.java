/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.config.config;

import java.beans.ConstructorProperties;
import java.io.Serializable;
import java.util.List;

/**
 * @author _Klaro | Pasqual K. / created on 10.05.2019
 */

public final class CloudFlareConfig implements Serializable {

    private String email;

    private String apiToken;

    private String domain;

    private CloudFlareZone cloudFlareZone;

    private List<CloudFlareGroup> cloudFlareGroups;

    @ConstructorProperties({"email", "apiToken", "domain", "cloudFlareZone", "cloudFlareGroups"})
    public CloudFlareConfig(String email, String apiToken, String domain,
        CloudFlareZone cloudFlareZone, List<CloudFlareGroup> cloudFlareGroups) {
        this.email = email;
        this.apiToken = apiToken;
        this.domain = domain;
        this.cloudFlareZone = cloudFlareZone;
        this.cloudFlareGroups = cloudFlareGroups;
    }

    public String getEmail() {
        return email;
    }

    public String getApiToken() {
        return apiToken;
    }

    public String getDomain() {
        return domain;
    }

    public CloudFlareZone getCloudFlareZone() {
        return cloudFlareZone;
    }

    public List<CloudFlareGroup> getCloudFlareGroups() {
        return cloudFlareGroups;
    }

    public static class CloudFlareZone implements Serializable {

        private boolean useOwn;
        private String zoneID;

        @ConstructorProperties({"useOwn", "zoneID"})
        public CloudFlareZone(boolean useOwn, String zoneID) {
            this.useOwn = useOwn;
            this.zoneID = zoneID;
        }

        public boolean isUseOwn() {
            return useOwn;
        }

        public String getZoneID() {
            return zoneID;
        }
    }

    public static class CloudFlareGroup implements Serializable {

        private String targetProxyGroup;

        private String subDomain;

        @ConstructorProperties({"targetProxyGroup", "subDomain"})
        public CloudFlareGroup(String targetProxyGroup, String subDomain) {
            this.targetProxyGroup = targetProxyGroup;
            this.subDomain = subDomain;
        }

        public String getTargetProxyGroup() {
            return targetProxyGroup;
        }

        public String getSubDomain() {
            return subDomain == null ? "@" : subDomain;
        }
    }
}
