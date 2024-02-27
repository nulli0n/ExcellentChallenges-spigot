package su.nightexpress.excellentchallenges.command;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentchallenges.ExcellentChallengesPlugin;
import su.nightexpress.excellentchallenges.config.Perms;
import su.nightexpress.excellentchallenges.command.tokens.TokensGiveCommand;
import su.nightexpress.excellentchallenges.command.tokens.TokensRemoveCommand;
import su.nightexpress.excellentchallenges.command.tokens.TokensSetCommand;
import su.nightexpress.excellentchallenges.config.Lang;
import su.nightexpress.nightcore.command.CommandResult;
import su.nightexpress.nightcore.command.base.HelpSubCommand;
import su.nightexpress.nightcore.command.impl.PluginCommand;

public class RerollTokensCommand extends PluginCommand<ExcellentChallengesPlugin> {

    public RerollTokensCommand(@NotNull ExcellentChallengesPlugin plugin) {
        super(plugin, new String[]{"rerolltokens", "rtokens"}, Perms.COMMAND_REROLL_TOKENS);
        this.setDescription(Lang.COMMAND_REROLL_TOKENS_DESC);
        this.setUsage(Lang.COMMAND_REROLL_TOKENS_USAGE);

        this.addDefaultCommand(new HelpSubCommand(plugin));
        this.addChildren(new TokensGiveCommand(plugin));
        this.addChildren(new TokensRemoveCommand(plugin));
        this.addChildren(new TokensSetCommand(plugin));
    }

    @Override
    protected void onExecute(@NotNull CommandSender sender, @NotNull CommandResult result) {

    }
}
