package org.thepazitivik.krasheegorvadim.api;

import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.ItemStack;

public interface CustomItem {

    String getId();

    ItemStack build();

    default void onInteract(Player player, Action action) {}

    default void onInteractAtEntity(Player player, LivingEntity target) {}

    default void onDrop(Player player, Item item) {}

    default void onPickup(Player player, ItemStack item) {}

    default void onHeld(Player player) {}

    default void onUnheld(Player player) {}

    default void onBurn(Item item) {}

    default void onItemLand(Item item) {}
}

