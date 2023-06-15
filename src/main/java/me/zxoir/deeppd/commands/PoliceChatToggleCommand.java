package me.zxoir.deeppd.commands;

import lombok.Getter;
import me.zxoir.deeppd.managers.ConfigManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

import static me.zxoir.deeppd.utils.Utils.colorize;

public class PoliceChatToggleCommand implements CommandExecutor {
    @Getter
    private static final CopyOnWriteArrayList<UUID> policeChatToggle = new CopyOnWriteArrayList<>();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (sender instanceof ConsoleCommandSender) {
            sender.sendMessage("Only players can use this command");
            return true;
        }
        Player player = (Player) sender;

        if (!(player.hasPermission("deeppd.*") || player.hasPermission("deeppd.pc.toggle"))) {
            player.sendMessage(ConfigManager.getInvalidPermissionMessage());
            return true;
        }

        if (policeChatToggle.contains(player.getUniqueId())) {
            policeChatToggle.remove(player.getUniqueId());
            player.sendMessage(colorize("&cPolice chat toggled off"));
            return true;
        }

        policeChatToggle.add(player.getUniqueId());
        player.sendMessage(colorize("&aPolice chat has been toggled on"));
        return true;
    }
}
