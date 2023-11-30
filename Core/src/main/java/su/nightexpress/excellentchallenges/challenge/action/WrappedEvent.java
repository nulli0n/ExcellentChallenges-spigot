package su.nightexpress.excellentchallenges.challenge.action;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.Listener;
import org.bukkit.plugin.EventExecutor;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentchallenges.ExcellentChallengesPlugin;

public class WrappedEvent<E extends Event, O> implements Listener, EventExecutor, ChallengeProcessor<O> {

    private final ExcellentChallengesPlugin plugin;
    private final Class<E>                  eventClass;
    private final ActionType<E, O> actionType;

    public WrappedEvent(@NotNull ExcellentChallengesPlugin plugin,
                        @NotNull Class<E> eventClass,
                        @NotNull ActionType<E, O> actionType) {
        this.plugin = plugin;
        this.eventClass = eventClass;
        this.actionType = actionType;
    }

    @Override
    public void execute(@NotNull Listener listener, @NotNull Event bukkitEvent) {
        if (!this.eventClass.isAssignableFrom(bukkitEvent.getClass())) return;

        E event = this.eventClass.cast(bukkitEvent);
        this.actionType.getEventHelper().handle(this.plugin, event, this);
    }

    @Override
    public void progressChallenge(@NotNull Player player, @NotNull O object, int amount) {
        if (player.getGameMode() == GameMode.CREATIVE) return;

        this.plugin.getChallengeManager().progressChallenge(player, this.actionType, object, amount);
    }
}
