package me.zxoir.deeppd.managers;

import lombok.Getter;
import me.zxoir.deeppd.DeepPD;
import org.jetbrains.annotations.NotNull;

import static me.zxoir.deeppd.utils.Utils.colorize;

public class ConfigManager {
    private static final DeepPD main = DeepPD.getInstance();

    @Getter
    private static String invalidPermissionMessage;

    private static String policeChatPrefix;

    @Getter
    private static String policeSayPrefix;

    @Getter
    private static int hitsToJail;

    private static String jailAnnounce;

    @Getter
    private static int policeTPMinDistance;

    @Getter
    private static int policeTPMaxDistance;

    @Getter
    private static int pointsDelay;

    @Getter
    private static int hitWithFist;

    @Getter
    private static int hitWithWeapon;

    @Getter
    private static int hitPoliceWithFist;

    @Getter
    private static int hitPoliceWithWeapon;

    @Getter
    private static int killPlayer;

    @Getter
    private static int killOfficer;

    @Getter
    private static int maximumLevel;

    private static String promoteJrPoliceCommand;
    private static String promoteJrPoliceAnnounce;
    private static String promoteJrPolicePoliceChat;

    private static String promoteSrPoliceCommand;
    private static String promoteSrPoliceAnnounce;
    private static String promoteSrPolicePoliceChat;

    private static String promoteChiefCommand;
    private static String promoteChiefAnnounce;
    private static String promoteChiefPoliceChat;

    private static String demoteCitizenCommand;
    private static String demoteCitizenAnnounce;
    private static String demoteCitizenPoliceChat;

    private static String demoteJrPoliceCommand;
    private static String demoteJrPoliceAnnounce;
    private static String demoteJrPolicePoliceChat;

    private static String demoteSrPoliceCommand;
    private static String demoteSrPoliceAnnounce;
    private static String demoteSrPolicePoliceChat;

    private static void getConfigData() {
        invalidPermissionMessage = colorize(main.getConfig().getString("InvalidPermissionMessage"));
        policeChatPrefix = colorize(main.getConfig().getString("PoliceChatPrefix"));
        policeSayPrefix = colorize(main.getConfig().getString("PoliceSayPrefix"));
        hitsToJail = main.getConfig().getInt("HitsToJail");
        jailAnnounce = colorize(main.getConfig().getString("JailAnnounce"));
        policeTPMinDistance = main.getConfig().getInt("PoliceTP.MinDistance");
        policeTPMaxDistance = main.getConfig().getInt("PoliceTP.MaxDistance");
        pointsDelay = main.getConfig().getInt("PointsDelay");
        hitWithFist = main.getConfig().getInt("Wanted System.HitWithFist");
        hitWithWeapon = main.getConfig().getInt("Wanted System.HitWithWeapon");
        hitPoliceWithFist = main.getConfig().getInt("Wanted System.HitPoliceWithFist");
        hitPoliceWithWeapon = main.getConfig().getInt("Wanted System.HitPoliceWithWeapon");
        killPlayer = main.getConfig().getInt("Wanted System.KillPlayer");
        killOfficer = main.getConfig().getInt("Wanted System.KillOfficer");
        maximumLevel = main.getConfig().getInt("Wanted System.MaximumLevel");
        promoteJrPoliceCommand = main.getConfig().getString("Promote System.JrPolice.Command");
        promoteJrPoliceAnnounce = colorize(main.getConfig().getString("Promote System.JrPolice.Announce"));
        promoteJrPolicePoliceChat = colorize(main.getConfig().getString("Promote System.JrPolice.PoliceChat"));
        promoteSrPoliceCommand = main.getConfig().getString("Promote System.SrPolice.Command");
        promoteSrPoliceAnnounce = colorize(main.getConfig().getString("Promote System.SrPolice.Announce"));
        promoteSrPolicePoliceChat = colorize(main.getConfig().getString("Promote System.SrPolice.PoliceChat"));
        promoteChiefCommand = main.getConfig().getString("Promote System.Chief.Command");
        promoteChiefAnnounce = colorize(main.getConfig().getString("Promote System.Chief.Announce"));
        promoteChiefPoliceChat = colorize(main.getConfig().getString("Promote System.Chief.PoliceChat"));
        demoteCitizenCommand = main.getConfig().getString("Demote System.Citizen.Command");
        demoteCitizenAnnounce = colorize(main.getConfig().getString("Demote System.Citizen.Announce"));
        demoteCitizenPoliceChat = colorize(main.getConfig().getString("Demote System.Citizen.PoliceChat"));
        demoteJrPoliceCommand = main.getConfig().getString("Demote System.JrPolice.Command");
        demoteJrPoliceAnnounce = colorize(main.getConfig().getString("Demote System.JrPolice.Announce"));
        demoteJrPolicePoliceChat = colorize(main.getConfig().getString("Demote System.JrPolice.PoliceChat"));
        demoteSrPoliceCommand = main.getConfig().getString("Demote System.SrPolice.Command");
        demoteSrPoliceAnnounce = colorize(main.getConfig().getString("Demote System.SrPolice.Announce"));
        demoteSrPolicePoliceChat = colorize(main.getConfig().getString("Demote System.SrPolice.PoliceChat"));
    }

