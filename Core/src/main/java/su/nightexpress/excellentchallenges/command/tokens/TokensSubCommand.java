package su.nightexpress.excellentchallenges.command.tokens;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentchallenges.ExcellentChallengesPlugin;
import su.nightexpress.excellentchallenges.Placeholders;
import su.nightexpress.excellentchallenges.challenge.ChallengeCategory;
import su.nightexpress.excellentchallenges.config.Config;
import su.nightexpress.excellentchallenges.config.Lang;
import su.nightexpress.excellentchallenges.data.object.ChallengeUser;
import su.nightexpress.nightcore.command.CommandResult;
import su.nightexpress.nightcore.command.impl.AbstractCommand;
import su.nightexpress.nightcore.language.entry.LangText;
import su.nightexpress.nightcore.util.Players;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class TokensSubCommand extends AbstractCommand<ExcellentChallengesPlugin> {

    protected LangText doneMessage;

    public TokensSubCommand(@NotNull ExcellentChallengesPlugin plugin, @NotNull String[] aliases, @NotNull Permission permission) {
        super(plugin, aliases, permission);
        this.setUsage(Lang.COMMAND_REROLL_TOKENS_SUB_USAGE);
    }

    public void setDoneMessage(@NotNull LangText doneMessage) {
        this.doneMessage = doneMessage;
    }

    @Override
    @NotNull
    public List<String> getTab(@NotNull Player player, int arg, @NotNull String[] args) {
        if (arg == 2) {
            return Players.playerNames(player);
        }
        if (arg == 3) {
            return new ArrayList<>(Config.CATEGORIES.get().keySet());
        }
        if (arg == 4) {
            return Arrays.asList("0", "1", "5", "25", "100");
        }
        return super.getTab(player, arg, args);
    }

    protected abstract void editTokens(@NotNull ChallengeUser user, @NotNull ChallengeCategory category, int amount);

    @Override
    protected void onExecute(@NotNull CommandSender sender, @NotNull CommandResult result) {
        if (result.length() < 5) {
            this.errorUsage(sender);
            return;
        }

        ChallengeCategory category = plugin.getChallengeManager().getChallengeType(result.getArg(3));
        if (category == null) {
            Lang.ERROR_CATEGORY_INVALID.getMessage().send(sender);
            return;
        }

        int amount = result.getInt(4, 0);
        if (amount == 0) {
            this.errorNumber(sender, result.getArg(4));
            return;
        }

        this.plugin.getUserManager().getUserDataAndPerformAsync(result.getArg(2), user -> {
            if (user == null) {
                this.errorPlayer(sender);
                return;
            }

            this.editTokens(user, category, amount);
            this.plugin.getUserManager().save(user);

            this.doneMessage.getMessage()
                .replace(Placeholders.PLAYER_NAME, user.getName())
                .replace(Placeholders.GENERIC_AMOUNT, String.valueOf(amount))
                .replace(Placeholders.GENERIC_TYPE, category.getName())
                .send(sender);
        });
    }
}
