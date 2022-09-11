package io.github.endergamerhun.deathhistory.events;

import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.UUID;

public class PlayerDeath implements Listener {

    private static final Hashtable<UUID, List<ItemStack>> drops = new Hashtable<>();
    private static final Hashtable<UUID, Integer> exp = new Hashtable<>();
    private static final Hashtable<UUID, Location> location = new Hashtable<>();
    private static final Hashtable<UUID, Date> time = new Hashtable<>();

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        UUID uuid = e.getEntity().getUniqueId();

        location.put(uuid, e.getEntity().getLocation());
        time.put(uuid, new Date());

        if (e.getKeepInventory()) drops.remove(uuid);
        else drops.put(uuid, e.getDrops());

        if (e.getKeepLevel()) exp.remove(uuid);
        else exp.put(uuid, e.getDroppedExp());
    }

    public static boolean containsDrops(UUID uuid) {
        return drops.containsKey(uuid);
    }
    public static boolean containsExp(UUID uuid) {
        return exp.containsKey(uuid);
    }
    public static boolean containsLocation(UUID uuid) {
        return location.containsKey(uuid);
    }
    public static boolean containsTime(UUID uuid) {
        return time.containsKey(uuid);
    }
    public static List<ItemStack> getDrops(UUID uuid) {
        return drops.get(uuid);
    }
    public static int getExp(UUID uuid) {
        return exp.get(uuid);
    }
    public static Location getLocation(UUID uuid) {
        return location.get(uuid);
    }
    public static Date getTime(UUID uuid) {
        return time.get(uuid);
    }
}
