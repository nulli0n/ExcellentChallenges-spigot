package su.nightexpress.excellentchallenges.challenge.action;

import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentchallenges.ChallengesPlugin;

public interface EventHelper<E extends Event, O> {

    boolean handle(@NotNull ChallengesPlugin plugin, @NotNull E event, @NotNull ChallengeProcessor<O> processor);
}
