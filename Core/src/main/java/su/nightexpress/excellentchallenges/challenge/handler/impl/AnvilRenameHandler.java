package su.nightexpress.excellentchallenges.challenge.handler.impl;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import su.nexmedia.engine.utils.ItemUtil;
import su.nightexpress.excellentchallenges.ExcellentChallenges;
import su.nightexpress.excellentchallenges.challenge.handler.ChallengeHandler;
import su.nightexpress.excellentchallenges.challenge.type.ChallengeJobType;

public class AnvilRenameHandler extends ChallengeHandler {

    public AnvilRenameHandler(@NotNull ExcellentChallenges plugin) {
        super(plugin, ChallengeJobType.ANVIL_RENAME);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onChallengeInventoryHandler(InventoryClickEvent e) {
        Inventory inventory = e.getInventory();
        Player player = (Player) e.getWhoClicked();

        if (inventory.getType() == InventoryType.ANVIL) {
            if (e.getRawSlot() != 2 || e.getClick() == ClickType.MIDDLE) return;

            AnvilInventory aInventory = (AnvilInventory) inventory;
            if (aInventory.getRepairCost() <= 0) return;

            ItemStack src = aInventory.getItem(0);
            if (src == null || src.getType().isAir()) return;

            ItemStack result = aInventory.getItem(2);
            if (result == null || result.getType().isAir()) return;

            String nameSrc = ItemUtil.getItemName(src);
            String nameResult = aInventory.getRenameText();
            if (nameResult == null || nameSrc.equalsIgnoreCase(nameResult)) return;

            plugin.getServer().getScheduler().runTask(plugin, () -> {
                ItemStack result2 = aInventory.getItem(2);
                if (result2 != null && !result2.getType().isAir()) return;

                this.progressChallenge(player, result.getType().name(), result.getAmount());
            });
        }
    }
}
