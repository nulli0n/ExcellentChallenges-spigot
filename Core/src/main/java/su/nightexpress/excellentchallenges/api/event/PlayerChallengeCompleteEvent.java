package su.nightexpress.excellentchallenges.api.event;

import su.nightexpress.excellentchallenges.challenge.GeneratedChallenge;
import su.nightexpress.excellentchallenges.data.object.ChallengeUser;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class PlayerChallengeCompleteEvent extends PlayerChallengeEvent {

    private static final HandlerList handlerList = new HandlerList();

    public PlayerChallengeCompleteEvent(@NotNull Player player, @NotNull ChallengeUser user, @NotNull GeneratedChallenge progress) {
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
