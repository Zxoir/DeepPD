package me.zxoir.deeppd.commands;

import me.zxoir.deeppd.managers.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.jetbrains.annotations.NotNull;

import static me.zxoir.deeppd.utils.Utils.colorize;

public class PoliceSayCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (!(sender instanceof ConsoleCommandSender || sender.hasPermission("deeppd.*") || sender.hasPermission("deeppd.policesay"))) {
            sender.sendMessage(ConfigManager.getInvalidPermissionMessage());
            return true;
        }

        StringBuilder message = new StringBuilder();
        for (String arg : args) {
            message.append(arg);
            message.append(" ");
        }

        PoliceSay(message.toString());
        return true;
    }

    public static void PoliceSay(String message) {
        Bukkit.broadcastMessage(ConfigManager.getPoliceSayPrefix() + colorize(message));
    }
}
