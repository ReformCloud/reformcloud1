/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.utility.startup;

import java.io.IOException;
import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 27.05.2019
 */

public interface ServiceAble extends Serializable {

    boolean bootstrap();

    void bootstrap0();

    boolean isAlive();

    void executeCommand(String command);

    String uploadLog() throws IOException;

    void shutdown(boolean update);

    void shutdown();
}
