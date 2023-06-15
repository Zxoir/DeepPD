package me.zxoir.deeppd.commands;

import me.zxoir.deeppd.customclasses.Criminal;
import me.zxoir.deeppd.customclasses.Incident;
import me.zxoir.deeppd.managers.ConfigManager;
import me.zxoir.deeppd.managers.CriminalManager;
import me.zxoir.deeppd.utils.TimeManager;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;

public class IncidentsCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String @NotNull [] args) {
        boolean isPlayer = sender instanceof Player;

        if (!(sender.hasPermission("deeppd.*") || sender.hasPermission("deeppd.release") || !isPlayer)) {
            sender.sendMessage(ConfigManager.getInvalidPermissionMessage());
            return true;
        }

        if (args.length == 0) {
            sender.sendMessage("Usage: /incidents <player>");
            return true;
        }

        OfflinePlayer player = Bukkit.getPlayer(args[0]);
        Criminal criminal = CriminalManager.getCachedCriminals().getIfPresent(player.getUniqueId());
        if (criminal == null) {
            sender.sendMessage("Player not found.");
            return true;
        }

        if (criminal.isJailed())
            sender.sendMessage("This criminal is jailed for " + TimeManager.formatTime(criminal.getJailDuration().toEpochMilli()));

        if (criminal.getLevel() > 0)
            sender.sendMessage("This criminal is wanted for " + TimeManager.formatTime(criminal.getWantedDuration().toEpochMilli()));

        if (criminal.getIncidents().isEmpty()) {
            sender.sendMessage("This player doesn't have any incidents yet.");
            return true;
        }

        StringBuilder message = new StringBuilder();
        message.append("Incidents for player ").append(player.getName()).append(":\n");

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (Incident incident : criminal.getIncidents()) {
            message.append(dateFormat.format(incident.getDate())).append(" - Killed ").append(Bukkit.getOfflinePlayer(incident.getUuid()).getName()).append("\n");
        }

        sender.sendMessage(message.toString());
        return true;
    }
}

