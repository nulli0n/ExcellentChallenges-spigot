package su.nightexpress.excellentchallenges.challenge.handler.impl;

import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.projectiles.ProjectileSource;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentchallenges.ExcellentChallenges;
import su.nightexpress.excellentchallenges.challenge.handler.ChallengeHandler;
import su.nightexpress.excellentchallenges.challenge.type.ChallengeJobType;

public class ProjectileLaunchHandler extends ChallengeHandler {

    public ProjectileLaunchHandler(@NotNull ExcellentChallenges plugin) {
        super(plugin, ChallengeJobType.PROJECTILE_LAUNCH);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onChallengeProjectileLaunch(ProjectileLaunchEvent e) {
        Projectile projectile = e.getEntity();
        ProjectileSource source = projectile.getShooter();
        if (!(source instanceof Player player)) return;

        this.progressChallenge(player, projectile.getType().name(), 1);
    }
}
