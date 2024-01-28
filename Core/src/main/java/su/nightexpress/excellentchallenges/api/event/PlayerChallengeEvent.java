package su.nightexpress.excellentchallenges.api.event;

import su.nightexpress.excellentchallenges.challenge.GeneratedChallenge;
import su.nightexpress.excellentchallenges.data.object.ChallengeUser;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

public abstract class PlayerChallengeEvent extends Event {

    protected final Player             player;
    protected final ChallengeUser      user;
    protected final GeneratedChallenge challenge;

    public PlayerChallengeEvent(@NotNull Player player, @NotNull ChallengeUser user, @NotNull GeneratedChallenge challenge) {
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
    public GeneratedChallenge getChallenge() {
        return challenge;
    }
}
