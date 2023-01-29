package su.nightexpress.excellentchallenges.challenge.handler.impl;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentchallenges.ExcellentChallenges;
import su.nightexpress.excellentchallenges.challenge.handler.ChallengeHandler;
import su.nightexpress.excellentchallenges.challenge.type.ChallengeJobType;

public class ItemDisenchantHandler extends ChallengeHandler {

    public ItemDisenchantHandler(@NotNull ExcellentChallenges plugin) {
        super(plugin, ChallengeJobType.ITEM_DISENCHANT);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onChallengeInventoryHandler(InventoryClickEvent e) {
        Inventory inventory = e.getInventory();
        Player player = (Player) e.getWhoClicked();

        if (inventory.getType() == InventoryType.GRINDSTONE) {
            if (e.getRawSlot() != 2 || e.getClick() == ClickType.MIDDLE) return;

            ItemStack result = inventory.getItem(2);
            if (result == null || result.getType().isAir()) return;

            ItemStack source = inventory.getItem(0);
            if (source == null || result.getType().isAir()) return;

            if (source.getEnchantments().size() == result.getEnchantments().size()) return;

            this.progressChallenge(player, result.getType().name(), 1);
        }
    }
}
