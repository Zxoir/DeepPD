package me.zxoir.deeppd.customclasses;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.UUID;

@Getter
public class Officer {
    UUID uuid;
    ItemStack[] inventory;
    ItemStack[] armour;
    Collection<PotionEffect> potionEffects;
    @Setter
    UUID lastHit;
    @Setter
    BukkitTask lastHitTask;

    public Officer(@NotNull Player player) {
        uuid = player.getUniqueId();
        inventory = player.getInventory().getContents().clone();
        armour = player.getInventory().getArmorContents().clone();
        potionEffects = player.getActivePotionEffects();
    }

}
