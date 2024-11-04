package su.nightexpress.excellentchallenges.command;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentchallenges.ChallengesPlugin;
import su.nightexpress.excellentchallenges.Placeholders;
import su.nightexpress.excellentchallenges.challenge.ChallengeCategory;
import su.nightexpress.excellentchallenges.config.Lang;
import su.nightexpress.excellentchallenges.config.Perms;
import su.nightexpress.excellentchallenges.data.object.ChallengeUser;
import su.nightexpress.nightcore.command.CommandResult;
import su.nightexpress.nightcore.command.impl.AbstractCommand;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ResetAllCommand extends AbstractCommand<ChallengesPlugin> {

    public ResetAllCommand(@NotNull ChallengesPlugin plugin) {
        super(plugin, new String[]{"resetall"}, Perms.COMMAND_RESET);
        this.setDescription(Lang.COMMAND_RESET_ALL_DESC);
        this.setUsage(Lang.COMMAND_RESET_ALL_USAGE);
    }

    @Override
    @NotNull
    public List<String> getTab(@NotNull Player player, int arg, @NotNull String[] args) {
        if (arg == 1) {
            return this.plugin.getChallengeManager().getCategoryIds();
        }
        return super.getTab(player, arg, args);
    }

    @Override
    protected void onExecute(@NotNull CommandSender sender, @NotNull CommandResult result) {
        Set<ChallengeCategory> categories = new HashSet<>();
        if (result.length() >= 2) {
            ChallengeCategory category = plugin.getChallengeManager().getChallengeCategory(result.getArg(1));
            if (category == null) {
                Lang.ERROR_CATEGORY_INVALID.getMessage().send(sender);
                return;
            }
            categories.add(category);
        }
        else categories.addAll(this.plugin.getChallengeManager().getCategories());

        Set<ChallengeUser> users = plugin.getUserManager().getAllUsers();

        for (ChallengeCategory category : categories) {
            users.forEach(user -> {
                if (user == null) {
                    this.errorPlayer(sender);
                    return;
                }

                user.getChallenges(category).clear();
                plugin.getUserManager().scheduleSave(user);

                Player player = user.getPlayer();
                if (player != null) {
                    plugin.getChallengeManager().updateChallenges(player, false);
                }
            });

            Lang.COMMAND_RESET_ALL_DONE.getMessage().replace(Placeholders.GENERIC_TYPE, category.getName()).send(sender);
        }
    }
}
