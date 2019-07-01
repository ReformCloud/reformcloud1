/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.meta.dev;

import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.meta.server.ServerGroup;
import systems.reformcloud.utility.annotiations.MayNotBePresent;
import systems.reformcloud.utility.annotiations.ShouldNotBeNull;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 24.04.2019
 */

public final class DevProcess implements Serializable {

    /**
     * The server group of the process
     */
    @ShouldNotBeNull
    private ServerGroup serverGroup;

    /**
     * The given pre config of the process
     */
    @ShouldNotBeNull
    private Configuration preConfig;

    /**
     * The template which should be started
     */
    @MayNotBePresent
    private String template;

    /**
     * The time since the process is in the queue
     */
    @ShouldNotBeNull
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
