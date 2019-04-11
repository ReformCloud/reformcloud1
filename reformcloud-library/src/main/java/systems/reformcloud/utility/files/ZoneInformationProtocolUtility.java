/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.utility.files;

import systems.reformcloud.ReformCloudLibraryServiceProvider;
import systems.reformcloud.utility.StringUtil;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.LinkedList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * @author _Klaro | Pasqual K. / created on 06.12.2018
 */

public class ZoneInformationProtocolUtility {
    public static void unZip(File zippedPath, String destinationPath) throws Exception {
        File destDir = new File(destinationPath);
        if (!destDir.exists())
            destDir.mkdir();

        byte[] buffer = new byte[1024];
        ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(zippedPath));
        ZipEntry zipEntry = zipInputStream.getNextEntry();
        while (zipEntry != null) {
            String fileName = zipEntry.getName();
            File newFile = new File(destinationPath + "/" + fileName);
            if (!newFile.exists()) {
                if (zipEntry.isDirectory()) {
                    newFile.mkdirs();
                } else {
                    newFile.getParentFile().mkdirs();
                    newFile.createNewFile();
                }
            }

            if (!newFile.isDirectory()) {
                FileOutputStream fos = new FileOutputStream(newFile);
                int len;
                while ((len = zipInputStream.read(buffer)) > 0)
                    fos.write(buffer, 0, len);

                fos.close();
            }

            zipEntry = zipInputStream.getNextEntry();
        }
        zipInputStream.closeEntry();
        zipInputStream.close();
    }

    public static Collection<File> unZip(byte[] zippedBytes, File destDir) throws Exception {
        String destinationPath = destDir.toString();
        if (!destDir.exists()) {
            destDir.mkdir();
        }

        Collection<File> unzipped = new LinkedList<>();

        byte[] buffer = new byte[1024];
        ZipInputStream zis = new ZipInputStream(new ByteArrayInputStream(zippedBytes));
        ZipEntry zipEntry = zis.getNextEntry();
        while (zipEntry != null) {
            String fileName = zipEntry.getName();
            File newFile = new File(destinationPath + "/" + fileName);
            if (!unzipped.contains(newFile))
                unzipped.add(newFile);
            if (newFile.exists())
                newFile.delete();
            if (zipEntry.isDirectory()) {
                newFile.mkdirs();
            } else {
                newFile.getParentFile().mkdirs();
                newFile.createNewFile();
            }
            if (!newFile.isDirectory()) {
                FileOutputStream fos = new FileOutputStream(newFile);
                int len;
                while ((len = zis.read(buffer)) > 0) {
                    fos.write(buffer, 0, len);
                }
                fos.close();
            }
            zipEntry = zis.getNextEntry();
        }
        zis.closeEntry();
        zis.close();

        return unzipped;
    }

    public static void toZip(byte[] zip, Path to) {
        try {
            Files.write(to, zip);
        } catch (final IOException ex) {
            StringUtil.printError(
                    ReformCloudLibraryServiceProvider.getInstance().getLoggerProvider(),
                    "Error while writing byte array to zip file",
                    ex
            );
        }
    }

    public static void toZip(byte[] zip, File to) {
        toZip(zip, to.toPath());
    }

    public static void toZip(byte[] zip, String to) {
        toZip(zip, Paths.get(to));
    }

    public static Collection<File> unZip(byte[] zippedBytes, String destinationPath) throws Exception {
        File destDir = new File(destinationPath);
        return unZip(zippedBytes, destDir);
    }

    public static byte[] zipDirectoryToBytes(File file) {
        try {
            if (!file.exists())
                file.mkdirs();

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ZipOutputStream zipOut = new ZipOutputStream(byteArrayOutputStream);
            for (File child : file.listFiles())
                zipFile(child, child.getName(), zipOut);
            zipOut.close();
            byte[] bytes = byteArrayOutputStream.toByteArray();
            byteArrayOutputStream.close();
            return bytes;
        } catch (final IOException ex) {
            StringUtil.printError(ReformCloudLibraryServiceProvider.getInstance().getLoggerProvider(), "Error while zipping dir", ex);
            return null;
        }
    }

    public static byte[] zipDirectoryToBytes(Path path) {
        return zipDirectoryToBytes(path.toFile());
    }

    public static void zipDirectoryToFile(File path, String destinationPath) {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(destinationPath);
            ZipOutputStream zipOut = new ZipOutputStream(fileOutputStream);
            zipFile(path, path.getName(), zipOut);
            zipOut.close();
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (final IOException ex) {
            StringUtil.printError(ReformCloudLibraryServiceProvider.getInstance().getLoggerProvider(), "Error while zipping dir", ex);
        }
    }

    public static void zipDirectoryToFile(Path path, String destinationPath) {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(destinationPath);
            ZipOutputStream zipOut = new ZipOutputStream(fileOutputStream);
            File fileToZip = path.toFile();
            zipFile(fileToZip, fileToZip.getName(), zipOut);
            zipOut.close();
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (IOException ex) {
            StringUtil.printError(ReformCloudLibraryServiceProvider.getInstance().getLoggerProvider(), "Error while zipping dir", ex);
        }
    }

    private static void zipFile(File fileToZip, String fileName, ZipOutputStream zipOut) throws IOException {
        if (fileToZip.isHidden())
            return;

        if (fileToZip.isDirectory()) {
            File[] children = fileToZip.listFiles();
            for (File childFile : children)
                zipFile(childFile, fileName + "/" + childFile.getName(), zipOut);

            return;
        }

        FileInputStream fileInputStream = new FileInputStream(fileToZip);
        ZipEntry zipEntry = new ZipEntry(fileName);
        zipOut.putNextEntry(zipEntry);
        byte[] bytes = new byte[1024];
        int length;
        while ((length = fileInputStream.read(bytes)) >= 0)
            zipOut.write(bytes, 0, length);

        fileInputStream.close();
    }
}
