package org.thepazitivik.krasheegorvadim.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.thepazitivik.krasheegorvadim.manager.ItemManager;

public class PlayerQuitListener implements Listener {

    private final ItemManager itemManager;

    public PlayerQuitListener(ItemManager itemManager) {
        this.itemManager = itemManager;
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        ItemStack mainHand = player.getInventory().getItemInMainHand();

        if (mainHand != null) {
            itemManager.getFromItemStack(mainHand).ifPresent(item -> item.onUnheld(player));
        }
    }
}

