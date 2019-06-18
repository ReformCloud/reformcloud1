/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.meta;

import java.io.Serializable;
import systems.reformcloud.meta.enums.TemplateBackend;

/**
 * @author _Klaro | Pasqual K. / created on 21.10.2018
 */

public class Template implements Serializable {

    private static final long serialVersionUID = -7937892033580579125L;

    /**
     * The name of the template
     */
    private String name;

    /**
     * The url of the template
     */
    private String templateUrl;

    /**
     * The backend of the template
     */
    private TemplateBackend templateBackend;

    @java.beans.ConstructorProperties({"name", "templateUrl", "templateBackend"})
    public Template(String name, String templateUrl, TemplateBackend templateBackend) {
        this.name = name;
        this.templateUrl = templateUrl;
        this.templateBackend = templateBackend;
    }

    public String getName() {
        return this.name;
    }

    public String getTemplateUrl() {
        return this.templateUrl;
    }

    public TemplateBackend getTemplateBackend() {
        return this.templateBackend;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTemplateUrl(String templateUrl) {
        this.templateUrl = templateUrl;
    }

    public void setTemplateBackend(TemplateBackend templateBackend) {
        this.templateBackend = templateBackend;
    }
}
