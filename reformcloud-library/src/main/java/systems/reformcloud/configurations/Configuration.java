/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.configurations;

import com.google.gson.JsonObject;
import lombok.Data;
import systems.reformcloud.ReformCloudLibraryService;
import systems.reformcloud.ReformCloudLibraryServiceProvider;
import systems.reformcloud.utility.StringUtil;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Class to create easier Configuration files
 *
 * @author _Klaro | Pasqual K. / created on 18.10.2018
 */

@Data
public final class Configuration {
    protected JsonObject jsonObject;

    public Configuration() {
        this.jsonObject = new JsonObject();
    }

    public Configuration(JsonObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    public Configuration addStringProperty(String key, String value) {
        if (key == null || value == null)
            return this;

        this.jsonObject.addProperty(key, value);
        return this;
    }

    public Configuration addIntegerProperty(String key, Integer value) {
        if (key == null || value == null)
            return this;

        this.jsonObject.addProperty(key, value);
        return this;
    }

    public Configuration addBooleanProperty(String key, Boolean value) {
        if (key == null || value == null)
            return this;

        this.jsonObject.addProperty(key, value);
        return this;
    }

    public Configuration addLongProperty(String key, Long value) {
        if (key == null || value == null)
            return this;

        this.jsonObject.addProperty(key, value);
        return this;
    }

    public Configuration addConfigurationProperty(String key, Configuration value) {
        if (key == null || value == null)
            return this;

        this.jsonObject.add(key, value.jsonObject);
        return this;
    }

    public Configuration addProperty(String key, Object value) {
        if (key == null || value == null)
            return this;

        this.jsonObject.add(key, ReformCloudLibraryService.GSON.toJsonTree(value));
        return this;
    }

    public Configuration remove(String key) {
        this.jsonObject.remove(key);
        return this;
    }

    public String getStringValue(String key) {
        return jsonObject.has(key) ? jsonObject.get(key).getAsString() : StringUtil.NULL;
    }

    public Long getLongValue(String key) {
        return jsonObject.has(key) ? jsonObject.get(key).getAsLong() : 0L;
    }

    public int getIntegerValue(String key) {
        return jsonObject.has(key) ? jsonObject.get(key).getAsInt() : 0;
    }

    public boolean getBooleanValue(String key) {
        return jsonObject.has(key) && jsonObject.get(key).getAsBoolean();
    }

    public <T> T getValue(String key, Class<T> clazz) {
        return jsonObject.has(key) ? ReformCloudLibraryService.GSON.fromJson(jsonObject.get(key), clazz) : null;
    }

    public <T> T getValue(String key, Type type) {
        return jsonObject.has(key) ? ReformCloudLibraryService.GSON.fromJson(jsonObject.get(key), type) : null;
    }

    public Configuration getConfiguration(String key) {
        return jsonObject.has(key) ? new Configuration(jsonObject.get(key).getAsJsonObject()) : null;
    }

    private boolean write(File backend) {
        if (backend.exists())
            backend.delete();

        try (OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(backend), StandardCharsets.UTF_8.name())) {
            ReformCloudLibraryService.GSON.toJson(jsonObject, (writer));
            return true;
        } catch (final IOException ex) {
            StringUtil.printError(ReformCloudLibraryServiceProvider.getInstance().getLoggerProvider(), "Error while storing configuration file", ex);
        }
        return false;
    }

    public boolean write(Path path) {
        return this.write(path.toFile());
    }

    public boolean write(String path) {
        return write(Paths.get(path));
    }

    public static Configuration parse(File file) {
        try (InputStreamReader reader = new InputStreamReader(Files.newInputStream(file.toPath()), StandardCharsets.UTF_8.name()); BufferedReader bufferedReader = new BufferedReader(reader)) {
            return new Configuration(ReformCloudLibraryService.PARSER.parse(bufferedReader).getAsJsonObject());
        } catch (final IOException ex) {
            StringUtil.printError(ReformCloudLibraryServiceProvider.getInstance().getLoggerProvider(), "Error while loading configuration", ex);
        }
        return new Configuration();
    }

    public static Configuration parse(Path path) {
        return parse(path.toFile());
    }

    public static Configuration parse(String path) {
        return parse(Paths.get(path));
    }

    public static Configuration fromString(String in) {
        return new Configuration(ReformCloudLibraryService.PARSER.parse(in).getAsJsonObject());
    }

    public Configuration clear() {
        this.jsonObject.entrySet().forEach(jsonObject -> remove(jsonObject.getKey()));
        return this;
    }

    public String getJsonString() {
        return this.jsonObject.toString();
    }

    public boolean contains(String key) {
        return this.jsonObject.has(key);
    }
}
