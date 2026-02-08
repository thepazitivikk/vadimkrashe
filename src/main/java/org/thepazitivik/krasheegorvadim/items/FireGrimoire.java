package org.thepazitivik.krasheegorvadim.items;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.thepazitivik.krasheegorvadim.api.AbstractCustomItem;
import org.thepazitivik.krasheegorvadim.api.ItemBuilder;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class FireGrimoire extends AbstractCustomItem {

    private static final double MIN_RADIUS = 1.5;
    private static final double MAX_RADIUS = 2.0;
    private static final int FIRE_TICKS = 60;

    private final Plugin plugin;
    private final Map<UUID, BukkitTask> activeTasks = new ConcurrentHashMap<>();

    public FireGrimoire(Plugin plugin) {
        super("fire_grimoire");
        this.plugin = plugin;
    }

    @Override
    public ItemStack build() {
        return new ItemBuilder(Material.BOOK)
                .displayName("&cГримуар огня")
                .lore(
                        "&7Книга, пропитанная пламенем",
                        "&7адских глубин",
                        "",
                        "&eВзять в руку &7- поджечь врагов рядом"
                )
                .customId(id)
                .build();
    }

    @Override
    public void onHeld(Player player) {
        UUID uuid = player.getUniqueId();

        if (activeTasks.containsKey(uuid)) return;

        BukkitTask task = new BukkitRunnable() {
            @Override
            public void run() {
                if (!player.isOnline()) {
                    stopEffect(uuid);
                    return;
                }

                Location loc = player.getLocation();
                double radius = (MIN_RADIUS + MAX_RADIUS) / 2;

                spawnFireCircle(loc, radius);

                player.getWorld().getNearbyEntities(loc, MAX_RADIUS, MAX_RADIUS, MAX_RADIUS).stream()
                        .filter(e -> e instanceof LivingEntity && e != player)
                        .map(e -> (LivingEntity) e)
                        .filter(e -> {
                            double dist = e.getLocation().distance(loc);
                            return dist >= MIN_RADIUS && dist <= MAX_RADIUS;
                        })
                        .forEach(e -> e.setFireTicks(FIRE_TICKS));
            }
        }.runTaskTimer(plugin, 0L, 5L);

        activeTasks.put(uuid, task);
    }

    @Override
    public void onUnheld(Player player) {
        stopEffect(player.getUniqueId());
    }

    private void stopEffect(UUID uuid) {
        BukkitTask task = activeTasks.remove(uuid);
        if (task != null) task.cancel();
    }

    private void spawnFireCircle(Location center, double radius) {
        for (int i = 0; i < 12; i++) {
            double angle = Math.toRadians(i * 30);
            double x = radius * Math.cos(angle);
            double z = radius * Math.sin(angle);
            center.getWorld().spawnParticle(Particle.FLAME,
                    center.getX() + x, center.getY() + 0.5, center.getZ() + z,
                    2, 0.1, 0.1, 0.1, 0.01);
        }
    }
}

