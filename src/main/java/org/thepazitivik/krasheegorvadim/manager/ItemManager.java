package org.thepazitivik.krasheegorvadim.manager;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.thepazitivik.krasheegorvadim.api.CustomItem;
import org.thepazitivik.krasheegorvadim.api.ItemBuilder;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ItemManager {

    private final Map<String, CustomItem> registeredItems = new HashMap<>();

    public void register(CustomItem item) {
        registeredItems.put(item.getId(), item);
    }

    public void unregister(String id) {
        registeredItems.remove(id);
    }

    public Optional<CustomItem> getById(String id) {
        return Optional.ofNullable(registeredItems.get(id));
    }

    public Optional<CustomItem> getFromItemStack(ItemStack itemStack) {
        if (itemStack == null || !itemStack.hasItemMeta()) {
            return Optional.empty();
        }

        ItemMeta meta = itemStack.getItemMeta();
        PersistentDataContainer container = meta.getPersistentDataContainer();

        if (!container.has(ItemBuilder.getCustomItemKey(), PersistentDataType.STRING)) {
            return Optional.empty();
        }

        String id = container.get(ItemBuilder.getCustomItemKey(), PersistentDataType.STRING);
        return getById(id);
    }

    public boolean isCustomItem(ItemStack itemStack) {
        return getFromItemStack(itemStack).isPresent();
    }

    public Collection<CustomItem> getAllItems() {
        return registeredItems.values();
    }

    public ItemStack give(String id) {
        return getById(id).map(CustomItem::build).orElse(null);
    }
}

