package me.zxoir.deeppd.managers;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lombok.Getter;
import lombok.Setter;
import me.zxoir.deeppd.customclasses.Officer;
import me.zxoir.deeppd.utils.ItemStackBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.UUID;

public class OfficerManager {
    @Getter
    private static final Cache<UUID, Officer> cachedOfficers;
    @Getter
    private static ItemStack[] dutyInventory = new ItemStack[]{};
    @Getter
    private static ItemStack[] dutyArmour = new ItemStack[]{};
    @Getter
    @Setter
    private static Location jail;
    @Getter
    @Setter
    private static Location unjail;
    public static final ItemStack POLICE_BATON;

    static {
        cachedOfficers = CacheBuilder.newBuilder().build();
        POLICE_BATON = new ItemStackBuilder(Material.STICK).withName("&9&lPolice Baton").isUnbreakable(true).build();
    }

    public synchronized static void beginDuty(@NotNull UUID uuid) {
        if (cachedOfficers.asMap().containsKey(uuid))
            return;

        Player player = Bukkit.getPlayer(uuid);
        Officer officer = new Officer(player);
        cachedOfficers.put(player.getUniqueId(), officer);

        player.getActivePotionEffects().forEach(potionEffect -> player.removePotionEffect(potionEffect.getType()));
        player.getInventory().setContents(dutyInventory);
        player.getInventory().setArmorContents(dutyArmour);
        player.updateInventory();
    }

    public synchronized static void endDuty(@NotNull UUID uuid) {
        Officer officer = cachedOfficers.getIfPresent(uuid);

        if (officer == null)
            return;

        cachedOfficers.asMap().remove(uuid);

        Player player = Bukkit.getPlayer(uuid);
        player.getInventory().setContents(officer.getInventory());
        player.getInventory().setArmorContents(officer.getArmour());
        player.addPotionEffects(officer.getPotionEffects());
        player.updateInventory();
    }

    public static void setDutyInventory(ItemStack[] dutyInventory) {
        OfficerManager.dutyInventory = dutyInventory;

        Arrays.stream(dutyInventory).filter(itemStack -> itemStack != null && !itemStack.getType().equals(Material.AIR)).forEach(itemStack -> new ItemStackBuilder(itemStack).isUnbreakable(true));
    }

    public static void setDutyArmour(ItemStack[] dutyArmour) {
        OfficerManager.dutyArmour = dutyArmour;

        Arrays.stream(dutyArmour).filter(itemStack -> itemStack != null && !itemStack.getType().equals(Material.AIR)).forEach(itemStack -> new ItemStackBuilder(itemStack).isUnbreakable(true));
    }
}
