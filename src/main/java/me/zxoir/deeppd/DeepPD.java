package me.zxoir.deeppd;

import lombok.Getter;
import me.zxoir.deeppd.commands.*;
import me.zxoir.deeppd.customclasses.Criminal;
import me.zxoir.deeppd.listeners.GeneralListeners;
import me.zxoir.deeppd.listeners.WantedSystem;
import me.zxoir.deeppd.managers.ConfigManager;
import me.zxoir.deeppd.managers.CriminalManager;
import me.zxoir.deeppd.managers.OfficerManager;
import me.zxoir.deeppd.tabcompleters.*;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class DeepPD extends JavaPlugin {
    @Getter
    private static DeepPD instance;
    @Getter
    private static DataFile dataFile;

    @Override
    public void onEnable() {
        instance = this;
        ConfigManager.setup();
        dataFile = new DataFile();
        dataFile.setup();

        registerCommands();
        registerCommandCompleters();
        registerListeners();

        if (Bukkit.getOnlinePlayers().isEmpty())
            return;

        Bukkit.getOnlinePlayers().forEach(online -> CriminalManager.getCachedCriminals().put(online.getUniqueId(), new Criminal(online.getUniqueId())));
    }

    @Override
    public void onDisable() {
        OfficerManager.getCachedOfficers().asMap().values().forEach(officer -> OfficerManager.endDuty(officer.getUuid()));

        instance = null;
    }

    private void registerCommands() {
        getCommand("deeppd").setExecutor(new DeepPDCommand());
        getCommand("getbaton").setExecutor(new GetBatonCommand());
        getCommand("duty").setExecutor(new DutyCommand());
        getCommand("police").setExecutor(new PoliceCommand());
        getCommand("policechat").setExecutor(new PoliceChatCommand());
        getCommand("policechattoggle").setExecutor(new PoliceChatToggleCommand());
        getCommand("policesay").setExecutor(new PoliceSayCommand());
        getCommand("policetp").setExecutor(new PoliceTPCommand());
        getCommand("release").setExecutor(new ReleaseCommand());
        getCommand("incidents").setExecutor(new IncidentsCommand());
        getCommand("wanted").setExecutor(new WantedCommand());
    }

    private void registerCommandCompleters() {
        getCommand("deeppd").setTabCompleter(new DeepPDCompleter());
        getCommand("incidents").setTabCompleter(new IncidentsCompleter());
        getCommand("police").setTabCompleter(new PoliceCompleter());
        getCommand("policetp").setTabCompleter(new PoliceTPCompleter());
        getCommand("release").setTabCompleter(new ReleaseCompleter());
        getCommand("wanted").setTabCompleter(new WantedCompleter());
    }

    private void registerListeners() {
        getServer().getPluginManager().registerEvents(new GeneralListeners(), this);
        getServer().getPluginManager().registerEvents(new WantedSystem(), this);
    }
}
