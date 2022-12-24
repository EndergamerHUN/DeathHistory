package io.github.endergamerhun.deathhistory.command;

import io.github.endergamerhun.deathhistory.events.DeathManager;
import io.github.endergamerhun.deathhistory.inventory.DeathInventory;
import io.github.endergamerhun.deathhistory.utils.DeathRecord;
import io.github.endergamerhun.deathhistory.utils.Util;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

public class CommandHandler implements TabExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!cmd.getName().equalsIgnoreCase("deathhistory")) return true;
        if (args.length < 1) {
            sender.sendMessage("§cPlease provide a subcommand");
            return true;
        }
        if (sender instanceof Player p) {
            switch (args[0].toLowerCase()) {
                case "latest" -> {
                    if (args.length < 2) {
                        sender.sendMessage("§cPlease provide a player.");
                        return true;
                    }
                    OfflinePlayer player = Util.getOfflinePlayer(args[1]);
                    if (player == null) {
                        sender.sendMessage("§cThat is not a valid player");
                        return true;
                    }
                    UUID uuid = player.getUniqueId();
                    if (!DeathManager.hasRecord(uuid)) {
                        sender.sendMessage("§cThere is no record for this player.");
                        return true;
                    }
                    DeathInventory.openMain(p, DeathManager.getLatestRecord(uuid));
                    return true;
                }
                case "open" -> {
                    if (args.length < 2) {
                        sender.sendMessage("§cPlease provide a player.");
                        return true;
                    }
                    DeathRecord record = DeathManager.getRecord(args[1]);
                    if (record == null) {
                        sender.sendMessage("§cThat is not a valid record");
                        return true;
                    }
                    DeathInventory.openMain(p, record);
                    return true;
                }
            }
        }
        switch (args[0].toLowerCase()) {
            case "list" -> {
                List<DeathRecord> records = DeathManager.getRecords();
                sendRecords(records, sender);
                return true;
            }
            case "player" -> {
                if (args.length < 2) {
                    sender.sendMessage("§cPlease provide a player.");
                    return true;
                }
                OfflinePlayer player = Util.getOfflinePlayer(args[1]);
                if (player == null) {
                    sender.sendMessage("§cThat is not a valid player");
                    return true;
                }
                UUID uuid = player.getUniqueId();
                List<DeathRecord> records = DeathManager.getRecords(uuid);
                sendRecords(records, sender);
                return true;
            }
            case "clear" -> {
                if (args.length < 2) {
                    sender.sendMessage("§cPlease provide an argument.");
                    return true;
                }
                if ("*".equals(args[1])) {
                    DeathManager.clear();
                    sender.sendMessage("§aDeleted all recorded deaths.");
                    return true;
                }
                OfflinePlayer player = Util.getOfflinePlayer(args[1]);
                if (player == null) {
                    sender.sendMessage("§cThat is not a valid player");
                    return true;
                }
                UUID uuid = player.getUniqueId();
                DeathManager.clear(uuid);
                sender.sendMessage("§aDeleted records from player §e" + args[1]);
                return true;
            }
        }
        sender.sendMessage(sender instanceof Player ? "§cThis command does not exist!" : "§cThis command can only be executed by players or does not exist!");
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        List<String> completions = new ArrayList<>();
        int arg = args.length;
        switch (arg) {
            case 1 -> completions.addAll(List.of("list","latest","player","open","clear"));
            case 2 -> {
                switch (args[0].toLowerCase()) {
                    case "latest", "player" -> completions.addAll(DeathManager.getPlayerNames());
                    case "open" -> completions.addAll(DeathManager.getRecords().stream().map(DeathRecord::id).toList());
                    case "clear" -> {
                        completions.add("*");
                        completions.addAll(DeathManager.getPlayerNames());
                    }
                }
            }
        }
        return StringUtil.copyPartialMatches(args[arg-1], completions, new ArrayList<>());
    }

    public static void sendRecords(Collection<DeathRecord> records, CommandSender sender) {
        if (records.size() == 0) {
            sender.sendMessage("§cThere are no deaths recorded.");
            return;
        }
        sender.sendMessage("§eSaved death history:");
        records.forEach(record -> {
            TextComponent component = new TextComponent(String.format("§7- §a%s§7 | §2%s§7 | §e%s",
                    record.player().getName(),
                    record.location().getWorld().getName(),
                    new SimpleDateFormat("MM/dd HH:mm:ss").format(record.time())));
            component.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/deathhistory open " + record.id()));
            component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("§eClick to open §7["+record.player().getName()+"]")));
            sender.spigot().sendMessage(component);
        });
    }
}
