package su.nightexpress.excellentchallenges.challenge.handler.impl;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.jetbrains.annotations.NotNull;
import su.nexmedia.engine.hooks.Hooks;
import su.nexmedia.engine.hooks.external.MythicMobsHook;
import su.nexmedia.engine.utils.PDCUtil;
import su.nightexpress.excellentchallenges.ExcellentChallenges;
import su.nightexpress.excellentchallenges.Keys;
import su.nightexpress.excellentchallenges.challenge.handler.ChallengeHandler;
import su.nightexpress.excellentchallenges.challenge.type.ChallengeJobType;
import su.nightexpress.excellentchallenges.config.Config;

public class EntityKillHandler extends ChallengeHandler {

    public EntityKillHandler(@NotNull ExcellentChallenges plugin) {
        super(plugin, ChallengeJobType.ENTITY_KILL);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onChallengeEntitySpawn(CreatureSpawnEvent e) {
        CreatureSpawnEvent.SpawnReason reason = e.getSpawnReason();
        if (!Config.OBJECTIVES_ANTI_GLITCH_ENTITY_SPAWN_REASONS.get().contains(reason)) {
            PDCUtil.setData(e.getEntity(), Keys.ENTITY_TRACKED, true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onChallengeEntityKill(EntityDeathEvent e) {
        LivingEntity entity = e.getEntity();
        if (PDCUtil.getBooleanData(entity, Keys.ENTITY_TRACKED)) return;

        Player killer = entity.getKiller();
        if (killer == null || Hooks.isCitizensNPC(killer)) return;

        // Do not count MythicMobs here.
        if (Hooks.hasMythicMobs() && MythicMobsHook.isMythicMob(entity)) return;

        this.progressChallenge(killer, entity.getType().name(), 1);
    }
}
