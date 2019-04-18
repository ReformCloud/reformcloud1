/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.configurations;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.Data;
import systems.reformcloud.ReformCloudLibraryService;
import systems.reformcloud.ReformCloudLibraryServiceProvider;
import systems.reformcloud.utility.StringUtil;
import systems.reformcloud.utility.map.MapUtility;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Set;

/**
 * Class to create easier Configuration files
 *
 * @author _Klaro | Pasqual K. / created on 18.10.2018
 */

@Data
public final class Configuration {
    /**
     * The json object of the configuration
     */
    protected JsonObject jsonObject;

    /**
     * Creates a new configuration
     */
    public Configuration() {
        this.jsonObject = new JsonObject();
    }

    /**
     * Creates a new configuration
     *
     * @param jsonObject The json object of an existing configuration
     */
    public Configuration(JsonObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    /**
     * Adds a string property to the config
     *
     * @param key       The key of the item
     * @param value     The string as value
     * @return The config class instance
     */
    public Configuration addStringProperty(String key, String value) {
        if (key == null || value == null)
            return this;

        this.jsonObject.addProperty(key, value);
        return this;
    }

    /**
     * Adds a integer property to the config
     *
     * @param key       The key of the item
     * @param value     The string as value
     * @return The config class instance
     */
    public Configuration addIntegerProperty(String key, Integer value) {
        if (key == null || value == null)
            return this;

        this.jsonObject.addProperty(key, value);
        return this;
    }

    /**
     * Adds a boolean property to the config
     *
     * @param key       The key of the item
     * @param value     The string as value
     * @return The config class instance
     */
    public Configuration addBooleanProperty(String key, Boolean value) {
        if (key == null || value == null)
            return this;

        this.jsonObject.addProperty(key, value);
        return this;
    }

    /**
     * Adds a long property to the config
     *
     * @param key       The key of the item
     * @param value     The string as value
     * @return The config class instance
     */
    public Configuration addLongProperty(String key, Long value) {
        if (key == null || value == null)
            return this;

        this.jsonObject.addProperty(key, value);
        return this;
    }

    /**
     * Adds a configuration property to the config
     *
     * @param key       The key of the item
     * @param value     The string as value
     * @return The config class instance
     */
    public Configuration addConfigurationProperty(String key, Configuration value) {
        if (key == null || value == null)
            return this;

        this.jsonObject.add(key, value.jsonObject);
        return this;
    }

    /**
     * Adds an object property to the config
     *
     * @param key       The key of the item
     * @param value     The string as value
     * @return The config class instance
     */
    public Configuration addProperty(String key, Object value) {
        if (key == null || value == null)
            return this;

        this.jsonObject.add(key, ReformCloudLibraryService.GSON.toJsonTree(value));
        return this;
    }

    /**
     * Removes an key out of the config
     *
     * @param key       The key of the value which should be removed
     * @return The config class instance
     */
    public Configuration remove(String key) {
        this.jsonObject.remove(key);
        return this;
    }

    /**
     * Gets a string value out of the config
     *
     * @param key       The key of the config value
     * @return The config class instance
     */
    public String getStringValue(String key) {
        return jsonObject.has(key) ? jsonObject.get(key).getAsString() : StringUtil.NULL;
    }

    /**
     * Gets a long value out of the config
     *
     * @param key       The key of the config value
     * @return The config class instance
     */
    public Long getLongValue(String key) {
        return jsonObject.has(key) ? jsonObject.get(key).getAsLong() : 0L;
    }

    /**
     * Gets a integer value out of the config
     *
     * @param key       The key of the config value
     * @return The config class instance
     */
    public int getIntegerValue(String key) {
        return jsonObject.has(key) ? jsonObject.get(key).getAsInt() : 0;
    }

    /**
     * Gets a boolean value out of the config
     *
     * @param key       The key of the config value
     * @return The config class instance
     */
    public boolean getBooleanValue(String key) {
        return jsonObject.has(key) && jsonObject.get(key).getAsBoolean();
    }

    /**
     * Gets a object value out of the config
     *
     * @param key       The key of the config value
     * @param clazz     The defining class of the containing object
     * @return The config class instance
     */
    public <T> T getValue(String key, Class<T> clazz) {
        return jsonObject.has(key) ? ReformCloudLibraryService.GSON.fromJson(jsonObject.get(key), clazz) : null;
    }

    /**
     * Gets a object value out of the config
     *
     * @param key       The key of the config value
     * @param type      The defining type of the containing object
     * @return The config class instance
     */
    public <T> T getValue(String key, Type type) {
        return jsonObject.has(key) ? ReformCloudLibraryService.GSON.fromJson(jsonObject.get(key), type) : null;
    }

    /**
     * Gets a configuration value out of the config
     *
     * @param key       The key of the config value
     * @return The config class instance
     */
    public Configuration getConfiguration(String key) {
        return jsonObject.has(key) ? new Configuration(jsonObject.get(key).getAsJsonObject()) : null;
    }

    /**
     * Saves the file to a specific path
     *
     * @param path          The path as file where the file should be saved to
     * @return If the operation was successful
     */
    private boolean write(File path) {
        if (path.exists())
            path.delete();

        try (OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(path), StandardCharsets.UTF_8)) {
            ReformCloudLibraryService.GSON.toJson(jsonObject, (writer));
            return true;
        } catch (final IOException ex) {
            StringUtil.printError(ReformCloudLibraryServiceProvider.getInstance().getLoggerProvider(), "Error while storing configuration file", ex);
        }
        return false;
    }

