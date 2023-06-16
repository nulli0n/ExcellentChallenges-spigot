package su.nightexpress.excellentchallenges.challenge.handler.impl;

import io.lumine.mythic.bukkit.events.MythicMobDeathEvent;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.jetbrains.annotations.NotNull;
import su.nexmedia.engine.utils.EntityUtil;
import su.nightexpress.excellentchallenges.ExcellentChallenges;
import su.nightexpress.excellentchallenges.challenge.handler.ChallengeHandler;
import su.nightexpress.excellentchallenges.challenge.type.ChallengeJobType;

public class EntityKillMythicHandler extends ChallengeHandler {

    public EntityKillMythicHandler(@NotNull ExcellentChallenges plugin) {
        super(plugin, ChallengeJobType.ENTITY_KILL_MYTHIC);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onChallengeEntityKillMythic(MythicMobDeathEvent e) {
        LivingEntity killer = e.getKiller();
        if (!(killer instanceof Player player)) return;
        if (EntityUtil.isNPC(player)) return;

        String mobId = e.getMob().getType().getInternalName();
        this.progressChallenge(player, mobId, 1);
    }
}
