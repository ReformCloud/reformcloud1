/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.utility.files;

import systems.reformcloud.ReformCloudLibraryServiceProvider;
import systems.reformcloud.utility.StringUtil;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * @author _Klaro | Pasqual K. / created on 06.12.2018
 */

public class ZoneInformationProtocolUtility {
    /**
     * Extracts the given file
     */
    public static void extract(final String oldFile, final String to) {
        try {
            ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(oldFile));
            ZipEntry zipEntry = zipInputStream.getNextEntry();

            while (zipEntry != null) {
                String filePath = to + File.separator + zipEntry.getName();
                if (!zipEntry.isDirectory())
                    extractFile(zipInputStream, filePath);
                else
                    new File(filePath).mkdir();

                zipInputStream.closeEntry();
                zipEntry = zipInputStream.getNextEntry();
            }
            zipInputStream.close();
        } catch (final IOException ex) {
            StringUtil.printError(ReformCloudLibraryServiceProvider.getInstance().getLoggerProvider(), "Error while unpacking zip file", ex);
        }
    }

    /**
     * Extracts a file out of a specific {@link ZipInputStream} to {@param filePath}
     *
     * @param zipIn
     * @param filePath
     * @throws IOException if the file was not found
     */
    private static void extractFile(final ZipInputStream zipIn, final String filePath) throws IOException {
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(filePath));
        byte[] bytesIn = new byte[4096];
        int read;

        while ((read = zipIn.read(bytesIn)) != -1)
            bufferedOutputStream.write(bytesIn, 0, read);

        bufferedOutputStream.close();
    }

    /**
     * Starts the zip of a file
     *
     * @param sourceFile
     * @param newFile
     * @throws IOException
     */
    public static void zipDirectory(final String sourceFile, final String newFile) throws IOException {
        final FileOutputStream fileOutputStream = new FileOutputStream(newFile);
        ZipOutputStream zipOut = new ZipOutputStream(fileOutputStream);
        File fileToZip = new File(sourceFile);

        zipFile(fileToZip, fileToZip.getName(), zipOut);
        zipOut.close();
        fileOutputStream.close();
    }

    /**
     * Puts the given files into the new zip file
     *
     * @param fileToZip
     * @param fileName
     * @param zipOut
     * @throws IOException
     */
    private static void zipFile(File fileToZip, String fileName, ZipOutputStream zipOut) throws IOException {
        if (fileToZip.isHidden())
            return;

        if (fileToZip.isDirectory()) {
            if (fileName.endsWith("/")) {
                zipOut.putNextEntry(new ZipEntry(fileName));
                zipOut.closeEntry();
            } else {
                zipOut.putNextEntry(new ZipEntry(fileName + "/"));
                zipOut.closeEntry();
            }
            File[] children = fileToZip.listFiles();
            for (File childFile : children)
                zipFile(childFile, fileName + "/" + childFile.getName(), zipOut);

            return;
        }
        FileInputStream fis = new FileInputStream(fileToZip);
        ZipEntry zipEntry = new ZipEntry(fileName);
        zipOut.putNextEntry(zipEntry);
        byte[] bytes = new byte[1024];
        int length;
        while ((length = fis.read(bytes)) >= 0)
            zipOut.write(bytes, 0, length);

        fis.close();
    }
}
