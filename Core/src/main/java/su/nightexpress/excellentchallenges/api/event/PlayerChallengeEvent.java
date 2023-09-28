package su.nightexpress.excellentchallenges.api.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentchallenges.challenge.Challenge;
import su.nightexpress.excellentchallenges.data.object.ChallengeUser;

public abstract class PlayerChallengeEvent extends Event {

    protected final Player        player;
    protected final ChallengeUser user;
    protected final Challenge     challenge;

    public PlayerChallengeEvent(@NotNull Player player, @NotNull ChallengeUser user, @NotNull Challenge challenge) {
        this.player = player;
        this.user = user;
        this.challenge = challenge;
    }

    @NotNull
    public Player getPlayer() {
        return player;
    }

    @NotNull
    public ChallengeUser getUser() {
        return user;
    }

    @NotNull
    public Challenge getChallenge() {
        return challenge;
    }
}
