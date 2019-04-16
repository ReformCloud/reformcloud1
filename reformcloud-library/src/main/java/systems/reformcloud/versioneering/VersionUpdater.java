/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.versioneering;

import systems.reformcloud.ReformCloudLibraryService;
import systems.reformcloud.ReformCloudLibraryServiceProvider;
import systems.reformcloud.utility.ExitUtil;
import systems.reformcloud.utility.StringUtil;
import systems.reformcloud.utility.files.DownloadManager;
import systems.reformcloud.utility.files.FileUtils;

import java.io.File;
import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 08.01.2019
 */

public final class VersionUpdater implements Serializable {
    /**
     * Downloads the latest reformcloud version
     */
    public void update() {
        if (!VersionLoader.getNewestVersion().equalsIgnoreCase(StringUtil.REFORM_VERSION)) {
            DownloadManager.downloadSilentAndDisconnect("https://dl.reformcloud.systems/update/latest/" + whereIAm() + ".jar", whereIAm() + "-Update-" + ReformCloudLibraryService.THREAD_LOCAL_RANDOM.nextLong() + ".jar");
            FileUtils.deleteOnExit(new File(FileUtils.getInternalFileName()));
            System.exit(ExitUtil.VERSION_UPDATE);
        }
    }

    /**
     * Checks if the jar is the controller or client
     *
     * @return The file name related to the runtime
     */
    private String whereIAm() {
        return ReformCloudLibraryServiceProvider.getInstance().getControllerIP() == null ? "ReformController" : "ReformClient";
    }
}
