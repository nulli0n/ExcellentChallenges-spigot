package su.nightexpress.excellentchallenges.command;

import su.nightexpress.excellentchallenges.config.Config;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import su.nexmedia.engine.api.command.AbstractCommand;
import su.nexmedia.engine.api.command.CommandResult;
import su.nexmedia.engine.utils.CollectionsUtil;
import su.nightexpress.excellentchallenges.ExcellentChallengesPlugin;
import su.nightexpress.excellentchallenges.Perms;
import su.nightexpress.excellentchallenges.Placeholders;
import su.nightexpress.excellentchallenges.challenge.ChallengeCategory;
import su.nightexpress.excellentchallenges.config.Lang;
import su.nightexpress.excellentchallenges.data.object.ChallengeUser;

import java.util.ArrayList;
import java.util.List;

public class ResetCommand extends AbstractCommand<ExcellentChallengesPlugin> {

    public ResetCommand(@NotNull ExcellentChallengesPlugin plugin) {
        super(plugin, new String[]{"reset"}, Perms.COMMAND_RESET);
        this.setDescription(plugin.getMessage(Lang.COMMAND_RESET_DESC));
        this.setUsage(plugin.getMessage(Lang.COMMAND_RESET_USAGE));
    }

    @Override
    @NotNull
    public List<String> getTab(@NotNull Player player, int arg, @NotNull String[] args) {
        if (arg == 1) {
            return CollectionsUtil.playerNames(player);
        }
        if (arg == 2) {
            return new ArrayList<>(Config.CATEGORIES.get().keySet());
        }
        return super.getTab(player, arg, args);
    }

    @Override
    protected void onExecute(@NotNull CommandSender sender, @NotNull CommandResult result) {
        if (result.length() < 2) {
            this.printUsage(sender);
            return;
        }

        ChallengeUser user = plugin.getUserManager().getUserData(result.getArg(1));
        if (user == null) {
            this.errorPlayer(sender);
            return;
        }

        String type = result.length() >= 3 ? result.getArg(2) : null;
        ChallengeCategory cType = type != null ? plugin.getChallengeManager().getChallengeType(type) : null;
        ChallengeCategory[] types = cType != null ? new ChallengeCategory[]{cType} : Config.CATEGORIES.get().values().toArray(new ChallengeCategory[0]);

        for (ChallengeCategory type2 : types) {
            user.getChallenges(type2).clear();

            plugin.getMessage(Lang.COMMAND_RESET_DONE)
                .replace(Placeholders.GENERIC_TYPE, type2.getName())
                .replace(Placeholders.PLAYER_NAME, user.getName())
                .send(sender);
        }

        Player player = user.getPlayer();
        if (player != null) {
            plugin.getChallengeManager().updateChallenges(player, false);
        }
    }
}
