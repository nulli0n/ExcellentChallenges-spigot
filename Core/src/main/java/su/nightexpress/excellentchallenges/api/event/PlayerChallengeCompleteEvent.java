package su.nightexpress.excellentchallenges.api.event;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentchallenges.challenge.Challenge;
import su.nightexpress.excellentchallenges.data.object.ChallengeUser;

public class PlayerChallengeCompleteEvent extends PlayerChallengeEvent {

    private static final HandlerList handlerList = new HandlerList();

    public PlayerChallengeCompleteEvent(@NotNull Player player, @NotNull ChallengeUser user, @NotNull Challenge progress) {
        super(player, user, progress);
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }
}
