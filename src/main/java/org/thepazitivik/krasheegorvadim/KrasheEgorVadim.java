package org.thepazitivik.krasheegorvadim;

import org.bukkit.plugin.java.JavaPlugin;
import org.thepazitivik.krasheegorvadim.api.ItemBuilder;
import org.thepazitivik.krasheegorvadim.command.GiveItemCommand;
import org.thepazitivik.krasheegorvadim.database.DatabaseManager;
import org.thepazitivik.krasheegorvadim.items.BlindingStaff;
import org.thepazitivik.krasheegorvadim.items.FireGrimoire;
import org.thepazitivik.krasheegorvadim.items.Grenade;
import org.thepazitivik.krasheegorvadim.listener.*;
import org.thepazitivik.krasheegorvadim.manager.ItemManager;

public final class KrasheEgorVadim extends JavaPlugin {

    private ItemManager itemManager;
    private DatabaseManager databaseManager;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        ItemBuilder.init(this);

        itemManager = new ItemManager();

        databaseManager = new DatabaseManager(this);
        databaseManager.initialize();

        registerItems();

        registerListeners();

        registerCommands();

        getLogger().info("Кастом апи запущен");
    }

    @Override
    public void onDisable() {
        if (databaseManager != null) {
            databaseManager.shutdown();
        }
        getLogger().info("Плагин успешно выключен.");
    }

    private void registerItems() {
        itemManager.register(new BlindingStaff());
        itemManager.register(new FireGrimoire(this));
        itemManager.register(new Grenade(this));
    }

    private void registerListeners() {
        getServer().getPluginManager().registerEvents(new ItemInteractListener(itemManager), this);
        getServer().getPluginManager().registerEvents(new ItemHeldListener(itemManager), this);
        getServer().getPluginManager().registerEvents(new ItemBurnListener(itemManager), this);
        getServer().getPluginManager().registerEvents(new InventoryClickListener(itemManager, this), this);
        getServer().getPluginManager().registerEvents(new PlayerQuitListener(itemManager), this);
    }

    private void registerCommands() {
        GiveItemCommand giveItemCommand = new GiveItemCommand(itemManager);
        getCommand("giveitem").setExecutor(giveItemCommand);
        getCommand("giveitem").setTabCompleter(giveItemCommand);
    }

    public ItemManager getItemManager() {
        return itemManager;
    }

    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }
}
