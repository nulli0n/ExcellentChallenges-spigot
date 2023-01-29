package su.nightexpress.excellentchallenges.challenge.handler.impl;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityBreedEvent;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentchallenges.ExcellentChallenges;
import su.nightexpress.excellentchallenges.challenge.handler.ChallengeHandler;
import su.nightexpress.excellentchallenges.challenge.type.ChallengeJobType;

public class EntityBreedHandler extends ChallengeHandler {

    public EntityBreedHandler(@NotNull ExcellentChallenges plugin) {
        super(plugin, ChallengeJobType.ENTITY_BREED);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onChallengeEntityBreed(EntityBreedEvent e) {
        LivingEntity breeder = e.getBreeder();
        if (!(breeder instanceof Player player)) return;

        this.progressChallenge(player, e.getEntity().getType().name(), 1);
    }
}
