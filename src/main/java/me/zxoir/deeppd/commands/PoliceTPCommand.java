package me.zxoir.deeppd.commands;

import me.zxoir.deeppd.managers.ConfigManager;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class PoliceTPCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("This command can only be executed by a player.");
            return true;
        }

        if (args.length != 1) {
            sender.sendMessage("Usage: /ptp <target>");
            return true;
        }

        Player target = player.getServer().getPlayer(args[0]);

        if (target == null) {
            sender.sendMessage("The specified target player is not online.");
            return true;
        }

        Location targetLocation = target.getLocation();
        Location safeLocation = findSafeLocation(targetLocation);

        if (safeLocation == null) {
            player.sendMessage("Unable to find a safe location near the target player.");
            return true;
        }

        player.sendMessage("You are being tracked by " + player.getName() + "!");
        player.teleport(safeLocation);

        return true;
    }

    private @Nullable Location findSafeLocation(Location targetLocation) {
        Random random = new Random();
        int distance = ConfigManager.getPoliceTPMinDistance() + random.nextInt(ConfigManager.getPoliceTPMaxDistance() - ConfigManager.getPoliceTPMinDistance() + 1);
        int max = 50;

        while (max > 0) {
            double angle = random.nextDouble() * 2 * Math.PI;
            double offsetX = Math.cos(angle) * distance;
            double offsetZ = Math.sin(angle) * distance;

            Location location = targetLocation.clone().add(offsetX, 0, offsetZ);
            location.setY(location.getWorld().getHighestBlockYAt(location) + 1);

            if (isSafeLocation(location))
                return location;

            distance = ConfigManager.getPoliceTPMinDistance() + random.nextInt(ConfigManager.getPoliceTPMaxDistance() - ConfigManager.getPoliceTPMinDistance() + 1);
            max -= 1;
        }

        return null;
    }

    public boolean isSafeLocation(@NotNull Location location) {
        Block feet = location.getBlock();
        return feet.getType().isSolid() || feet.getLocation().add(0, 1, 0).getBlock().getType().isSolid() || feet.getLocation().add(0, -1, 0).getBlock().getType().isSolid();
    }
}