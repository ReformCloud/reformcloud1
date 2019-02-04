/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.meta.server;

import lombok.AllArgsConstructor;
import lombok.Data;
import systems.reformcloud.meta.Template;
import systems.reformcloud.meta.enums.ServerModeType;
import systems.reformcloud.meta.enums.TemplateBackend;
import systems.reformcloud.meta.server.versions.SpigotVersions;

import java.io.Serializable;
import java.util.List;
import java.util.Random;

/**
 * @author _Klaro | Pasqual K. / created on 21.10.2018
 */

@AllArgsConstructor
@Data
public class ServerGroup implements Serializable {
    private static final long serialVersionUID = -6849497313084944255L;

    protected String name, motd, join_permission;

    protected List<String> clients;
    protected List<Template> templates;

    protected int memory, minOnline, maxOnline, maxPlayers, startPort;

    protected boolean maintenance;
    protected ServerModeType serverModeType;

    protected SpigotVersions spigotVersions;

    public Template getTemplate(final String name) {
        return this.templates.stream().filter(template -> template.getName().equals(name)).findFirst().orElse(randomTemplate());
    }

    public Template randomTemplate() {
        if (this.templates.size() == 0) {
            return new Template("default", null, TemplateBackend.CLIENT);
        }

        return this.templates.get(new Random().nextInt(this.templates.size()));
    }
}
