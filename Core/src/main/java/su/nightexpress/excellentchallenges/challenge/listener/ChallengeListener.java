package su.nightexpress.excellentchallenges.challenge.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;
import org.jetbrains.annotations.NotNull;
import su.nexmedia.engine.api.manager.AbstractListener;
import su.nightexpress.excellentchallenges.ExcellentChallenges;
import su.nightexpress.excellentchallenges.api.event.PlayerChallengeCompleteEvent;
import su.nightexpress.excellentchallenges.api.event.PlayerChallengeObjectiveEvent;
import su.nightexpress.excellentchallenges.challenge.Challenge;
import su.nightexpress.excellentchallenges.challenge.ChallengeManager;
import su.nightexpress.excellentchallenges.challenge.ChallengeType;
import su.nightexpress.excellentchallenges.config.Lang;
import su.nightexpress.excellentchallenges.data.object.ChallengeUser;

import java.util.Objects;

public class ChallengeListener extends AbstractListener<ExcellentChallenges> {

    public ChallengeListener(@NotNull ChallengeManager challengeManager) {
        super(challengeManager.plugin());
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onJoin(@NotNull PlayerJoinEvent e) {
        Player player = e.getPlayer();
        plugin.getChallengeManager().updateChallenges(player, false);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onChallengeObjectiveEvent(PlayerChallengeObjectiveEvent e) {
        Player player = e.getPlayer().getPlayer();
        if (player == null) return;

        Challenge progress = e.getChallenge();
        String objId = e.getObjective();
        plugin.getMessage(Lang.CHALLENGE_NOTIFY_PROGRESS)
            .replace(progress.replacePlaceholders(objId))
            .send(player);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onChallengeCompleteEvent(PlayerChallengeCompleteEvent e) {
        Player player = e.getPlayer().getPlayer();
        if (player == null) return;

        Challenge challenge = e.getChallenge();
        ChallengeUser user = e.getUser();
        ChallengeType type = this.plugin.getChallengeManager().getChallengeType(challenge.getTypeId());

        challenge.getRewards().stream()
            .map(rewardId -> plugin.getChallengeManager().getReward(rewardId)).filter(Objects::nonNull)
            .forEach(reward -> reward.give(player));

        user.addCompletedChallenge(challenge);

        if (type != null && user.isAllChallengesCompleted(type)) {
            type.getCompletionRewards().forEach(reward -> reward.give(player));
        }

        plugin.getMessage(Lang.CHALLENGE_NOTIFY_COMPLETED)
            .replace(challenge.replacePlaceholders())
            .send(player);
    }
}
