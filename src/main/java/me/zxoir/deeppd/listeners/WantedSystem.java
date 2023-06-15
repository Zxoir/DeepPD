package me.zxoir.deeppd.listeners;

import me.zxoir.deeppd.DeepPD;
import me.zxoir.deeppd.customclasses.Criminal;
import me.zxoir.deeppd.customclasses.Incident;
import me.zxoir.deeppd.managers.ConfigManager;
import me.zxoir.deeppd.managers.CriminalManager;
import me.zxoir.deeppd.managers.OfficerManager;
import org.bukkit.Bukkit;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Date;

public class WantedSystem implements Listener {

    @EventHandler
    public void onJoin(@NotNull PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if (!CriminalManager.getCachedCriminals().asMap().containsKey(player.getUniqueId()))
            CriminalManager.getCachedCriminals().put(player.getUniqueId(), new Criminal(player.getUniqueId()));
        else {
            Criminal criminal = CriminalManager.getCachedCriminals().getIfPresent(player.getUniqueId());
            if (!criminal.isJailed())
                return;

            Bukkit.getScheduler().runTaskLater(DeepPD.getInstance(), () -> player.teleport(OfficerManager.getJail()), 1L);
        }
    }

    @EventHandler
    public void onKill(@NotNull PlayerDeathEvent event) {
        Player player = event.getEntity();
        Player killer = player.getKiller();

        if (OfficerManager.getCachedOfficers().asMap().containsKey(killer.getUniqueId()))
            return;

        Criminal criminal = CriminalManager.getCachedCriminals().getIfPresent(killer.getUniqueId());
        if (criminal == null)
            return;

        boolean isOfficer = OfficerManager.getCachedOfficers().asMap().containsKey(player.getUniqueId());

        if (isOfficer)
            criminal.addPoints(ConfigManager.getKillOfficer());
        else
            criminal.addPoints(ConfigManager.getKillPlayer());

        criminal.getIncidents().add(new Incident(player.getUniqueId(), new Date()));
    }

    @EventHandler(ignoreCancelled = true)
    public void onHit(@NotNull EntityDamageByEntityEvent event) {
        if (event.getDamager().getType() != EntityType.PLAYER || event.getEntityType() != EntityType.PLAYER)
            return;

        Player damager = (Player) event.getDamager();
        Player player = (Player) event.getEntity();

        if (OfficerManager.getCachedOfficers().asMap().containsKey(damager.getUniqueId()))
            return;

        boolean hasWeapon = EnchantmentTarget.WEAPON.includes(damager.getInventory().getItemInMainHand()) || EnchantmentTarget.WEAPON.includes(damager.getInventory().getItemInOffHand());
        boolean isOfficer = OfficerManager.getCachedOfficers().asMap().containsKey(player.getUniqueId());

        Criminal criminal = CriminalManager.getCachedCriminals().getIfPresent(damager.getUniqueId());
        if (criminal == null)
            return;

        if (criminal.getLastAddedPoints().isBefore(criminal.getLastAddedPoints().plusSeconds(ConfigManager.getPointsDelay())))
            return;

        if (hasWeapon && isOfficer)
            criminal.addPoints(ConfigManager.getHitPoliceWithWeapon());
        else if (isOfficer)
            criminal.addPoints(ConfigManager.getHitPoliceWithFist());
        else if (hasWeapon)
            criminal.addPoints(ConfigManager.getHitWithWeapon());
        else
            criminal.addPoints(ConfigManager.getHitWithFist());
    }
}
