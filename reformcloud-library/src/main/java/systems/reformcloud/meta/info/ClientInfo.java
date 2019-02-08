/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.meta.info;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * @author _Klaro | Pasqual K. / created on 02.02.2019
 */

@AllArgsConstructor
@Getter
public final class ClientInfo implements Serializable {
    private static final long serialVersionUID = 588638903114905632L;

    @Setter
    public int maxMemory, cpuCoresSystem;

    private boolean ready;
    private List<String> startedServers;
    private List<String> startedProxies;

    @Setter
    private int usedMemory;
    @Setter
    private double cpuUsage;
    @Setter
    private long systemMemoryUsage;
    @Setter
    private long systemMemoryMax;
}
