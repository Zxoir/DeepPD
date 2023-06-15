package me.zxoir.deeppd.commands;

import me.zxoir.deeppd.customclasses.Criminal;
import me.zxoir.deeppd.managers.ConfigManager;
import me.zxoir.deeppd.managers.CriminalManager;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ReleaseCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        boolean isPlayer = sender instanceof Player;

        if (!(sender.hasPermission("deeppd.*") || sender.hasPermission("deeppd.release") || !isPlayer)) {
            sender.sendMessage(ConfigManager.getInvalidPermissionMessage());
            return true;
        }

        if (args.length != 1) {
            sender.sendMessage("Incorrect usage. /release <Player>");
            return true;
        }

        OfflinePlayer player = Bukkit.getOfflinePlayer(args[0]);
        Criminal criminal = CriminalManager.getCachedCriminals().getIfPresent(player.getUniqueId());

        if (criminal == null) {
            sender.sendMessage("This player can't be found.");
            return true;
        }

        if (!criminal.isJailed()) {
            sender.sendMessage("This player is not jailed.");
            return true;
        }

        if (criminal.getJailTask() != null)
            criminal.getJailTask().cancel();

        CriminalManager.ReleasePlayer(player);
        sender.sendMessage(player.getName() + " has been released from Jail.");
        return true;
    }
}
