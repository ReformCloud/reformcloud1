/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud;

import eu.byteexception.requestbuilder.RequestBuilder;
import eu.byteexception.requestbuilder.result.RequestResult;
import eu.byteexception.requestbuilder.result.stream.StreamType;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.jar.JarFile;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author _Klaro | Pasqual K. / created on 14.05.2019
 */

final class ControllerPreLoader implements Serializable {

    public static synchronized void main(String[] args) throws IOException {
        LibraryPreLoader.prepareDependencies(true, true);
        List<URL> libs = LibraryPreLoader.downloadDependencies();
        System.out.println("\nSuccessfully installed all necessary libraries");

        prepareFolder();
        if (!Files.exists(Paths.get("configuration.properties"))) {
            downloadCloudVersion();
        } else {
            checkForNewVersion();
        }

        run(args, libs);
    }

    private static synchronized void run(String[] args, List<URL> libs) {
        if (args == null || libs == null) {
            throw new IllegalStateException("Null element detected");
        }

        run0(args, libs);
    }

    private static synchronized void run0(String[] args, List<URL> libs) {
        File file = new File("version/ReformCloudController-" +
            CommonLoader.getCurrentFallbackVersion() + ".jar");
        if (!file.exists()) {
            throw new IllegalStateException("File of controller not found");
        }

        try {
            libs.add(file.toURI().toURL());
            ClassLoader classLoader = CommonLoader.createClassLoader(libs);
            setSystemClassLoaderTo(classLoader);
            run1(args, classLoader, file);
        } catch (final MalformedURLException ex) {
            ex.printStackTrace();
        }
    }

    private static synchronized void run1(String[] args, ClassLoader classLoader, File file) {
        if (classLoader == null) {
            throw new IllegalStateException("Null element detected");
        }

        if (!file.exists()) {
            downloadCloudVersion();
        }

        JarFile jarFile = null;
        try {
            jarFile = new JarFile(file);
            String main = jarFile.getManifest().getMainAttributes().getValue("Main-Class");
            Method mainMethod = classLoader.loadClass(main).getMethod("main", String[].class);
            run2(args, classLoader, mainMethod);
        } catch (final IOException | ClassNotFoundException | NoSuchMethodException ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (jarFile != null) {
                    jarFile.close();
                }
            } catch (final IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    private static synchronized void run2(String[] args, ClassLoader classLoader, Method main) {
        main.setAccessible(true);
        Thread.currentThread().setContextClassLoader(classLoader);

        CommonLoader.finishStartup();

        try {
            main.invoke(null, (Object) args);
        } catch (final IllegalAccessException | InvocationTargetException ex) {
            ex.printStackTrace();
        }
    }

    private static synchronized void setSystemClassLoaderTo(ClassLoader classLoaderTo) {
        if (classLoaderTo == null) {
            throw new IllegalStateException("Null element detected");
        }

        try {
            Field field = ClassLoader.class.getDeclaredField("scl");
            field.setAccessible(true);
            field.set(null, classLoaderTo);
        } catch (final NoSuchFieldException | IllegalAccessException ex) {
            ex.printStackTrace();
        }
    }

    private static void prepareFolder() throws IOException {
        if (!Files.exists(Paths.get("version"))) {
            Files.createDirectories(Paths.get("version"));
        }
    }

    private static void downloadCloudVersion() {
        deleteExistingVersion();

        try {
            final RequestResult requestResult = RequestBuilder.newBuilder(downloadURL(), Proxy.NO_PROXY)
                .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11")
                .disableCaches()
                .fireAndForget();

            try (InputStream inputStream = requestResult.getStream(StreamType.DEFAULT)) {
                Files.copy(inputStream, Paths.get("version/ReformCloudController-" +
                        CommonLoader.getCurrentFallbackVersion() + ".jar"),
                    StandardCopyOption.REPLACE_EXISTING);
            }

            requestResult.forget();
        } catch (final IOException ex) {
            ex.printStackTrace();
        }
    }

    private static String downloadURL() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder
            .append("https://internal.reformcloud.systems")
            .append("/")
            .append("latest")
            .append("/")
            .append("ReformCloudController-")
            .append(CommonLoader.getCurrentFallbackVersion())
            .append(".jar");
        return stringBuilder.substring(0);
    }

    private static void checkForNewVersion() {
        boolean dontCheck = Boolean.getBoolean("reformcloud.noversioncheck");
        if (dontCheck) {
            System.out.println("WARN: You have auto-update disabled!");
            return;
        }

        checkForNewVersion0();
    }

    private static void checkForNewVersion0() {
        try {
            final RequestResult requestResult = RequestBuilder.newBuilder("https://internal.reformcloud.systems/update/version.json", Proxy.NO_PROXY)
                .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11")
                .disableCaches()
                .fireAndForget();

            StringBuilder stringBuilder = new StringBuilder();
            String line;
            try (BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(requestResult.getStream(StreamType.DEFAULT)))) {
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line);
                }
            }

            requestResult.forget();
            checkForNewVersion1(stringBuilder.substring(0));
        } catch (final IOException ex) {
            if (ex instanceof UnknownHostException) {
                System.err.println("Cannot check for the newest version :(");
                return;
            }

            ex.printStackTrace();
        }
    }

    private static void checkForNewVersion1(String version) {
        if (version == null) {
            return;
        }

        String fallback = null;
        File file = Arrays.stream(new File("version").listFiles())
            .filter(Objects::nonNull)
            .filter(e -> e.getName().contains("ReformCloudController"))
            .filter(e -> e.getName().endsWith(".jar"))
            .findFirst()
            .orElse(null);
        if (file != null) {
            String[] split = file.getName().split("-");
            fallback = split[1].replace(".jar", "");
        }

        Matcher matcher = Pattern
            .compile("\\{ {5}\"version\": \"(.*)\", {3}\"oldVersion\": \"(.*)\"}").matcher(version);
        boolean newVersionAvailable = matcher.matches() && !matcher.group(1)
            .equals(fallback != null ? fallback : CommonLoader.getCurrentFallbackVersion());
        if (!newVersionAvailable) {
            System.out.println("You're running on the ReformCloud Version " + CommonLoader
                .getCurrentFallbackVersion() + ". This is the newest version");
        } else {
            downloadNewVersion(matcher.group(1), fallback);
        }
    }

    private static void downloadNewVersion(String newVersion, String oldVersion) {
        if (newVersion == null || newVersion.trim().isEmpty()) {
            return;
        }

        System.out.println(
            "A newer version was detected! Should the cloud be updated automatically? [\"y\", \"n\"]");
        try {
            BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(System.in, StandardCharsets.UTF_8));
            String line = bufferedReader.readLine();
            if (line.equalsIgnoreCase("y")) {
                downloadNewVersion0(newVersion);
            } else {
                System.out.println("Please update the cloud manually later");
                CommonLoader.setVersion(oldVersion);
            }
        } catch (final IOException ex) {
            ex.printStackTrace();
        }
    }

    private static void downloadNewVersion0(String newVersion) {
        if (Files.exists(Paths.get("version/ReformCloudController-" + newVersion + ".jar"))) {
            return;
        }

        downloadCloudVersion();
    }

    private static void deleteExistingVersion() {
        File[] files = new File("version").listFiles();
        if (files == null) {
            return;
        }

        for (File file : files) {
            if (file.getName().contains("ReformCloudController") && file.getName().endsWith(".jar")
                && !file.getName().contains(CommonLoader.getCurrentFallbackVersion())) {
                if (file.delete()) {
                    break;
                }
            }
        }
    }
}
