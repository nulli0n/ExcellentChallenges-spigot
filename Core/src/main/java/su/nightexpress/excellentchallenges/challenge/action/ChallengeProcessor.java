package su.nightexpress.excellentchallenges.challenge.action;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public interface ChallengeProcessor<O> {

    void progressChallenge(@NotNull Player player, @NotNull O object, int amount);
}
