package me.zxoir.deeppd.listeners;

import me.zxoir.deeppd.DeepPD;
import me.zxoir.deeppd.commands.PoliceChatCommand;
import me.zxoir.deeppd.commands.PoliceChatToggleCommand;
import me.zxoir.deeppd.customclasses.Officer;
import me.zxoir.deeppd.managers.ConfigManager;
import me.zxoir.deeppd.managers.CriminalManager;
import me.zxoir.deeppd.managers.OfficerManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.UUID;

public class GeneralListeners implements Listener {
    private final HashMap<UUID, Integer> hitsToJail = new HashMap<>();

    @EventHandler
    public void onJoin(@NotNull PlayerJoinEvent event) {
        Player player = event.getPlayer();

        Officer officer = OfficerManager.getCachedOfficers().getIfPresent(player.getUniqueId());

        if (officer == null)
            return;

        Bukkit.getScheduler().runTaskLater(DeepPD.getInstance(), () -> {
            player.getInventory().setContents(OfficerManager.getDutyInventory());
            player.getInventory().setArmorContents(OfficerManager.getDutyArmour());
            player.updateInventory();
        }, 1L);
    }

    @EventHandler
    public void onQuit(@NotNull PlayerQuitEvent event) {
        Player player = event.getPlayer();

        Officer officer = OfficerManager.getCachedOfficers().getIfPresent(player.getUniqueId());
        if (officer == null)
            return;

        player.getInventory().setContents(officer.getInventory());
        player.getInventory().setArmorContents(officer.getArmour());
        player.addPotionEffects(officer.getPotionEffects());
        player.updateInventory();
    }

    @EventHandler(ignoreCancelled = true)
    public void onHit(@NotNull EntityDamageByEntityEvent event) {
        if (event.getDamager().getType() != EntityType.PLAYER && event.getEntityType() != EntityType.PLAYER)
            return;

        Player damager = (Player) event.getDamager();
        Player player = (Player) event.getEntity();

        if (!OfficerManager.getCachedOfficers().asMap().containsKey(damager.getUniqueId()))
            return;

        Officer officer = OfficerManager.getCachedOfficers().getIfPresent(damager.getUniqueId());

        if (!damager.getInventory().getItemInMainHand().isSimilar(OfficerManager.POLICE_BATON))
            return;

        if (OfficerManager.getJail() == null)
            return;

        if (officer.getLastHit() != null && !officer.getLastHit().equals(player.getUniqueId()))
            hitsToJail.remove(damager.getUniqueId());

        if (!hitsToJail.containsKey(damager.getUniqueId()))
            hitsToJail.put(damager.getUniqueId(), 1);
        else
            hitsToJail.put(damager.getUniqueId(), hitsToJail.get(damager.getUniqueId()) + 1);

        int hits = hitsToJail.get(damager.getUniqueId());

        officer.setLastHitTask(Bukkit.getScheduler().runTaskLater(DeepPD.getInstance(), () -> hitsToJail.remove(damager.getUniqueId()), 100L));
        officer.setLastHit(player.getUniqueId());

        if (hits < ConfigManager.getHitsToJail())
            return;

        if (officer.getLastHitTask() != null)
            officer.getLastHitTask().cancel();

        hitsToJail.remove(damager.getUniqueId());

        CriminalManager.JailPlayer(player, damager);
    }

    @EventHandler(ignoreCancelled = true)
    public void onChat(@NotNull AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();

        if (!PoliceChatToggleCommand.getPoliceChatToggle().contains(player.getUniqueId()))
            return;

        if (!(player.hasPermission("deeppd.*") || player.hasPermission("deeppd.pc.toggle"))) {
            PoliceChatToggleCommand.getPoliceChatToggle().remove(player.getUniqueId());
            return;
        }

        PoliceChatCommand.PoliceChat(event.getMessage(), player.getName());
        event.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true)
    public void onDropItem(@NotNull PlayerDropItemEvent event) {
        Player player = event.getPlayer();

        if (!OfficerManager.getCachedOfficers().asMap().containsKey(player.getUniqueId()))
            return;

        event.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true)
    public void onReceiveItem(@NotNull EntityPickupItemEvent event) {
        if (event.getEntityType() != EntityType.PLAYER)
            return;

        Player player = (Player) event.getEntity();

        if (!OfficerManager.getCachedOfficers().asMap().containsKey(player.getUniqueId()))
            return;

        event.setCancelled(true);
    }

}
