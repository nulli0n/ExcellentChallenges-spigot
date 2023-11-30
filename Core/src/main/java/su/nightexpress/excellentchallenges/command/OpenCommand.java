package su.nightexpress.excellentchallenges.command;

import su.nightexpress.excellentchallenges.config.Config;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import su.nexmedia.engine.api.command.AbstractCommand;
import su.nexmedia.engine.api.command.CommandResult;
import su.nightexpress.excellentchallenges.ExcellentChallengesPlugin;
import su.nightexpress.excellentchallenges.Perms;
import su.nightexpress.excellentchallenges.challenge.ChallengeCategory;
import su.nightexpress.excellentchallenges.config.Lang;

import java.util.ArrayList;
import java.util.List;

public class OpenCommand extends AbstractCommand<ExcellentChallengesPlugin> {

    public OpenCommand(@NotNull ExcellentChallengesPlugin plugin) {
        super(plugin, new String[]{"open"}, Perms.COMMAND_OPEN);
        this.setDescription(plugin.getMessage(Lang.COMMAND_OPEN_DESC));
        this.setUsage(plugin.getMessage(Lang.COMMAND_OPEN_USAGE));
        this.setPlayerOnly(true);
    }

    @Override
    @NotNull
    public List<String> getTab(@NotNull Player player, int arg, @NotNull String[] args) {
        if (arg == 1) {
            return new ArrayList<>(Config.CATEGORIES.get().keySet());
        }
        return super.getTab(player, arg, args);
    }

    @Override
    protected void onExecute(@NotNull CommandSender sender, @NotNull CommandResult result) {
        Player player = (Player) sender;
        ChallengeCategory category = result.length() >= 2 ? plugin.getChallengeManager().getChallengeType(result.getArg(1)) : null;

        if (category == null) {
            plugin.getChallengeManager().getCategoriesMenu().open(player, 1);
        }
        else {
            this.plugin.getChallengeManager().openChallengesMenu(player, category);
        }
    }
}
