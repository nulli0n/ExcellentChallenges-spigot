package su.nightexpress.excellentchallenges.command;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.jetbrains.annotations.NotNull;
import su.nexmedia.engine.api.command.AbstractCommand;
import su.nexmedia.engine.api.command.CommandResult;
import su.nexmedia.engine.api.command.GeneralCommand;
import su.nexmedia.engine.api.lang.LangMessage;
import su.nexmedia.engine.command.list.HelpSubCommand;
import su.nexmedia.engine.utils.CollectionsUtil;
import su.nightexpress.excellentchallenges.ExcellentChallenges;
import su.nightexpress.excellentchallenges.Perms;
import su.nightexpress.excellentchallenges.Placeholders;
import su.nightexpress.excellentchallenges.challenge.ChallengeType;
import su.nightexpress.excellentchallenges.config.Config;
import su.nightexpress.excellentchallenges.config.Lang;
import su.nightexpress.excellentchallenges.data.object.ChallengeUser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RerollTokensCommand extends GeneralCommand<ExcellentChallenges> {

    public RerollTokensCommand(@NotNull ExcellentChallenges plugin) {
        super(plugin, new String[]{"rerolltokens", "retok"}, Perms.COMMAND_REROLL_TOKENS);
        this.setDescription(plugin.getMessage(Lang.COMMAND_REROLL_TOKENS_DESC));
        this.setUsage(plugin.getMessage(Lang.COMMAND_REROLL_TOKENS_USAGE));

        this.addDefaultCommand(new HelpSubCommand<>(plugin));
        this.addChildren(new SubCommand(plugin, Mode.GIVE, new String[]{"give"}, Perms.COMMAND_REROLL_TOKENS_GIVE));
        this.addChildren(new SubCommand(plugin, Mode.TAKE, new String[]{"take"}, Perms.COMMAND_REROLL_TOKENS_TAKE));
        this.addChildren(new SubCommand(plugin, Mode.SET, new String[]{"set"}, Perms.COMMAND_REROLL_TOKENS_SET));
    }

    @Override
    protected void onExecute(@NotNull CommandSender sender, @NotNull CommandResult result) {

    }

    enum Mode {
        GIVE, TAKE, SET
    }

    static class SubCommand extends AbstractCommand<ExcellentChallenges> {

        private final Mode mode;

        public SubCommand(@NotNull ExcellentChallenges plugin, @NotNull Mode mode, @NotNull String[] aliases, @NotNull Permission permission) {
            super(plugin, aliases, permission);
            this.mode = mode;
        }

        @Override
        @NotNull
        public String getUsage() {
            return plugin.getMessage(Lang.COMMAND_REROLL_TOKENS_SUB_USAGE).getLocalized();
        }

        @Override
        @NotNull
        public String getDescription() {
            return switch (this.mode) {
                case GIVE -> plugin.getMessage(Lang.COMMAND_REROLL_TOKENS_GIVE_DESC).getLocalized();
                case TAKE -> plugin.getMessage(Lang.COMMAND_REROLL_TOKENS_TAKE_DESC).getLocalized();
                case SET -> plugin.getMessage(Lang.COMMAND_REROLL_TOKENS_SET_DESC).getLocalized();
            };
        }

        @Override
        public boolean isPlayerOnly() {
            return false;
        }

        @Override
        @NotNull
        public List<String> getTab(@NotNull Player player, int arg, @NotNull String[] args) {
            if (arg == 2) {
                return CollectionsUtil.playerNames(player);
            }
            if (arg == 3) {
                return new ArrayList<>(Config.CHALLENGES_TYPES.get().keySet());
            }
            if (arg == 4) {
                return Arrays.asList("0", "1", "5", "25", "100");
            }
            return super.getTab(player, arg, args);
        }

        @Override
        protected void onExecute(@NotNull CommandSender sender, @NotNull CommandResult result) {
            if (result.length() < 5) {
                this.printUsage(sender);
                return;
            }

            ChallengeUser user = plugin.getUserManager().getUserData(result.getArg(2));
            if (user == null) {
                this.errorPlayer(sender);
                return;
            }

            ChallengeType challengeType = plugin.getChallengeManager().getChallengeType(result.getArg(3));
            if (challengeType == null) {

                return;
            }

            int amount = result.getInt(4, 0);
            if (amount == 0) {
                this.errorNumber(sender, result.getArg(4));
                return;
            }

            LangMessage message = switch (this.mode) {
                case GIVE -> {
                    user.addRerollTokens(challengeType, amount);
                    yield plugin.getMessage(Lang.COMMAND_REROLL_TOKENS_GIVE_DONE);
                }
                case TAKE -> {
                    user.takeRerollTokens(challengeType, amount);
                    yield plugin.getMessage(Lang.COMMAND_REROLL_TOKENS_TAKE_DONE);
                }
                case SET -> {
                    user.setRerollTokens(challengeType, amount);
                    yield plugin.getMessage(Lang.COMMAND_REROLL_TOKENS_SET_DONE);
                }
            };
            this.plugin.getUserManager().saveUser(user);

            message
                .replace(Placeholders.PLAYER_NAME, user.getName())
                .replace(Placeholders.GENERIC_AMOUNT, String.valueOf(amount))
                .replace(Placeholders.GENERIC_TYPE, challengeType.getName())
                .send(sender);
        }
    }

}
