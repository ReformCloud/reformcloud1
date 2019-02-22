/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.versioneering;

import systems.reformcloud.ReformCloudLibraryService;
import systems.reformcloud.ReformCloudLibraryServiceProvider;
import systems.reformcloud.utility.StringUtil;
import systems.reformcloud.utility.files.DownloadManager;
import systems.reformcloud.utility.files.FileUtils;

import java.io.File;

/**
 * @author _Klaro | Pasqual K. / created on 08.01.2019
 */

public final class VersionUpdater {
    public void update() {
        if (!VersionLoader.getNewestVersion().equalsIgnoreCase(StringUtil.REFORM_VERSION)) {
            DownloadManager.downloadSilentAndDisconnect("https://dl.reformcloud.systems/update/latest/" + whereIAm() + ".jar", whereIAm() + "-Update-" + ReformCloudLibraryService.THREAD_LOCAL_RANDOM.nextLong() + ".jar");
            FileUtils.deleteOnExit(new File(FileUtils.getInternalFileName()));
            System.exit(1);
        }
    }

    private String whereIAm() {
        return ReformCloudLibraryServiceProvider.getInstance().getControllerIP() == null ? "ReformController" : "ReformClient";
    }
}
