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
            controller_reload, global_reload_done,
            controller_deleting_servergroup, controller_deleting_proxygroup,
            controller_delete_client, controller_icon_size_invalid,
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
            client_wait_start, channel_global_disconnected,
            screen_kicked_process_disconnect, client_controller_info_reload_success,
            controller_socket_bind_success, download_trying,
            download_success, command_addons_no_addons_loaded,
            command_addons_following_loaded, command_addons_addon_description,
            servergroup_not_found, proxygroup_not_found,
            client_not_found, client_not_connected, process_not_connected,
            command_assignment_value_updated, command_assignment_value_removed,
            command_assignment_value_added, command_assignment_value_not_updatable,
            command_copy_try, command_copy_backend_not_client,
            setup_name_of_group, setup_name_of_client,
            setup_choose_minecraft_version, setup_choose_spigot_version,
            setup_choose_reset_type, setup_choose_proxy_reset_type,
            setup_trying_to_create,
            setup_choose_proxy_version, setup_name_of_new_client,
            setup_ip_of_new_client, setup_controller_ip,
            setup_name_of_first_client, setup_ram_of_default_group,
            setup_ram_of_default_proxy_group, setup_load_default_addons,
            setup_default_user_created, command_error_occurred,
            command_create_webuser_created, command_create_template_created_success,
            command_deploy_trying, command_developer_debug_enable,
            command_developer_debug_disable, command_developer_standby_enable,
            command_developer_standby_disable, command_execute_success,
            command_exit_doing, command_listgroup_list,
            command_log_success, command_process_queue_requested,
            command_process_remove_queue_entry, command_process_trying_startup,
            no_available_client_for_startup, command_process_try_stop,
            command_screen_successfully_left, command_webpermission_remove_success,
            command_webpermission_add_success, command_whitelist_success,
            command_whitelist_removed;
}
