/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.utility.files;

import systems.reformcloud.ReformCloudLibraryServiceProvider;
import systems.reformcloud.utility.StringUtil;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.EnumSet;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * @author _Klaro | Pasqual K. / created on 06.12.2018
 */

public final class ZoneInformationProtocolUtility implements Serializable {

    /**
     * Unzips a specific file
     *
     * @param zippedPath The path of the zipped directory
     * @param destinationPath The path where the file should be unzipped to
     * @throws Exception If any exception occurs
     */
    public static void unZip(File zippedPath, String destinationPath) throws Exception {
        File destDir = new File(destinationPath);
        if (!destDir.exists()) {
            destDir.mkdir();
        }

        if (destDir.isDirectory()) {
            FileUtils.deleteFullDirectory(destDir);
        } else {
            FileUtils.deleteFileIfExists(destDir);
        }

        byte[] buffer = new byte[0x1FFF];
        ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(zippedPath));
        ZipEntry zipEntry = zipInputStream.getNextEntry();
        while (zipEntry != null) {
            File newFile = new File(destinationPath + "/" + zipEntry.getName());
            if (zipEntry.isDirectory()) {
                newFile.mkdirs();
            } else {
                newFile.getParentFile().mkdirs();
                newFile.createNewFile();

                try (OutputStream outputStream = Files.newOutputStream(newFile.toPath())) {
                    int length;
                    while ((length = zipInputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, length);
                    }
                }
            }

            zipInputStream.closeEntry();
            zipEntry = zipInputStream.getNextEntry();
        }

