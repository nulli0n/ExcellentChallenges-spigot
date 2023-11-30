package su.nightexpress.excellentchallenges.data;

import su.nightexpress.excellentchallenges.data.object.ChallengeUser;
import org.jetbrains.annotations.NotNull;
import su.nexmedia.engine.api.data.AbstractUserManager;
import su.nightexpress.excellentchallenges.ExcellentChallengesPlugin;

import java.util.UUID;

public class UserManager extends AbstractUserManager<ExcellentChallengesPlugin, ChallengeUser> {

    public UserManager(@NotNull ExcellentChallengesPlugin plugin) {
        super(plugin, plugin);
    }

    @Override
    @NotNull
    protected ChallengeUser createData(@NotNull UUID uuid, @NotNull String name) {
        return new ChallengeUser(plugin, uuid, name);
    }
}
