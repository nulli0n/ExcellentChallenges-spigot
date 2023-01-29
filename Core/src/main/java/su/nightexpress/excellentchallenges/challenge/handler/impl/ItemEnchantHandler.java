package su.nightexpress.excellentchallenges.challenge.handler.impl;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentchallenges.ExcellentChallenges;
import su.nightexpress.excellentchallenges.challenge.handler.ChallengeHandler;
import su.nightexpress.excellentchallenges.challenge.type.ChallengeJobType;

public class ItemEnchantHandler extends ChallengeHandler {

    public ItemEnchantHandler(@NotNull ExcellentChallenges plugin) {
        super(plugin, ChallengeJobType.ITEM_ENCHANT);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onChallengeItemEnchant(EnchantItemEvent e) {
        Player player = e.getEnchanter();
        e.getEnchantsToAdd().forEach((en, lvl) -> {
            this.progressChallenge(player, en.getKey().getKey(), 1);
        });
    }
}
