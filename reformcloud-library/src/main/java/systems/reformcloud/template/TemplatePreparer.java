/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.template;

import systems.reformcloud.utility.files.DownloadManager;
import lombok.AllArgsConstructor;

/**
 * @author _Klaro | Pasqual K. / created on 30.10.2018
 */

@AllArgsConstructor
public class TemplatePreparer {
    private String old;

    /**
     * Downloads the packet with the {@link DownloadManager}
     *
     * @param url
     * @return this
     */
    public void loadTemplate(String url) {
        if (url.isEmpty())
            return;

        DownloadManager.downloadSilentAndDisconnect(url, old);
    }
}
