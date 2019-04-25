/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.meta.startup;

import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.meta.server.ServerGroup;

import java.io.Serializable;
import java.util.UUID;

/**
 * @author _Klaro | Pasqual K. / created on 29.10.2018
 */

public class ServerStartupInfo implements Serializable {
    private static final long serialVersionUID = 3276684735275715610L;

    private UUID uid;
    private String name, template;
    private ServerGroup serverGroup;
    private Configuration configuration;
    private int id;

    @java.beans.ConstructorProperties({"uid", "name", "template", "serverGroup", "configuration", "id"})
    public ServerStartupInfo(UUID uid, String name, String template, ServerGroup serverGroup, Configuration configuration, int id) {
        this.uid = uid;
        this.name = name;
        this.template = template;
        this.serverGroup = serverGroup;
        this.configuration = configuration;
        this.id = id;
    }

    public UUID getUid() {
        return this.uid;
    }

    public String getName() {
        return this.name;
    }

    public String getTemplate() {
        return this.template;
    }

    public ServerGroup getServerGroup() {
        return this.serverGroup;
    }

    public Configuration getConfiguration() {
        return this.configuration;
    }

    public int getId() {
        return this.id;
    }
}
