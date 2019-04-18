/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.examples.configuration;

import com.google.gson.reflect.TypeToken;
import systems.reformcloud.configurations.Configuration;

import java.io.Serializable;
import java.nio.file.Paths;

/**
 * @author _Klaro | Pasqual K. / created on 27.12.2018
 */

public final class ConfigurationExample implements Serializable {
    public ConfigurationExample() {
        {
            Configuration configuration = new Configuration(); //Returns a new, empty configuration

            configuration
                    .addStringProperty("key", "value") //Adds a String value into the json configuration

                    .addProperty("object", new Object()) //Adds a object into the Configuration ; Can be a class, too

                    .addBooleanProperty("boolean", true) //Adds a boolean property

                    .addConfigurationProperty("config", new Configuration()) //Adds a full configuration

                    .addIntegerProperty("int", 1) //Adds an Integer into the configuration
                    .write(Paths.get("URLClassPath.json")); //saves it as a file : !! Don't forget the .json !!
        }

        Configuration configuration = Configuration.parse(Paths.get("URLClassPath.json")); //loads an existing configuration

        configuration.getStringValue("key"); //returns the given String

        configuration.getValue("object", new TypeToken<Object>() {
        }.getType()); //returns the object with a type token

        configuration.getValue("object", Object.class); //also available with the class

        configuration.getConfiguration("config"); //returns a configuration

        configuration.getIntegerValue("int"); //returns an integer property
    }
}
