/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.meta.startup;

import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.meta.proxy.ProxyGroup;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * @author _Klaro | Pasqual K. / created on 29.10.2018
 */

public class ProxyStartupInfo implements Serializable {
    private static final long serialVersionUID = -295123813122792999L;

    private UUID uid;
    private String name, template;
    private ProxyGroup proxyGroup;
    private Configuration configuration;
    private int id;

    @java.beans.ConstructorProperties({"uid", "name", "template", "proxyGroup", "configuration", "id"})
    public ProxyStartupInfo(UUID uid, String name, String template, ProxyGroup proxyGroup, Configuration configuration, int id) {
        this.uid = uid;
        this.name = name;
        this.template = template;
        this.proxyGroup = proxyGroup;
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

    public ProxyGroup getProxyGroup() {
        return this.proxyGroup;
    }

    public Configuration getConfiguration() {
        return this.configuration;
    }

    public int getId() {
        return this.id;
    }

    public void setUid(UUID uid) {
        this.uid = uid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public void setProxyGroup(ProxyGroup proxyGroup) {
        this.proxyGroup = proxyGroup;
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof ProxyStartupInfo)) return false;
        final ProxyStartupInfo other = (ProxyStartupInfo) o;
        if (!other.canEqual(this)) return false;
        final Object this$uid = this.getUid();
        final Object other$uid = other.getUid();
        if (!Objects.equals(this$uid, other$uid)) return false;
        final Object this$name = this.getName();
        final Object other$name = other.getName();
        if (!Objects.equals(this$name, other$name)) return false;
        final Object this$template = this.getTemplate();
        final Object other$template = other.getTemplate();
        if (!Objects.equals(this$template, other$template)) return false;
        final Object this$proxyGroup = this.getProxyGroup();
        final Object other$proxyGroup = other.getProxyGroup();
        if (!Objects.equals(this$proxyGroup, other$proxyGroup))
            return false;
        final Object this$configuration = this.getConfiguration();
        final Object other$configuration = other.getConfiguration();
        if (!Objects.equals(this$configuration, other$configuration))
            return false;
        if (this.getId() != other.getId()) return false;
        return true;
    }

    private boolean canEqual(final Object other) {
        return other instanceof ProxyStartupInfo;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $uid = this.getUid();
        result = result * PRIME + ($uid == null ? 43 : $uid.hashCode());
        final Object $name = this.getName();
        result = result * PRIME + ($name == null ? 43 : $name.hashCode());
        final Object $template = this.getTemplate();
        result = result * PRIME + ($template == null ? 43 : $template.hashCode());
        final Object $proxyGroup = this.getProxyGroup();
        result = result * PRIME + ($proxyGroup == null ? 43 : $proxyGroup.hashCode());
        final Object $configuration = this.getConfiguration();
        result = result * PRIME + ($configuration == null ? 43 : $configuration.hashCode());
        result = result * PRIME + this.getId();
        return result;
    }

    public String toString() {
        return "ProxyStartupInfo(uid=" + this.getUid() + ", name=" + this.getName() + ", template=" + this.getTemplate() + ", proxyGroup=" + this.getProxyGroup() + ", configuration=" + this.getConfiguration() + ", id=" + this.getId() + ")";
    }
}
