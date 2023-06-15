package me.zxoir.deeppd.commands;

import me.zxoir.deeppd.customclasses.Criminal;
import me.zxoir.deeppd.managers.ConfigManager;
import me.zxoir.deeppd.managers.CriminalManager;
import me.zxoir.deeppd.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class WantedCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String @NotNull [] args) {
        boolean isPlayer = sender instanceof Player;

        if (!(sender.hasPermission("deeppd.*") || sender.hasPermission("deeppd.duty") || !isPlayer)) {
            sender.sendMessage(ConfigManager.getInvalidPermissionMessage());
            return true;
        }

        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("list")) {
                if (CriminalManager.getCachedCriminals().asMap().isEmpty()) {
                    sender.sendMessage("No wanted players found.");
                    return true;
                }

                List<Criminal> criminalList = new ArrayList<>(CriminalManager.getCachedCriminals().asMap().values());

                criminalList.sort(Comparator.comparingInt(Criminal::getLevel).reversed());

                StringBuilder message = new StringBuilder();
                message.append("Wanted Players:\n");

                boolean empty = true;
                for (Criminal criminal : criminalList) {
                    if (criminal.getLevel() >= 1) {
                        String playerName = Bukkit.getOfflinePlayer(criminal.getUuid()).getName();
                        message.append(playerName).append(" - Level ").append(criminal.getLevel()).append("\n");
                        empty = false;
                    }
                }

                if (empty)
                    sender.sendMessage("No wanted players found.");
                else
                    sender.sendMessage(message.toString());
            }
        } else if (args.length == 3) {

            if (args[0].equalsIgnoreCase("set")) {
                Player player = Bukkit.getPlayer(args[1]);

                if (player == null) {
                    sender.sendMessage("This player cant be found");
                    return true;
                }

                Criminal criminal = CriminalManager.getCachedCriminals().getIfPresent(player.getUniqueId());
                if (criminal == null) {
                    sender.sendMessage("This player cant be found.");
                    return true;
                }

                if (!Utils.isInteger(args[2])) {
                    sender.sendMessage("Level must be a number");
                    return true;
                }

                int level = Integer.parseInt(args[2]);

                if (level > ConfigManager.getMaximumLevel()) {
                    sender.sendMessage("That level is higher than the maximum wanted level!");
                    return true;
                }

                criminal.setLevel(level);
                sender.sendMessage(player.getName() + " is now on Wanted Level " + level);
            }

        } else
            sender.sendMessage("List of Wanted Commands:\n/Wanted set <Player> <Level>\n/Wanted list");

        return true;
    }
}
