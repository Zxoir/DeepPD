package me.zxoir.deeppd.commands;

import me.zxoir.deeppd.managers.ConfigManager;
import me.zxoir.deeppd.managers.CriminalManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class DeepPDCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (sender instanceof ConsoleCommandSender)
            return true;

        Player player = (Player) sender;

        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("reload")) {
                ConfigManager.reloadConfig();
                player.sendMessage("DeepPD has been reloaded");
                return true;
            }
        }

        player.sendMessage("/deeppd reload");
        player.sendMessage(CriminalManager.getCachedCriminals().getIfPresent(player.getUniqueId()).getDefaultWantedDuration() + "");
        player.sendMessage(CriminalManager.getCachedCriminals().getIfPresent(player.getUniqueId()).getDefaultJailDuration() + "");

        return true;
    }

}
