/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.meta;

import systems.reformcloud.configurations.Configuration;

import java.io.Serializable;
import java.util.UUID;

/**
 * @author _Klaro | Pasqual K. / created on 29.10.2018
 */

public class CloudProcess implements Serializable {
    private static final long serialVersionUID = -532002825303576279L;

    /**
     * The name of the process
     */
    private String name;

    /**
     * The uid of the process
     */
    private UUID processUID;

    /**
     * The client where the process was started
     */
    private String client;

    /**
     * The group of the server
     */
    private String group;

    /**
     * The pre config of the server
     */
    private Configuration preConfig;

    /**
     * The template which was loaded
     */
    private Template loadedTemplate;

    /**
     * The process id of the process
     */
    private int processID;

    @java.beans.ConstructorProperties({"name", "processUID", "client", "group", "preConfig", "loadedTemplate", "processID"})
    public CloudProcess(String name, UUID processUID, String client, String group, Configuration preConfig, Template loadedTemplate, int processID) {
        this.name = name;
        this.processUID = processUID;
        this.client = client;
        this.group = group;
        this.preConfig = preConfig;
        this.loadedTemplate = loadedTemplate;
        this.processID = processID;
    }

    public String getName() {
        return this.name;
    }

    public UUID getProcessUID() {
        return this.processUID;
    }

    public String getClient() {
        return this.client;
    }

    public String getGroup() {
        return this.group;
    }

    public Configuration getPreConfig() {
        return this.preConfig;
    }

    public Template getLoadedTemplate() {
        return this.loadedTemplate;
    }

    public int getProcessID() {
        return this.processID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setProcessUID(UUID processUID) {
        this.processUID = processUID;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public void setPreConfig(Configuration preConfig) {
        this.preConfig = preConfig;
    }

    public void setLoadedTemplate(Template loadedTemplate) {
        this.loadedTemplate = loadedTemplate;
    }

    public void setProcessID(int processID) {
        this.processID = processID;
    }
}
