/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.addons.dependency.util;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 17.05.2019
 */

public abstract class DynamicDependency implements Serializable {

    private static final long serialVersionUID = 697761527505608255L;

    /**
     * The download url of the dependency
     */
    public String downloadUrl = "http://central.maven.org/maven2/";

    /**
     * Creates a new dynamic dependency using the default url
     */
    protected DynamicDependency() {
        this(null);
    }

    /**
     * Creates a new constructor of the dependency
     *
     * @param url The download url of the dependency or {@code null} if the cloud should use the
     * default url
     */
    protected DynamicDependency(final String url) {
        if (url != null) {
            this.downloadUrl = url.endsWith("/") ? url : url + "/";
        }
    }

    /**
     * The group id of the dependency
     *
     * @return The group id of the dependency
     */
    public abstract String getGroupID();

    /**
     * The name of the dependency
     *
     * @return The name of the dependency
     */
    public abstract String getName();

    /**
     * The version id of the dependency
     *
     * @return The version id of the dependency
     */
    public abstract String getVersion();

    public String getDownloadUrl() {
        return this.downloadUrl;
    }
}
