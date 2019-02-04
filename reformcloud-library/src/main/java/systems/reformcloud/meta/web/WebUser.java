/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.meta.web;

import systems.reformcloud.ReformCloudLibraryService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Map;

/**
 * @author _Klaro | Pasqual K. / created on 16.12.2018
 */

@AllArgsConstructor
@Getter
@Setter
public class WebUser implements Serializable {
    private static final long serialVersionUID = 6104918827767931388L;

    private String user, password;
    private Map<String, Boolean> permissions = ReformCloudLibraryService.concurrentHashMap();
}
