/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.libloader;

import systems.reformcloud.ReformCloudLibraryServiceProvider;
import systems.reformcloud.libloader.classloader.RuntimeClassLoader;
import systems.reformcloud.libloader.libraries.*;
import systems.reformcloud.libloader.utility.Dependency;
import systems.reformcloud.utility.ExitUtil;
import systems.reformcloud.utility.StringUtil;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author _Klaro | Pasqual K. / created on 23.01.2019
 */

public final class LibraryLoader {
    private List<Dependency> libraries = new ArrayList<>();

    public LibraryLoader() {
        if (Float.parseFloat(System.getProperty("java.class.version")) != 52.0D) {
            System.out.println("This application currently needs Java 8.");
            try {
                Thread.sleep(2000);
            } catch (final InterruptedException ignored) {
            }
            System.exit(ExitUtil.NOT_JAVA_8);
            return;
        }

        this.libraries.addAll(Arrays.asList(new Netty(), new Quartz(), new SnakeYaml(), new CommonsIO(), new JLine(),
                new Gson(), new CommonsCodec(), new CommonsLogging(), new ApacheHttpCore(), new ApacheHttpComponents()));
    }

    public void loadJarFileAndInjectLibraries() {
        List<URL> urls = new ArrayList<>();
        final File dir = new File("libraries");

        if (!dir.exists())
            dir.mkdirs();

        this.libraries.forEach(e -> {
            if (!Files.exists(Paths.get("libraries/" + e.getName() + "-" + e.getVersion() + ".jar"))) {
                if (dir.listFiles() != null) {
                    Arrays.stream(dir.listFiles()).forEach(file -> {
                        if (file != null && file.getName().contains(e.getName()) && file.getName().endsWith(".jar")) {
                            file.delete();
                        }
                    });
                }

                this.downloadLib(e);
            }

            System.out.println("Detected dependency " + e.getName() + "...");
            try {
                urls.add(new File("libraries/" + e.getName() + "-" + e.getVersion() + ".jar").toURI().toURL());
            } catch (final MalformedURLException ex) {
                StringUtil.printError(ReformCloudLibraryServiceProvider.getInstance().getLoggerProvider(), "Could not prepared dependency", ex);
            }
        });

        RuntimeClassLoader runtimeClassLoader = new RuntimeClassLoader(urls.toArray(new URL[urls.size()]), ClassLoader.getSystemClassLoader());

        urls.forEach(url -> {
            runtimeClassLoader.addURL(url);

            final String[] name = url.getFile().split("/");
            System.out.println("Successfully installed dependency " + name[name.length - 1].replace(".jar", ""));
        });
        Thread.currentThread().setContextClassLoader(runtimeClassLoader);
    }

    private void downloadLib(final Dependency dependency) {
        try {
            System.out.println("Downloading dependency " + dependency.getName() + " from \"" + this.format(dependency) + "\"...");
            HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(this.format(dependency)).openConnection();
            httpURLConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
            httpURLConnection.setDoOutput(false);
            httpURLConnection.setUseCaches(false);
            httpURLConnection.connect();

            try (InputStream inputStream = httpURLConnection.getInputStream()) {
                Files.copy(inputStream, Paths.get("libraries/" + dependency.getName() + "-" + dependency.getVersion() + ".jar"), StandardCopyOption.REPLACE_EXISTING);
            }

            httpURLConnection.disconnect();
        } catch (final IOException ex) {
            StringUtil.printError(ReformCloudLibraryServiceProvider.getInstance().getLoggerProvider(), "Error while downloading dependency", ex);
        }

        System.out.println("Dependency " + dependency.getName() + " was downloaded successfully");
    }

    private String format(final Dependency dependency) {
        return dependency.download_url + dependency.getGroupID().replace(".", "/") + "/" + dependency.getName() + "/" + dependency.getVersion() + "/" + dependency.getName() + "-" + dependency.getVersion() + ".jar";
    }
}
