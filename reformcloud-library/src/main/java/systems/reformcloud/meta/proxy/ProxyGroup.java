/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.meta.proxy;

import systems.reformcloud.meta.Template;
import systems.reformcloud.meta.auto.start.AutoStart;
import systems.reformcloud.meta.auto.stop.AutoStop;
import systems.reformcloud.meta.enums.ProxyModeType;
import systems.reformcloud.meta.enums.TemplateBackend;
import systems.reformcloud.meta.proxy.versions.ProxyVersions;

import java.io.Serializable;
import java.util.*;

/**
 * @author _Klaro | Pasqual K. / created on 21.10.2018
 */

public class ProxyGroup implements Serializable {
    private static final long serialVersionUID = 4196459006374952552L;

    private String name;

    private List<String> clients, disabledServerGroups;
    private List<Template> templates;
    private Collection<UUID> whitelist;

    private AutoStart autoStart;
    private AutoStop autoStop;

    private ProxyModeType proxyModeType;

    private boolean controllerCommandLogging, maintenance, save_logs;

    private int startPort, minOnline, maxOnline, maxPlayers, memory;

    protected ProxyVersions proxyVersions;

    @java.beans.ConstructorProperties({"name", "clients", "disabledServerGroups", "templates", "whitelist", "proxyModeType", "autoStart", "autoStop", "controllerCommandLogging", "maintenance", "save_logs", "startPort", "minOnline", "maxOnline", "maxPlayers", "memory", "proxyVersions"})
    public ProxyGroup(String name, List<String> clients, List<String> disabledServerGroups, List<Template> templates, Collection<UUID> whitelist, ProxyModeType proxyModeType, AutoStart autoStart, AutoStop autoStop, boolean controllerCommandLogging, boolean maintenance, boolean save_logs, int startPort, int minOnline, int maxOnline, int maxPlayers, int memory, ProxyVersions proxyVersions) {
        this.name = name;
        this.clients = clients;
        this.disabledServerGroups = disabledServerGroups;
        this.templates = templates;
        this.whitelist = whitelist;
        this.proxyModeType = proxyModeType;
        this.controllerCommandLogging = controllerCommandLogging;
        this.autoStart = autoStart;
        this.autoStop = autoStop;
        this.maintenance = maintenance;
        this.save_logs = save_logs;
        this.startPort = startPort;
        this.minOnline = minOnline;
        this.maxOnline = maxOnline;
        this.maxPlayers = maxPlayers;
        this.memory = memory;
        this.proxyVersions = proxyVersions;
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

    public boolean isStatic() {
        return this.proxyModeType.equals(ProxyModeType.STATIC);
    }

    public String getName() {
        return this.name;
    }

    public List<String> getClients() {
        return this.clients;
    }

    public List<String> getDisabledServerGroups() {
        return this.disabledServerGroups;
    }

    public List<Template> getTemplates() {
        return this.templates;
    }

    public Collection<UUID> getWhitelist() {
        return this.whitelist;
    }

    public ProxyModeType getProxyModeType() {
        return this.proxyModeType;
    }

    public boolean isControllerCommandLogging() {
        return this.controllerCommandLogging;
    }

    public boolean isMaintenance() {
        return this.maintenance;
    }

    public boolean isSave_logs() {
        return this.save_logs;
    }

    public int getStartPort() {
        return this.startPort;
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

    public int getMemory() {
        return this.memory;
    }

    public ProxyVersions getProxyVersions() {
        return this.proxyVersions;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setClients(List<String> clients) {
        this.clients = clients;
    }

    public void setDisabledServerGroups(List<String> disabledServerGroups) {
        this.disabledServerGroups = disabledServerGroups;
    }

    public void setTemplates(List<Template> templates) {
        this.templates = templates;
    }

    public void setWhitelist(Collection<UUID> whitelist) {
        this.whitelist = whitelist;
    }

    public void setProxyModeType(ProxyModeType proxyModeType) {
        this.proxyModeType = proxyModeType;
    }

    public void setControllerCommandLogging(boolean controllerCommandLogging) {
        this.controllerCommandLogging = controllerCommandLogging;
    }

    public void setMaintenance(boolean maintenance) {
        this.maintenance = maintenance;
    }

    public void setSave_logs(boolean save_logs) {
        this.save_logs = save_logs;
    }

    public void setStartPort(int startPort) {
        this.startPort = startPort;
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

    public void setMemory(int memory) {
        this.memory = memory;
    }

    public void setProxyVersions(ProxyVersions proxyVersions) {
        this.proxyVersions = proxyVersions;
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

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof ProxyGroup)) return false;
        final ProxyGroup other = (ProxyGroup) o;
        if (!other.canEqual(this)) return false;
        final Object this$name = this.getName();
        final Object other$name = other.getName();
        if (!Objects.equals(this$name, other$name)) return false;
        final Object this$clients = this.getClients();
        final Object other$clients = other.getClients();
        if (!Objects.equals(this$clients, other$clients)) return false;
        final Object this$disabledServerGroups = this.getDisabledServerGroups();
        final Object other$disabledServerGroups = other.getDisabledServerGroups();
        if (!Objects.equals(this$disabledServerGroups, other$disabledServerGroups))
            return false;
        final Object this$templates = this.getTemplates();
        final Object other$templates = other.getTemplates();
        if (!Objects.equals(this$templates, other$templates)) return false;
        final Object this$whitelist = this.getWhitelist();
        final Object other$whitelist = other.getWhitelist();
        if (!Objects.equals(this$whitelist, other$whitelist)) return false;
        final Object this$proxyModeType = this.getProxyModeType();
        final Object other$proxyModeType = other.getProxyModeType();
        if (!Objects.equals(this$proxyModeType, other$proxyModeType))
            return false;
        if (this.isControllerCommandLogging() != other.isControllerCommandLogging()) return false;
        if (this.isMaintenance() != other.isMaintenance()) return false;
        if (this.isSave_logs() != other.isSave_logs()) return false;
        if (this.getStartPort() != other.getStartPort()) return false;
        if (this.getMinOnline() != other.getMinOnline()) return false;
        if (this.getMaxOnline() != other.getMaxOnline()) return false;
        if (this.getMaxPlayers() != other.getMaxPlayers()) return false;
        if (this.getMemory() != other.getMemory()) return false;
        final Object this$proxyVersions = this.getProxyVersions();
        final Object other$proxyVersions = other.getProxyVersions();
        if (!Objects.equals(this$proxyVersions, other$proxyVersions))
            return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof ProxyGroup;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $name = this.getName();
        result = result * PRIME + ($name == null ? 43 : $name.hashCode());
        final Object $clients = this.getClients();
        result = result * PRIME + ($clients == null ? 43 : $clients.hashCode());
        final Object $disabledServerGroups = this.getDisabledServerGroups();
        result = result * PRIME + ($disabledServerGroups == null ? 43 : $disabledServerGroups.hashCode());
        final Object $templates = this.getTemplates();
        result = result * PRIME + ($templates == null ? 43 : $templates.hashCode());
        final Object $whitelist = this.getWhitelist();
        result = result * PRIME + ($whitelist == null ? 43 : $whitelist.hashCode());
        final Object $proxyModeType = this.getProxyModeType();
        result = result * PRIME + ($proxyModeType == null ? 43 : $proxyModeType.hashCode());
        result = result * PRIME + (this.isControllerCommandLogging() ? 79 : 97);
        result = result * PRIME + (this.isMaintenance() ? 79 : 97);
        result = result * PRIME + (this.isSave_logs() ? 79 : 97);
        result = result * PRIME + this.getStartPort();
        result = result * PRIME + this.getMinOnline();
        result = result * PRIME + this.getMaxOnline();
        result = result * PRIME + this.getMaxPlayers();
        result = result * PRIME + this.getMemory();
        final Object $proxyVersions = this.getProxyVersions();
        result = result * PRIME + ($proxyVersions == null ? 43 : $proxyVersions.hashCode());
        return result;
    }

    public String toString() {
        return "ProxyGroup(name=" + this.getName() + ", clients=" + this.getClients() + ", disabledServerGroups=" + this.getDisabledServerGroups() + ", templates=" + this.getTemplates() + ", whitelist=" + this.getWhitelist() + ", proxyModeType=" + this.getProxyModeType() + ", controllerCommandLogging=" + this.isControllerCommandLogging() + ", maintenance=" + this.isMaintenance() + ", save_logs=" + this.isSave_logs() + ", startPort=" + this.getStartPort() + ", minOnline=" + this.getMinOnline() + ", maxOnline=" + this.getMaxOnline() + ", maxPlayers=" + this.getMaxPlayers() + ", memory=" + this.getMemory() + ", proxyVersions=" + this.getProxyVersions() + ")";
    }
}
