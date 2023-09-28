package su.nightexpress.excellentchallenges.api.event;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentchallenges.challenge.Challenge;
import su.nightexpress.excellentchallenges.data.object.ChallengeUser;

public class PlayerChallengeObjectiveEvent extends PlayerChallengeEvent {

    private static final HandlerList handlerList = new HandlerList();

    protected final String objective;
    protected final int amount;

    public PlayerChallengeObjectiveEvent(
        @NotNull Player player, @NotNull ChallengeUser user, @NotNull Challenge progress,
        @NotNull String objective, int amount) {
        super(player, user, progress);

        this.objective = objective.toUpperCase();
        this.amount = amount;
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

    @NotNull
    public String getObjective() {
        return objective;
    }

    public int getAmount() {
        return amount;
    }
}
