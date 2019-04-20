/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.meta.proxy;

import lombok.AllArgsConstructor;
import lombok.Data;
import systems.reformcloud.meta.Template;
import systems.reformcloud.meta.enums.ProxyModeType;
import systems.reformcloud.meta.enums.TemplateBackend;
import systems.reformcloud.meta.proxy.versions.ProxyVersions;

import java.io.Serializable;
import java.util.*;

/**
 * @author _Klaro | Pasqual K. / created on 21.10.2018
 */

@AllArgsConstructor
@Data
public class ProxyGroup implements Serializable {
    private static final long serialVersionUID = 4196459006374952552L;

    protected String name;

    protected List<String> clients, disabledServerGroups;
    protected List<Template> templates;
    protected Collection<UUID> whitelist;

    protected ProxyModeType proxyModeType;

    protected boolean controllerCommandLogging, maintenance, save_logs;

    protected int startPort, minOnline, maxOnline, maxPlayers, memory;

    protected ProxyVersions proxyVersions;

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
}
