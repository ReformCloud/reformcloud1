/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud;

import systems.reformcloud.loader.CapableClassLoader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author _Klaro | Pasqual K. / created on 14.05.2019
 */

public final class CommonLoader implements Serializable {
    private static String version;

    public static ClassLoader createClassLoader(List<URL> urls) {
        checkNonNull(urls);
        return createClassLoader(urls.toArray(new URL[0]));
    }

    public static ClassLoader createClassLoader(URL urls) {
        checkNonNull(urls);
        return createClassLoader(new URL[]{urls});
    }

    public static ClassLoader createClassLoader(URL[] urls) {
        checkNonNull(urls);
        return new CapableClassLoader(urls, ClassLoader.getSystemClassLoader());
    }

    public static String getCurrentFallbackVersion() {
        if (version == null)
            getCurrentFallbackVersion0();

        return version;
    }

    private static void getCurrentFallbackVersion0() {
        try {
            HttpURLConnection urlConnection = (HttpURLConnection) new URL("https://internal.reformcloud.systems/update/version.json").openConnection();
            urlConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
            urlConnection.setUseCaches(false);
            urlConnection.connect();

            StringBuilder stringBuilder = new StringBuilder();
            String line;
            try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()))) {
                while ((line = bufferedReader.readLine()) != null)
                    stringBuilder.append(line);
            }

            urlConnection.disconnect();
            Matcher matcher = Pattern.compile("\\{\"version\": \"(.*)\"}").matcher(stringBuilder.substring(0));
            version = matcher.matches() ? matcher.group(1) : CommonLoader.class.getPackage().getImplementationVersion();
        } catch (final IOException ex) {
            if (ex instanceof UnknownHostException) {
                version = CommonLoader.class.getPackage().getImplementationVersion();
                return;
            }

            version = CommonLoader.class.getPackage().getImplementationVersion();
            ex.printStackTrace();
        }
    }

    private static void checkNonNull(Object x) {
        if (x == null)
            throw new IllegalStateException("Invocation with null parameters");
    }
}
