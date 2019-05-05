/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.template;

import systems.reformcloud.utility.files.DownloadManager;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 30.10.2018
 */

public final class TemplatePreparer implements Serializable {
    private String old;

    @java.beans.ConstructorProperties({"old"})
    public TemplatePreparer(String old) {
        this.old = old;
    }

    /**
     * Downloads the packet with the {@link DownloadManager}
     *
     * @param url       The url where the template is located
     */
    public void loadTemplate(String url) {
        if (url.isEmpty())
            return;

        DownloadManager.downloadSilentAndDisconnect(url, old);
    }
}
