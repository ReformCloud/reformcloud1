/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.utility.files;

import systems.reformcloud.ReformCloudLibraryServiceProvider;
import systems.reformcloud.utility.StringUtil;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author _Klaro | Pasqual K. / created on 30.10.2018
 */

public class FileUtils {
    /**
     * Deletes a directory
     *
     * @param path
     * @see Files#delete(Path)
     * @see File#toPath()
     */
    public static void deleteFullDirectory(Path path) {
        final File[] files = path.toFile().listFiles();
        if (files == null) return;

        for (File file : files) {
            if (file.isDirectory())
                deleteFullDirectory(file.toPath());
            else
                file.delete();
        }

        path.toFile().delete();
    }

    /**
     * Copies a file from the given directory {@param from} to the given directory {@param to}
     *
     * @param from
     * @param to
     * @see Files#copy(Path, Path, CopyOption...)
     */
    public static void copyFile(final String from, final String to) {
        try {
            Files.copy(Paths.get(from), Paths.get(to), StandardCopyOption.REPLACE_EXISTING);
        } catch (final IOException ex) {
            StringUtil.printError(ReformCloudLibraryServiceProvider.getInstance().getLoggerProvider(), "Could not copy file", ex);
        }
    }

    /**
     * Copies a compiled file to the given directory
     *
     * @param from
     * @param to
     * @see Files#copy(InputStream, Path, CopyOption...)
     */
    public static void copyCompiledFile(final String from, final String to) {
        try (InputStream localInputStream = FileUtils.class.getClassLoader().getResourceAsStream(from)) {
            Files.copy(localInputStream, Paths.get(to), StandardCopyOption.REPLACE_EXISTING);
        } catch (final IOException ex) {
            StringUtil.printError(ReformCloudLibraryServiceProvider.getInstance().getLoggerProvider(), "Could not copy local file", ex);
        }
    }

    public static void rename(final String file, final String newName) {
        new File(file).renameTo(new File(newName));
    }

    /**
     * Copies all files to the given directory
     *
     * @param directory
     * @param targetDirectory
     * @see Files#copy(Path, Path, CopyOption...)
     */
    public static void copyAllFiles(final Path directory, final String targetDirectory) {
        if (! Files.exists(directory)) return;

        try {
            Files.walkFileTree(directory, new SimpleFileVisitor<Path>() {
                        @Override
                        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                            Path target = Paths.get(targetDirectory, directory.relativize(file).toString());
                            Path parent = target.getParent();
                            if (parent != null && ! Files.exists(parent))
                                Files.createDirectories(parent);
                            Files.copy(file, target, StandardCopyOption.REPLACE_EXISTING);
                            return FileVisitResult.CONTINUE;
                        }
                    }
            );
        } catch (final IOException ex) {
            StringUtil.printError(ReformCloudLibraryServiceProvider.getInstance().getLoggerProvider(), "Could not copy files", ex);
        }
    }

    /**
     * Copies all files to the given directory without the excluded files
     *
     * @param directory
     * @param targetDirectory
     * @see Files#copy(Path, Path, CopyOption...)
     */
    public static void copyAllFiles(final Path directory, final String targetDirectory, final String excluded) {
        if (!Files.exists(directory))
            return;

        try {
            Files.walkFileTree(directory, new SimpleFileVisitor<Path>() {
                        @Override
                        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                            if (file.getFileName().equals(Paths.get(excluded)))
                                return FileVisitResult.CONTINUE;
                            Path target = Paths.get(targetDirectory, directory.relativize(file).toString());
                            Path parent = target.getParent();
                            if (parent != null && ! Files.exists(parent))
                                Files.createDirectories(parent);
                            Files.copy(file, target, StandardCopyOption.REPLACE_EXISTING);
                            return FileVisitResult.CONTINUE;
                        }
                    }
            );
        } catch (final IOException ex) {
            StringUtil.printError(ReformCloudLibraryServiceProvider.getInstance().getLoggerProvider(), "Could not copy files", ex);
        }
    }

    public static void copyAllFiles(final Path directory, final String targetDirectory, final String... excluded) {
        if (!Files.exists(directory))
            return;

        List<Path> continueFile = new ArrayList<>();
        Arrays.stream(excluded).forEach(e -> continueFile.add(Paths.get(e)));

        try {
            Files.walkFileTree(directory, new SimpleFileVisitor<Path>() {
                        @Override
                        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                            if (continueFile.contains(file.getFileName()))
                                return FileVisitResult.CONTINUE;
                            Path target = Paths.get(targetDirectory, directory.relativize(file).toString());
                            Path parent = target.getParent();
                            if (parent != null && !Files.exists(parent))
                                Files.createDirectories(parent);
                            Files.copy(file, target, StandardCopyOption.REPLACE_EXISTING);
                            return FileVisitResult.CONTINUE;
                        }
                    }
            );
        } catch (final IOException ex) {
            StringUtil.printError(ReformCloudLibraryServiceProvider.getInstance().getLoggerProvider(), "Could not copy files", ex);
        }
    }

    /**
     * Deletes a file if it exists
     *
     * @param path
     * @see Files#deleteIfExists(Path)
     */
    public static void deleteFileIfExists(Path path) {
        try {
            if (Files.exists(path))
                Files.delete(path);
        } catch (final IOException ex) {
            StringUtil.printError(ReformCloudLibraryServiceProvider.getInstance().getLoggerProvider(), "Could not delete file", ex);
        }
    }

    /**
     * Get the current fileName
     *
     * @return the fileName of the executed jar
     */
    public static String getInternalFileName() {
        String internalName = FileUtils.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        if (internalName.contains("/")) {
            final String[] split = internalName.split("/");
            internalName = split[split.length - 1];
        }
        return internalName;
    }

    /**
     * Writes the given content to the given file
     *
     * @param path
     * @param content
     * @see OutputStreamWriter
     */
    public static void writeToFile(Path path, String content) {
        try {
            if (!Files.exists(path)) {
                if (path.getParent() != null)
                    Files.createDirectories(path.getParent());

                Files.createFile(path);
            }

            OutputStreamWriter writer = new OutputStreamWriter(Files.newOutputStream(path), StandardCharsets.UTF_8);
            writer.write(content);
            writer.flush();
            writer.close();
        } catch (final IOException ex) {
            StringUtil.printError(ReformCloudLibraryServiceProvider.getInstance().getLoggerProvider(), "Error while writing string to file", ex);
        }
    }

    /**
     * Deletes the given file on program exit
     *
     * @param file
     * @see File#deleteOnExit()
     */
    public static void deleteOnExit(final File file) {
        file.deleteOnExit();
    }

    /**
     * Deletes the given path on program exit
     *
     * @param path
     * @see FileUtils#deleteOnExit(File)
     * @see Path#toFile()
     */
    public static void deleteOnExit(final Path path) {
        deleteOnExit(path.toFile());
    }

    /**
     * Creates a specific directory
     *
     * @param path
     * @see File#mkdirs()
     */
    public static void createDirectory(Path path) {
        path.toFile().mkdirs();
    }

    /**
     * Read a file as string
     *
     * @param file for reading
     * @return the final string
     */
    public static String readFileAsString(File file) {
        try {
            return new String(Files.readAllBytes(file.toPath()), StandardCharsets.UTF_8);
        } catch (final IOException ex) {
            return null;
        }
    }
}
