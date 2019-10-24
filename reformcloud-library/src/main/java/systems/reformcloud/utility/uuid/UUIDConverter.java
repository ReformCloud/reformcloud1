/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.utility.uuid;

import com.google.gson.JsonObject;
import java.io.IOException;
import java.io.Serializable;
import java.net.Proxy;
import java.util.UUID;
import java.util.regex.Pattern;

import com.google.gson.JsonParser;
import eu.byteexception.requestbuilder.RequestBuilder;
import eu.byteexception.requestbuilder.method.RequestMethod;
import eu.byteexception.requestbuilder.result.RequestResult;
import systems.reformcloud.ReformCloudLibraryService;
import systems.reformcloud.cache.Cache;

/**
 * @author _Klaro | Pasqual K. / created on 01.01.2019
 */

public final class UUIDConverter implements Serializable {

    private static final long serialVersionUID = 5547714493710676047L;

    /**
     * From https://stackoverflow.com/a/47238049/10939910
     */
    private static final Pattern PATTERN = Pattern
        .compile("(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})");

    /**
     * Converts a uuid string without dashes to a uuid object
     *
     * @param rawUuid The raw uuid without dashes
     * @return The final uuid
     */
    public static UUID toUUID(String rawUuid) {
        if (rawUuid.length() != 32) {
            throw new IllegalArgumentException("The rawuuid must be 32 char long.");
        }
        return UUID.fromString(UUIDConverter.PATTERN.matcher(rawUuid).replaceAll("$1-$2-$3-$4-$5"));
    }

    /**
     * The cache in which all uuid are cached
     */
    private static Cache<String, UUID> uuids = ReformCloudLibraryService.newCache(300);

    /**
     * The cache in which all names are cached
     */
    private static Cache<UUID, String> names = ReformCloudLibraryService.newCache(300);

    /**
     * Gets a uuid from the given name
     *
     * @param name The name of the player
     * @return The uuid of the player
     */
    public static UUID getUUIDFromName(final String name) {
        if (uuids.contains(name)) {
            return uuids.getSave(name).get();
        }

        try {
            final RequestResult requestResult = RequestBuilder.newBuilder("https://api.minetools.eu/uuid/" + name, Proxy.NO_PROXY)
                .setRequestMethod(RequestMethod.GET)
                .addHeader("User-Agent",
                    "\"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11\"")
                .disableCaches()
                .enableOutput()
                .fireAndForget();

            final JsonObject jsonObject = JsonParser.parseString(requestResult.getResultAsString()).getAsJsonObject();

            if (jsonObject.get("status").getAsString().equals("OK")) {
                final StringBuilder stringBuilder = new StringBuilder();
                for (int i = 0; i <= 31; i++) {
                    stringBuilder.append(jsonObject.get("id").getAsString().charAt(i));
                    if (i == 7 || i == 11 || i == 15 || i == 19) {
                        stringBuilder.append("-");
                    }
                }

                final UUID uuid = UUID.fromString(stringBuilder.substring(0));
                uuids.add(name, uuid);
                return uuid;
            }

        } catch (final IOException ignored) { }

        return null;
    }

    /**
     * Gets a name from the given uuid
     *
     * @param uuid The uuid of the player
     * @return The name of the player
     */
    public static String getNameFromUUID(final UUID uuid) {
        if (names.contains(uuid)) {
            return names.getSave(uuid).get();
        }

        try {
            final RequestResult requestResult = RequestBuilder.newBuilder("https://api.minetools.eu/uuid/" + uuid.toString().replace("-", ""),
                Proxy.NO_PROXY)
                .setRequestMethod(RequestMethod.GET)
                .addHeader("User-Agent",
                    "\"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11\"")
                .disableCaches()
                .enableOutput()
                .fireAndForget();

            final JsonObject jsonObject = JsonParser.parseString(requestResult.getResultAsString()).getAsJsonObject();

            if (jsonObject.get("status").getAsString().equals("OK")) {
                final String name = jsonObject.get("name").getAsString();
                names.add(uuid, name);
                return name;
            }
        } catch (final IOException ignored) { }

        return null;
    }
}
