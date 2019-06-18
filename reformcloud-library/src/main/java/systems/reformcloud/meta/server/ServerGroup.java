/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.meta.server;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import systems.reformcloud.meta.Template;
import systems.reformcloud.meta.auto.start.AutoStart;
import systems.reformcloud.meta.auto.stop.AutoStop;
import systems.reformcloud.meta.enums.ServerModeType;
import systems.reformcloud.meta.enums.TemplateBackend;
import systems.reformcloud.meta.server.versions.SpigotVersions;

/**
 * @author _Klaro | Pasqual K. / created on 21.10.2018
 */

public class ServerGroup implements Serializable {

    private static final long serialVersionUID = -6849497313084944255L;

    /**
     * The name of the server group
     */
    private String name;

    /**
     * The motd of the server group
     */
    private String motd;

    /**
     * The join permission of the server
     */
    private String joinPermission;

    /**
     * The list of the clients
     */
    private List<String> clients;

    /**
     * All templates of the server group
     */
    private List<Template> templates;

    /**
     * The max memory of the group
     */
    private int memory;

    /**
     * The min online service count
     */
    private int minOnline;

    /**
     * The max online count
     */
    private int maxOnline;

    /**
     * The max players of the group
     */
    private int maxPlayers;

    /**
     * The start port of the group
     */
    private int startPort;

    /**
     * The maintenance state of the group
     */
    private boolean maintenance;

    /**
     * The log save state of the group
     */
    private boolean saveLogs;

    /**
     * The server type
     */
    private ServerModeType serverModeType;

    /**
     * The auto start config
     */
    private AutoStart autoStart;

    /**
     * The auto stop config
     */
    private AutoStop autoStop;

    /**
     * The spigot version of the group
     */
    private SpigotVersions spigotVersions;

    @java.beans.ConstructorProperties({"name", "motd", "joinPermission", "clients", "templates",
        "memory", "minOnline", "maxOnline", "maxPlayers", "startPort", "maintenance", "saveLogs",
        "autoStart", "autoStop", "serverModeType", "spigotVersions"})
    public ServerGroup(String name, String motd, String joinPermission, List<String> clients,
        List<Template> templates, int memory, int minOnline, int maxOnline, int maxPlayers,
        int startPort, boolean maintenance, boolean saveLogs, AutoStart autoStart,
        AutoStop autoStop, ServerModeType serverModeType, SpigotVersions spigotVersions) {
        this.name = name;
        this.motd = motd;
        this.joinPermission = joinPermission;
        this.clients = clients;
        this.templates = templates;
        this.memory = memory;
        this.minOnline = minOnline;
        this.maxOnline = maxOnline;
        this.maxPlayers = maxPlayers;
        this.startPort = startPort;
        this.maintenance = maintenance;
        this.saveLogs = saveLogs;
        this.autoStart = autoStart;
        this.autoStop = autoStop;
        this.serverModeType = serverModeType;
        this.spigotVersions = spigotVersions;
    }

    public Template getTemplate(final String name) {
        return this.templates.stream().filter(template -> template.getName().equals(name))
            .findFirst().orElse(randomTemplate());
    }

    public Template getTemplateOrElseNull(final String name) {
        return this.templates.stream().filter(template -> template.getName().equals(name))
            .findFirst().orElse(null);
    }

    public Template randomTemplate() {
        if (this.templates.size() == 0) {
            return new Template("default", null, TemplateBackend.CLIENT);
        }

        for (Template template : this.templates) {
            if (template.getName().equals("every")) {
                continue;
            }

            return template;
        }

        return new Template("default", null, TemplateBackend.CLIENT);
    }

    public void deleteTemplate(String name) {
        List<Template> copyOf = new ArrayList<>(this.templates);
        copyOf.stream().filter(template -> template.getName().equals(name)).findFirst()
            .ifPresent(template -> this.templates.remove(template));
    }

    public String getName() {
        return this.name;
    }

    public String getMotd() {
        return this.motd;
    }

    public String getJoinPermission() {
        return this.joinPermission;
    }

    public List<String> getClients() {
        return this.clients;
    }

    public List<Template> getTemplates() {
        return this.templates;
    }

    public int getMemory() {
        return this.memory;
    }

    public int getMinOnline() {
        return this.minOnline;
    }

    public int getMaxOnline() {
        return this.maxOnline;
    }

    public int getMaxPlayers() {
        return this.maxPlayers;
    }

    public int getStartPort() {
        return this.startPort;
    }

    public boolean isMaintenance() {
        return this.maintenance;
    }

    public boolean isSaveLogs() {
        return this.saveLogs;
    }

    public ServerModeType getServerModeType() {
        return this.serverModeType;
    }

    public SpigotVersions getSpigotVersions() {
        return this.spigotVersions;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMotd(String motd) {
        this.motd = motd;
    }

    public void setJoinPermission(String joinPermission) {
        this.joinPermission = joinPermission;
    }

    public void setClients(List<String> clients) {
        this.clients = clients;
    }

    public void setTemplates(List<Template> templates) {
        this.templates = templates;
    }

    public void setMemory(int memory) {
        this.memory = memory;
    }

    public void setMinOnline(int minOnline) {
        this.minOnline = minOnline;
    }

    public void setMaxOnline(int maxOnline) {
        this.maxOnline = maxOnline;
    }

    public void setMaxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    public void setStartPort(int startPort) {
        this.startPort = startPort;
    }

    public void setMaintenance(boolean maintenance) {
        this.maintenance = maintenance;
    }

    public void setSaveLogs(boolean saveLogs) {
        this.saveLogs = saveLogs;
    }

    public void setServerModeType(ServerModeType serverModeType) {
        this.serverModeType = serverModeType;
    }

    public void setSpigotVersions(SpigotVersions spigotVersions) {
        this.spigotVersions = spigotVersions;
    }

    public AutoStart getAutoStart() {
        return autoStart;
    }

    public void setAutoStart(AutoStart autoStart) {
        this.autoStart = autoStart;
    }

    public AutoStop getAutoStop() {
        return autoStop;
    }

    public void setAutoStop(AutoStop autoStop) {
        this.autoStop = autoStop;
    }
}
