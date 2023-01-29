package su.nightexpress.excellentchallenges.data;

import org.jetbrains.annotations.NotNull;
import su.nexmedia.engine.api.data.AbstractUserManager;
import su.nightexpress.excellentchallenges.ExcellentChallenges;
import su.nightexpress.excellentchallenges.data.object.ChallengeUser;

import java.util.UUID;

public class UserManager extends AbstractUserManager<ExcellentChallenges, ChallengeUser> {

    public UserManager(@NotNull ExcellentChallenges plugin) {
        super(plugin, plugin);
    }

    @Override
    @NotNull
    protected ChallengeUser createData(@NotNull UUID uuid, @NotNull String name) {
        return new ChallengeUser(plugin, uuid, name);
    }
}
