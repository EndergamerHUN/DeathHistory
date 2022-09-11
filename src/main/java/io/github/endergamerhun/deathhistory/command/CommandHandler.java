package io.github.endergamerhun.deathhistory.command;

import io.github.endergamerhun.deathhistory.events.PlayerDeath;
import io.github.endergamerhun.deathhistory.inventory.DeathInventory;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.List;

public class CommandHandler implements TabExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!cmd.getName().equalsIgnoreCase("deathhistory")) return true;
        if (sender instanceof Player p) {

            if (args.length < 1) {
                p.sendMessage("§2Usage: §6/deathhistory §b<player>");
                return true;
            }

            Player player = Bukkit.getServer().getPlayer(args[0]);

            if (player == null) {
                p.sendMessage("§cThis is not a valid Player!");
                return true;
            }
            if (!PlayerDeath.containsLocation(player.getUniqueId())) {
                p.sendMessage("§cThere is no stored data for this player!");
                return true;
            }

            DeathInventory inv = new DeathInventory(player);
            p.openInventory(inv.getInventory());
            p.sendMessage("§aOpening inventory");
            return true;
        }
        sender.sendMessage("§cThis command can only be executed by a Player!");
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        return null;
    }
}
