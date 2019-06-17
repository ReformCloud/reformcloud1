/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.utility.files;

import systems.reformcloud.ReformCloudLibraryServiceProvider;
import systems.reformcloud.utility.Require;
import systems.reformcloud.utility.StringUtil;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author _Klaro | Pasqual K. / created on 30.10.2018
 */

public final class FileUtils implements Serializable {

    /**
     * Deletes a directory
     *
     * @param path The path of the directory which should be deleted
     */
    public static void deleteFullDirectory(Path path) {
        Require.requireNotNull(path);
        final File[] files = path.toFile().listFiles();
        if (files == null) {
            return;
        }

        for (File file : files) {
            if (file.isDirectory()) {
                deleteFullDirectory(file.toPath());
            } else {
                file.delete();
            }
        }

        path.toFile().delete();
    }

    /**
     * Deletes a directory
     *
     * @param path The path as file of the directory which should be deleted
     */
    public static void deleteFullDirectory(File path) {
        deleteFullDirectory(path.toPath());
    }

    /**
     * Deletes a directory
     *
     * @param path The path as string of the directory which should be deleted
     */
    public static void deleteFullDirectory(String path) {
        deleteFullDirectory(Paths.get(path));
    }

    /**
     * Copies a specific file
     *
     * @param from The current location of the file
     * @param to The new location of the file
     */
    public static void copyFile(final String from, final String to) {
        try {
            Files.copy(Paths.get(from), Paths.get(to), StandardCopyOption.REPLACE_EXISTING);
        } catch (final IOException ex) {
            StringUtil
                .printError(ReformCloudLibraryServiceProvider.getInstance().getColouredConsoleProvider(),
                    "Could not copy file", ex);
        }
    }

    /**
     * Copies a specific file
     *
     * @param from The current location as path of the file
     * @param to The new location as path of the file
     */
    public static void copyFile(final Path from, final Path to) {
        copyFile(from.toString(), to.toString());
    }

    /**
     * Copies a specific file
     *
     * @param from The current location as file of the file
     * @param to The new location as file of the file
     */
    public static void copyFile(final File from, final File to) {
        copyFile(from.toString(), to.toString());
    }

    /**
     * Copies a compiled file from the source to the new location
     *
     * @param from The source location of the file
     * @param to The new location of the file
     */
    public static void copyCompiledFile(final String from, final String to) {
        try (InputStream localInputStream = FileUtils.class.getClassLoader()
            .getResourceAsStream(from)) {
            Files.copy(localInputStream, Paths.get(to), StandardCopyOption.REPLACE_EXISTING);
        } catch (final IOException ex) {
            StringUtil
                .printError(ReformCloudLibraryServiceProvider.getInstance().getColouredConsoleProvider(),
                    "Could not copy local file", ex);
        }
    }

    /**
     * Copies a compiled file from the source to the new location
     *
     * @param from The source location as path of the file
     * @param to The new location as path of the file
     */
    public static void copyCompiledFile(final Path from, final Path to) {
        copyCompiledFile(from.toString(), to.toString());
    }

    /**
     * Copies a compiled file from the source to the new location
     *
     * @param from The source location as file of the file
     * @param to The new location as file of the file
     */
    public static void copyCompiledFile(final File from, final File to) {
        copyCompiledFile(from.toString(), to.toString());
    }

    /**
     * Renames a specific file
     *
     * @param file The file which should be renamed
     * @param newName The new name of the file
     */
    public static void rename(final String file, final String newName) {
        rename(Paths.get(file), newName);
    }

    /**
     * Renames a specific file
     *
     * @param file The file as path which should be renamed
     * @param newName The new name of the file
     */
    public static void rename(final Path file, final String newName) {
        rename(file.toFile(), newName);
    }


    /**
     * Renames a specific file
     *
     * @param file The file as file which should be renamed
     * @param newName The new name of the file
     */
    public static void rename(final File file, final String newName) {
        file.renameTo(new File(newName));
    }

    /**
     * Copies all files of the given directory to another directory
     *
     * @param directory The source directory of the file
     * @param targetDirectory The new directory of the file
     */
    public static void copyAllFiles(final Path directory, final String targetDirectory) {
        if (!Files.exists(directory)) {
            return;
        }

        try {
            Files.walkFileTree(directory, new SimpleFileVisitor<Path>() {
                    @Override
                    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
                        throws IOException {
                        Path target = Paths.get(targetDirectory, directory.relativize(file).toString());
                        Path parent = target.getParent();
                        if (parent != null && !Files.exists(parent)) {
                            Files.createDirectories(parent);
                        }
                        Files.copy(file, target, StandardCopyOption.REPLACE_EXISTING);
                        return FileVisitResult.CONTINUE;
                    }
                }
            );
        } catch (final IOException ex) {
            StringUtil
                .printError(ReformCloudLibraryServiceProvider.getInstance().getColouredConsoleProvider(),
                    "Could not copy files", ex);
        }
    }

