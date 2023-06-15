package me.zxoir.deeppd.commands;

import me.zxoir.deeppd.DeepPD;
import me.zxoir.deeppd.managers.ConfigManager;
import me.zxoir.deeppd.managers.CriminalManager;
import me.zxoir.deeppd.managers.OfficerManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static me.zxoir.deeppd.utils.Utils.colorize;

public class PoliceCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String @NotNull [] args) {
        boolean isPlayer = sender instanceof Player;

        if (args.length == 1) {

            if (args[0].equalsIgnoreCase("setinventory")) {
                if (!isPlayer) {
                    sender.sendMessage("You must be a player to use this command");
                    return true;
                }

                Player player = (Player) sender;
                if (!(player.hasPermission("deeppd.*") || player.hasPermission("deeppd.setinventory"))) {
                    player.sendMessage(ConfigManager.getInvalidPermissionMessage());
                    return true;
                }

                OfficerManager.setDutyInventory(player.getInventory().getContents());
                OfficerManager.setDutyArmour(player.getInventory().getArmorContents());

                DeepPD.getDataFile().getConfig().set("dutyInventory", player.getInventory().getContents());
                DeepPD.getDataFile().getConfig().set("dutyArmour", player.getInventory().getArmorContents());
                DeepPD.getDataFile().saveConfig();

                player.sendMessage(colorize("&a&lDuty inventory has been saved"));
                return true;
            }

            if (args[0].equalsIgnoreCase("setjail")) {
                if (!isPlayer) {
                    sender.sendMessage("You must be a player to use this command");
                    return true;
                }

                Player player = (Player) sender;
                if (!(player.hasPermission("deeppd.*") || player.hasPermission("deeppd.setjail"))) {
                    player.sendMessage(ConfigManager.getInvalidPermissionMessage());
                    return true;
                }

                OfficerManager.setJail(player.getLocation());

                DeepPD.getDataFile().getConfig().set("jail", player.getLocation());
                DeepPD.getDataFile().saveConfig();

                player.sendMessage(colorize("&a&lJail location has been saved"));
                return true;
            }

            if (args[0].equalsIgnoreCase("setunjail")) {
                if (!isPlayer) {
                    sender.sendMessage("You must be a player to use this command");
                    return true;
                }

                Player player = (Player) sender;
                if (!(player.hasPermission("deeppd.*") || player.hasPermission("deeppd.setunjail"))) {
                    player.sendMessage(ConfigManager.getInvalidPermissionMessage());
                    return true;
                }

                OfficerManager.setUnjail(player.getLocation());

                DeepPD.getDataFile().getConfig().set("unjail", player.getLocation());
                DeepPD.getDataFile().saveConfig();

                player.sendMessage(colorize("&a&lUnjail location has been saved"));
                return true;
            }

        } else if (args.length == 3) {

            if (args[0].equalsIgnoreCase("drug")) {
                if (!isPlayer) {
                    sender.sendMessage("Only players can use this command.");
                    return true;
                }

                Player player = (Player) sender;

                if (!(player.hasPermission("deeppd.*") || player.hasPermission("deeppd.drug"))) {
                    player.sendMessage(ConfigManager.getInvalidPermissionMessage());
                    return true;
                }

                if (args[1].equalsIgnoreCase("add")) {
                    Material material = Material.valueOf(args[2]);

                    if (material == null) {
                        player.sendMessage("That is not a valid material.");
                        return true;
                    }

                    if (CriminalManager.getDrugItems().contains(material)) {
                        player.sendMessage("This item type is already in the drug item list.");
                        return true;
                    }

                    CriminalManager.getDrugItems().add(material);
                    List<String> materialNames = new ArrayList<>();
                    CriminalManager.getDrugItems().forEach(mat -> materialNames.add(mat.name()));
                    DeepPD.getDataFile().getConfig().set("drugItems", materialNames);
                    DeepPD.getDataFile().saveConfig();
                    player.sendMessage(material.name() + " has been added to the drug item list.");
                } else if (args[1].equalsIgnoreCase("remove")) {
                    Material material = Material.valueOf(args[2]);

                    if (material == null) {
                        player.sendMessage("That is not a valid material.");
                        return true;
                    }

                    if (!CriminalManager.getDrugItems().contains(material)) {
                        player.sendMessage("That item type is not in the drug items list.");
                        return true;
                    }

                    CriminalManager.getDrugItems().remove(material);
                    List<String> materialNames = new ArrayList<>();
                    CriminalManager.getDrugItems().forEach(mat -> materialNames.add(mat.name()));
                    DeepPD.getDataFile().getConfig().set("drugItems", materialNames);
                    DeepPD.getDataFile().saveConfig();
                    player.sendMessage(material.name() + " has been removed form the drug items list.");
                } else player.sendMessage("Incorrect usage. /police drug <add/remove> <item_type>");
            }

            if (args[0].equalsIgnoreCase("promote")) {

                if (!(sender.hasPermission("deeppd.*") || sender.hasPermission("deeppd.promote") || !isPlayer)) {
                    sender.sendMessage(ConfigManager.getInvalidPermissionMessage());
                    return true;
                }

                Player player = Bukkit.getPlayer(args[1]);
                if (player == null) {
                    sender.sendMessage(colorize("&c" + args[1] + " is not Online"));
                    return true;
                }

                switch (args[2].toLowerCase()) {
                    case "jr.police", "jrpolice" -> {
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), ConfigManager.getPromoteJrPoliceCommand(sender.getName(), player.getName()));
                        String jrPoliceAnnounce = ConfigManager.getPromoteJrPoliceAnnounce(sender.getName(), player.getName());
                        String jrPolicePoliceChat = ConfigManager.getPromoteJrPolicePoliceChat(sender.getName(), player.getName());
                        if (!jrPoliceAnnounce.isEmpty())
                            PoliceSayCommand.PoliceSay(jrPoliceAnnounce);
                        if (!jrPolicePoliceChat.isEmpty())
                            PoliceChatCommand.PoliceChat(jrPolicePoliceChat, sender.getName());
                    }
                    case "sr.police", "srpolice" -> {
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), ConfigManager.getPromoteSrPoliceCommand(sender.getName(), player.getName()));
                        String srPoliceAnnounce = ConfigManager.getPromoteSrPoliceAnnounce(sender.getName(), player.getName());
                        String srPolicePoliceChat = ConfigManager.getPromoteSrPolicePoliceChat(sender.getName(), player.getName());
                        if (!srPoliceAnnounce.isEmpty())
                            PoliceSayCommand.PoliceSay(srPoliceAnnounce);
                        if (!srPolicePoliceChat.isEmpty())
                            PoliceChatCommand.PoliceChat(srPolicePoliceChat, sender.getName());
                    }
                    case "chief" -> {
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), ConfigManager.getPromoteChiefCommand(sender.getName(), player.getName()));
                        String chiefAnnounce = ConfigManager.getPromoteChiefAnnounce(sender.getName(), player.getName());
                        String chiefPoliceChat = ConfigManager.getPromoteChiefPoliceChat(sender.getName(), player.getName());
                        if (!chiefAnnounce.isEmpty())
                            PoliceSayCommand.PoliceSay(chiefAnnounce);
                        if (!chiefPoliceChat.isEmpty())
                            PoliceChatCommand.PoliceChat(chiefPoliceChat, sender.getName());
                    }
                    default ->
                            sender.sendMessage(colorize("&cYou must only choose between these 3 ranks: Jr.Police, Sr.Police, and Chief"));
                }
                sender.sendMessage(colorize("&a" + player.getName() + " has been promoted to &l" + args[2]));
                return true;
            }

            if (args[0].equalsIgnoreCase("demote")) {

                if (!(sender.hasPermission("deeppd.*") || sender.hasPermission("deeppd.demote") || !isPlayer)) {
                    sender.sendMessage(ConfigManager.getInvalidPermissionMessage());
                    return true;
                }

                Player player = Bukkit.getPlayer(args[1]);
                if (player == null) {
                    sender.sendMessage(colorize("&c" + args[1] + " is not Online"));
                    return true;
                }

                switch (args[2].toLowerCase()) {
                    case "citizen" -> {
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), ConfigManager.getDemoteCitizenCommand(sender.getName(), player.getName()));
                        String citizenAnnounce = ConfigManager.getDemoteCitizenAnnounce(sender.getName(), player.getName());
                        String citizenPoliceChat = ConfigManager.getDemoteCitizenPoliceChat(sender.getName(), player.getName());
                        if (!citizenAnnounce.isEmpty())
                            PoliceSayCommand.PoliceSay(citizenAnnounce);
                        if (!citizenPoliceChat.isEmpty())
                            PoliceChatCommand.PoliceChat(citizenPoliceChat, sender.getName());
                    }
                    case "jr.police", "jrpolice" -> {
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), ConfigManager.getDemoteJrPoliceCommand(sender.getName(), player.getName()));
                        String jrPoliceAnnounce = ConfigManager.getDemoteJrPoliceAnnounce(sender.getName(), player.getName());
                        String jrPolicePoliceChat = ConfigManager.getDemoteJrPolicePoliceChat(sender.getName(), player.getName());
                        if (!jrPoliceAnnounce.isEmpty())
                            PoliceSayCommand.PoliceSay(jrPoliceAnnounce);
                        if (!jrPolicePoliceChat.isEmpty())
                            PoliceChatCommand.PoliceChat(jrPolicePoliceChat, sender.getName());
                    }
                    case "sr.police", "srpolice" -> {
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), ConfigManager.getDemoteSrPoliceCommand(sender.getName(), player.getName()));
                        String srPoliceAnnounce = ConfigManager.getDemoteSrPoliceAnnounce(sender.getName(), player.getName());
                        String srPolicePoliceChat = ConfigManager.getDemoteSrPolicePoliceChat(sender.getName(), player.getName());
                        if (!srPoliceAnnounce.isEmpty())
                            PoliceSayCommand.PoliceSay(srPoliceAnnounce);
                        if (!srPolicePoliceChat.isEmpty())
                            PoliceChatCommand.PoliceChat(srPolicePoliceChat, sender.getName());
                    }
                    default ->
                            sender.sendMessage(colorize("&cYou must only choose between these 3 ranks: Citizen, Jr.Police, and Sr.Police"));
                }
                sender.sendMessage(colorize("&a" + player.getName() + " has been demoted to &l" + args[2]));
                return true;
            }

        } else
            sender.sendMessage("Here is a list of commands:\n/police setinventory\n/police setjail\n/police setunjail\n/police promote <player> <rank>\n/police demote <player> <rank>");

        return true;
    }

}
