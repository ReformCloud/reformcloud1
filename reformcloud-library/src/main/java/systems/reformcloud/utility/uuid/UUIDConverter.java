package systems.reformcloud.utility.uuid;

import com.google.gson.stream.JsonReader;
import systems.reformcloud.ReformCloudLibraryService;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

public final class UUIDConverter implements Serializable {
    private static final long serialVersionUID = 5547714493710676047L;

    /**
     * From https://stackoverflow.com/a/47238049/10939910
     */
    private static final Pattern PATTERN = Pattern.compile("(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})");

    /**
     * convert a uuid string without dashes to a uuid object
     *
     * @param rawUuid the raw uuid without dashes
     * @return the final uuid
     */
    public static UUID toUUID(String rawUuid) {
        if (rawUuid.length() != 32)
            throw new IllegalArgumentException("The rawuuid must be 32 char long.");
        return UUID.fromString(UUIDConverter.PATTERN.matcher(rawUuid).replaceAll("$1-$2-$3-$4-$5"));
    }

    private static Map<String, UUID> uuids = new ConcurrentHashMap<>();

    public static UUID getUUIDFromName(final String name) {
        if (uuids.containsKey(name)) {
            return uuids.get(name);
        }

        try {
            HttpURLConnection httpURLConnection = (HttpURLConnection) new URL("https://api.mojang.com/users/profiles/minecraft/" + name).openConnection();
            httpURLConnection.setDoOutput(false);
            httpURLConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
            httpURLConnection.setUseCaches(true);
            httpURLConnection.connect();

            String in;
            try (JsonReader jsonReader = new JsonReader(new InputStreamReader(httpURLConnection.getInputStream()))) {
                in = ReformCloudLibraryService.PARSER.parse(jsonReader).getAsJsonObject().get("id").getAsString();
            }

            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i <= 31; i++) {
                stringBuilder.append(in.charAt(i));
                if (i == 7 || i == 11 || i == 15 || i == 19)
                    stringBuilder.append("-");
            }

            UUID uuid = UUID.fromString(stringBuilder.substring(0));
            uuids.put(name, uuid);

            return uuid;
        } catch (final IOException ignored) {
        }

        return null;
    }
}
