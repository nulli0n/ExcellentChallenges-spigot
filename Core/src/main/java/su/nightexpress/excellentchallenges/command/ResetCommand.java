package su.nightexpress.excellentchallenges.command;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentchallenges.ChallengesPlugin;
import su.nightexpress.excellentchallenges.config.Perms;
import su.nightexpress.excellentchallenges.Placeholders;
import su.nightexpress.excellentchallenges.challenge.ChallengeCategory;
import su.nightexpress.excellentchallenges.config.Lang;
import su.nightexpress.nightcore.command.CommandResult;
import su.nightexpress.nightcore.command.impl.AbstractCommand;
import su.nightexpress.nightcore.util.Players;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ResetCommand extends AbstractCommand<ChallengesPlugin> {

    public ResetCommand(@NotNull ChallengesPlugin plugin) {
        super(plugin, new String[]{"reset"}, Perms.COMMAND_RESET);
        this.setDescription(Lang.COMMAND_RESET_DESC);
        this.setUsage(Lang.COMMAND_RESET_USAGE);
    }

    @Override
    @NotNull
    public List<String> getTab(@NotNull Player player, int arg, @NotNull String[] args) {
        if (arg == 1) {
            return Players.playerNames(player);
        }
        if (arg == 2) {
            return this.plugin.getChallengeManager().getCategoryIds();
        }
        return super.getTab(player, arg, args);
    }

    @Override
    protected void onExecute(@NotNull CommandSender sender, @NotNull CommandResult result) {
        if (result.length() < 2) {
            this.errorUsage(sender);
            return;
        }

        Set<ChallengeCategory> categories = new HashSet<>();
        if (result.length() >= 3) {
            ChallengeCategory category = plugin.getChallengeManager().getChallengeCategory(result.getArg(2));
            if (category == null) {
                Lang.ERROR_CATEGORY_INVALID.getMessage().send(sender);
                return;
            }
            categories.add(category);
        }
        else categories.addAll(this.plugin.getChallengeManager().getCategories());

        this.plugin.getUserManager().getUserDataAndPerform(result.getArg(1), user -> {
            if (user == null) {
                this.errorPlayer(sender);
                return;
            }

            for (ChallengeCategory category : categories) {
                user.getChallenges(category).clear();

                Lang.COMMAND_RESET_DONE.getMessage()
                    .replace(Placeholders.GENERIC_TYPE, category.getName())
                    .replace(Placeholders.PLAYER_NAME, user.getName())
                    .send(sender);
            }

            Player player = user.getPlayer();
            if (player != null) {
                plugin.getChallengeManager().updateChallenges(player, false);
            }
        });
    }
}
