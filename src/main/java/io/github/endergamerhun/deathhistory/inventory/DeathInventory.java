package io.github.endergamerhun.deathhistory.inventory;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import io.github.endergamerhun.deathhistory.events.DeathManager;
import io.github.endergamerhun.deathhistory.utils.DeathRecord;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.List;

public class DeathInventory {

    public static void openMain(Player player, DeathRecord record) {
        Gui gui = Gui.gui().title(Component.text("")).rows(5).create();
        GuiItem filler = new GuiItem(ItemBuilder.from(Material.GRAY_STAINED_GLASS_PANE).name(Component.text("§f")).build(), event -> event.setCancelled(true));
        for (int i = 0; i < 5 * 9; i++) {
            gui.setItem(i, filler);
        }

        ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) skull.getItemMeta();
        assert meta != null;
        meta.setOwningPlayer(record.player());
        meta.setDisplayName("§e" + player.getName());
        skull.setItemMeta(meta);

        gui.setItem(4, new GuiItem(skull, event -> event.setCancelled(true)));
        gui.setItem(20, ItemBuilder.from(Material.HOPPER).name(Component.text("§eCollect Items")).lore(List.of(Component.text(" §7Click to Collect"))).asGuiItem(event -> {
            event.setCancelled(true);
            if (event.getWhoClicked() instanceof Player p) {
                for (ItemStack i : record.drops()) {
                    p.getInventory().addItem(i);
                }
                p.sendMessage("§aItems received!");
            }
        }));
        gui.setItem(21, ItemBuilder.from(Material.EMERALD).name(Component.text("§eCollect EXP")).lore(List.of(Component.text("§7 Click to Collect"))).asGuiItem(event -> {
            event.setCancelled(true);
            if (event.getWhoClicked() instanceof Player p) {
                p.giveExp(record.exp());
            }
        }));
        gui.setItem(29, ItemBuilder.from(Material.ENDER_PEARL).name(Component.text("§eTeleport to location")).lore(List.of(Component.text("§7 Click to Teleport"))).asGuiItem(event -> {
            event.setCancelled(true);
            if (event.getWhoClicked() instanceof Player p) {
                p.teleport(record.location());
            }
        }));
        gui.setItem(30, ItemBuilder.from(Material.TNT).name(Component.text("§cDelete Save")).lore(List.of(Component.text("§7 Click to §7§lDELETE"))).asGuiItem(event -> {
            event.setCancelled(true);
            DeathManager.removeRecord(record);
            player.sendMessage("§aRecord deleted!");
            gui.close(player);
        }));

        gui.setItem(23, ItemBuilder.from(Material.CHEST).name(Component.text("§bInspect Drops")).lore(List.of(Component.text("§7 Click to View"))).asGuiItem(event -> {
            event.setCancelled(true);
            openStored(player, record);
        }));
        gui.setItem(24, ItemBuilder.from(Material.EXPERIENCE_BOTTLE).name(Component.text("§bDropped EXP:")).lore(List.of(Component.text("§e " + record.exp()))).asGuiItem(event -> event.setCancelled(true)));
        Location loc = record.location();
        gui.setItem(32, ItemBuilder.from(Material.OAK_SIGN).name(Component.text("§bLocation")).lore(List.of(
                Component.text(" §7X: §e" + loc.getBlockX()),
                Component.text(" §7Y: §e" + loc.getBlockY()),
                Component.text(" §7Z: §e" + loc.getBlockZ())
        )).asGuiItem(event -> event.setCancelled(true)));
        gui.setItem(33, ItemBuilder.from(Material.CLOCK).name(Component.text("§bTime:")).lore(List.of(Component.text("§e " + record.time().toString()))).asGuiItem(event -> event.setCancelled(true)));

        gui.open(player);
    }

    public static void openStored(Player player, DeathRecord record) {
        Gui gui = Gui.gui().title(Component.text("")).rows(5).create();
        GuiItem filler = new GuiItem(ItemBuilder.from(Material.GRAY_STAINED_GLASS_PANE).name(Component.text("§f")).build(), event -> event.setCancelled(true));

        for (int i = 0; i < 5 * 9; i++) {
            gui.setItem(i, filler);
        }

        for (int i = 2; i < 5; i++) {
            for (int j = 2; j < 9; j++) {
                gui.removeItem(i, j);
            }
        }

        List<ItemStack> drops = record.drops();
        for (int i = 0; i < drops.size(); i++) {
            gui.setItem((int) (2 + Math.floor(i/7f)), 2 + i % 7, new GuiItem(drops.get(i).clone()));
        }

        gui.open(player);
    }
}
