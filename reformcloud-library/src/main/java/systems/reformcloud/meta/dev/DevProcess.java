/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.meta.dev;

import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.meta.server.ServerGroup;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 24.04.2019
 */

public final class DevProcess implements Serializable {

    private ServerGroup serverGroup;

    private Configuration preConfig;

    private String template;

    private long waitingSince;

    @java.beans.ConstructorProperties({"serverGroup", "preConfig", "template", "waitingSince"})
    public DevProcess(ServerGroup serverGroup, Configuration preConfig, String template,
        long waitingSince) {
        this.serverGroup = serverGroup;
        this.preConfig = preConfig;
        this.template = template;
        this.waitingSince = waitingSince;
    }

    public ServerGroup getServerGroup() {
        return this.serverGroup;
    }

    public Configuration getPreConfig() {
        return this.preConfig;
    }

    public String getTemplate() {
        return this.template;
    }

    public long getWaitingSince() {
        return this.waitingSince;
    }
}
