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

public class GetBatonCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {

        if (sender instanceof ConsoleCommandSender) {
            sender.sendMessage("You must be a player to use this command");
            return true;
        }

        Player player = (Player) sender;
        if (!(player.hasPermission("deeppd.*") || player.hasPermission("deeppd.getbaton"))) {
            player.sendMessage(ConfigManager.getInvalidPermissionMessage());
            return true;
        }

        if (player.getInventory().addItem(OfficerManager.POLICE_BATON).isEmpty())
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(colorize("&aPolice Baton has been added to your inventory")));
        else
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(colorize("&cYour inventory is full")));

        return true;
    }

}
