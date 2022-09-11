package io.github.endergamerhun.deathhistory;

import io.github.endergamerhun.deathhistory.command.CommandHandler;
import io.github.endergamerhun.deathhistory.events.PlayerDeath;
import io.github.endergamerhun.deathhistory.utils.Util;
import org.bukkit.plugin.java.JavaPlugin;

@SuppressWarnings("unused")
public class DeathHistory extends JavaPlugin {

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new PlayerDeath(), this);
        getCommand("deathhistory").setExecutor(new CommandHandler());
        Util.log("Plugin loaded");
    }
    @Override
    public void onDisable() {
        Util.log("Plugin unloaded");
    }
}
