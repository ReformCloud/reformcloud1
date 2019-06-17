/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.backup.util;

import systems.reformcloud.ReformCloudLibraryServiceProvider;
import systems.reformcloud.utility.StringUtil;

import java.io.*;
import java.nio.file.Path;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author _Klaro | Pasqual K. / created on 12.05.2019
 */

final class ZipUtil implements Serializable {

    private static void zipFile(File fileToZip, String fileName, ZipOutputStream zipOut,
        List<String> skip) throws IOException {
        if (fileToZip.isDirectory()) {
            File[] children = fileToZip.listFiles();
            for (File childFile : children) {
                if (isSkipped(skip, childFile.toPath())) {
                    continue;
                }

                zipFile(childFile, fileName + "/" + childFile.getName(), zipOut, skip);
            }

            return;
        }

        FileInputStream fileInputStream = new FileInputStream(fileToZip);
        ZipEntry zipEntry = new ZipEntry(fileName);
        zipOut.putNextEntry(zipEntry);
        byte[] bytes = new byte[1024];
        int length;
        while ((length = fileInputStream.read(bytes)) >= 0) {
            zipOut.write(bytes, 0, length);
        }

        fileInputStream.close();
    }

    static void zipDirectoryToFile(File path, String destinationPath, List<String> skip) {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(destinationPath);
            ZipOutputStream zipOut = new ZipOutputStream(fileOutputStream);
            zipFile(path, path.getName(), zipOut, skip);
            zipOut.close();
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (final IOException ex) {
            StringUtil
                .printError(ReformCloudLibraryServiceProvider.getInstance().getColouredConsoleProvider(),
                    "Error while zipping dir", ex);
        }
    }

    private static boolean isSkipped(List<String> skip, Path filePath) {
        return skip.stream().anyMatch(e -> filePath.toString().equals(e.replace("/", "\\")));
    }
}
