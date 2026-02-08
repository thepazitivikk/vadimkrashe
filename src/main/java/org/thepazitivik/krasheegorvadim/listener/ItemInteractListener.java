package org.thepazitivik.krasheegorvadim.listener;

import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.thepazitivik.krasheegorvadim.manager.ItemManager;

public class ItemInteractListener implements Listener {

    private final ItemManager itemManager;

    public ItemInteractListener(ItemManager itemManager) {
        this.itemManager = itemManager;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.getItem() == null) return;
        if (event.getHand() != EquipmentSlot.HAND) return;

        itemManager.getFromItemStack(event.getItem()).ifPresent(customItem ->
            customItem.onInteract(event.getPlayer(), event.getAction())
        );
    }

    @EventHandler
    public void onInteractEntity(PlayerInteractEntityEvent event) {
        if (event.getHand() != EquipmentSlot.HAND) return;

        Player player = event.getPlayer();
        itemManager.getFromItemStack(player.getInventory().getItemInMainHand()).ifPresent(customItem -> {
            if (event.getRightClicked() instanceof LivingEntity) {
                LivingEntity target = (LivingEntity) event.getRightClicked();
                customItem.onInteractAtEntity(player, target);
            }
        });
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        Item droppedItem = event.getItemDrop();
        itemManager.getFromItemStack(droppedItem.getItemStack()).ifPresent(customItem ->
            customItem.onDrop(event.getPlayer(), droppedItem)
        );
    }

    @EventHandler
    public void onPickup(EntityPickupItemEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        Player player = (Player) event.getEntity();

        itemManager.getFromItemStack(event.getItem().getItemStack()).ifPresent(customItem ->
            customItem.onPickup(player, event.getItem().getItemStack())
        );
    }
}


