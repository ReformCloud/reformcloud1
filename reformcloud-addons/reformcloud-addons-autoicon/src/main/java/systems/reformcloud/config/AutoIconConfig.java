/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.config;

import com.google.gson.reflect.TypeToken;
import systems.reformcloud.ReformCloudController;
import systems.reformcloud.ReformCloudLibraryServiceProvider;
import systems.reformcloud.autoicon.IconData;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.exceptions.InstanceAlreadyExistsException;
import systems.reformcloud.utility.StringUtil;
import systems.reformcloud.utility.files.FileUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;

/**
 * @author _Klaro | Pasqual K. / created on 23.04.2019
 */

public final class AutoIconConfig implements Serializable {

    private Map<String, IconData> icons = new HashMap<>();

    private static AutoIconConfig instance;

    public AutoIconConfig() {
        if (instance != null) {
            throw new InstanceAlreadyExistsException();
        }

        instance = this;

        if (!Files.exists(Paths.get("reformcloud/addons/icons"))) {
            FileUtils.createDirectory(Paths.get("reformcloud/addons/icons/files"));
            new Configuration()
                .addValue("config", Collections.singletonList(new IconConfig(
                    "Proxy",
                    10,
                    Collections.singletonList("reformcloud/addons/icons/files/default.png")
                ))).write(Paths.get("reformcloud/addons/icons/config.json"));
            copyCompiledFile();
        }

        List<IconConfig> iconConfigs = Configuration.parse("reformcloud/addons/icons/config.json")
            .getValue("config", new TypeToken<List<IconConfig>>() {
            });

        iconConfigs.forEach(e -> e.getIconPaths().forEach(path -> {
            try {
                BufferedImage bufferedImage = ImageIO.read(new File(path));
                if (bufferedImage.getWidth() != 64 || bufferedImage.getHeight() != 64) {
                    ReformCloudController.getInstance().getColouredConsoleProvider().serve()
                        .accept("The icon size of icon \"" + path + "\" is incorrect");
                } else {
                    byte[] file = Files.readAllBytes(Paths.get(path));
                    if (!icons.containsKey(e.getTargetGroup())) {
                        icons.put(e.getTargetGroup(),
                            new IconData(new ArrayList<>(), e.getUpdateTimeInSeconds()));
                    }

                    icons.get(e.getTargetGroup()).getIcons().add(file);
                }
            } catch (final IOException ex) {
                StringUtil.printError(
                    ReformCloudController.getInstance().getColouredConsoleProvider(),
                    "Can't read the input file (Are you sure, the file exists)",
                    ex
                );
            }
        }));
    }

    public static AutoIconConfig getInstance() {
        return AutoIconConfig.instance;
    }

    private void copyCompiledFile() {
        try (InputStream localInputStream = AutoIconConfig.class.getClassLoader()
            .getResourceAsStream("defaults/default.png")) {
            assert localInputStream != null;
            Files.copy(localInputStream, Paths.get("reformcloud/addons/icons/files/default.png"),
                StandardCopyOption.REPLACE_EXISTING);
        } catch (final IOException ex) {
            StringUtil
                .printError(ReformCloudLibraryServiceProvider.getInstance().getColouredConsoleProvider(),
                    "Could not copy local file", ex);
        }
    }

    public IconData ofGroup(String name) {
        return icons.get(name);
    }
}
