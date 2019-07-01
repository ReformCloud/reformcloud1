/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.meta.system.system;

import java.beans.ConstructorProperties;
import java.io.Serializable;
import java.lang.management.RuntimeMXBean;
import java.util.Map;
import systems.reformcloud.ReformCloudLibraryService;
import systems.reformcloud.utility.annotiations.NeverNull;

/**
 * @author _Klaro | Pasqual K. / created on 26.06.2019
 */

public final class SystemRuntimeSnapshot implements Serializable {

    private String bootClassPath;

    private String classPath;

    private String javaRuntimeEnvoirementName;

    private String vmVendor;

    private String vmName;

    private long bootstrapTime;

    private long uptime;

    private Map<String, String> systemProperties;

    @ConstructorProperties({"bootClassPath", "classPath", "javaRuntimeEnvoirementName", "vmVendor",
        "vmName", "bootstrapTime", "uptime", "systemProperties"})
    private SystemRuntimeSnapshot(String bootClassPath, String classPath,
        String javaRuntimeEnvoirementName, String vmVendor, String vmName, long bootstrapTime,
        long uptime, Map<String, String> systemProperties) {
        this.bootClassPath = bootClassPath;
        this.classPath = classPath;
        this.javaRuntimeEnvoirementName = javaRuntimeEnvoirementName;
        this.vmVendor = vmVendor;
        this.vmName = vmName;
        this.bootstrapTime = bootstrapTime;
        this.uptime = uptime;
        this.systemProperties = systemProperties;
    }

    @NeverNull
    public static SystemRuntimeSnapshot create() {
        RuntimeMXBean runtimeMXBean =
            ReformCloudLibraryService.getRuntimeMXBean();
        return new SystemRuntimeSnapshot(
            runtimeMXBean.getBootClassPath(),
            runtimeMXBean.getClassPath(),
            runtimeMXBean.getName(),
            runtimeMXBean.getVmVendor(),
            runtimeMXBean.getVmName(),
            runtimeMXBean.getStartTime(),
            runtimeMXBean.getUptime(),
            runtimeMXBean.getSystemProperties()
        );
    }

    public String getBootClassPath() {
        return bootClassPath;
    }

    public String getClassPath() {
        return classPath;
    }

    public String getJavaRuntimeEnvoirementName() {
        return javaRuntimeEnvoirementName;
    }

    public String getVmVendor() {
        return vmVendor;
    }

    public String getVmName() {
        return vmName;
    }

    public long getBootstrapTime() {
        return bootstrapTime;
    }

    public long getUptime() {
        return uptime;
    }

    public Map<String, String> getSystemProperties() {
        return systemProperties;
    }
}
