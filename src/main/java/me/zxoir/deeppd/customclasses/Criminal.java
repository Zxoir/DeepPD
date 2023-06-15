package me.zxoir.deeppd.customclasses;

import lombok.Getter;
import me.zxoir.deeppd.DeepPD;
import me.zxoir.deeppd.managers.ConfigManager;
import me.zxoir.deeppd.utils.TimeManager;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Getter
public class Criminal {
    UUID uuid;
    int points;
    int level;
    List<Incident> incidents = Collections.synchronizedList(new ArrayList<>());
    boolean isJailed;
    BukkitTask wantedTask;
    BukkitTask jailTask;
    Instant lastAddedPoints;
    Instant wantedDuration;
    Instant jailDuration;

    public Criminal(UUID uuid) {
        this.uuid = uuid;
        this.points = 0;
        this.level = 0;
        this.isJailed = false;
        lastAddedPoints = Instant.now();
    }

    public void addPoints(int points) {
        this.points += points;

        int updatedLevel = checkLevel();

        if (updatedLevel > level) {
            level = updatedLevel;
        }

        if (getWantedTask() != null)
            getWantedTask().cancel();

        wantedDuration = Instant.now().plusMillis(getDefaultWantedDuration());

        setWantedTask(Bukkit.getScheduler().runTaskLater(DeepPD.getInstance(), this::clearWanted, getDefaultWantedDuration()));
    }

    public void setLevel(int level) {
        this.level = level;

        this.points = level < 1 ? 0 : DeepPD.getInstance().getConfig().getInt("Wanted System." + level + ".Points");

        if (getWantedTask() != null)
            getWantedTask().cancel();

        wantedDuration = Instant.now().plusMillis(getDefaultWantedDuration());

        setWantedTask(Bukkit.getScheduler().runTaskLater(DeepPD.getInstance(), this::clearWanted, getDefaultWantedDuration()));
    }

    public long getDefaultWantedDuration() {
        return TimeManager.toMilliSecond(DeepPD.getInstance().getConfig().getString("Wanted System." + level + ".WantedDuration"));
    }

    public long getDefaultJailDuration() {
        return TimeManager.toMilliSecond(DeepPD.getInstance().getConfig().getString("Wanted System." + level + ".JailDuration"));
    }

    public void setJailed(boolean jailed) {
        isJailed = jailed;
    }

    public void setJailTask(BukkitTask jailTask) {
        this.jailTask = jailTask;
    }

    public void setWantedTask(BukkitTask wantedTask) {
        this.wantedTask = wantedTask;
    }

    public void clearWanted() {
        points = 0;
        level = 0;
    }

    public void setJailDuration(Instant jailDuration) {
        this.jailDuration = jailDuration;
    }

    public void setWantedDuration(Instant wantedDuration) {
        this.wantedDuration = wantedDuration;
    }

    private int checkLevel() {
        int max = ConfigManager.getMaximumLevel();
        int currentLevel = 0;

        for (int i = 1; i <= max; i++) {
            int wantedPoints = DeepPD.getInstance().getConfig().getInt("Wanted System." + i + ".Points");
            currentLevel = points >= wantedPoints ? i : currentLevel;
        }

        return currentLevel;
    }
}