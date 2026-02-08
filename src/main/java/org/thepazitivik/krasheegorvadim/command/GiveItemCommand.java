package org.thepazitivik.krasheegorvadim.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.thepazitivik.krasheegorvadim.api.CustomItem;
import org.thepazitivik.krasheegorvadim.manager.ItemManager;

import java.util.ArrayList;
import java.util.List;

public class GiveItemCommand implements CommandExecutor, TabCompleter {

    private final ItemManager itemManager;

    public GiveItemCommand(ItemManager itemManager) {
        this.itemManager = itemManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cКоманда только для игроков!");
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("customitems.give")) {
            player.sendMessage("§cУ вас нет прав на использование этой команды!");
            return true;
        }

        if (args.length < 1) {
            player.sendMessage("§cИспользование: /giveitem <id>");
            player.sendMessage("§7Доступные предметы: " + String.join(", ", getItemIds()));
            return true;
        }

        String itemId = args[0].toLowerCase();
        ItemStack item = itemManager.give(itemId);

        if (item == null) {
            player.sendMessage("§cПредмет с ID '" + itemId + "' не найден!");
            return true;
        }

        player.getInventory().addItem(item);
        player.sendMessage("§aВы получили предмет: §e" + itemId);
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            String input = args[0].toLowerCase();
            return getItemIds().stream()
                    .filter(id -> id.startsWith(input))
                    .toList();
        }
        return new ArrayList<>();
    }

    private List<String> getItemIds() {
        return itemManager.getAllItems().stream()
                .map(CustomItem::getId)
                .toList();
    }
}

