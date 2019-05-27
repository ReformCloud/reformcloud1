/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud;

import systems.reformcloud.libraries.*;
import systems.reformcloud.utility.Dependency;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * @author _Klaro | Pasqual K. / created on 14.05.2019
 */

final class LibraryPreLoader implements Serializable {
    private static List<Dependency> dependencies;

    private LibraryPreLoader() {
    }

    public static void prepareDependencies(boolean installNetty) {
        prepareFolder();
        if (dependencies == null)
            prepareDependencies0(installNetty);
    }

    public static List<URL> downloadDependencies() {
        List<URL> downloaded = new ArrayList<>();
        dependencies.forEach(dependency -> {
            try {
                URL result = downloadLib(dependency);
                if (result != null)
                    downloaded.add(result);
            } catch (final MalformedURLException ex) {
                ex.printStackTrace();
            }
        });

        return downloaded;
    }

    private static URL downloadLib(final Dependency dependency) throws MalformedURLException {
        if (Files.exists(Paths.get("libraries/" + dependency.getName() + "-" + dependency.getVersion() + ".jar")))
            return new File("libraries/" + dependency.getName() + "-" + dependency.getVersion() + ".jar").toURI().toURL();

        deleteExistingDependency(dependency);

        try {
            System.out.println("Downloading dependency " + dependency.getName() + " from \"" + format(dependency) + "\"...");
            HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(format(dependency)).openConnection();
            httpURLConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
            httpURLConnection.setDoOutput(false);
            httpURLConnection.setUseCaches(false);
            httpURLConnection.connect();

            try (InputStream inputStream = httpURLConnection.getInputStream()) {
                Files.copy(inputStream, Paths.get("libraries/" + dependency.getName() + "-" + dependency.getVersion() + ".jar"),
                        StandardCopyOption.REPLACE_EXISTING);
            }

            httpURLConnection.disconnect();
            return new File("libraries/" + dependency.getName() + "-" + dependency.getVersion() + ".jar").toURI().toURL();
        } catch (final IOException ex) {
            ex.printStackTrace();
        }

        return null;
    }

    private static void deleteExistingDependency(Dependency dependency) {
        File[] files = new File("libraries").listFiles();
        if (files == null)
            return;

        for (File file : files) {
            if (file.getName().contains(dependency.getName()) && file.getName().endsWith(".jar"))
                if (file.delete())
                    break;
        }
    }

    private static void prepareDependencies0(boolean installNetty) {
        dependencies = new LinkedList<>(Arrays.asList(new SnakeYaml(), new CommonsIO(), new JLine(), new ApacheCommonsNet(),
                new Gson(), new CommonsCodec(), new CommonsLogging(), new ApacheHttpCore(), new ApacheHttpComponents()));

        if (installNetty)
            dependencies.add(new Netty());
    }

    /**
     * Formats a dependency to a url
     *
     * @param dependency The dependency which is needed
     * @return A string usable as download url for the lib
     */
    private static String format(final Dependency dependency) {
        return dependency.download_url + dependency.getGroupID().replace(".", "/") + "/" +
                dependency.getName() + "/" + dependency.getVersion() + "/" + dependency.getName() + "-" + dependency.getVersion() + ".jar";
    }

    private static void prepareFolder() {
        if (!Files.exists(Paths.get("libraries")))
            if (!new File("libraries").mkdirs())
                new SecurityException("Cannot create libraries folder").printStackTrace();
    }
}
