package io.github.endergamerhun.deathhistory.utils;

import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;

import java.util.Date;
import java.util.List;

public record DeathRecord(OfflinePlayer player, List<ItemStack> drops, int exp, Location location, Date time) {
    public String id() {
        return player.getUniqueId()+":"+time.getTime();
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (o instanceof String s) {
            return id().equals(s);
        }
        if (!(o instanceof DeathRecord r)) return false;
        return id().equals(r.id());
    }
}
