package me.zxoir.deeppd.commands;

import me.zxoir.deeppd.managers.ConfigManager;
import me.zxoir.deeppd.managers.OfficerManager;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static me.zxoir.deeppd.utils.Utils.colorize;

public class DutyCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {

        if (sender instanceof ConsoleCommandSender) {
            sender.sendMessage("You must be a player to use this command");
            return true;
        }

        Player player = (Player) sender;
        if (!(player.hasPermission("deeppd.*") || player.hasPermission("deeppd.duty"))) {
            player.sendMessage(ConfigManager.getInvalidPermissionMessage());
            return true;
        }

        if (OfficerManager.getCachedOfficers().asMap().containsKey(player.getUniqueId())) {
            OfficerManager.endDuty(player.getUniqueId());
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(colorize("&cYou are no longer on Duty")));
            return true;
        }

        OfficerManager.beginDuty(player.getUniqueId());
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(colorize("&aYou have started your Duty")));
        return true;
    }
}
