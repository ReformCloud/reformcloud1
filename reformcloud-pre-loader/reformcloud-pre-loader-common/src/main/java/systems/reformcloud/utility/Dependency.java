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
    private String downloadUrl = "http://central.maven.org/maven2/";

    /**
     * The version of the current dependency
     */
    public String version;

    /**
     * Initializes the dependency using the default url
     *
     * @deprecated Use version initialized constructor
     */
    @Deprecated
    protected Dependency() {
        this(null, "1.0");
    }

    /**
     * Creates a new constructor of the dependency
     *
     * @param url The download url of the dependency or {@code null} if the
     *            cloud should use the default url
     * @param defaultVersion The default version of the dependency if the
     *                       current version cannot be loaded
     */
    protected Dependency(final String url, final String defaultVersion) {
        if (url != null) {
            this.downloadUrl = url;
        }

        if (defaultVersion == null) {
            throw new IllegalStateException("default version may not be null");
        }

        this.version = defaultVersion;
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
    public String getVersion() {
        return this.version;
    }

    /**
     * Sets the version of the current dependency
     *
     * @param version The new version of the dependency
     * @return The current dependency instance
     */
    public Dependency setVersion(String version) {
        this.version = version;
        return this;
    }

    public String downloadUrl() {
        return this.downloadUrl;
    }
}
