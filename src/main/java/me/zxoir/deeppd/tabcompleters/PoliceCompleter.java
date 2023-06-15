package me.zxoir.deeppd.tabcompleters;

import me.zxoir.deeppd.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PoliceCompleter implements TabCompleter {
    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        List<String> completer = new ArrayList<>();

        if (args.length == 1) {
            completer.add("setInventory");
            completer.add("setJail");
            completer.add("setUnjail");
            completer.add("promote");
            completer.add("demote");
            completer.add("drug");
        }

        if (args.length == 2) {
            if (args[0].equalsIgnoreCase("promote") || args[0].equalsIgnoreCase("demote")) {
                completer.add("<PLAYER>");
                Bukkit.getOnlinePlayers().forEach(player -> completer.add(player.getName()));
            }

            if (args[0].equalsIgnoreCase("drug")) {
                completer.add("add");
                completer.add("remove");
            }
        }

        if (args.length == 3) {

            if (args[0].equalsIgnoreCase("drug")) {
                completer.add("<ITEM_TYPE>");
                Arrays.stream(Material.values()).forEach(material -> completer.add(material.name()));
            }

            if (args[0].equalsIgnoreCase("promote")) {
                completer.add("Jr.Police");
                completer.add("Sr.Police");
                completer.add("Chief");
            }

            if (args[0].equalsIgnoreCase("demote")) {
                completer.add("Citizen");
                completer.add("Jr.Police");
                completer.add("Sr.Police");
            }
        }

        return Utils.smartComplete(args, completer);
    }
}
