/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.meta.server;

import systems.reformcloud.meta.Template;
import systems.reformcloud.meta.auto.start.AutoStart;
import systems.reformcloud.meta.auto.stop.AutoStop;
import systems.reformcloud.meta.enums.ServerModeType;
import systems.reformcloud.meta.enums.TemplateBackend;
import systems.reformcloud.meta.server.versions.SpigotVersions;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author _Klaro | Pasqual K. / created on 21.10.2018
 */

public class ServerGroup implements Serializable {
    private static final long serialVersionUID = -6849497313084944255L;

    private String name, motd, join_permission;

    protected List<String> clients;
    protected List<Template> templates;

    private int memory, minOnline, maxOnline, maxPlayers, startPort;

    private boolean maintenance, save_logs;
    private ServerModeType serverModeType;

    private AutoStart autoStart;
    private AutoStop autoStop;

    private SpigotVersions spigotVersions;

    @java.beans.ConstructorProperties({"name", "motd", "join_permission", "clients", "templates", "memory", "minOnline", "maxOnline", "maxPlayers", "startPort", "maintenance", "save_logs", "autoStart", "autoStop", "serverModeType", "spigotVersions"})
    public ServerGroup(String name, String motd, String join_permission, List<String> clients, List<Template> templates, int memory, int minOnline, int maxOnline, int maxPlayers, int startPort, boolean maintenance, boolean save_logs, AutoStart autoStart, AutoStop autoStop, ServerModeType serverModeType, SpigotVersions spigotVersions) {
        this.name = name;
        this.motd = motd;
        this.join_permission = join_permission;
        this.clients = clients;
        this.templates = templates;
        this.memory = memory;
        this.minOnline = minOnline;
        this.maxOnline = maxOnline;
        this.maxPlayers = maxPlayers;
        this.startPort = startPort;
        this.maintenance = maintenance;
        this.save_logs = save_logs;
        this.autoStart = autoStart;
        this.autoStop = autoStop;
        this.serverModeType = serverModeType;
        this.spigotVersions = spigotVersions;
    }

    public Template getTemplate(final String name) {
        return this.templates.stream().filter(template -> template.getName().equals(name)).findFirst().orElse(randomTemplate());
    }

    public Template getTemplateOrElseNull(final String name) {
        return this.templates.stream().filter(template -> template.getName().equals(name)).findFirst().orElse(null);
    }

    public Template randomTemplate() {
        if (this.templates.size() == 0) {
            return new Template("default", null, TemplateBackend.CLIENT);
        }

        return this.templates.get(new Random().nextInt(this.templates.size()));
    }

    public void deleteTemplate(String name) {
        List<Template> copyOf = new ArrayList<>(this.templates);
        copyOf.stream().filter(template -> template.getName().equals(name)).findFirst().ifPresent(template -> this.templates.remove(template));
    }

    public String getName() {
        return this.name;
    }

    public String getMotd() {
        return this.motd;
    }

    public String getJoin_permission() {
        return this.join_permission;
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

    public boolean isSave_logs() {
        return this.save_logs;
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

    public void setJoin_permission(String join_permission) {
        this.join_permission = join_permission;
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

    public void setSave_logs(boolean save_logs) {
        this.save_logs = save_logs;
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
