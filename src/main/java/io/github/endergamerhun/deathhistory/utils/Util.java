package io.github.endergamerhun.deathhistory.utils;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

public class Util {

    private static final String PREFIX = "§7[§cDeath§eHistory§7]§f ";

    public static void log(String format, Object... objects) {
        final String log = String.format(format, objects);
        Bukkit.getConsoleSender().sendMessage(PREFIX + log);
    }
    public static OfflinePlayer getOfflinePlayer(String name) {
        for (OfflinePlayer player : Bukkit.getOfflinePlayers()) {
            if (name.equals(player.getName())) return player;
        }
        return null;
    }
}
