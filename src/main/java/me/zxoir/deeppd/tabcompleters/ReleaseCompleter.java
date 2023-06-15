package me.zxoir.deeppd.tabcompleters;

import me.zxoir.deeppd.customclasses.Criminal;
import me.zxoir.deeppd.managers.CriminalManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ReleaseCompleter implements TabCompleter {
    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        List<String> completer = new ArrayList<>();

        if (args.length == 1) {
            completer.add("<PLAYER>");
            CriminalManager.getCachedCriminals().asMap().values().stream().filter(Criminal::isJailed).forEach(criminal -> completer.add(Bukkit.getOfflinePlayer(criminal.getUuid()).getName()));
        }

        return completer;
    }
}
