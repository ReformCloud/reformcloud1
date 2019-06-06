/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.utility.parameters;

import systems.reformcloud.parameters.ParameterGroup;
import systems.reformcloud.utility.StringUtil;
import systems.reformcloud.utility.files.DownloadManager;

import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * @author _Klaro | Pasqual K. / created on 14.04.2019
 */

public final class ParameterManager implements Serializable {

    private List<ParameterGroup> parameterGroups;

    @java.beans.ConstructorProperties({"parameterGroups"})
    public ParameterManager(List<ParameterGroup> parameterGroups) {
        this.parameterGroups = parameterGroups;
    }

    public boolean exists(String name) {
        return this.parameterGroups.stream().anyMatch(e -> e.getGroupName().equals(name));
    }

    private ParameterGroup forGroup(String name) {
        return this.parameterGroups.stream().filter(e -> e.getGroupName().equals(name)).findFirst()
            .orElse(null);
    }

    public String buildJavaCommand(String group, String[] before, String[] after) {
        StringBuilder stringBuilder = new StringBuilder();
        ParameterGroup parameterGroup = this.forGroup(group);
        this.checkForLoader();

        if (parameterGroup != null) {
            stringBuilder.append(parameterGroup.getJavaCommand()).append(StringUtil.SPACE);
        } else {
            stringBuilder.append(StringUtil.JAVA).append(StringUtil.SPACE);
        }

        for (String s : before) {
            stringBuilder.append(s).append(StringUtil.SPACE);
        }

        if (parameterGroup != null) {
            parameterGroup.getPreParameters()
                .forEach(e -> stringBuilder.append(e).append(StringUtil.SPACE));
        }

        for (String s : after) {
            stringBuilder.append(s).append(StringUtil.SPACE);
        }

        if (parameterGroup != null) {
            parameterGroup.getAfterParameters()
                .forEach(e -> stringBuilder.append(e).append(StringUtil.SPACE));
        }

        return stringBuilder.substring(0, stringBuilder.length() - 1);
    }

    public void update(List<ParameterGroup> parameterGroups) {
        if (parameterGroups == null) {
            return;
        }

        this.parameterGroups = parameterGroups;
    }

    private void checkForLoader() {
        if (Files.exists(Paths.get("reformcloud/files/ReformCloudProcess.jar"))) {
            return;
        }

        DownloadManager.downloadSilentAndDisconnect(
            "https://internal.reformcloud.systems/latest/ReformCloudProcess.jar",
            "reformcloud/files/ReformCloudProcess.jar"
        );
    }
}
