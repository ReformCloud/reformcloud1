/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud;

import eu.byteexception.requestbuilder.RequestBuilder;
import eu.byteexception.requestbuilder.result.RequestResult;
import eu.byteexception.requestbuilder.result.stream.StreamType;
import systems.reformcloud.loader.CapableClassLoader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.Proxy;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author _Klaro | Pasqual K. / created on 14.05.2019
 */

final class CommonLoader implements Serializable {

    private static String version;

    static {
        checkForJavaVersion();
    }

    public static ClassLoader createClassLoader(List<URL> urls) {
        checkNonNull(urls);
        return createClassLoader(urls.toArray(new URL[0]));
    }

    private static ClassLoader createClassLoader(URL[] urls) {
        checkNonNull(urls);
        return new CapableClassLoader(urls, ClassLoader.getSystemClassLoader());
    }

    public static String getCurrentFallbackVersion() {
        if (version == null) {
            getCurrentFallbackVersion0();
        }

        return version;
    }

    private static void getCurrentFallbackVersion0() {
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
            Matcher matcher = Pattern
                .compile("\\{ {5}\"version\": \"(.*)\", {3}\"oldVersion\": \"(.*)\"}")
                .matcher(stringBuilder.substring(0));
            version = matcher.matches() ? matcher.group(1)
                : CommonLoader.class.getPackage().getImplementationVersion();
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
        if (x == null) {
            throw new IllegalStateException("Invocation with null parameters");
        }
    }

    private static void checkForJavaVersion() {
        if (Double.parseDouble(System.getProperty("java.class.version")) < 52D) {
            System.exit(2);
        }
    }

    public static void setVersion(String version) {
        CommonLoader.version = version;
    }

    public static void finishStartup() {
        System.setProperty("reformcloud.version", version);
    }
}
