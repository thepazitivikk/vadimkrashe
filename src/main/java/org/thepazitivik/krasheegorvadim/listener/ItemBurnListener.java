package org.thepazitivik.krasheegorvadim.listener;

import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.ItemMergeEvent;
import org.thepazitivik.krasheegorvadim.manager.ItemManager;

public class ItemBurnListener implements Listener {

    private final ItemManager itemManager;

    public ItemBurnListener(ItemManager itemManager) {
        this.itemManager = itemManager;
    }

    @EventHandler
    public void onBurn(EntityCombustEvent event) {
        if (!(event.getEntity() instanceof Item)) return;
        Item item = (Item) event.getEntity();

        itemManager.getFromItemStack(item.getItemStack()).ifPresent(customItem ->
            customItem.onBurn(item)
        );
    }

    @EventHandler
    public void onMerge(ItemMergeEvent event) {
        if (itemManager.isCustomItem(event.getEntity().getItemStack()) ||
            itemManager.isCustomItem(event.getTarget().getItemStack())) {
            event.setCancelled(true);
        }
    }
}

