/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.meta;

import systems.reformcloud.meta.enums.TemplateBackend;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 21.10.2018
 */

public class Template implements Serializable {
    private static final long serialVersionUID = -7937892033580579125L;

    private String name, template_url;
    private TemplateBackend templateBackend;

    @java.beans.ConstructorProperties({"name", "template_url", "templateBackend"})
    public Template(String name, String template_url, TemplateBackend templateBackend) {
        this.name = name;
        this.template_url = template_url;
        this.templateBackend = templateBackend;
    }

    public String getName() {
        return this.name;
    }

    public String getTemplate_url() {
        return this.template_url;
    }

    public TemplateBackend getTemplateBackend() {
        return this.templateBackend;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTemplate_url(String template_url) {
        this.template_url = template_url;
    }

    public void setTemplateBackend(TemplateBackend templateBackend) {
        this.templateBackend = templateBackend;
    }
}
