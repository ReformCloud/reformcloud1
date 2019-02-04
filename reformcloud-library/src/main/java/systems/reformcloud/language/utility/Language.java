/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.language.utility;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 04.02.2019
 */

@AllArgsConstructor
@Getter
public class Language implements Serializable {
    private static final long serialVersionUID = -897922544991008954L;

    private String
            help_default, help_command_not_found,
            controller_loading_client, controller_loading_server,
            controller_loading_proxy, loading_done,
            controller_reload, controller_reload_done,
            version_available, version_update,
            controller_servprocess_stopped, controller_proxyprocess_stopped,
            waiting_for_tasks, addon_prepared,
            addon_enabled, addon_closed,
            webserver_bound, netty_server_bound,
            controller_channel_connected, controller_channel_disconnected,
            controller_process_add, controller_process_stopped,
            controller_process_ready, controller_command_executed,
            controller_command_executed_packet, controller_get_log_in,
            controller_server_added_to_queue, controller_proxy_added_to_queue,
            client_shutdown_process, client_copies_template,
            client_wait_start, channel_global_disconnected;
}
