/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.configuration;

import com.google.gson.reflect.TypeToken;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import systems.reformcloud.ReformCloudController;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.meta.server.ServerGroup;
import systems.reformcloud.network.in.PacketInRequestParameters;
import systems.reformcloud.parameters.ParameterGroup;
import systems.reformcloud.utility.StringUtil;
import systems.reformcloud.utility.files.FileUtils;
import systems.reformcloud.utility.map.MapUtility;

/**
 * @author _Klaro | Pasqual K. / created on 14.04.2019
 */

public final class ParametersConfiguration implements Serializable {

    private List<ParameterGroup> parameters;

    public ParametersConfiguration() {
        if (!Files.exists(Paths.get("reformcloud/addons/parameters/config.json"))) {
            FileUtils.createDirectory(Paths.get("reformcloud/addons/parameters"));
            ServerGroup serverGroup = ReformCloudController.getInstance().getAllServerGroups()
                .stream().findFirst().orElse(null);
            Configuration configuration = new Configuration();

            configuration.addValue("config", serverGroup == null ? Collections.EMPTY_LIST
                : Collections.singletonList(new ParameterGroup(
                    serverGroup.getName(), StringUtil.JAVA, new ArrayList<>(), new ArrayList<>()
                )));

            configuration.write("reformcloud/addons/parameters/config.json");
        }

        this.parameters = Configuration.parse("reformcloud/addons/parameters/config.json")
            .getValue("config", new TypeToken<List<ParameterGroup>>() {
            }.getType());

        ReformCloudController.getInstance().getNettyHandler()
            .registerQueryHandler("RequestParameters", new PacketInRequestParameters());
    }

    public ParameterGroup forGroup(String name) {
        return this.parameters.stream().filter(e -> e.getGroupName().equals(name)).findFirst()
            .orElse(null);
    }

    public void delete(String name) {
        List<ParameterGroup> copy = MapUtility.copyOf(this.parameters);
        copy.stream().filter(e -> e.getGroupName().equals(name))
            .forEach(e -> this.parameters.remove(e));
        new Configuration().addValue("config", this.parameters)
            .write("reformcloud/addons/parameters/config.json");
    }

    public void create(ParameterGroup parameterGroup) {
        this.parameters.add(parameterGroup);
        new Configuration().addValue("config", this.parameters)
            .write("reformcloud/addons/parameters/config.json");
    }

    public boolean exists(String name) {
        return this.parameters.stream().anyMatch(e -> e.getGroupName().equals(name));
    }

    public List<ParameterGroup> getParameters() {
        return this.parameters;
    }
}
