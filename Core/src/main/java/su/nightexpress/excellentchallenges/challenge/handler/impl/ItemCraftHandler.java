package su.nightexpress.excellentchallenges.challenge.handler.impl;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import su.nexmedia.engine.utils.PlayerUtil;
import su.nightexpress.excellentchallenges.ExcellentChallenges;
import su.nightexpress.excellentchallenges.challenge.handler.ChallengeHandler;
import su.nightexpress.excellentchallenges.challenge.type.ChallengeJobType;

public class ItemCraftHandler extends ChallengeHandler {

    public ItemCraftHandler(@NotNull ExcellentChallenges plugin) {
        super(plugin, ChallengeJobType.ITEM_CRAFT);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onChallengeItemCraft(CraftItemEvent e) {
        if (e.getClick() == ClickType.MIDDLE) return;

        ItemStack item = e.getCurrentItem();
        if (item == null || item.getType().isAir()) return;

        Player player = (Player) e.getWhoClicked();
        ItemStack craft = new ItemStack(item);
        String type = craft.getType().name();

        // Идеальный вариант
        // Считаем до, считаем после, разницу записываем в прогресс хД
        if (e.isShiftClick()) {
            int has = PlayerUtil.countItem(player, craft);
            this.plugin.getServer().getScheduler().runTask(plugin, () -> {
                int now = PlayerUtil.countItem(player, craft);
                int crafted = now - has;
                this.progressChallenge(player, type, crafted);
            });
        }
        else {
            ItemStack cursor = e.getCursor();
            if (cursor != null && !cursor.getType().isAir() && (!cursor.isSimilar(craft) || cursor.getAmount() >= cursor.getMaxStackSize()))
                return;

            this.progressChallenge(player, type, 1);
        }
    }
}
