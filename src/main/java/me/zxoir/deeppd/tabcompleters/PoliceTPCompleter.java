package me.zxoir.deeppd.tabcompleters;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class PoliceTPCompleter implements TabCompleter {

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        List<String> completer = new ArrayList<>();

        if (args.length == 1) {
            completer.add("<PLAYER>");
            Bukkit.getOnlinePlayers().forEach(player -> completer.add(player.getName()));
        }

        return completer;
    }

}
