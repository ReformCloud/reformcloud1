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

    /**
     * The uid of the server
     */
    private UUID uid;

    /**
     * The name of the server
     */
    private String name;

    /**
     * The template of the server which should be loaded
     */
    private String template;

    /**
     * The server group of the server
     */
    private ServerGroup serverGroup;

    /**
     * The pre config given by the user
     */
    private Configuration configuration;

    /**
     * The id of the server
     */
    private int id;

    @java.beans.ConstructorProperties({"uid", "name", "template", "serverGroup", "configuration",
        "id"})
    public ServerStartupInfo(UUID uid, String name, String template, ServerGroup serverGroup,
        Configuration configuration, int id) {
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
