package su.nightexpress.excellentchallenges.challenge.handler.impl;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentchallenges.ExcellentChallenges;
import su.nightexpress.excellentchallenges.challenge.handler.ChallengeHandler;
import su.nightexpress.excellentchallenges.challenge.type.ChallengeJobType;

public class ItemConsumeHandler extends ChallengeHandler {

    public ItemConsumeHandler(@NotNull ExcellentChallenges plugin) {
        super(plugin, ChallengeJobType.ITEM_CONSUME);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onChallengeItemConsume(PlayerItemConsumeEvent e) {
        Player player = e.getPlayer();
        ItemStack item = e.getItem();

        this.progressChallenge(player, item.getType().name(), 1);
    }
}
