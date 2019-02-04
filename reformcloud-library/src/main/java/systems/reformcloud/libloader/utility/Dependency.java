/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.libloader.utility;

import lombok.Getter;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 22.01.2019
 */

@Getter
public abstract class Dependency implements Serializable {
    private static final long serialVersionUID = 8492066666707362125L;

    public String download_url = "http://central.maven.org/maven2/";

    protected Dependency(final String url) {
        if (url != null)
            this.download_url = url;
    }

    public abstract String getGroupID();

    public abstract String getName();

    public abstract String getVersion();
}
