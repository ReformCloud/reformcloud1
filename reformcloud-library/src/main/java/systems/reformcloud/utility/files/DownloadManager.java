/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.utility.files;

import eu.byteexception.requestbuilder.RequestBuilder;
import eu.byteexception.requestbuilder.result.RequestResult;
import eu.byteexception.requestbuilder.result.stream.StreamType;
import systems.reformcloud.ReformCloudLibraryServiceProvider;
import systems.reformcloud.utility.Require;
import systems.reformcloud.utility.StringUtil;
import systems.reformcloud.utility.map.maps.Double;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.Proxy;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.concurrent.TimeUnit;

/**
 * @author _Klaro | Pasqual K. / created on 30.10.2018
 */

public final class DownloadManager implements Serializable {

    /**
     * The used request property of the cloud system
     */
    public static final Double<String, String> REQUEST_PROPERTY = new Double<>("User-Agent",
        "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");

    /**
     * Downloads a specific file from the given url and copies it to the correct place
     *
     * @param input The name of the file which should be downloaded, for information only
     * @param url The url of the file which should be downloaded
     * @param to The path where the file should be copied to
     */
    public static void download(final String input, final String url, final String to) {
        Require.requiresNotNull(url, to);
        if (input == null) {
            downloadSilent(url, to);
            return;
        }
        ReformCloudLibraryServiceProvider.getInstance().getColouredConsoleProvider().info(
            ReformCloudLibraryServiceProvider.getInstance().getLoaded().getDownload_trying()
                .replace("%name%", input)
        );
        try {
            final RequestResult requestResult = RequestBuilder.newBuilder(url, Proxy.NO_PROXY)
                .addHeader(REQUEST_PROPERTY.getFirst(), REQUEST_PROPERTY.getSecond())
                .setConnectTimeout(1, TimeUnit.SECONDS)
                .disableCaches()
                .fireAndForget();

            try (InputStream inputStream = requestResult.getStream(StreamType.DEFAULT)) {
                Files.copy(inputStream, Paths.get(to), StandardCopyOption.REPLACE_EXISTING);
            }

            ReformCloudLibraryServiceProvider.getInstance().getColouredConsoleProvider().info(
                ReformCloudLibraryServiceProvider.getInstance().getLoaded().getDownload_success()
            );
            requestResult.forget();
        } catch (final IOException ex) {
            StringUtil
                .printError(ReformCloudLibraryServiceProvider.getInstance().getColouredConsoleProvider(),
                    "Could not download", ex);
        }
    }

    /**
     * Downloads a specific file from the given url and copies it to the correct place
     *
     * @param url The url of the file which should be downloaded
     * @param to The path where the file should be copied to
     */
    public static void downloadSilent(final String url, final String to) {
        Require.requiresNotNull(url, to);
        try {
            final RequestResult requestResult = RequestBuilder.newBuilder(url, Proxy.NO_PROXY)
                .addHeader(REQUEST_PROPERTY.getFirst(), REQUEST_PROPERTY.getSecond())
                .setConnectTimeout(1, TimeUnit.SECONDS)
                .disableCaches()
                .fireAndForget();

            try (InputStream inputStream = requestResult.getStream(StreamType.DEFAULT)) {
                Files.copy(inputStream, Paths.get(to), StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (final IOException ex) {
            StringUtil
                .printError(ReformCloudLibraryServiceProvider.getInstance().getColouredConsoleProvider(),
                    "Could not download file", ex);
        }
    }

    /**
     * Downloads a specific file from the given url and copies it to the correct place and disconnects
     * the connection
     *
     * @param input The name of the file which should be downloaded, for information only
     * @param url The url of the file which should be downloaded
     * @param to The path where the file should be copied to
     * @return If the download was successfully
     */
    public static Boolean downloadAndDisconnect(final String input, final String url, final String to) {
        Require.requiresNotNull(url, to);
        if (input == null) {
            downloadSilentAndDisconnect(url, to);
            return false;
        }

        ReformCloudLibraryServiceProvider.getInstance().getColouredConsoleProvider().info(
            ReformCloudLibraryServiceProvider.getInstance().getLoaded().getDownload_trying()
                .replace("%name%", input)
        );
        try {
            final RequestResult requestResult = RequestBuilder.newBuilder(url, Proxy.NO_PROXY)
                .addHeader(REQUEST_PROPERTY.getFirst(), REQUEST_PROPERTY.getSecond())
                .setConnectTimeout(1, TimeUnit.SECONDS)
                .disableCaches()
                .fireAndForget();

            if (requestResult.hasFailed()) {
                ReformCloudLibraryServiceProvider.getInstance().getColouredConsoleProvider().warn().accept("Download for " + input + " failed with statuscode " + requestResult.getStatusCode());
                requestResult.forget();
                return false;
            }

            try (InputStream inputStream = requestResult.getStream(StreamType.DEFAULT)) {
                Files.copy(inputStream, Paths.get(to), StandardCopyOption.REPLACE_EXISTING);
            }

            requestResult.forget();

            ReformCloudLibraryServiceProvider.getInstance().getColouredConsoleProvider().info(
                ReformCloudLibraryServiceProvider.getInstance().getLoaded().getDownload_success()
            );

            return true;
        } catch (final IOException ex) {
            StringUtil
                .printError(ReformCloudLibraryServiceProvider.getInstance().getColouredConsoleProvider(),
                    "Download failed", ex);
        }
        return false;
    }

    /**
     * Downloads a specific file from the given url and copies it to the correct place and disconnects
     * the connection
     *
     * @param url The url of the file which should be downloaded
     * @param to The path where the file should be copied to
     */
    public static void downloadSilentAndDisconnect(final String url, final String to) {
        Require.requiresNotNull(url, to);
        try {
            final RequestResult requestResult = RequestBuilder.newBuilder(url, Proxy.NO_PROXY)
                .addHeader(REQUEST_PROPERTY.getFirst(), REQUEST_PROPERTY.getSecond())
                .setConnectTimeout(1, TimeUnit.SECONDS)
                .disableCaches()
                .fireAndForget();

            try (InputStream inputStream = requestResult.getStream(StreamType.DEFAULT)) {
                Files.copy(inputStream, Paths.get(to), StandardCopyOption.REPLACE_EXISTING);
            }

            requestResult.forget();
        } catch (final IOException ex) {
            StringUtil
                .printError(ReformCloudLibraryServiceProvider.getInstance().getColouredConsoleProvider(),
                    "Error in download", ex);
        }
    }
}
