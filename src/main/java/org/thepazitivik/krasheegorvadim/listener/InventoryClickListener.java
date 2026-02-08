package org.thepazitivik.krasheegorvadim.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.Plugin;
import org.thepazitivik.krasheegorvadim.manager.ItemManager;

public class InventoryClickListener implements Listener {

    private final ItemManager itemManager;
    private final Plugin plugin;

    public InventoryClickListener(ItemManager itemManager, Plugin plugin) {
        this.itemManager = itemManager;
        this.plugin = plugin;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;
        Player player = (Player) event.getWhoClicked();

        if (event.getClickedInventory() == null) return;
        if (event.getClickedInventory().getType() != InventoryType.PLAYER) return;

        PlayerInventory inventory = player.getInventory();
        int heldSlot = inventory.getHeldItemSlot();

        ItemStack clickedItem = event.getCurrentItem();
        ItemStack cursorItem = event.getCursor();

        if (event.getSlot() == heldSlot && clickedItem != null) {
            itemManager.getFromItemStack(clickedItem).ifPresent(customItem ->
                plugin.getServer().getScheduler().runTask(plugin, () -> customItem.onUnheld(player))
            );
        }

        if (event.getSlot() == heldSlot && cursorItem != null && cursorItem.getType().isItem()) {
            itemManager.getFromItemStack(cursorItem).ifPresent(customItem ->
                plugin.getServer().getScheduler().runTask(plugin, () -> customItem.onHeld(player))
            );
        }

        if (event.getClick().name().contains("NUMBER_KEY")) {
            int hotbarSlot = event.getHotbarButton();
            if (hotbarSlot == heldSlot) {
                ItemStack hotbarItem = inventory.getItem(hotbarSlot);

                if (hotbarItem != null) {
                    itemManager.getFromItemStack(hotbarItem).ifPresent(customItem ->
                        plugin.getServer().getScheduler().runTask(plugin, () -> customItem.onUnheld(player))
                    );
                }

                if (clickedItem != null) {
                    itemManager.getFromItemStack(clickedItem).ifPresent(customItem ->
                        plugin.getServer().getScheduler().runTask(plugin, () -> customItem.onHeld(player))
                    );
                }
            }
        }
    }
}

