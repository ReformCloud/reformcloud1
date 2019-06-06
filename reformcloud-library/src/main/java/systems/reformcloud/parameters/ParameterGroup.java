/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.parameters;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * @author _Klaro | Pasqual K. / created on 14.04.2019
 */

public final class ParameterGroup implements Serializable {

    private String groupName;

    private String javaCommand;

    private List<String> preParameters;

    private List<String> afterParameters;

    @java.beans.ConstructorProperties({"groupName", "javaCommand", "preParameters",
        "afterParameters"})
    public ParameterGroup(String groupName, String javaCommand, List<String> preParameters,
        List<String> afterParameters) {
        this.groupName = groupName;
        this.javaCommand = javaCommand;
        this.preParameters = preParameters;
        this.afterParameters = afterParameters;
    }

    public String getGroupName() {
        return this.groupName;
    }

    public String getJavaCommand() {
        return this.javaCommand;
    }

    public List<String> getPreParameters() {
        return this.preParameters;
    }

    public List<String> getAfterParameters() {
        return this.afterParameters;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public void setJavaCommand(String javaCommand) {
        this.javaCommand = javaCommand;
    }

    public void setPreParameters(List<String> preParameters) {
        this.preParameters = preParameters;
    }

    public void setAfterParameters(List<String> afterParameters) {
        this.afterParameters = afterParameters;
    }

    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ParameterGroup)) {
            return false;
        }
        final ParameterGroup other = (ParameterGroup) o;
        final Object this$groupName = this.getGroupName();
        final Object other$groupName = other.getGroupName();
        if (!Objects.equals(this$groupName, other$groupName)) {
            return false;
        }
        final Object this$javaCommand = this.getJavaCommand();
        final Object other$javaCommand = other.getJavaCommand();
        if (!Objects.equals(this$javaCommand, other$javaCommand)) {
            return false;
        }
        final Object this$preParameters = this.getPreParameters();
        final Object other$preParameters = other.getPreParameters();
        if (!Objects.equals(this$preParameters, other$preParameters)) {
            return false;
        }
        final Object this$afterParameters = this.getAfterParameters();
        final Object other$afterParameters = other.getAfterParameters();
        if (!Objects.equals(this$afterParameters, other$afterParameters)) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $groupName = this.getGroupName();
        result = result * PRIME + ($groupName == null ? 43 : $groupName.hashCode());
        final Object $javaCommand = this.getJavaCommand();
        result = result * PRIME + ($javaCommand == null ? 43 : $javaCommand.hashCode());
        final Object $preParameters = this.getPreParameters();
        result = result * PRIME + ($preParameters == null ? 43 : $preParameters.hashCode());
        final Object $afterParameters = this.getAfterParameters();
        result = result * PRIME + ($afterParameters == null ? 43 : $afterParameters.hashCode());
        return result;
    }

    public String toString() {
        return "ParameterGroup(groupName=" + this.getGroupName() + ", javaCommand=" + this
            .getJavaCommand() + ", preParameters=" + this.getPreParameters() + ", afterParameters="
            + this.getAfterParameters() + ")";
    }
}
