package su.nightexpress.excellentchallenges.challenge.handler.impl;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerShearEntityEvent;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentchallenges.ExcellentChallenges;
import su.nightexpress.excellentchallenges.challenge.handler.ChallengeHandler;
import su.nightexpress.excellentchallenges.challenge.type.ChallengeJobType;

public class EntityShearHandler extends ChallengeHandler {

    public EntityShearHandler(@NotNull ExcellentChallenges plugin) {
        super(plugin, ChallengeJobType.ENTITY_SHEAR);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onChallengeEntityShear(PlayerShearEntityEvent e) {
        Player player = e.getPlayer();
        Entity entity = e.getEntity();

        this.progressChallenge(player, entity.getType().name(), 1);
    }
}
