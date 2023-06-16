package su.nightexpress.excellentchallenges.challenge.handler.impl;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.jetbrains.annotations.NotNull;
import su.nexmedia.engine.utils.EntityUtil;
import su.nightexpress.excellentchallenges.ExcellentChallenges;
import su.nightexpress.excellentchallenges.challenge.handler.ChallengeHandler;
import su.nightexpress.excellentchallenges.challenge.type.ChallengeJobType;

public class DamageHandler extends ChallengeHandler {

    public DamageHandler(@NotNull ExcellentChallenges plugin, @NotNull ChallengeJobType jobType) {
        super(plugin, jobType);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onChallengeDamageReceiveInflict(EntityDamageEvent e) {
        Entity victim = e.getEntity();
        EntityDamageEvent.DamageCause cause = e.getCause();
        double damage = e.getDamage();

        if (victim instanceof Player pVictim && !EntityUtil.isNPC(victim) && this.getJobType() == ChallengeJobType.DAMAGE_RECEIVE) {
            this.progressChallenge(pVictim, cause.name(), (int) damage);
        }
        if (e instanceof EntityDamageByEntityEvent ede) {
            Entity damager = ede.getDamager();
            if (damager instanceof Player pDamager && !EntityUtil.isNPC(damager) && this.getJobType() == ChallengeJobType.DAMAGE_INFLICT) {
                this.progressChallenge(pDamager, cause.name(), (int) damage);
            }
        }
    }
}
