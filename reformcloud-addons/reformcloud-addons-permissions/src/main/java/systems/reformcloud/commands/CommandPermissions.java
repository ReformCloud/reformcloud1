/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.commands;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import systems.reformcloud.PermissionsAddon;
import systems.reformcloud.ReformCloudController;
import systems.reformcloud.ReformCloudLibraryService;
import systems.reformcloud.commands.utility.Command;
import systems.reformcloud.commands.utility.CommandSender;
import systems.reformcloud.player.permissions.group.PermissionGroup;
import systems.reformcloud.player.permissions.player.PermissionHolder;

/**
 * @author _Klaro | Pasqual K. / created on 10.03.2019
 */

public final class CommandPermissions extends Command implements Serializable {
    public CommandPermissions() {
        super("permissions", "Manage the permissions", "reformcloud.commands.permissions", new String[]{"perms"});
    }

    @Override
    public void executeCommand(CommandSender commandSender, String[] args) {
        if (args.length == 1 && args[0].equalsIgnoreCase("list")) {
            List<PermissionGroup> registered = PermissionsAddon.getInstance().getPermissionDatabase()
                    .getAllGroups();
            registered.add(PermissionsAddon.getInstance().getPermissionDatabase().getPermissionCache().getDefaultGroup());
            registered.sort((os, as) -> {
                int id1 = os.getGroupID();
                int id2 = as.getGroupID();
                return Integer.compare(id1, id2);
            });
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("The following groups are registered").append(": ");
            registered.forEach(permissionGroup -> stringBuilder
                    .append("\n")
                    .append("   - ")
                    .append(permissionGroup.getName())
                    .append("/ID=")
                    .append(permissionGroup.getGroupID())
                    .append("/Default=")
                    .append(PermissionsAddon.getInstance().getPermissionDatabase().getPermissionCache().getDefaultGroup().getName().equals(permissionGroup.getName()))
            );
            commandSender.sendMessage(stringBuilder.substring(0));
        } else if (args.length == 2 && args[1].equalsIgnoreCase("create")) {
            if (PermissionsAddon.getInstance().getPermissionDatabase()
                    .getAllGroups().stream().filter(e -> e.getName().equals(args[0]))
                    .findFirst().orElse(null) != null) {
                commandSender.sendMessage("PermissionGroup already exists");
                return;
            }

            PermissionGroup permissionGroup = new PermissionGroup(args[0], "", "", "", "7", 1, new HashMap<>());
            PermissionsAddon.getInstance().getPermissionDatabase().createPermissionGroup(permissionGroup);
            PermissionsAddon.getInstance().getPermissionDatabase().update();

            commandSender.sendMessage("PermissionGroup " + args[0] + " was created successfully");
        } else if (args.length == 2 && args[1].equalsIgnoreCase("delete")) {
            PermissionGroup permissionGroup = PermissionsAddon.getInstance().getPermissionDatabase()
                    .getAllGroups().stream().filter(e -> e.getName().equals(args[0]))
                    .findFirst().orElse(null);
            if (permissionGroup == null) {
                commandSender.sendMessage("The PermissionGroup doesn't exists");
                return;
            }

            if (PermissionsAddon.getInstance().getPermissionDatabase().getPermissionCache().getDefaultGroup()
                    .getName().equals(permissionGroup.getName())) {
                PermissionsAddon.getInstance().getPermissionDatabase().getPermissionCache().setDefaultGroup(
                        new PermissionGroup("default" + ReformCloudLibraryService.THREAD_LOCAL_RANDOM.nextLong()
                                , "", "", "", "7", 1, new HashMap<>())
                );
            }

            PermissionsAddon.getInstance().getPermissionDatabase().deletePermissionGroup(permissionGroup);
            PermissionsAddon.getInstance().getPermissionDatabase().update();

            commandSender.sendMessage("The PermissionGroup was deleted successfully");
        } else if (args.length == 2 && args[1].equalsIgnoreCase("list")) {
            UUID uuid = ReformCloudController.getInstance().getPlayerDatabase().getFromName(args[0]);
            if (uuid == null) {
                commandSender.sendMessage("Could not found uuid of player in database");
                return;
            }

            PermissionHolder permissionHolder = PermissionsAddon.getInstance().getPermissionDatabase().getPermissionHolder(uuid);
            if (permissionHolder == null) {
                commandSender.sendMessage("Could not find PermissionHolder");
                return;
            }

            commandSender.sendMessage("  - " + args[0] + "/Groups=" + permissionHolder.getPermissionGroups() +
                    "/Permissions=" + permissionHolder.getPlayerPermissions());
        } else if (args.length == 3 && args[1].equalsIgnoreCase("addperm")) {
            UUID uuid = ReformCloudController.getInstance().getPlayerDatabase().getFromName(args[0]);
            if (uuid == null) {
                commandSender.sendMessage("Could not found uuid of player in database");
                return;
            }

            PermissionHolder permissionHolder = PermissionsAddon.getInstance().getPermissionDatabase().getPermissionHolder(uuid);
            if (permissionHolder == null) {
                commandSender.sendMessage("Could not find PermissionHolder");
                return;
            }

            permissionHolder.getPlayerPermissions().put(args[2].replace("-", ""), !args[2].startsWith("-"));
            PermissionsAddon.getInstance().getPermissionDatabase().updatePermissionHolder(permissionHolder);

            commandSender.sendMessage("The permission " + args[2] + " was added to the user " + args[0]);
        } else if (args.length == 3 && args[1].equalsIgnoreCase("removeperm")) {
            UUID uuid = ReformCloudController.getInstance().getPlayerDatabase().getFromName(args[0]);
            if (uuid == null) {
                commandSender.sendMessage("Could not found uuid of player in database");
                return;
            }

            PermissionHolder permissionHolder = PermissionsAddon.getInstance().getPermissionDatabase().getPermissionHolder(uuid);
            if (permissionHolder == null) {
                commandSender.sendMessage("Could not find PermissionHolder");
                return;
            }

            permissionHolder.getPlayerPermissions().remove(args[2]);
            PermissionsAddon.getInstance().getPermissionDatabase().updatePermissionHolder(permissionHolder);

            commandSender.sendMessage("The permission " + args[2] + " was removed from the user " + args[0]);
        } else if (args.length == 3 && args[1].equalsIgnoreCase("add")) {
            PermissionGroup permissionGroup = PermissionsAddon.getInstance().getPermissionDatabase()
                    .getAllGroups().stream().filter(e -> e.getName().equals(args[0]))
                    .findFirst().orElse(null);
            if (permissionGroup == null) {
                commandSender.sendMessage("Could not find PermissionGroup " + args[0]);
                return;
            }

            permissionGroup.getPermissions().put(args[2].replace("-", ""), !args[2].startsWith("-"));
            PermissionsAddon.getInstance().getPermissionDatabase().updatePermissionGroup(permissionGroup);
            PermissionsAddon.getInstance().getPermissionDatabase().update();

            commandSender.sendMessage("You added the permission " + args[2] + " to the group " + permissionGroup.getName());
        } else if (args.length == 3 && args[1].equalsIgnoreCase("remove")) {
            PermissionGroup permissionGroup = PermissionsAddon.getInstance().getPermissionDatabase()
                    .getAllGroups().stream().filter(e -> e.getName().equals(args[0]))
                    .findFirst().orElse(null);
            if (permissionGroup == null) {
                commandSender.sendMessage("Could not find PermissionGroup " + args[0]);
                return;
            }

            permissionGroup.getPermissions().remove(args[2]);
            PermissionsAddon.getInstance().getPermissionDatabase().updatePermissionGroup(permissionGroup);
            PermissionsAddon.getInstance().getPermissionDatabase().update();

            commandSender.sendMessage("You removed the permission " + args[2] + " from the group " + permissionGroup.getName());
        } else if (args.length == 3 && args[1].equalsIgnoreCase("addgroup")) {
            UUID uuid = ReformCloudController.getInstance().getPlayerDatabase().getFromName(args[0]);
            if (uuid == null) {
                commandSender.sendMessage("Could not found uuid of player in database");
                return;
            }

            PermissionHolder permissionHolder = PermissionsAddon.getInstance().getPermissionDatabase().getPermissionHolder(uuid);
            if (permissionHolder == null) {
                commandSender.sendMessage("Could not find PermissionHolder");
                return;
            }

            PermissionGroup permissionGroup = PermissionsAddon.getInstance().getPermissionDatabase()
                    .getAllGroups().stream().filter(e -> e.getName().equals(args[2]))
                    .findFirst().orElse(null);
            if (permissionGroup == null) {
                commandSender.sendMessage("Could not find PermissionGroup " + args[2]);
                return;
            }

            if (permissionHolder.getPermissionGroups().containsKey(permissionGroup.getName())) {
                commandSender.sendMessage("The Player is already in this group");
                return;
            }

            permissionHolder.getPermissionGroups().put(permissionGroup.getName(), -1L);
            PermissionsAddon.getInstance().getPermissionDatabase().updatePermissionHolder(permissionHolder);

            commandSender.sendMessage("The User " + args[0] + " is now in group " + permissionGroup.getName());
        } else if (args.length == 3 && args[1].equalsIgnoreCase("removegroup")) {
            UUID uuid = ReformCloudController.getInstance().getPlayerDatabase().getFromName(args[0]);
            if (uuid == null) {
                commandSender.sendMessage("Could not found uuid of player in database");
                return;
            }

            PermissionHolder permissionHolder = PermissionsAddon.getInstance().getPermissionDatabase().getPermissionHolder(uuid);
            if (permissionHolder == null) {
                commandSender.sendMessage("Could not find PermissionHolder");
                return;
            }

            PermissionGroup permissionGroup = PermissionsAddon.getInstance().getPermissionDatabase()
                    .getAllGroups().stream().filter(e -> e.getName().equals(args[2]))
                    .findFirst().orElse(null);
            if (permissionGroup == null) {
                commandSender.sendMessage("Could not find PermissionGroup " + args[2]);
                return;
            }

            permissionHolder.getPermissionGroups().remove(permissionGroup.getName());
            PermissionsAddon.getInstance().getPermissionDatabase().updatePermissionHolder(permissionHolder);

            commandSender.sendMessage("The User " + args[0] + " is not longer in group " + permissionGroup.getName());
        } else if (args.length == 3 && args[1].equalsIgnoreCase("setgroup")) {
            UUID uuid = ReformCloudController.getInstance().getPlayerDatabase().getFromName(args[0]);
            if (uuid == null) {
                commandSender.sendMessage("Could not found uuid of player in database");
                return;
            }

            PermissionHolder permissionHolder = PermissionsAddon.getInstance().getPermissionDatabase().getPermissionHolder(uuid);
            if (permissionHolder == null) {
                commandSender.sendMessage("Could not find PermissionHolder");
                return;
            }

            PermissionGroup permissionGroup = PermissionsAddon.getInstance().getPermissionDatabase()
                    .getAllGroups().stream().filter(e -> e.getName().equals(args[2]))
                    .findFirst().orElse(null);
            if (permissionGroup == null) {
                commandSender.sendMessage("Could not find PermissionGroup " + args[2]);
                return;
            }

            permissionHolder.getPermissionGroups().clear();
            permissionHolder.getPermissionGroups().put(permissionGroup.getName(), -1L);
            PermissionsAddon.getInstance().getPermissionDatabase().updatePermissionHolder(permissionHolder);

            commandSender.sendMessage("Set " + args[0] + "'s group to " + permissionGroup.getName());
        } else if (args.length == 2 && args[1].equalsIgnoreCase("setdefault")) {
            PermissionGroup permissionGroup = PermissionsAddon.getInstance().getPermissionDatabase()
                    .getAllGroups().stream().filter(e -> e.getName().equals(args[0]))
                    .findFirst().orElse(null);
            if (permissionGroup == null) {
                commandSender.sendMessage("Could not find PermissionGroup " + args[0]);
                return;
            }

            if (PermissionsAddon.getInstance().getPermissionDatabase().getPermissionCache().getDefaultGroup()
                    .getName().equals(args[0])) {
                commandSender.sendMessage("The Group is already the default group");
                return;
            }

            PermissionsAddon.getInstance().getPermissionDatabase().getPermissionCache().getAllRegisteredGroups().remove(permissionGroup);
            PermissionsAddon.getInstance().getPermissionDatabase().getPermissionCache().setDefaultGroup(permissionGroup);
            PermissionsAddon.getInstance().getPermissionDatabase().update();

            commandSender.sendMessage("Successfully set the default group to " + permissionGroup.getName());
        } else if (args.length == 4 && args[1].equalsIgnoreCase("addgroup")) {
            if (this.isNoLong(args[3])) {
                commandSender.sendMessage("The given time is not valid");
                return;
            }

            UUID uuid = ReformCloudController.getInstance().getPlayerDatabase().getFromName(args[0]);
            if (uuid == null) {
                commandSender.sendMessage("Could not found uuid of player in database");
                return;
            }

            PermissionHolder permissionHolder = PermissionsAddon.getInstance().getPermissionDatabase().getPermissionHolder(uuid);
            if (permissionHolder == null) {
                commandSender.sendMessage("Could not find PermissionHolder");
                return;
            }

            PermissionGroup permissionGroup = PermissionsAddon.getInstance().getPermissionDatabase()
                    .getAllGroups().stream().filter(e -> e.getName().equals(args[2]))
                    .findFirst().orElse(null);
            if (permissionGroup == null) {
                commandSender.sendMessage("Could not find PermissionGroup " + args[2]);
                return;
            }

            long timeout = System.currentTimeMillis() + TimeUnit.DAYS.toMillis(Long.parseLong(args[3]));

            permissionHolder.getPermissionGroups().put(permissionGroup.getName(), timeout);
            PermissionsAddon.getInstance().getPermissionDatabase().updatePermissionHolder(permissionHolder);

            commandSender.sendMessage("The User " + args[0] + " is now in group " + permissionGroup.getName());
        } else if (args.length == 4 && args[1].equalsIgnoreCase("setgroup")) {
            if (this.isNoLong(args[3])) {
                commandSender.sendMessage("The given time is not valid");
                return;
            }

            UUID uuid = ReformCloudController.getInstance().getPlayerDatabase().getFromName(args[0]);
            if (uuid == null) {
                commandSender.sendMessage("Could not found uuid of player in database");
                return;
            }

            PermissionHolder permissionHolder = PermissionsAddon.getInstance().getPermissionDatabase().getPermissionHolder(uuid);
            if (permissionHolder == null) {
                commandSender.sendMessage("Could not find PermissionHolder");
                return;
            }

            PermissionGroup permissionGroup = PermissionsAddon.getInstance().getPermissionDatabase()
                    .getAllGroups().stream().filter(e -> e.getName().equals(args[2]))
                    .findFirst().orElse(null);
            if (permissionGroup == null) {
                commandSender.sendMessage("Could not find PermissionGroup " + args[2]);
                return;
            }

            long timeout = System.currentTimeMillis() + TimeUnit.DAYS.toMillis(Long.parseLong(args[3]));

            permissionHolder.getPermissionGroups().clear();
            permissionHolder.getPermissionGroups().put(permissionGroup.getName(), timeout);
            PermissionsAddon.getInstance().getPermissionDatabase().updatePermissionHolder(permissionHolder);

            commandSender.sendMessage("Set " + args[0] + "'s group to " + permissionGroup.getName());
        } else if (args.length == 3 && args[1].equalsIgnoreCase("setprefix")) {
            PermissionGroup permissionGroup = PermissionsAddon.getInstance().getPermissionDatabase()
                    .getAllGroups().stream().filter(e -> e.getName().equals(args[0]))
                    .findFirst().orElse(null);
            if (permissionGroup == null) {
                commandSender.sendMessage("Could not find PermissionGroup " + args[0]);
                return;
            }

            if (permissionGroup.getPrefix().equals(args[2])) {
                commandSender.sendMessage("The Group prefix equals the new prefix");
                return;
            }

            permissionGroup.setPrefix(args[2].replace("_", " "));
            PermissionsAddon.getInstance().getPermissionDatabase().updatePermissionGroup(permissionGroup);
            PermissionsAddon.getInstance().getPermissionDatabase().update();

            commandSender.sendMessage("Successfully set the prefix to " + permissionGroup.getPrefix());
        } else if (args.length == 3 && args[1].equalsIgnoreCase("setdisplay")) {
            PermissionGroup permissionGroup = PermissionsAddon.getInstance().getPermissionDatabase()
                    .getAllGroups().stream().filter(e -> e.getName().equals(args[0]))
                    .findFirst().orElse(null);
            if (permissionGroup == null) {
                commandSender.sendMessage("Could not find PermissionGroup " + args[0]);
                return;
            }

            if (permissionGroup.getDisplay().equals(args[2])) {
                commandSender.sendMessage("The Group display equals the new display");
                return;
            }

            permissionGroup.setDisplay(args[2].replace("_", " "));
            PermissionsAddon.getInstance().getPermissionDatabase().updatePermissionGroup(permissionGroup);
            commandSender.sendMessage("Successfully set the display to " + permissionGroup.getDisplay());
        } else if (args.length == 3 && args[1].equalsIgnoreCase("setsuffix")) {
            PermissionGroup permissionGroup = PermissionsAddon.getInstance().getPermissionDatabase()
                    .getAllGroups().stream().filter(e -> e.getName().equals(args[0]))
                    .findFirst().orElse(null);
            if (permissionGroup == null) {
                commandSender.sendMessage("Could not find PermissionGroup " + args[0]);
                return;
            }

            if (permissionGroup.getSuffix().equals(args[2].replace("_", " "))) {
                commandSender.sendMessage("The Group suffix equals the new suffix");
                return;
            }

            permissionGroup.setSuffix(args[2]);
            PermissionsAddon.getInstance().getPermissionDatabase().updatePermissionGroup(permissionGroup);
            commandSender.sendMessage("Successfully set the suffix to " + permissionGroup.getSuffix());
        } else if (args.length == 3 && args[1].equalsIgnoreCase("settabcolorcode")) {
            PermissionGroup permissionGroup = PermissionsAddon.getInstance().getPermissionDatabase()
                    .getAllGroups().stream().filter(e -> e.getName().equals(args[0]))
                    .findFirst().orElse(null);
            if (permissionGroup == null) {
                commandSender.sendMessage("Could not find PermissionGroup " + args[0]);
                return;
            }

            if (permissionGroup.getTabColorCode().equals(args[2])) {
                commandSender.sendMessage("The Group color-code equals the new color-code");
                return;
            }

            permissionGroup.setTabColorCode(args[2]);
            PermissionsAddon.getInstance().getPermissionDatabase().updatePermissionGroup(permissionGroup);
            commandSender.sendMessage("Successfully set the display to " + permissionGroup.getTabColorCode());
        } else if (args.length == 3 && args[1].equalsIgnoreCase("setgroupid")) {
            PermissionGroup permissionGroup = PermissionsAddon.getInstance().getPermissionDatabase()
                    .getAllGroups().stream().filter(e -> e.getName().equals(args[0]))
                    .findFirst().orElse(null);
            if (permissionGroup == null) {
                commandSender.sendMessage("Could not find PermissionGroup " + args[0]);
                return;
            }

            if (!ReformCloudLibraryService.checkIsInteger(args[2])) {
                commandSender.sendMessage("Please provide an integer as argument");
                return;
            }

            int groupID = Integer.parseInt(args[2]);
            if (permissionGroup.getGroupID() == groupID) {
                commandSender.sendMessage("The Group id equals the new id");
                return;
            }

            permissionGroup.setGroupID(groupID);
            PermissionsAddon.getInstance().getPermissionDatabase().updatePermissionGroup(permissionGroup);
            commandSender.sendMessage("Successfully set the group-id to " + groupID);
        } else {
            commandSender.sendMessage("perms list");
            commandSender.sendMessage("perms <USERNAME> list");
            commandSender.sendMessage("perms <USERNAME> <ADDPERM/REMOVEPERM> <PERMISSION>");
            commandSender.sendMessage("perms <USERNAME> <ADDGROUP/REMOVEGROUP/SETGROUP> <GROUPNAME>");
            commandSender.sendMessage("perms <USERNAME> <ADDGROUP/SETGROUP> <GROUPNAME> <TIMEOUTINDAYS>");
            commandSender.sendMessage("perms <GROUPNAME> setdefault");
            commandSender.sendMessage("perms <GROUPNAME> <CREATE/DELETE>");
            commandSender.sendMessage("perms <GROUPNAME> <ADD/REMOVE> <PERMISSION>");
            commandSender.sendMessage("perms <GROUPNAME> <SETPREFIX/SETSUFFIX/SETDISPLAY/SETTABCOLORCODE/SETGROUPID> <VALUE>");
        }
    }

    private boolean isNoLong(String in) {
        try {
            Long.parseLong(in);
            return false;
        } catch (final Throwable throwable) {
            return true;
        }
    }
}
