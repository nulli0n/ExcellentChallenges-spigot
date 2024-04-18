package su.nightexpress.excellentchallenges.command.tokens;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentchallenges.ChallengesPlugin;
import su.nightexpress.excellentchallenges.config.Perms;
import su.nightexpress.excellentchallenges.challenge.ChallengeCategory;
import su.nightexpress.excellentchallenges.config.Lang;
import su.nightexpress.excellentchallenges.data.object.ChallengeUser;

public class TokensRemoveCommand extends TokensSubCommand {

    public TokensRemoveCommand(@NotNull ChallengesPlugin plugin) {
        super(plugin, new String[]{"remove", "take"}, Perms.COMMAND_REROLL_TOKENS_REMOVE);
        this.setDescription(Lang.COMMAND_REROLL_TOKENS_REMOVE_DESC);
        this.setDoneMessage(Lang.COMMAND_REROLL_TOKENS_REMOVE_DONE);
    }

    @Override
    protected void editTokens(@NotNull ChallengeUser user, @NotNull ChallengeCategory category, int amount) {
        user.removeRerollTokens(category, amount);
    }
}
