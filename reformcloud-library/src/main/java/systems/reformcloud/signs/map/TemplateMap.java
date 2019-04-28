/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.signs.map;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 09.02.2019
 */

public final class TemplateMap<F, S, T> implements Serializable {
    /**
     * The name of the group
     */
    private F group;

    /**
     * The name of the template
     */
    private S template;

    /**
     * The template sign layout
     */
    private T layout;

    @java.beans.ConstructorProperties({"group", "template", "layout"})
    public TemplateMap(F group, S template, T layout) {
        this.group = group;
        this.template = template;
        this.layout = layout;
    }

    public F getGroup() {
        return this.group;
    }

    public S getTemplate() {
        return this.template;
    }

    public T getLayout() {
        return this.layout;
    }
}