        zipInputStream.closeEntry();
        zipInputStream.close();
    }

    /**
     * Unzips the given byte array
     *
     * @param zippedBytes The byte array of the zip file
     * @param destDir The path where the file should be unzipped to
     * @throws Exception If any exception occurs
     */
    private static void unZip(byte[] zippedBytes, File destDir) throws Exception {
        String destinationPath = destDir.toString();
        if (!destDir.exists()) {
            destDir.mkdir();
        }

        byte[] buffer = new byte[0x1FFF];
        try (ZipInputStream zipInputStream = new ZipInputStream(
            new ByteArrayInputStream(zippedBytes), StandardCharsets.UTF_8)) {
            ZipEntry zipEntry;
            while ((zipEntry = zipInputStream.getNextEntry()) != null) {
                File newFile = new File(destinationPath + "/" + zipEntry.getName());

                if (newFile.exists()) {
                    Files.deleteIfExists(newFile.toPath());
                }

                if (zipEntry.isDirectory()) {
                    Files.createDirectories(newFile.toPath());
                } else {
                    File parent = newFile.getParentFile();
                    if (!Files.exists(parent.toPath())) {
                        Files.createDirectories(parent.toPath());
                    }

                    Files.createFile(newFile.toPath());

                    try (OutputStream outputStream = Files.newOutputStream(newFile.toPath())) {
                        int length;
                        while ((length = zipInputStream.read(buffer)) != -1) {
                            outputStream.write(buffer, 0, length);
                        }

                        outputStream.flush();
                    }
                }

                zipInputStream.closeEntry();
            }

            zipInputStream.closeEntry();
        }
    }

    /**
     * Writes a byte array to a file
     *
     * @param zip The byte array of the file
     * @param to The target directory of the file
     */
    private static void toZip(byte[] zip, Path to) {
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

    /**
     * Unzips the given byte array
     *
     * @param zippedBytes The byte array of the zip file
     * @param destinationPath The path where the file should be unzipped to
     * @throws Exception If any exception occurs
     */
    public static void unZip(byte[] zippedBytes, String destinationPath) throws Exception {
        File destDir = new File(destinationPath);
        unZip(zippedBytes, destDir);
    }

    /**
     * Unzips the given byte array
     *
     * @param zippedBytes The byte array of the zip file
     * @param destinationPath The path where the file should be unzipped to
     * @throws Exception If any exception occurs
     */
    public static void unZip(byte[] zippedBytes, Path destinationPath) throws Exception {
        unZip(zippedBytes, destinationPath.toFile());
    }

    /**
     * Writes a byte array to a file
     *
     * @param zip The byte array of the file
     * @param to The target directory of the file
     */
    public static void toZip(byte[] zip, File to) {
        toZip(zip, to.toPath());
    }

    /**
     * Writes a byte array to a file
     *
     * @param zip The byte array of the file
     * @param to The target directory of the file
     */
    public static void toZip(byte[] zip, String to) {
        toZip(zip, Paths.get(to));
    }

    /**
     * Zips a specific zip file to a byte array
     *
     * @param file The file which should be zipped
     * @return The byte array of the file
     */
    private static byte[] zipDirectoryToBytes(File file) {
        try {
            if (!file.exists()) {
                file.mkdirs();
            }

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ZipOutputStream zipOut = new ZipOutputStream(byteArrayOutputStream,
                StandardCharsets.UTF_8);

            Files.walkFileTree(
                file.toPath(),
                EnumSet.noneOf(FileVisitOption.class),
                Integer.MAX_VALUE,
                new SimpleFileVisitor<Path>() {
                    @Override
                    public FileVisitResult visitFile(Path path, BasicFileAttributes attrs)
                        throws IOException {
                        try {
                            zipOut.putNextEntry(
                                new ZipEntry(file.toPath().relativize(path).toString()));
                            Files.copy(path, zipOut);
                            zipOut.closeEntry();
                        } catch (final Throwable throwable) {
                            zipOut.closeEntry();
                        }

                        return FileVisitResult.CONTINUE;
                    }
                }
            );

            zipOut.flush();
            zipOut.finish();
            zipOut.close();
            byte[] bytes = byteArrayOutputStream.toByteArray();
            byteArrayOutputStream.close();
            return bytes;
        } catch (final IOException ex) {
            StringUtil
                .printError(ReformCloudLibraryServiceProvider.getInstance().getLoggerProvider(),
                    "Error while zipping dir", ex);
            return null;
        }
    }

    /**
     * Zips a specific zip file to a byte array
     *
     * @param file The file which should be zipped
     * @param excluded The excluded files which should not be zipped
     * @return The byte array of the file
     */
    private static byte[] zipDirectoryToBytes(File file, List<String> excluded) {
        try {
            if (!file.exists()) {
                file.mkdirs();
            }

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ZipOutputStream zipOut = new ZipOutputStream(byteArrayOutputStream);
            for (File child : file.listFiles()) {
                if (excluded.contains(child.getName())) {
                    continue;
                }

                zipFile(child, child.getName(), zipOut);
            }

            zipOut.flush();
            zipOut.finish();
            zipOut.close();
            byteArrayOutputStream.flush();

            byte[] bytes = byteArrayOutputStream.toByteArray();
            byteArrayOutputStream.close();
            return bytes;
        } catch (final IOException ex) {
            StringUtil
                .printError(ReformCloudLibraryServiceProvider.getInstance().getLoggerProvider(),
                    "Error while zipping dir", ex);
            return null;
        }
    }

    /**
     * Zips a specific zip file to a byte array
     *
     * @param path The path of the file which should be zipped
     * @param excluded The excluded files which should not be zipped
     * @return The byte array of the file
     */
    public static byte[] zipDirectoryToBytes(Path path, List<String> excluded) {
        return zipDirectoryToBytes(path.toFile(), excluded);
    }

    /**
     * Zips a specific zip file to a byte array
     *
     * @param path The path of the file which should be zipped
     * @param excluded The excluded files which should not be zipped
     * @return The byte array of the file
     */
    public static byte[] zipDirectoryToBytes(String path, List<String> excluded) {
        return zipDirectoryToBytes(new File(path), excluded);
    }

    /**
     * Zips a specific zip file to a byte array
     *
     * @param path The path of the file which should be zipped
     * @return The byte array of the file
     */
    public static byte[] zipDirectoryToBytes(Path path) {
        return zipDirectoryToBytes(path.toFile());
    }

    /**
     * Zips a specific zip file to a byte array
     *
     * @param path The path of the file which should be zipped
     * @return The byte array of the file
     */
    public static byte[] zipDirectoryToBytes(String path) {
        return zipDirectoryToBytes(new File(path));
    }

    /**
     * Zips a specific zip file to a byte array
     *
     * @param path The path of the file which should be zipped
     * @return The byte array of the file
     */
    public static byte[] zipToBytes(Path path) {
        File file = path.toFile();
        try {
            if (!file.exists()) {
                return new byte[1024];
            }

            FileInputStream fileInputStream = new FileInputStream(file);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

            byte[] buffer = new byte[1024];
            int read;
            while ((read = fileInputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, read);
            }

            return byteArrayOutputStream.toByteArray();
        } catch (final IOException ex) {
            StringUtil
                .printError(ReformCloudLibraryServiceProvider.getInstance().getLoggerProvider(),
                    "Error while zipping dir", ex);
            return null;
        }
    }

    /**
     * Zips a specific zip file
     *
     * @param path The path of the file which should be zipped
     * @param destinationPath The path where the zipped file should be saved to
     */
    private static void zipDirectoryToFile(File path, String destinationPath) {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(destinationPath);
            ZipOutputStream zipOut = new ZipOutputStream(fileOutputStream);
            zipFile(path, path.getName(), zipOut);
            zipOut.close();
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (final IOException ex) {
            StringUtil
                .printError(ReformCloudLibraryServiceProvider.getInstance().getLoggerProvider(),
                    "Error while zipping dir", ex);
        }
    }

    /**
     * Zips a specific zip file
     *
     * @param path The path of the file which should be zipped
     * @param destinationPath The path where the zipped file should be saved to
     */
    public static void zipDirectoryToFile(Path path, String destinationPath) {
        zipDirectoryToFile(path.toFile(), destinationPath);
    }

    /**
     * Zips a specific zip file
     *
     * @param path The path of the file which should be zipped
     * @param destinationPath The path where the zipped file should be saved to
     */
    public static void zipDirectoryToFile(String path, String destinationPath) {
        zipDirectoryToFile(new File(path), destinationPath);
    }

    /**
     * Zips a specific zip file
     *
     * @param fileToZip The file which should be zipped
     * @param fileName The file name
     * @param zipOut The zip output stream
     */
    private static void zipFile(File fileToZip, String fileName, ZipOutputStream zipOut)
        throws IOException {
        if (fileToZip.isHidden()) {
            return;
        }

        if (fileToZip.isDirectory()) {
            File[] children = fileToZip.listFiles();
            for (File childFile : children) {
                zipFile(childFile, fileName + "/" + childFile.getName(), zipOut);
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
}
