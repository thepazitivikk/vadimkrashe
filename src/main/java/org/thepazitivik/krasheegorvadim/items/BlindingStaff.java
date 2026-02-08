package org.thepazitivik.krasheegorvadim.items;

import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.thepazitivik.krasheegorvadim.api.AbstractCustomItem;
import org.thepazitivik.krasheegorvadim.api.ItemBuilder;

public class BlindingStaff extends AbstractCustomItem {

    private static final int BLINDNESS_DURATION = 100; // 5 секунд | измерен. в тиках 5 сек 20 тик
    private static final int BLINDNESS_AMPLIFIER = 0;

    public BlindingStaff() {
        super("blinding_staff");
    }

    @Override
    public ItemStack build() {
        return new ItemBuilder(Material.BLAZE_ROD)
                .displayName("&6Ослепляющий посох")
                .lore(
                        "&7Древний посох, наделенный",
                        "&7силой ослеплять врагов",
                        "",
                        "&eПКМ по существу &7- ослепить"
                )
                .customId(id)
                .build();
    }

    @Override
    public void onInteractAtEntity(Player player, LivingEntity target) {
        target.addPotionEffect(new PotionEffect(
                PotionEffectType.BLINDNESS,
                BLINDNESS_DURATION,
                BLINDNESS_AMPLIFIER,
                false,
                true
        ));
    }
}

