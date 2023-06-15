package me.zxoir.deeppd.utils;

import org.bukkit.ChatColor;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class Utils {

    @Contract("_ -> new")
    public static @NotNull String colorize(String arg) {
        return ChatColor.translateAlternateColorCodes('&', arg);
    }

    public static boolean isInteger(String string) {

        try {
            Integer.parseInt(string);
        } catch (IllegalArgumentException e) {
            return false;
        }

        return true;
    }

    public static @NotNull List<String> smartComplete(String @NotNull [] args, @NotNull List<String> list) {
        String arg = args[args.length - 1];
        List<String> temp = new ArrayList<>();

        for (String item : list) {
            if (item.toUpperCase().startsWith(arg.toUpperCase())) {
                temp.add(item);
            }
        }

        return temp;
    }
}
