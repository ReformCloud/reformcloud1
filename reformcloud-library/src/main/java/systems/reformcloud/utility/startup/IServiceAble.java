/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.utility.startup;

import java.io.IOException;

/**
 * @author _Klaro | Pasqual K. / created on 27.05.2019
 */

public interface IServiceAble {
    boolean bootstrap();

    void bootstrap0();

    boolean isAlive();

    void executeCommand(String command);

    String uploadLog() throws IOException;

    void shutdown(boolean update);

    void shutdown();
}
