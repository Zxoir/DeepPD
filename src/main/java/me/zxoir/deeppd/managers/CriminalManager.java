package me.zxoir.deeppd.managers;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lombok.Getter;
import me.zxoir.deeppd.DeepPD;
import me.zxoir.deeppd.commands.PoliceSayCommand;
import me.zxoir.deeppd.customclasses.Criminal;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class CriminalManager {
    @Getter
    private static final Cache<UUID, Criminal> cachedCriminals = CacheBuilder.newBuilder().build();
    @Getter
    private static final List<Material> drugItems = new ArrayList<>();

    public static void JailPlayer(@NotNull Player player, @NotNull Player officer) {
        player.teleport(OfficerManager.getJail());

        Criminal criminal = cachedCriminals.getIfPresent(player.getUniqueId());
        criminal.clearWanted();
        criminal.setJailed(true);
        criminal.setJailDuration(Instant.now().plusMillis(criminal.getDefaultJailDuration()));

        criminal.setJailTask(Bukkit.getScheduler().runTaskLater(DeepPD.getInstance(), () -> ReleasePlayer(player), criminal.getDefaultJailDuration()));

        Arrays.stream(player.getInventory().getContents()).filter(itemStack -> drugItems.contains(itemStack.getType())).forEach(itemStack -> {
            player.getInventory().remove(itemStack);
            officer.getInventory().addItem(itemStack);
        });

        PoliceSayCommand.PoliceSay(ConfigManager.getJailAnnounce(player.getName(), officer.getName()));
    }

    public static void ReleasePlayer(@NotNull OfflinePlayer player) {
        Criminal criminal = cachedCriminals.getIfPresent(player.getUniqueId());
        criminal.setJailed(false);

        if (player.getPlayer() != null && player.isOnline())
            player.getPlayer().teleport(OfficerManager.getUnjail());
    }

}