    /**
     * Copies all files of the given directory to another directory
     *
     * @param directory The source directory of the file
     * @param targetDirectory The new directory of the file
     * @param excluded The excluded file which should not be copied
     */
    public static void copyAllFiles(final Path directory, final String targetDirectory,
        final String excluded) {
        if (!Files.exists(directory)) {
            return;
        }

        try {
            Files.walkFileTree(directory, new SimpleFileVisitor<Path>() {
                    @Override
                    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
                        throws IOException {
                        if (file.getFileName().equals(Paths.get(excluded))) {
                            return FileVisitResult.CONTINUE;
                        }
                        Path target = Paths.get(targetDirectory, directory.relativize(file).toString());
                        Path parent = target.getParent();
                        if (parent != null && !Files.exists(parent)) {
                            Files.createDirectories(parent);
                        }
                        Files.copy(file, target, StandardCopyOption.REPLACE_EXISTING);
                        return FileVisitResult.CONTINUE;
                    }
                }
            );
        } catch (final IOException ex) {
            StringUtil
                .printError(ReformCloudLibraryServiceProvider.getInstance().getColouredConsoleProvider(),
                    "Could not copy files", ex);
        }
    }

    /**
     * Copies all files of the given directory to another directory
     *
     * @param directory The source directory of the file
     * @param targetDirectory The new directory of the file
     * @param excluded All excluded files which should not be copied
     */
    public static void copyAllFiles(final Path directory, final String targetDirectory,
        final String... excluded) {
        if (!Files.exists(directory)) {
            return;
        }

        List<Path> continueFile = new ArrayList<>();
        Arrays.stream(excluded).forEach(e -> continueFile.add(Paths.get(e)));

        try {
            Files.walkFileTree(directory, new SimpleFileVisitor<Path>() {
                    @Override
                    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
                        throws IOException {
                        if (continueFile.contains(file.getFileName())) {
                            return FileVisitResult.CONTINUE;
                        }
                        Path target = Paths.get(targetDirectory, directory.relativize(file).toString());
                        Path parent = target.getParent();
                        if (parent != null && !Files.exists(parent)) {
                            Files.createDirectories(parent);
                        }
                        Files.copy(file, target, StandardCopyOption.REPLACE_EXISTING);
                        return FileVisitResult.CONTINUE;
                    }
                }
            );
        } catch (final IOException ex) {
            StringUtil
                .printError(ReformCloudLibraryServiceProvider.getInstance().getColouredConsoleProvider(),
                    "Could not copy files", ex);
        }
    }

    /**
     * Deletes a file if it exists
     *
     * @param path The path of the file which should be deleted
     */
    public static void deleteFileIfExists(Path path) {
        try {
            Files.deleteIfExists(path);
        } catch (final IOException ex) {
            StringUtil
                .printError(ReformCloudLibraryServiceProvider.getInstance().getColouredConsoleProvider(),
                    "Could not delete file", ex);
        }
    }

    /**
     * Deletes a file if it exists
     *
     * @param path The path as file of the file which should be deleted
     */
    public static void deleteFileIfExists(File path) {
        deleteFileIfExists(path.toPath());
    }

    /**
     * Deletes a file if it exists
     *
     * @param path The path as string of the file which should be deleted
     */
    public static void deleteFileIfExists(String path) {
        deleteFileIfExists(Paths.get(path));
    }

    /**
     * Get the current file name
     *
     * @return The file name of the executed jar
     */
    public static String getInternalFileName() {
        String internalName = FileUtils.class.getProtectionDomain().getCodeSource().getLocation()
            .getPath();
        if (internalName.contains("/")) {
            final String[] split = internalName.split("/");
            internalName = split[split.length - 1];
        }
        return internalName;
    }

    /**
     * Writes the content to a specific file
     *
     * @param path The path of the file in which the content should be written
     * @param content The content which should be written
     */
    public static void writeToFile(Path path, String content) {
        try {
            if (!Files.exists(path)) {
                if (path.getParent() != null) {
                    Files.createDirectories(path.getParent());
                }

                Files.createFile(path);
            }

            FileOutputStream fileOutputStream = new FileOutputStream(path.toFile());
            fileOutputStream.write(content.getBytes());
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (final IOException ex) {
            StringUtil
                .printError(ReformCloudLibraryServiceProvider.getInstance().getColouredConsoleProvider(),
                    "Error while writing string to file", ex);
        }
    }

    /**
     * Deletes the given file on program exit
     *
     * @param file The file which should be deleted
     */
    public static void deleteOnExit(final File file) {
        file.deleteOnExit();
    }

    /**
     * This method delete the internal file when exiting (This feature is broken on windows)
     */
    public static void deleteInternalFileOnExit() {
        deleteOnExit(Paths.get(getInternalFileName()));
    }

    /**
     * Deletes the given file on program exit
     *
     * @param path The file as path which should be deleted
     */
    public static void deleteOnExit(final Path path) {
        deleteOnExit(path.toFile());
    }

    /**
     * Creates a new directory
     *
     * @param path The path of the new directory
     */
    public static void createDirectory(Path path) {
        path.toFile().mkdirs();
    }

    /**
     * Read a file to a string
     *
     * @param file The file which should be read
     * @return The string of the file
     */
    public static String readFileAsString(File file) {
        try {
            return new String(Files.readAllBytes(file.toPath()), StandardCharsets.UTF_8);
        } catch (final IOException ex) {
            return null;
        }
    }
}