    /**
     * Saves the file to a specific path
     *
     * @param path          The path where the file should be saved to
     * @return If the operation was successful
     */
    public boolean write(Path path) {
        return this.write(path.toFile());
    }

    /**
     * Saves the file to a specific path
     *
     * @param path          The path as string where the file should be saved to
     * @return If the operation was successful
     */
    public boolean write(String path) {
        return this.write(Paths.get(path));
    }

    /**
     * Reads a config from the given path as file
     *
     * @param path      The path where the file is saved
     * @return The configuration which was loaded or an empty
     *                  configuration if the file wasn't found or an error occurred
     */
    public static Configuration parse(File path) {
        try (InputStreamReader reader = new InputStreamReader(Files.newInputStream(path.toPath()), StandardCharsets.UTF_8);
             BufferedReader bufferedReader = new BufferedReader(reader)) {
            return new Configuration(ReformCloudLibraryService.PARSER.parse(bufferedReader).getAsJsonObject());
        } catch (final IOException ex) {
            StringUtil.printError(ReformCloudLibraryServiceProvider.getInstance().getLoggerProvider(), "Error while loading configuration", ex);
        }
        return new Configuration();
    }

    /**
     * Reads a config from the given path
     *
     * @param path      The path where the file is saved
     * @return The configuration which was loaded or an empty
     *                  configuration if the file wasn't found or an error occurred
     */
    public static Configuration parse(Path path) {
        return parse(path.toFile());
    }

    /**
     * Reads a config from the given path as string
     *
     * @param path      The path where the file is saved
     * @return The configuration which was loaded or an empty
     *                  configuration if the file wasn't found or an error occurred
     */
    public static Configuration parse(String path) {
        return parse(Paths.get(path));
    }

    /**
     * Creates a new configuration of the given string
     *
     * @param in        The string of the config in json format
     * @return A new configuration which contains the content of the string
     */
    public static Configuration fromString(String in) {
        return new Configuration(ReformCloudLibraryService.PARSER.parse(in).getAsJsonObject());
    }

    /**
     * Creates a new configuration of the given map
     *
     * @param in        A map containing all keys of the config
     * @return A new configuration which contains the content of the map
     */
    public static Configuration fromMap(Map<? extends String, ?> in) {
        Configuration configuration = new Configuration();
        for (Map.Entry<? extends String, ?> entry : in.entrySet())
            configuration.addProperty(entry.getKey(), entry.getValue());

        return configuration;
    }

    /**
     * Removes all keys out of the config
     *
     * @return An instance of this class
     */
    public Configuration clear() {
        Set<Map.Entry<String, JsonElement>> copy = MapUtility.copyOf(this.jsonObject.entrySet());
        copy.forEach(jsonObject -> remove(jsonObject.getKey()));
        return this;
    }

    /**
     * Get the config as string
     *
     * @return The parsed string of the config
     */
    public String getJsonString() {
        return this.jsonObject.toString();
    }

    /**
     * Checks if the config contains a specific key
     *
     * @param key       The key which should be found
     * @return If the config contains the key or not
     */
    public boolean contains(String key) {
        return this.jsonObject.has(key);
    }
}
