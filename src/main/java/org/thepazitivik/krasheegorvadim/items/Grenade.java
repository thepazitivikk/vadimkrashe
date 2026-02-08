package org.thepazitivik.krasheegorvadim.items;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.thepazitivik.krasheegorvadim.api.AbstractCustomItem;
import org.thepazitivik.krasheegorvadim.api.ItemBuilder;

public class Grenade extends AbstractCustomItem {

    private static final float EXPLOSION_POWER = 2.0f;

    private final Plugin plugin;

    public Grenade(Plugin plugin) {
        super("grenade");
        this.plugin = plugin;
    }

    @Override
    public ItemStack build() {
        return new ItemBuilder(Material.TNT)
                .displayName("&cГраната")
                .lore(
                        "&7Взрывчатка, готовая",
                        "&7к применению",
                        "",
                        "&eВыбросить &7- создать взрыв"
                )
                .customId(id)
                .build();
    }

    @Override
    public void onDrop(Player player, Item item) {
        new BukkitRunnable() { // id 2
            @Override
            public void run() {
                if (!item.isValid() || item.isDead()) {
                    cancel();
                    return;
                }

                if (item.isOnGround()) {
                    Location loc = item.getLocation();
                    item.remove();
                    loc.getWorld().createExplosion(loc, EXPLOSION_POWER, false, true);
                    cancel();
                }
            }
        }.runTaskTimer(plugin, 5L, 1L);
    }
}

