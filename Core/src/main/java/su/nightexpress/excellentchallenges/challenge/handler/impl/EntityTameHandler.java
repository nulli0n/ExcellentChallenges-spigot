package su.nightexpress.excellentchallenges.challenge.handler.impl;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityTameEvent;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentchallenges.ExcellentChallenges;
import su.nightexpress.excellentchallenges.challenge.handler.ChallengeHandler;
import su.nightexpress.excellentchallenges.challenge.type.ChallengeJobType;

public class EntityTameHandler extends ChallengeHandler {

    public EntityTameHandler(@NotNull ExcellentChallenges plugin) {
        super(plugin, ChallengeJobType.ENTITY_TAME);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onChallengeEntityTame(EntityTameEvent e) {
        Player player = (Player) e.getOwner();
        LivingEntity entity = e.getEntity();

        this.progressChallenge(player, entity.getType().name(), 1);
    }
}