    public static void setup() {
        main.saveDefaultConfig();
        getConfigData();
    }

    public static void reloadConfig() {
        main.reloadConfig();
        getConfigData();
    }

    public static @NotNull String getPoliceChatPrefix(String playerName) {
        return policeChatPrefix.replace("%player%", playerName);
    }

    public static @NotNull String getJailAnnounce(String playerName, String officerName) {
        return jailAnnounce.replace("%player%", playerName).replace("%officer%", officerName);
    }

    public static @NotNull String getPromoteJrPoliceCommand(String promoter, String playerName) {
        return promoteJrPoliceCommand.replace("%promoter%", promoter).replace("%player%", playerName);
    }

    public static @NotNull String getPromoteJrPoliceAnnounce(String promoter, String playerName) {
        return promoteJrPoliceAnnounce.replace("%promoter%", promoter).replace("%player%", playerName);
    }

    public static @NotNull String getPromoteJrPolicePoliceChat(String promoter, String playerName) {
        return promoteJrPolicePoliceChat.replace("%promoter%", promoter).replace("%player%", playerName);
    }

    public static @NotNull String getPromoteSrPoliceCommand(String promoter, String playerName) {
        return promoteSrPoliceCommand.replace("%promoter%", promoter).replace("%player%", playerName);
    }

    public static @NotNull String getPromoteSrPoliceAnnounce(String promoter, String playerName) {
        return promoteSrPoliceAnnounce.replace("%promoter%", promoter).replace("%player%", playerName);
    }

    public static @NotNull String getPromoteSrPolicePoliceChat(String promoter, String playerName) {
        return promoteSrPolicePoliceChat.replace("%promoter%", promoter).replace("%player%", playerName);
    }

    public static @NotNull String getPromoteChiefCommand(String promoter, String playerName) {
        return promoteChiefCommand.replace("%promoter%", promoter).replace("%player%", playerName);
    }

    public static @NotNull String getPromoteChiefAnnounce(String promoter, String playerName) {
        return promoteChiefAnnounce.replace("%promoter%", promoter).replace("%player%", playerName);
    }

    public static @NotNull String getPromoteChiefPoliceChat(String promoter, String playerName) {
        return promoteChiefPoliceChat.replace("%promoter%", promoter).replace("%player%", playerName);
    }

    public static @NotNull String getDemoteCitizenCommand(String demoter, String playerName) {
        return demoteCitizenCommand.replace("%demoter%", demoter).replace("%player%", playerName);
    }

    public static @NotNull String getDemoteCitizenAnnounce(String demoter, String playerName) {
        return demoteCitizenAnnounce.replace("%demoter%", demoter).replace("%player%", playerName);
    }

    public static @NotNull String getDemoteCitizenPoliceChat(String demoter, String playerName) {
        return demoteCitizenPoliceChat.replace("%demoter%", demoter).replace("%player%", playerName);
    }

    public static @NotNull String getDemoteJrPoliceCommand(String demoter, String playerName) {
        return demoteJrPoliceCommand.replace("%demoter%", demoter).replace("%player%", playerName);
    }

    public static @NotNull String getDemoteJrPoliceAnnounce(String demoter, String playerName) {
        return demoteJrPoliceAnnounce.replace("%demoter%", demoter).replace("%player%", playerName);
    }

    public static @NotNull String getDemoteJrPolicePoliceChat(String demoter, String playerName) {
        return demoteJrPolicePoliceChat.replace("%demoter%", demoter).replace("%player%", playerName);
    }

    public static @NotNull String getDemoteSrPoliceCommand(String demoter, String playerName) {
        return demoteSrPoliceCommand.replace("%demoter%", demoter).replace("%player%", playerName);
    }

    public static @NotNull String getDemoteSrPoliceAnnounce(String demoter, String playerName) {
        return demoteSrPoliceAnnounce.replace("%demoter%", demoter).replace("%player%", playerName);
    }

    public static @NotNull String getDemoteSrPolicePoliceChat(String demoter, String playerName) {
        return demoteSrPolicePoliceChat.replace("%demoter%", demoter).replace("%player%", playerName);
    }
}
