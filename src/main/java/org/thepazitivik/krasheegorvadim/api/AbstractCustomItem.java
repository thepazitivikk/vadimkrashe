package org.thepazitivik.krasheegorvadim.api;

import org.bukkit.inventory.ItemStack;

public abstract class AbstractCustomItem implements CustomItem {

    protected final String id;

    protected AbstractCustomItem(String id) {
        this.id = id;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public abstract ItemStack build();
}

