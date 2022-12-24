package io.github.endergamerhun.deathhistory.events;

import io.github.endergamerhun.deathhistory.utils.DeathRecord;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.*;

public class DeathManager implements Listener {

    private static final List<DeathRecord> deathRecords = new ArrayList<>();
    private static final HashMap<UUID, DeathRecord> latestDeaths = new HashMap<>();

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        UUID uuid = e.getEntity().getUniqueId();
        DeathRecord record = new DeathRecord(e.getEntity(), (e.getKeepInventory() ? e.getDrops() : new ArrayList<>()), (e.getKeepLevel() ? e.getDroppedExp() : 0), e.getEntity().getLocation(), new Date());
        deathRecords.add(record);
        latestDeaths.put(uuid, record);
    }

    public static List<DeathRecord> getRecords() {
        return deathRecords;
    }
    public static List<DeathRecord> getRecords(UUID uuid) {
        String id = uuid.toString();
        return deathRecords.stream().filter(record -> record.id().startsWith(id)).toList();
    }
    public static DeathRecord getRecord(String id) {
        for (DeathRecord record : deathRecords) {
            if (record.equals(id)) return record;
        }
        return null;
    }
    public static List<OfflinePlayer> getPlayers() {
        return deathRecords.stream().map(DeathRecord::player).toList();
    }
    public static List<String> getPlayerNames() {
        return getPlayers().stream().map(OfflinePlayer::getName).toList();
    }
    public static DeathRecord getLatestRecord(UUID uuid) {
        return latestDeaths.get(uuid);
    }
    public static boolean hasRecord(UUID uuid) {
        return latestDeaths.containsKey(uuid);
    }
    public static void removeRecord(DeathRecord record) {
        latestDeaths.remove(record.player().getUniqueId(), record);
        deathRecords.remove(record);
    }
    public static void removeRecord(String id) {
        deathRecords.removeIf(record -> id.equals(record.id()));
        latestDeaths.forEach((uuid, record) -> {
            if (id.equals(record.id())) latestDeaths.remove(uuid);
        });
    }
    public static void clear() {
        deathRecords.clear();
        latestDeaths.clear();
    }
    public static void clear(UUID uuid) {
        deathRecords.removeIf(record -> uuid.equals(record.player().getUniqueId()));
        latestDeaths.remove(uuid);
    }
}
