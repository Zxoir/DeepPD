package me.zxoir.deeppd.commands;

import me.zxoir.deeppd.managers.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.jetbrains.annotations.NotNull;

import static me.zxoir.deeppd.utils.Utils.colorize;

public class PoliceChatCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String @NotNull [] args) {

        if (!(sender instanceof ConsoleCommandSender || sender.hasPermission("deeppd.*") || sender.hasPermission("deeppd.pc"))) {
            sender.sendMessage(ConfigManager.getInvalidPermissionMessage());
            return true;
        }

        StringBuilder message = new StringBuilder();
        for (String arg : args) {
            message.append(arg);
            message.append(" ");
        }

        PoliceChat(message.toString(), sender.getName());
        return true;
    }

    public static void PoliceChat(String message, String name) {
        Bukkit.getOnlinePlayers().stream().filter(player -> player.hasPermission("deeppd.*") || player.hasPermission("deeppd.pc")).forEach(player -> player.sendMessage(ConfigManager.getPoliceChatPrefix(name) + colorize(message)));
    }
}