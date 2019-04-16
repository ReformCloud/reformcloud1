/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.utility.files;

import systems.reformcloud.ReformCloudLibraryServiceProvider;
import systems.reformcloud.utility.StringUtil;
import systems.reformcloud.utility.map.maps.Double;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

/**
 * @author _Klaro | Pasqual K. / created on 30.10.2018
 */

public final class DownloadManager implements Serializable {
    public static final Double<String, String> REQUEST_PROPERTY = new Double<>("User-Agent",
            "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");

    /**
     * Downloads the given file by the {@param url} to the final position ({@param to})
     *
     * @param input
     * @param url
     * @param to
     */
    public static void download(final String input, final String url, final String to) {
        ReformCloudLibraryServiceProvider.getInstance().getLoggerProvider().info("Trying to download " + input + "...");
        try {
            URLConnection urlConnection = new URL(url).openConnection();
            urlConnection.setRequestProperty(REQUEST_PROPERTY.getFirst(), REQUEST_PROPERTY.getSecond());
            urlConnection.setConnectTimeout(1000);
            urlConnection.setUseCaches(false);
            urlConnection.connect();

            try (InputStream inputStream = urlConnection.getInputStream()) {
                Files.copy(inputStream, Paths.get(to), StandardCopyOption.REPLACE_EXISTING);
            }

            ReformCloudLibraryServiceProvider.getInstance().getLoggerProvider().info("Download was completed successfully");
        } catch (final IOException ex) {
            StringUtil.printError(ReformCloudLibraryServiceProvider.getInstance().getLoggerProvider(), "Could not download", ex);
        }
    }

    /**
     * Downloads the given file by the {@param url} to the final position ({@param to}) without console message
     *
     * @param url
     * @param to
     */
    public static void downloadSilent(final String url, final String to) {
        try {
            URLConnection urlConnection = new URL(url).openConnection();
            urlConnection.setRequestProperty(REQUEST_PROPERTY.getFirst(), REQUEST_PROPERTY.getSecond());
            urlConnection.setConnectTimeout(1000);
            urlConnection.setUseCaches(false);
            urlConnection.connect();

            try (InputStream inputStream = urlConnection.getInputStream()) {
                Files.copy(inputStream, Paths.get(to), StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (final IOException ex) {
            StringUtil.printError(ReformCloudLibraryServiceProvider.getInstance().getLoggerProvider(), "Could not download file", ex);
        }
    }

    /**
     * Downloads the given file by the {@param url} to the final position ({@param to}) and disconnects at the end as {@link HttpURLConnection}
     *
     * @param url
     * @param to
     */
    public static void downloadAndDisconnect(final String input, final String url, final String to) {
        ReformCloudLibraryServiceProvider.getInstance().getLoggerProvider().info("Trying to download " + input + "...");
        try {
            URLConnection urlConnection = new URL(url).openConnection();
            urlConnection.setRequestProperty(REQUEST_PROPERTY.getFirst(), REQUEST_PROPERTY.getSecond());
            urlConnection.setConnectTimeout(1000);
            urlConnection.setUseCaches(false);
            urlConnection.connect();

            try (InputStream inputStream = urlConnection.getInputStream()) {
                Files.copy(inputStream, Paths.get(to), StandardCopyOption.REPLACE_EXISTING);
            }

            ((HttpURLConnection) urlConnection).disconnect();

            ReformCloudLibraryServiceProvider.getInstance().getLoggerProvider().info("Download was completed successfully");
        } catch (final IOException ex) {
            StringUtil.printError(ReformCloudLibraryServiceProvider.getInstance().getLoggerProvider(), "Download failed", ex);
        }
    }

    /**
     * Downloads the given file by the {@param url} to the final position ({@param to}) without console message
     * and disconnects at the end as {@link HttpURLConnection}
     *
     * @param url
     * @param to
     */
    public static void downloadSilentAndDisconnect(final String url, final String to) {
        try {
            URLConnection urlConnection = new URL(url).openConnection();
            urlConnection.setRequestProperty(REQUEST_PROPERTY.getFirst(), REQUEST_PROPERTY.getSecond());
            urlConnection.setConnectTimeout(1000);
            urlConnection.setUseCaches(false);
            urlConnection.connect();

            try (InputStream inputStream = urlConnection.getInputStream()) {
                Files.copy(inputStream, Paths.get(to), StandardCopyOption.REPLACE_EXISTING);
            }

            ((HttpURLConnection) urlConnection).disconnect();
        } catch (final IOException ex) {
            StringUtil.printError(ReformCloudLibraryServiceProvider.getInstance().getLoggerProvider(), "Error in download", ex);
        }
    }
}
