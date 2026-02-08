package org.thepazitivik.krasheegorvadim.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import org.thepazitivik.krasheegorvadim.manager.ItemManager;

public class ItemHeldListener implements Listener {

    private final ItemManager itemManager;

    public ItemHeldListener(ItemManager itemManager) {
        this.itemManager = itemManager;
    }

    @EventHandler
    public void onItemHeld(PlayerItemHeldEvent event) {
        Player player = event.getPlayer();

        ItemStack previousItem = player.getInventory().getItem(event.getPreviousSlot());
        if (previousItem != null) {
            itemManager.getFromItemStack(previousItem).ifPresent(item -> item.onUnheld(player));
        }

        ItemStack newItem = player.getInventory().getItem(event.getNewSlot());
        if (newItem != null) {
            itemManager.getFromItemStack(newItem).ifPresent(item -> item.onHeld(player));
        }
    }
}

