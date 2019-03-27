/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.packets.in;

import systems.reformcloud.ReformCloudClient;
import systems.reformcloud.ReformCloudLibraryServiceProvider;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.meta.client.settings.ClientSettings;
import systems.reformcloud.network.interfaces.NetworkInboundHandler;
import systems.reformcloud.utility.StringUtil;
import systems.reformcloud.utility.TypeTokenAdaptor;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

/**
 * @author _Klaro | Pasqual K. / created on 27.03.2019
 */

public final class PacketInUpdateClientSetting implements Serializable, NetworkInboundHandler {
    @Override
    public void handle(Configuration configuration) {
        ClientSettings clientSettings = configuration.getValue("setting", TypeTokenAdaptor.getCLIENT_SETTING_TYPE());

        Properties properties = new Properties();
        try (InputStreamReader inputStreamReader = new InputStreamReader(Files.newInputStream(Paths.get("configuration.properties")))) {
            properties.load(inputStreamReader);
        } catch (final IOException ex) {
            StringUtil.printError(ReformCloudLibraryServiceProvider.getInstance().getLoggerProvider(), "Could not load configuration.properties", ex);
        }

        properties.setProperty(clientSettings.getConfigString(), configuration.getStringValue("value"));

        try (OutputStream outputStream = Files.newOutputStream(Paths.get("configuration.properties"))) {
            properties.store(outputStream, "ReformCloud default Configuration");
        } catch (final IOException ex) {
            StringUtil.printError(ReformCloudLibraryServiceProvider.getInstance().getLoggerProvider(), "Could not store configuration.properties", ex);
        }

        ReformCloudClient.getInstance().reloadAll();
    }
}
