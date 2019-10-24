/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.versioneering;

import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import eu.byteexception.requestbuilder.RequestBuilder;
import eu.byteexception.requestbuilder.result.RequestResult;
import eu.byteexception.requestbuilder.result.stream.StreamType;
import systems.reformcloud.ReformCloudLibraryService;
import systems.reformcloud.ReformCloudLibraryServiceProvider;
import systems.reformcloud.event.events.version.VersionCheckedEvent;
import systems.reformcloud.utility.StringUtil;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.Proxy;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;

/**
 * @author _Klaro | Pasqual K. / created on 08.01.2019
 */

final class VersionLoader implements Serializable {

    /**
     * Gets the newest version of the cloud system
     *
     * @return The newest version of the cloud system as string
     */
    static String getNewestVersion() {
        try {
            final RequestResult requestResult = RequestBuilder.newBuilder("https://internal.reformcloud.systems/update/version.json", Proxy.NO_PROXY)
                .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11")
                .disableCaches()
                .fireAndForget();

            String version;

            try (JsonReader jsonReader = new JsonReader(
                new InputStreamReader(requestResult.getStream(StreamType.DEFAULT), StandardCharsets.UTF_8))) {
                version = JsonParser.parseReader(jsonReader).getAsJsonObject()
                    .get("version").getAsString();
            }

            requestResult.forget();
            ReformCloudLibraryServiceProvider.getInstance().getEventManager().fire(new VersionCheckedEvent(version));
            return version;
        } catch (final IOException ex) {
            if (ex instanceof UnknownHostException) {
                ReformCloudLibraryServiceProvider.getInstance().getColouredConsoleProvider().serve()
                    .accept("Cannot resolve update host," +
                        " make sure you have an internet connection");
                ReformCloudLibraryServiceProvider.getInstance().getEventManager().fire(new VersionCheckedEvent(null));
                return StringUtil.REFORM_VERSION;
            }

            StringUtil
                .printError(ReformCloudLibraryServiceProvider.getInstance().getColouredConsoleProvider(),
                    "Error while checking newest version", ex);
        }

        ReformCloudLibraryServiceProvider.getInstance().getEventManager().fire(new VersionCheckedEvent(null));

        return StringUtil.REFORM_VERSION;
    }
}
