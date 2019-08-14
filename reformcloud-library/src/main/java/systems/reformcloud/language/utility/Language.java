/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.language.utility;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 04.02.2019
 */

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
        command_whitelist_removed, setup_choose_database, setup_database_host,
        setup_database_port, setup_database_username, setup_database_password,
        setup_database_name, setup_memory_servergroup, setup_memory_proxygroup;

    @java.beans.ConstructorProperties({"help_default", "help_command_not_found",
        "controller_loading_client", "controller_loading_server", "controller_loading_proxy",
        "loading_done", "controller_reload", "global_reload_done",
        "controller_deleting_servergroup", "controller_deleting_proxygroup",
        "controller_delete_client", "controller_icon_size_invalid", "version_available",
        "version_update", "controller_servprocess_stopped", "controller_proxyprocess_stopped",
        "waiting_for_tasks", "addon_prepared", "addon_enabled", "addon_closed", "webserver_bound",
        "netty_server_bound", "controller_channel_connected", "controller_channel_disconnected",
        "controller_process_add", "controller_process_stopped", "controller_process_ready",
        "controller_command_executed", "controller_command_executed_packet",
        "controller_get_log_in", "controller_server_added_to_queue",
        "controller_proxy_added_to_queue", "client_shutdown_process", "client_copies_template",
        "client_wait_start", "channel_global_disconnected", "screen_kicked_process_disconnect",
        "client_controller_info_reload_success", "controller_socket_bind_success",
        "download_trying", "download_success", "command_addons_no_addons_loaded",
        "command_addons_following_loaded", "command_addons_addon_description",
        "servergroup_not_found", "proxygroup_not_found", "client_not_found", "client_not_connected",
        "process_not_connected", "command_assignment_value_updated",
        "command_assignment_value_removed", "command_assignment_value_added",
        "command_assignment_value_not_updatable", "command_copy_try",
        "command_copy_backend_not_client", "setup_name_of_group", "setup_name_of_client",
        "setup_choose_minecraft_version", "setup_choose_spigot_version", "setup_choose_reset_type",
        "setup_choose_proxy_reset_type", "setup_trying_to_create", "setup_choose_proxy_version",
        "setup_name_of_new_client", "setup_ip_of_new_client", "setup_controller_ip",
        "setup_name_of_first_client", "setup_ram_of_default_group",
        "setup_ram_of_default_proxy_group", "setup_load_default_addons",
        "setup_default_user_created", "command_error_occurred", "command_create_webuser_created",
        "command_create_template_created_success", "command_deploy_trying",
        "command_developer_debug_enable", "command_developer_debug_disable",
        "command_developer_standby_enable", "command_developer_standby_disable",
        "command_execute_success", "command_exit_doing", "command_listgroup_list",
        "command_log_success", "command_process_queue_requested",
        "command_process_remove_queue_entry", "command_process_trying_startup",
        "no_available_client_for_startup", "command_process_try_stop",
        "command_screen_successfully_left", "command_webpermission_remove_success",
        "command_webpermission_add_success", "command_whitelist_success",
        "command_whitelist_removed", "setup_choose_database", "setup_database_host",
        "setup_database_port", "setup_database_username", "setup_database_password",
        "setup_database_name"})
    protected Language(String help_default, String help_command_not_found,
        String controller_loading_client, String controller_loading_server,
        String controller_loading_proxy, String loading_done, String controller_reload,
        String global_reload_done, String controller_deleting_servergroup,
        String controller_deleting_proxygroup, String controller_delete_client,
        String controller_icon_size_invalid, String version_available, String version_update,
        String controller_servprocess_stopped, String controller_proxyprocess_stopped,
        String waiting_for_tasks, String addon_prepared, String addon_enabled, String addon_closed,
        String webserver_bound, String netty_server_bound, String controller_channel_connected,
        String controller_channel_disconnected, String controller_process_add,
        String controller_process_stopped, String controller_process_ready,
        String controller_command_executed, String controller_command_executed_packet,
        String controller_get_log_in, String controller_server_added_to_queue,
        String controller_proxy_added_to_queue, String client_shutdown_process,
        String client_copies_template, String client_wait_start, String channel_global_disconnected,
        String screen_kicked_process_disconnect, String client_controller_info_reload_success,
        String controller_socket_bind_success, String download_trying, String download_success,
        String command_addons_no_addons_loaded, String command_addons_following_loaded,
        String command_addons_addon_description, String servergroup_not_found,
        String proxygroup_not_found, String client_not_found, String client_not_connected,
        String process_not_connected, String command_assignment_value_updated,
        String command_assignment_value_removed, String command_assignment_value_added,
        String command_assignment_value_not_updatable, String command_copy_try,
        String command_copy_backend_not_client, String setup_name_of_group,
        String setup_name_of_client, String setup_choose_minecraft_version,
        String setup_choose_spigot_version, String setup_choose_reset_type,
        String setup_choose_proxy_reset_type, String setup_trying_to_create,
        String setup_choose_proxy_version, String setup_name_of_new_client,
        String setup_ip_of_new_client, String setup_controller_ip,
        String setup_name_of_first_client, String setup_ram_of_default_group,
        String setup_ram_of_default_proxy_group, String setup_load_default_addons,
        String setup_default_user_created, String command_error_occurred,
        String command_create_webuser_created, String command_create_template_created_success,
        String command_deploy_trying, String command_developer_debug_enable,
        String command_developer_debug_disable, String command_developer_standby_enable,
        String command_developer_standby_disable, String command_execute_success,
        String command_exit_doing, String command_listgroup_list, String command_log_success,
        String command_process_queue_requested, String command_process_remove_queue_entry,
        String command_process_trying_startup, String no_available_client_for_startup,
        String command_process_try_stop, String command_screen_successfully_left,
        String command_webpermission_remove_success, String command_webpermission_add_success,
        String command_whitelist_success, String command_whitelist_removed) {
        this.help_default = help_default;
        this.help_command_not_found = help_command_not_found;
        this.controller_loading_client = controller_loading_client;
        this.controller_loading_server = controller_loading_server;
        this.controller_loading_proxy = controller_loading_proxy;
        this.loading_done = loading_done;
        this.controller_reload = controller_reload;
        this.global_reload_done = global_reload_done;
        this.controller_deleting_servergroup = controller_deleting_servergroup;
        this.controller_deleting_proxygroup = controller_deleting_proxygroup;
        this.controller_delete_client = controller_delete_client;
        this.controller_icon_size_invalid = controller_icon_size_invalid;
        this.version_available = version_available;
        this.version_update = version_update;
        this.controller_servprocess_stopped = controller_servprocess_stopped;
        this.controller_proxyprocess_stopped = controller_proxyprocess_stopped;
        this.waiting_for_tasks = waiting_for_tasks;
        this.addon_prepared = addon_prepared;
        this.addon_enabled = addon_enabled;
        this.addon_closed = addon_closed;
        this.webserver_bound = webserver_bound;
        this.netty_server_bound = netty_server_bound;
        this.controller_channel_connected = controller_channel_connected;
        this.controller_channel_disconnected = controller_channel_disconnected;
        this.controller_process_add = controller_process_add;
        this.controller_process_stopped = controller_process_stopped;
        this.controller_process_ready = controller_process_ready;
        this.controller_command_executed = controller_command_executed;
        this.controller_command_executed_packet = controller_command_executed_packet;
        this.controller_get_log_in = controller_get_log_in;
        this.controller_server_added_to_queue = controller_server_added_to_queue;
        this.controller_proxy_added_to_queue = controller_proxy_added_to_queue;
        this.client_shutdown_process = client_shutdown_process;
        this.client_copies_template = client_copies_template;
        this.client_wait_start = client_wait_start;
        this.channel_global_disconnected = channel_global_disconnected;
        this.screen_kicked_process_disconnect = screen_kicked_process_disconnect;
        this.client_controller_info_reload_success = client_controller_info_reload_success;
        this.controller_socket_bind_success = controller_socket_bind_success;
        this.download_trying = download_trying;
        this.download_success = download_success;
        this.command_addons_no_addons_loaded = command_addons_no_addons_loaded;
        this.command_addons_following_loaded = command_addons_following_loaded;
        this.command_addons_addon_description = command_addons_addon_description;
        this.servergroup_not_found = servergroup_not_found;
        this.proxygroup_not_found = proxygroup_not_found;
        this.client_not_found = client_not_found;
        this.client_not_connected = client_not_connected;
        this.process_not_connected = process_not_connected;
        this.command_assignment_value_updated = command_assignment_value_updated;
        this.command_assignment_value_removed = command_assignment_value_removed;
        this.command_assignment_value_added = command_assignment_value_added;
        this.command_assignment_value_not_updatable = command_assignment_value_not_updatable;
        this.command_copy_try = command_copy_try;
        this.command_copy_backend_not_client = command_copy_backend_not_client;
        this.setup_name_of_group = setup_name_of_group;
        this.setup_name_of_client = setup_name_of_client;
        this.setup_choose_minecraft_version = setup_choose_minecraft_version;
        this.setup_choose_spigot_version = setup_choose_spigot_version;
        this.setup_choose_reset_type = setup_choose_reset_type;
        this.setup_choose_proxy_reset_type = setup_choose_proxy_reset_type;
        this.setup_trying_to_create = setup_trying_to_create;
        this.setup_choose_proxy_version = setup_choose_proxy_version;
        this.setup_name_of_new_client = setup_name_of_new_client;
        this.setup_ip_of_new_client = setup_ip_of_new_client;
        this.setup_controller_ip = setup_controller_ip;
        this.setup_name_of_first_client = setup_name_of_first_client;
        this.setup_ram_of_default_group = setup_ram_of_default_group;
        this.setup_ram_of_default_proxy_group = setup_ram_of_default_proxy_group;
        this.setup_load_default_addons = setup_load_default_addons;
        this.setup_default_user_created = setup_default_user_created;
        this.command_error_occurred = command_error_occurred;
        this.command_create_webuser_created = command_create_webuser_created;
        this.command_create_template_created_success = command_create_template_created_success;
        this.command_deploy_trying = command_deploy_trying;
        this.command_developer_debug_enable = command_developer_debug_enable;
        this.command_developer_debug_disable = command_developer_debug_disable;
        this.command_developer_standby_enable = command_developer_standby_enable;
        this.command_developer_standby_disable = command_developer_standby_disable;
        this.command_execute_success = command_execute_success;
        this.command_exit_doing = command_exit_doing;
        this.command_listgroup_list = command_listgroup_list;
        this.command_log_success = command_log_success;
        this.command_process_queue_requested = command_process_queue_requested;
        this.command_process_remove_queue_entry = command_process_remove_queue_entry;
        this.command_process_trying_startup = command_process_trying_startup;
        this.no_available_client_for_startup = no_available_client_for_startup;
        this.command_process_try_stop = command_process_try_stop;
        this.command_screen_successfully_left = command_screen_successfully_left;
        this.command_webpermission_remove_success = command_webpermission_remove_success;
        this.command_webpermission_add_success = command_webpermission_add_success;
        this.command_whitelist_success = command_whitelist_success;
        this.command_whitelist_removed = command_whitelist_removed;
        this.setup_choose_database = "Please choose a database type [\"§eMYSQL§r\", \"§eMONGODB§r\", \"§eFILE§r\"]";
        this.setup_database_host = "Please provide the ip §3address§r of the database";
        this.setup_database_port = "Please provide the port §3port§r of the database";
        this.setup_database_username = "Please provide the §3username§r of the database user";
        this.setup_database_password = "Please provide the §3password§r of the database user";
        this.setup_database_name = "Please provide the §3name§r of the database";
    }

    @java.beans.ConstructorProperties({"help_default", "help_command_not_found",
        "controller_loading_client", "controller_loading_server", "controller_loading_proxy",
        "loading_done", "controller_reload", "global_reload_done",
        "controller_deleting_servergroup", "controller_deleting_proxygroup",
        "controller_delete_client", "controller_icon_size_invalid", "version_available",
        "version_update", "controller_servprocess_stopped", "controller_proxyprocess_stopped",
        "waiting_for_tasks", "addon_prepared", "addon_enabled", "addon_closed", "webserver_bound",
        "netty_server_bound", "controller_channel_connected", "controller_channel_disconnected",
        "controller_process_add", "controller_process_stopped", "controller_process_ready",
        "controller_command_executed", "controller_command_executed_packet",
        "controller_get_log_in", "controller_server_added_to_queue",
        "controller_proxy_added_to_queue", "client_shutdown_process", "client_copies_template",
        "client_wait_start", "channel_global_disconnected", "screen_kicked_process_disconnect",
        "client_controller_info_reload_success", "controller_socket_bind_success",
        "download_trying", "download_success", "command_addons_no_addons_loaded",
        "command_addons_following_loaded", "command_addons_addon_description",
        "servergroup_not_found", "proxygroup_not_found", "client_not_found", "client_not_connected",
        "process_not_connected", "command_assignment_value_updated",
        "command_assignment_value_removed", "command_assignment_value_added",
        "command_assignment_value_not_updatable", "command_copy_try",
        "command_copy_backend_not_client", "setup_name_of_group", "setup_name_of_client",
        "setup_choose_minecraft_version", "setup_choose_spigot_version", "setup_choose_reset_type",
        "setup_choose_proxy_reset_type", "setup_trying_to_create", "setup_choose_proxy_version",
        "setup_name_of_new_client", "setup_ip_of_new_client", "setup_controller_ip",
        "setup_name_of_first_client", "setup_ram_of_default_group",
        "setup_ram_of_default_proxy_group", "setup_load_default_addons",
        "setup_default_user_created", "command_error_occurred", "command_create_webuser_created",
        "command_create_template_created_success", "command_deploy_trying",
        "command_developer_debug_enable", "command_developer_debug_disable",
        "command_developer_standby_enable", "command_developer_standby_disable",
        "command_execute_success", "command_exit_doing", "command_listgroup_list",
        "command_log_success", "command_process_queue_requested",
        "command_process_remove_queue_entry", "command_process_trying_startup",
        "no_available_client_for_startup", "command_process_try_stop",
        "command_screen_successfully_left", "command_webpermission_remove_success",
        "command_webpermission_add_success", "command_whitelist_success",
        "command_whitelist_removed", "setup_choose_database", "setup_database_host",
        "setup_database_port", "setup_database_username", "setup_database_password",
        "setup_database_name", "setup_memory_servergroup", "setup_memory_proxygroup"})
    protected Language(String help_default, String help_command_not_found,
        String controller_loading_client, String controller_loading_server,
        String controller_loading_proxy, String loading_done, String controller_reload,
        String global_reload_done, String controller_deleting_servergroup,
        String controller_deleting_proxygroup, String controller_delete_client,
        String controller_icon_size_invalid, String version_available, String version_update,
        String controller_servprocess_stopped, String controller_proxyprocess_stopped,
        String waiting_for_tasks, String addon_prepared, String addon_enabled, String addon_closed,
        String webserver_bound, String netty_server_bound, String controller_channel_connected,
        String controller_channel_disconnected, String controller_process_add,
        String controller_process_stopped, String controller_process_ready,
        String controller_command_executed, String controller_command_executed_packet,
        String controller_get_log_in, String controller_server_added_to_queue,
        String controller_proxy_added_to_queue, String client_shutdown_process,
        String client_copies_template, String client_wait_start, String channel_global_disconnected,
        String screen_kicked_process_disconnect, String client_controller_info_reload_success,
        String controller_socket_bind_success, String download_trying, String download_success,
        String command_addons_no_addons_loaded, String command_addons_following_loaded,
        String command_addons_addon_description, String servergroup_not_found,
        String proxygroup_not_found, String client_not_found, String client_not_connected,
        String process_not_connected, String command_assignment_value_updated,
        String command_assignment_value_removed, String command_assignment_value_added,
        String command_assignment_value_not_updatable, String command_copy_try,
        String command_copy_backend_not_client, String setup_name_of_group,
        String setup_name_of_client, String setup_choose_minecraft_version,
        String setup_choose_spigot_version, String setup_choose_reset_type,
        String setup_choose_proxy_reset_type, String setup_trying_to_create,
        String setup_choose_proxy_version, String setup_name_of_new_client,
        String setup_ip_of_new_client, String setup_controller_ip,
        String setup_name_of_first_client, String setup_ram_of_default_group,
        String setup_ram_of_default_proxy_group, String setup_load_default_addons,
        String setup_default_user_created, String command_error_occurred,
        String command_create_webuser_created, String command_create_template_created_success,
        String command_deploy_trying, String command_developer_debug_enable,
        String command_developer_debug_disable, String command_developer_standby_enable,
        String command_developer_standby_disable, String command_execute_success,
        String command_exit_doing, String command_listgroup_list, String command_log_success,
        String command_process_queue_requested, String command_process_remove_queue_entry,
        String command_process_trying_startup, String no_available_client_for_startup,
        String command_process_try_stop, String command_screen_successfully_left,
        String command_webpermission_remove_success, String command_webpermission_add_success,
        String command_whitelist_success, String command_whitelist_removed,
        String setup_choose_database, String setup_database_host,
        String setup_database_port, String setup_database_username,
        String setup_database_password, String setup_database_name,
        String setup_memory_servergroup, String setup_memory_proxygroup) {
        this.help_default = help_default;
        this.help_command_not_found = help_command_not_found;
        this.controller_loading_client = controller_loading_client;
        this.controller_loading_server = controller_loading_server;
        this.controller_loading_proxy = controller_loading_proxy;
        this.loading_done = loading_done;
        this.controller_reload = controller_reload;
        this.global_reload_done = global_reload_done;
        this.controller_deleting_servergroup = controller_deleting_servergroup;
        this.controller_deleting_proxygroup = controller_deleting_proxygroup;
        this.controller_delete_client = controller_delete_client;
        this.controller_icon_size_invalid = controller_icon_size_invalid;
        this.version_available = version_available;
        this.version_update = version_update;
        this.controller_servprocess_stopped = controller_servprocess_stopped;
        this.controller_proxyprocess_stopped = controller_proxyprocess_stopped;
        this.waiting_for_tasks = waiting_for_tasks;
        this.addon_prepared = addon_prepared;
        this.addon_enabled = addon_enabled;
        this.addon_closed = addon_closed;
        this.webserver_bound = webserver_bound;
        this.netty_server_bound = netty_server_bound;
        this.controller_channel_connected = controller_channel_connected;
        this.controller_channel_disconnected = controller_channel_disconnected;
        this.controller_process_add = controller_process_add;
        this.controller_process_stopped = controller_process_stopped;
        this.controller_process_ready = controller_process_ready;
        this.controller_command_executed = controller_command_executed;
        this.controller_command_executed_packet = controller_command_executed_packet;
        this.controller_get_log_in = controller_get_log_in;
        this.controller_server_added_to_queue = controller_server_added_to_queue;
        this.controller_proxy_added_to_queue = controller_proxy_added_to_queue;
        this.client_shutdown_process = client_shutdown_process;
        this.client_copies_template = client_copies_template;
        this.client_wait_start = client_wait_start;
        this.channel_global_disconnected = channel_global_disconnected;
        this.screen_kicked_process_disconnect = screen_kicked_process_disconnect;
        this.client_controller_info_reload_success = client_controller_info_reload_success;
        this.controller_socket_bind_success = controller_socket_bind_success;
        this.download_trying = download_trying;
        this.download_success = download_success;
        this.command_addons_no_addons_loaded = command_addons_no_addons_loaded;
        this.command_addons_following_loaded = command_addons_following_loaded;
        this.command_addons_addon_description = command_addons_addon_description;
        this.servergroup_not_found = servergroup_not_found;
        this.proxygroup_not_found = proxygroup_not_found;
        this.client_not_found = client_not_found;
        this.client_not_connected = client_not_connected;
        this.process_not_connected = process_not_connected;
        this.command_assignment_value_updated = command_assignment_value_updated;
        this.command_assignment_value_removed = command_assignment_value_removed;
        this.command_assignment_value_added = command_assignment_value_added;
        this.command_assignment_value_not_updatable = command_assignment_value_not_updatable;
        this.command_copy_try = command_copy_try;
        this.command_copy_backend_not_client = command_copy_backend_not_client;
        this.setup_name_of_group = setup_name_of_group;
        this.setup_name_of_client = setup_name_of_client;
        this.setup_choose_minecraft_version = setup_choose_minecraft_version;
        this.setup_choose_spigot_version = setup_choose_spigot_version;
        this.setup_choose_reset_type = setup_choose_reset_type;
        this.setup_choose_proxy_reset_type = setup_choose_proxy_reset_type;
        this.setup_trying_to_create = setup_trying_to_create;
        this.setup_choose_proxy_version = setup_choose_proxy_version;
        this.setup_name_of_new_client = setup_name_of_new_client;
        this.setup_ip_of_new_client = setup_ip_of_new_client;
        this.setup_controller_ip = setup_controller_ip;
        this.setup_name_of_first_client = setup_name_of_first_client;
        this.setup_ram_of_default_group = setup_ram_of_default_group;
        this.setup_ram_of_default_proxy_group = setup_ram_of_default_proxy_group;
        this.setup_load_default_addons = setup_load_default_addons;
        this.setup_default_user_created = setup_default_user_created;
        this.command_error_occurred = command_error_occurred;
        this.command_create_webuser_created = command_create_webuser_created;
        this.command_create_template_created_success = command_create_template_created_success;
        this.command_deploy_trying = command_deploy_trying;
        this.command_developer_debug_enable = command_developer_debug_enable;
        this.command_developer_debug_disable = command_developer_debug_disable;
        this.command_developer_standby_enable = command_developer_standby_enable;
        this.command_developer_standby_disable = command_developer_standby_disable;
        this.command_execute_success = command_execute_success;
        this.command_exit_doing = command_exit_doing;
        this.command_listgroup_list = command_listgroup_list;
        this.command_log_success = command_log_success;
        this.command_process_queue_requested = command_process_queue_requested;
        this.command_process_remove_queue_entry = command_process_remove_queue_entry;
        this.command_process_trying_startup = command_process_trying_startup;
        this.no_available_client_for_startup = no_available_client_for_startup;
        this.command_process_try_stop = command_process_try_stop;
        this.command_screen_successfully_left = command_screen_successfully_left;
        this.command_webpermission_remove_success = command_webpermission_remove_success;
        this.command_webpermission_add_success = command_webpermission_add_success;
        this.command_whitelist_success = command_whitelist_success;
        this.command_whitelist_removed = command_whitelist_removed;
        this.setup_choose_database = setup_choose_database;
        this.setup_database_host = setup_database_host;
        this.setup_database_port = setup_database_port;
        this.setup_database_username = setup_database_username;
        this.setup_database_password = setup_database_password;
        this.setup_database_name = setup_database_name;
        this.setup_memory_servergroup = setup_memory_servergroup;
        this.setup_memory_proxygroup = setup_memory_proxygroup;
    }

    public String getHelp_default() {
        return this.help_default;
    }

    public String getHelp_command_not_found() {
        return this.help_command_not_found;
    }

    public String getController_loading_client() {
        return this.controller_loading_client;
    }

    public String getController_loading_server() {
        return this.controller_loading_server;
    }

    public String getController_loading_proxy() {
        return this.controller_loading_proxy;
    }

    public String getLoading_done() {
        return this.loading_done;
    }

    public String getController_reload() {
        return this.controller_reload;
    }

    public String getGlobal_reload_done() {
        return this.global_reload_done;
    }

    public String getController_deleting_servergroup() {
        return this.controller_deleting_servergroup;
    }

    public String getController_deleting_proxygroup() {
        return this.controller_deleting_proxygroup;
    }

    public String getController_delete_client() {
        return this.controller_delete_client;
    }

    public String getController_icon_size_invalid() {
        return this.controller_icon_size_invalid;
    }

    public String getVersion_available() {
        return this.version_available;
    }

    public String getVersion_update() {
        return this.version_update;
    }

    public String getController_servprocess_stopped() {
        return this.controller_servprocess_stopped;
    }

    public String getController_proxyprocess_stopped() {
        return this.controller_proxyprocess_stopped;
    }

    public String getWaiting_for_tasks() {
        return this.waiting_for_tasks;
    }

    public String getAddon_prepared() {
        return this.addon_prepared;
    }

    public String getAddon_enabled() {
        return this.addon_enabled;
    }

    public String getAddon_closed() {
        return this.addon_closed;
    }

    public String getWebserver_bound() {
        return this.webserver_bound;
    }

    public String getNetty_server_bound() {
        return this.netty_server_bound;
    }

    public String getController_channel_connected() {
        return this.controller_channel_connected;
    }

    public String getController_channel_disconnected() {
        return this.controller_channel_disconnected;
    }

    public String getController_process_add() {
        return this.controller_process_add;
    }

    public String getController_process_stopped() {
        return this.controller_process_stopped;
    }

    public String getController_process_ready() {
        return this.controller_process_ready;
    }

    public String getController_command_executed() {
        return this.controller_command_executed;
    }

    public String getController_command_executed_packet() {
        return this.controller_command_executed_packet;
    }

    public String getController_get_log_in() {
        return this.controller_get_log_in;
    }

    public String getController_server_added_to_queue() {
        return this.controller_server_added_to_queue;
    }

    public String getController_proxy_added_to_queue() {
        return this.controller_proxy_added_to_queue;
    }

    public String getClient_shutdown_process() {
        return this.client_shutdown_process;
    }

    public String getClient_copies_template() {
        return this.client_copies_template;
    }

    public String getClient_wait_start() {
        return this.client_wait_start;
    }

    public String getChannel_global_disconnected() {
        return this.channel_global_disconnected;
    }

    public String getScreen_kicked_process_disconnect() {
        return this.screen_kicked_process_disconnect;
    }

    public String getClient_controller_info_reload_success() {
        return this.client_controller_info_reload_success;
    }

    public String getController_socket_bind_success() {
        return this.controller_socket_bind_success;
    }

    public String getDownload_trying() {
        return this.download_trying;
    }

    public String getDownload_success() {
        return this.download_success;
    }

    public String getCommand_addons_no_addons_loaded() {
        return this.command_addons_no_addons_loaded;
    }

    public String getCommand_addons_following_loaded() {
        return this.command_addons_following_loaded;
    }

    public String getCommand_addons_addon_description() {
        return this.command_addons_addon_description;
    }

    public String getServergroup_not_found() {
        return this.servergroup_not_found;
    }

    public String getProxygroup_not_found() {
        return this.proxygroup_not_found;
    }

    public String getClient_not_found() {
        return this.client_not_found;
    }

    public String getClient_not_connected() {
        return this.client_not_connected;
    }

    public String getProcess_not_connected() {
        return this.process_not_connected;
    }

    public String getCommand_assignment_value_updated() {
        return this.command_assignment_value_updated;
    }

    public String getCommand_assignment_value_removed() {
        return this.command_assignment_value_removed;
    }

    public String getCommand_assignment_value_added() {
        return this.command_assignment_value_added;
    }

    public String getCommand_assignment_value_not_updatable() {
        return this.command_assignment_value_not_updatable;
    }

    public String getCommand_copy_try() {
        return this.command_copy_try;
    }

    public String getCommand_copy_backend_not_client() {
        return this.command_copy_backend_not_client;
    }

    public String getSetup_name_of_group() {
        return this.setup_name_of_group;
    }

    public String getSetup_name_of_client() {
        return this.setup_name_of_client;
    }

    public String getSetup_choose_minecraft_version() {
        return this.setup_choose_minecraft_version;
    }

    public String getSetup_choose_spigot_version() {
        return this.setup_choose_spigot_version;
    }

    public String getSetup_choose_reset_type() {
        return this.setup_choose_reset_type;
    }

    public String getSetup_choose_proxy_reset_type() {
        return this.setup_choose_proxy_reset_type;
    }

    public String getSetup_trying_to_create() {
        return this.setup_trying_to_create;
    }

    public String getSetup_choose_proxy_version() {
        return this.setup_choose_proxy_version;
    }

    public String getSetup_name_of_new_client() {
        return this.setup_name_of_new_client;
    }

    public String getSetup_ip_of_new_client() {
        return this.setup_ip_of_new_client;
    }

    public String getSetup_controller_ip() {
        return this.setup_controller_ip;
    }

    public String getSetup_name_of_first_client() {
        return this.setup_name_of_first_client;
    }

    public String getSetup_ram_of_default_group() {
        return this.setup_ram_of_default_group;
    }

    public String getSetup_ram_of_default_proxy_group() {
        return this.setup_ram_of_default_proxy_group;
    }

    public String getSetup_load_default_addons() {
        return this.setup_load_default_addons;
    }

    public String getSetup_default_user_created() {
        return this.setup_default_user_created;
    }

    public String getCommand_error_occurred() {
        return this.command_error_occurred;
    }

    public String getCommand_create_webuser_created() {
        return this.command_create_webuser_created;
    }

    public String getCommand_create_template_created_success() {
        return this.command_create_template_created_success;
    }

    public String getCommand_deploy_trying() {
        return this.command_deploy_trying;
    }

    public String getCommand_developer_debug_enable() {
        return this.command_developer_debug_enable;
    }

    public String getCommand_developer_debug_disable() {
        return this.command_developer_debug_disable;
    }

    public String getCommand_developer_standby_enable() {
        return this.command_developer_standby_enable;
    }

    public String getCommand_developer_standby_disable() {
        return this.command_developer_standby_disable;
    }

    public String getCommand_execute_success() {
        return this.command_execute_success;
    }

    public String getCommand_exit_doing() {
        return this.command_exit_doing;
    }

    public String getCommand_listgroup_list() {
        return this.command_listgroup_list;
    }

    public String getCommand_log_success() {
        return this.command_log_success;
    }

    public String getCommand_process_queue_requested() {
        return this.command_process_queue_requested;
    }

    public String getCommand_process_remove_queue_entry() {
        return this.command_process_remove_queue_entry;
    }

    public String getCommand_process_trying_startup() {
        return this.command_process_trying_startup;
    }

    public String getNo_available_client_for_startup() {
        return this.no_available_client_for_startup;
    }

    public String getCommand_process_try_stop() {
        return this.command_process_try_stop;
    }

    public String getCommand_screen_successfully_left() {
        return this.command_screen_successfully_left;
    }

    public String getCommand_webpermission_remove_success() {
        return this.command_webpermission_remove_success;
    }

    public String getCommand_webpermission_add_success() {
        return this.command_webpermission_add_success;
    }

    public String getCommand_whitelist_success() {
        return this.command_whitelist_success;
    }

    public String getCommand_whitelist_removed() {
        return this.command_whitelist_removed;
    }

    public String getSetup_choose_database() {
        return setup_choose_database;
    }

    public String getSetup_database_host() {
        return setup_database_host;
    }

    public String getSetup_database_port() {
        return setup_database_port;
    }

    public String getSetup_database_username() {
        return setup_database_username;
    }

    public String getSetup_database_password() {
        return setup_database_password;
    }

    public String getSetup_database_name() {
        return setup_database_name;
    }

    public String getSetup_memory_servergroup() {
        return setup_memory_servergroup;
    }

    public String getSetup_memory_proxygroup() {
        return setup_memory_proxygroup;
    }
}
