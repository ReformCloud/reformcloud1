/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.utility;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 22.01.2019
 */

public abstract class Dependency implements Serializable {
    private static final long serialVersionUID = 8492066666707362125L;

    /**
     * The download url of the dependency
     */
    public String download_url = "http://central.maven.org/maven2/";

    /**
     * Initializes the dependency using the default url
     */
    protected Dependency() {
        this(null);
    }

    /**
     * Creates a new constructor of the dependency
     *
     * @param url       The download url of the dependency or {@code null} if the cloud should use the default url
     */
    protected Dependency(final String url) {
        if (url != null)
            this.download_url = url;
    }

    /**
     * The group id of the dependency
     *
     * @return      The group id of the dependency
     */
    public abstract String getGroupID();

    /**
     * The name of the dependency
     *
     * @return      The name of the dependency
     */
    public abstract String getName();

    /**
     * The version id of the dependency
     *
     * @return      The version id of the dependency
     */
    public abstract String getVersion();

    public String getDownload_url() {
        return this.download_url;
    }
}
