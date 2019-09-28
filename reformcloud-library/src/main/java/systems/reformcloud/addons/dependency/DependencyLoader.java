/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.addons.dependency;

import eu.byteexception.requestbuilder.RequestBuilder;
import eu.byteexception.requestbuilder.result.RequestResult;
import eu.byteexception.requestbuilder.result.stream.StreamType;
import systems.reformcloud.addons.dependency.util.DynamicDependency;
import systems.reformcloud.logging.AbstractLoggerProvider;
import systems.reformcloud.utility.StringUtil;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

/**
 * @author _Klaro | Pasqual K. / created on 17.05.2019
 */

public final class DependencyLoader implements Serializable {

    public static void loadDependency(DynamicDependency dynamicDependency) {
        try {
            URL result = downloadLib(dynamicDependency);
            if (result == null) {
                return;
            }

            URLClassLoader urlClassLoader = (URLClassLoader) ClassLoader.getSystemClassLoader();
            try {
                Method addURL = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
                addURL.setAccessible(true);
                addURL.invoke(urlClassLoader, result);
            } catch (final NoSuchMethodException | IllegalAccessException | InvocationTargetException ex) {
                StringUtil.printError(
                    AbstractLoggerProvider.defaultLogger(),
                    "Error while loading dependency",
                    ex
                );
            }

            final String[] name = result.getFile().split("/");
            System.out.println(
                "Successfully installed dependency " + name[name.length - 1].replace(".jar", ""));
        } catch (final IOException ex) {
            StringUtil.printError(
                AbstractLoggerProvider.defaultLogger(),
                "Error while loading dependency",
                ex
            );
        }
    }

    private static URL downloadLib(final DynamicDependency dependency)
        throws MalformedURLException {
        if (Files.exists(Paths
            .get("libraries/" + dependency.getName() + "-" + dependency.getVersion() + ".jar"))) {
            return new File(
                "libraries/" + dependency.getName() + "-" + dependency.getVersion() + ".jar")
                .toURI().toURL();
        }

        deleteExistingDependency(dependency);

        try {
            System.out.println(
                "Downloading dependency " + dependency.getName() + " from \"" + format(dependency)
                    + "\"...");
            final RequestResult requestResult = RequestBuilder.newBuilder(format(dependency), Proxy.NO_PROXY)
                .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11")
                .disableCaches()
                .enableOutput()
                .fireAndForget();

            if (!requestResult.hasFailed()) {
                try (InputStream inputStream = requestResult.getStream(StreamType.DEFAULT)) {
                    Files.copy(inputStream, Paths.get(
                        "libraries/" + dependency.getName() + "-" + dependency.getVersion() + ".jar"),
                        StandardCopyOption.REPLACE_EXISTING);
                }

                requestResult.forget();

                return new File(
                    "libraries/" + dependency.getName() + "-" + dependency.getVersion() + ".jar")
                    .toURI().toURL();
            }
        } catch (final IOException ex) {
            ex.printStackTrace();
        }

        return null;
    }

    private static void deleteExistingDependency(DynamicDependency dependency) {
        File[] files = new File("libraries").listFiles();
        if (files == null) {
            return;
        }

        for (File file : files) {
            if (file.getName().contains(dependency.getName()) && file.getName().endsWith(".jar")) {
                if (file.delete()) {
                    break;
                }
            }
        }
    }

    private static String format(final DynamicDependency dependency) {
        return dependency.downloadUrl + dependency.getGroupID().replace(".", "/") + "/" +
            dependency.getName() + "/" + dependency.getVersion() + "/" + dependency.getName() + "-"
            + dependency.getVersion() + ".jar";
    }
}
