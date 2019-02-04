/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud;

import systems.reformcloud.utility.StringUtil;
import systems.reformcloud.utility.files.DownloadManager;
import systems.reformcloud.utility.files.FileUtils;

import java.io.File;
import java.nio.file.Paths;

/**
 * @author _Klaro | Pasqual K. / created on 15.01.2019
 */

public final class Installer {
    public static void main(final String... args) {
        ReformCloudLibraryService.sendHeader();
        System.out.println();
        System.out.println("Trying to pre-install ReformCloud...");

        for (File directory : new File[] {
                new File("ReformController"),
                new File("ReformClient")
        })
        directory.mkdirs();

        //TODO: Change download links
        DownloadManager.downloadSilent("https://dl.klarcloudservice.de/update/latest/KlarCloudService-Controller.jar", "ReformController/ReformController.jar");
        DownloadManager.downloadSilent("https://dl.klarcloudservice.de/update/latest/KlarCloudService-Client.jar", "ReformClient/ReformClient.jar");

        System.out.println("Download completed");

        final boolean windows = StringUtil.OS_NAME.contains("Windows");
        if (windows) {
            System.out.println("Detected Windows, creating \"start.bat\"...");
            FileUtils.writeToFile(Paths.get("ReformController/start.bat"), "java -XX:+UseG1GC -XX:MaxGCPauseMillis=50 -XX:+UseCompressedOops -Xmx512m -Xms256m -jar ReformController.jar");
            FileUtils.writeToFile(Paths.get("ReformClient/start.bat"), "java -XX:+UseG1GC -XX:MaxGCPauseMillis=50 -XX:+UseCompressedOops -Xmx512m -Xms256m -jar ReformClient.jar");
        } else {
            System.out.println("Detected Linux, creating \"start.sh\"...");
            FileUtils.writeToFile(Paths.get("ReformController/start.sh"), "screen -S ReformController java -XX:+UseG1GC -XX:MaxGCPauseMillis=50 -XX:+UseCompressedOops -Xmx512m -Xms256m -jar ReformController.jar");
            FileUtils.writeToFile(Paths.get("ReformClient/start.sh"), "screen -S ReformClient java -XX:+UseG1GC -XX:MaxGCPauseMillis=50 -XX:+UseCompressedOops -Xmx512m -Xms256m -jar ReformClient.jar");
        }

        System.out.println("ReformCloud has been installed successfully.");
        System.out.println("Deleting this now");
        ReformCloudLibraryService.sleep(1000);

        FileUtils.deleteOnExit(new File(FileUtils.getInternalFileName()));
        System.exit(1);
    }
}
