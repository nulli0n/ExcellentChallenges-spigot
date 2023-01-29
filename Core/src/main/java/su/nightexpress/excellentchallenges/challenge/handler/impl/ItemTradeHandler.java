package su.nightexpress.excellentchallenges.challenge.handler.impl;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantInventory;
import org.bukkit.inventory.MerchantRecipe;
import org.jetbrains.annotations.NotNull;
import su.nexmedia.engine.utils.PlayerUtil;
import su.nightexpress.excellentchallenges.ExcellentChallenges;
import su.nightexpress.excellentchallenges.challenge.handler.ChallengeHandler;
import su.nightexpress.excellentchallenges.challenge.type.ChallengeJobType;

public class ItemTradeHandler extends ChallengeHandler {

    // TODO Piglin support

    public ItemTradeHandler(@NotNull ExcellentChallenges plugin) {
        super(plugin, ChallengeJobType.ITEM_TRADE);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onChallengeInventoryHandler(InventoryClickEvent e) {
        Inventory inventory = e.getInventory();
        Player player = (Player) e.getWhoClicked();
        if (inventory.getType() == InventoryType.MERCHANT) {
            MerchantInventory mInventory = (MerchantInventory) inventory;

            MerchantRecipe recipe = mInventory.getSelectedRecipe();
            if (recipe == null) return;

            ItemStack result = recipe.getResult();
            int uses = recipe.getUses();
            int userHas = PlayerUtil.countItem(player, result);

            plugin.getServer().getScheduler().runTask(plugin, () -> {
                int uses2 = recipe.getUses();
                if (uses2 <= uses) return;

                int amount = 1;
                if (e.isShiftClick()) {
                    int resultSize = result.getAmount();
                    int userNow = PlayerUtil.countItem(player, result);
                    int diff = userNow - userHas;
                    amount = (int) ((double) diff / (double) resultSize);
                }

                this.progressChallenge(player, result.getType().name(), amount);
            });
        }
    }
}
