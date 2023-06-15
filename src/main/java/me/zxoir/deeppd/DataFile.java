package me.zxoir.deeppd;

import me.zxoir.deeppd.managers.CriminalManager;
import me.zxoir.deeppd.managers.OfficerManager;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class DataFile {
    public final DeepPD plugin = DeepPD.getInstance();
    public FileConfiguration playerscfg;
    public File playersfile;

    // Sets up the configuration yml
    @SuppressWarnings({"unchecked", "ResultOfMethodCallIgnored"})
    public void setup() {
        if (!this.plugin.getDataFolder().exists()) {
            this.plugin.getDataFolder().mkdir();
        }

        File dataFile = new File(this.plugin.getDataFolder() + File.separator + "Data" + File.separator);
        this.playersfile = new File(dataFile.getPath(), "DataFile.yml");

        if (!dataFile.exists()) {
            try {
                dataFile.mkdirs();
                this.plugin.getServer().getConsoleSender().sendMessage("the Data folder has been created!");
            } catch (SecurityException e) {
                this.plugin.getServer().getConsoleSender().sendMessage("Could not create the Data folder");
            }
        }

        if (!this.playersfile.exists()) {
            try {
                this.playersfile.createNewFile();
                this.plugin.getServer().getConsoleSender().sendMessage("the DataFile.yml file has been created!");
            } catch (IOException e) {
                this.plugin.getServer().getConsoleSender().sendMessage("Could not create the DataFile.yml file");
            }
        }

        this.playerscfg = YamlConfiguration.loadConfiguration(this.playersfile);

        if (getConfig().getString("dutyInventory") != null) {
            ArrayList<ItemStack> list = (ArrayList<ItemStack>) getConfig().getList("dutyInventory");
            OfficerManager.setDutyInventory(list.toArray(new ItemStack[0]));
        }

        if (getConfig().getString("dutyArmour") != null) {
            ArrayList<ItemStack> list = (ArrayList<ItemStack>) getConfig().getList("dutyArmour");
            OfficerManager.setDutyArmour(list.toArray(new ItemStack[0]));
        }

        if (getConfig().getString("jail") != null) {
            OfficerManager.setJail((Location) getConfig().get("jail", Location.class));
        }

        if (getConfig().getString("unjail") != null) {
            OfficerManager.setUnjail((Location) getConfig().get("unjail", Location.class));
        }

        if (getConfig().getString("drugItems") != null) {
            ArrayList<String> list = (ArrayList<String>) getConfig().getList("drugItems");
            list.forEach(material -> CriminalManager.getDrugItems().add(Material.valueOf(material)));
        }
    }

    // Gets the yml configuration
    public FileConfiguration getConfig() {
        return this.playerscfg;
    }

    // Saves the yml configuration
    public void saveConfig() {
        try {
            this.playerscfg.save(this.playersfile);
        } catch (IOException localIOException) {
            this.plugin.getServer().getConsoleSender().sendMessage("Could not save the DataFile.yml file");
        }
    }

    // Reloads the yml configuration
    public void reloadConfig() {
        this.playerscfg = YamlConfiguration.loadConfiguration(this.playersfile);
    }
}
