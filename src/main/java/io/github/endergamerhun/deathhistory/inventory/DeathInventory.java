package io.github.endergamerhun.deathhistory.inventory;

import io.github.endergamerhun.deathhistory.events.PlayerDeath;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.profile.PlayerProfile;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class DeathInventory implements InventoryHolder {

    private final Inventory inv;
    private final PlayerProfile player;


    public DeathInventory(OfflinePlayer p) {
        this(p.getPlayerProfile());
    }
    public DeathInventory(PlayerProfile profile) {
        player = profile;

        inv = Bukkit.createInventory(this, 5*9, "§6Death History of §e" + profile.getName());

        ItemStack filler = createItem(Material.GRAY_STAINED_GLASS_PANE, "§f");

        for (int i = 0; i < 5 * 9; i++) {
            inv.setItem(i, filler);
        }

        refresh();
    }

    public void refresh() {
        ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) skull.getItemMeta();
        assert meta != null;
        meta.setOwnerProfile(player);
        meta.setDisplayName("§e" + player.getName());
        skull.setItemMeta(meta);

        inv.setItem(4, skull);

        inv.setItem(20, createItem(Material.HOPPER, "§eCollect Items", Arrays.asList("","§7Click to collect")));
        inv.setItem(21, createItem(Material.EMERALD, "§eCollect EXP", Arrays.asList("","§7Click to collect")));
        inv.setItem(29, createItem(Material.ENDER_PEARL, "§eTeleport to location", Arrays.asList("","§7Click to teleport")));
        inv.setItem(30, createItem(Material.TNT, "§cDelete save", Arrays.asList("","§7Click to DELETE")));

        final UUID uuid = player.getUniqueId();

        inv.setItem(23, createItem(Material.CHEST, "§dInspect Drops", Arrays.asList("","§7Click to View")));
        inv.setItem(24, createItem(Material.EXPERIENCE_BOTTLE, "§Dropped EXP:", Arrays.asList("", "§7" + PlayerDeath.getExp(uuid))));
        Location loc = PlayerDeath.getLocation(uuid);
        inv.setItem(32, createItem(Material.OAK_SIGN, "§dLocation:", Arrays.asList("§7X: " + loc.getBlockX(), "§7Y: " + loc.getBlockY(), "§7Z: " + loc.getBlockZ())));
        inv.setItem(33, createItem(Material.CLOCK, "§dTime:", Arrays.asList("", "§7" + PlayerDeath.getTime(uuid).toString())));
    }


    private ItemStack createItem(Material mat, String name) {
        return createItem(mat, name, null);
    }
    private ItemStack createItem(Material mat, String name, List<String> lore) {
        ItemStack item = new ItemStack(mat);
        ItemMeta meta = item.getItemMeta();
        assert meta != null;
        meta.setDisplayName(name);
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    @Override
    public Inventory getInventory() {
        return inv;
    }
}
