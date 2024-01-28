package su.nightexpress.excellentchallenges.challenge.action;

import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentchallenges.ExcellentChallengesPlugin;

public interface EventHelper<E extends Event, O> {

    boolean handle(@NotNull ExcellentChallengesPlugin plugin, @NotNull E event, @NotNull ChallengeProcessor<O> processor);
}
