package su.nightexpress.excellentchallenges.command.tokens;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentchallenges.ExcellentChallengesPlugin;
import su.nightexpress.excellentchallenges.config.Perms;
import su.nightexpress.excellentchallenges.challenge.ChallengeCategory;
import su.nightexpress.excellentchallenges.config.Lang;
import su.nightexpress.excellentchallenges.data.object.ChallengeUser;

public class TokensSetCommand extends TokensSubCommand {

    public TokensSetCommand(@NotNull ExcellentChallengesPlugin plugin) {
        super(plugin, new String[]{"set"}, Perms.COMMAND_REROLL_TOKENS_SET);
        this.setDescription(Lang.COMMAND_REROLL_TOKENS_SET_DESC);
        this.setDoneMessage(Lang.COMMAND_REROLL_TOKENS_SET_DONE);
    }

    @Override
    protected void editTokens(@NotNull ChallengeUser user, @NotNull ChallengeCategory category, int amount) {
        user.setRerollTokens(category, amount);
    }
}
