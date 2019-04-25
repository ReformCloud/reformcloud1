/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.meta.server;

import systems.reformcloud.meta.Template;
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

    protected String name, motd, join_permission;

    protected List<String> clients;
    protected List<Template> templates;

    protected int memory, minOnline, maxOnline, maxPlayers, startPort;

    protected boolean maintenance, save_logs, one_startup_when_server_full;
    protected ServerModeType serverModeType;

    protected SpigotVersions spigotVersions;

    @java.beans.ConstructorProperties({"name", "motd", "join_permission", "clients", "templates", "memory", "minOnline", "maxOnline", "maxPlayers", "startPort", "maintenance", "save_logs", "one_startup_when_server_full", "serverModeType", "spigotVersions"})
    public ServerGroup(String name, String motd, String join_permission, List<String> clients, List<Template> templates, int memory, int minOnline, int maxOnline, int maxPlayers, int startPort, boolean maintenance, boolean save_logs, boolean one_startup_when_server_full, ServerModeType serverModeType, SpigotVersions spigotVersions) {
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
        this.one_startup_when_server_full = one_startup_when_server_full;
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

    public boolean isOne_startup_when_server_full() {
        return this.one_startup_when_server_full;
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

    public void setOne_startup_when_server_full(boolean one_startup_when_server_full) {
        this.one_startup_when_server_full = one_startup_when_server_full;
    }

    public void setServerModeType(ServerModeType serverModeType) {
        this.serverModeType = serverModeType;
    }

    public void setSpigotVersions(SpigotVersions spigotVersions) {
        this.spigotVersions = spigotVersions;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof ServerGroup)) return false;
        final ServerGroup other = (ServerGroup) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$name = this.getName();
        final Object other$name = other.getName();
        if (this$name == null ? other$name != null : !this$name.equals(other$name)) return false;
        final Object this$motd = this.getMotd();
        final Object other$motd = other.getMotd();
        if (this$motd == null ? other$motd != null : !this$motd.equals(other$motd)) return false;
        final Object this$join_permission = this.getJoin_permission();
        final Object other$join_permission = other.getJoin_permission();
        if (this$join_permission == null ? other$join_permission != null : !this$join_permission.equals(other$join_permission))
            return false;
        final Object this$clients = this.getClients();
        final Object other$clients = other.getClients();
        if (this$clients == null ? other$clients != null : !this$clients.equals(other$clients)) return false;
        final Object this$templates = this.getTemplates();
        final Object other$templates = other.getTemplates();
        if (this$templates == null ? other$templates != null : !this$templates.equals(other$templates)) return false;
        if (this.getMemory() != other.getMemory()) return false;
        if (this.getMinOnline() != other.getMinOnline()) return false;
        if (this.getMaxOnline() != other.getMaxOnline()) return false;
        if (this.getMaxPlayers() != other.getMaxPlayers()) return false;
        if (this.getStartPort() != other.getStartPort()) return false;
        if (this.isMaintenance() != other.isMaintenance()) return false;
        if (this.isSave_logs() != other.isSave_logs()) return false;
        if (this.isOne_startup_when_server_full() != other.isOne_startup_when_server_full()) return false;
        final Object this$serverModeType = this.getServerModeType();
        final Object other$serverModeType = other.getServerModeType();
        if (this$serverModeType == null ? other$serverModeType != null : !this$serverModeType.equals(other$serverModeType))
            return false;
        final Object this$spigotVersions = this.getSpigotVersions();
        final Object other$spigotVersions = other.getSpigotVersions();
        if (this$spigotVersions == null ? other$spigotVersions != null : !this$spigotVersions.equals(other$spigotVersions))
            return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof ServerGroup;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $name = this.getName();
        result = result * PRIME + ($name == null ? 43 : $name.hashCode());
        final Object $motd = this.getMotd();
        result = result * PRIME + ($motd == null ? 43 : $motd.hashCode());
        final Object $join_permission = this.getJoin_permission();
        result = result * PRIME + ($join_permission == null ? 43 : $join_permission.hashCode());
        final Object $clients = this.getClients();
        result = result * PRIME + ($clients == null ? 43 : $clients.hashCode());
        final Object $templates = this.getTemplates();
        result = result * PRIME + ($templates == null ? 43 : $templates.hashCode());
        result = result * PRIME + this.getMemory();
        result = result * PRIME + this.getMinOnline();
        result = result * PRIME + this.getMaxOnline();
        result = result * PRIME + this.getMaxPlayers();
        result = result * PRIME + this.getStartPort();
        result = result * PRIME + (this.isMaintenance() ? 79 : 97);
        result = result * PRIME + (this.isSave_logs() ? 79 : 97);
        result = result * PRIME + (this.isOne_startup_when_server_full() ? 79 : 97);
        final Object $serverModeType = this.getServerModeType();
        result = result * PRIME + ($serverModeType == null ? 43 : $serverModeType.hashCode());
        final Object $spigotVersions = this.getSpigotVersions();
        result = result * PRIME + ($spigotVersions == null ? 43 : $spigotVersions.hashCode());
        return result;
    }

    public String toString() {
        return "ServerGroup(name=" + this.getName() + ", motd=" + this.getMotd() + ", join_permission=" + this.getJoin_permission() + ", clients=" + this.getClients() + ", templates=" + this.getTemplates() + ", memory=" + this.getMemory() + ", minOnline=" + this.getMinOnline() + ", maxOnline=" + this.getMaxOnline() + ", maxPlayers=" + this.getMaxPlayers() + ", startPort=" + this.getStartPort() + ", maintenance=" + this.isMaintenance() + ", save_logs=" + this.isSave_logs() + ", one_startup_when_server_full=" + this.isOne_startup_when_server_full() + ", serverModeType=" + this.getServerModeType() + ", spigotVersions=" + this.getSpigotVersions() + ")";
    }
}
