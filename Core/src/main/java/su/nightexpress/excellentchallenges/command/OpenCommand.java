package su.nightexpress.excellentchallenges.command;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import su.nexmedia.engine.api.command.AbstractCommand;
import su.nexmedia.engine.api.command.CommandResult;
import su.nightexpress.excellentchallenges.ExcellentChallenges;
import su.nightexpress.excellentchallenges.Perms;
import su.nightexpress.excellentchallenges.challenge.ChallengeType;
import su.nightexpress.excellentchallenges.config.Config;
import su.nightexpress.excellentchallenges.config.Lang;

import java.util.ArrayList;
import java.util.List;

public class OpenCommand extends AbstractCommand<ExcellentChallenges> {

    public OpenCommand(@NotNull ExcellentChallenges plugin) {
        super(plugin, new String[]{"open"}, Perms.COMMAND_OPEN);
        this.setDescription(plugin.getMessage(Lang.COMMAND_OPEN_DESC));
        this.setUsage(plugin.getMessage(Lang.COMMAND_OPEN_USAGE));
        this.setPlayerOnly(true);
    }

    @Override
    @NotNull
    public List<String> getTab(@NotNull Player player, int arg, @NotNull String[] args) {
        if (arg == 1) {
            return new ArrayList<>(Config.CHALLENGES_TYPES.get().keySet());
        }
        return super.getTab(player, arg, args);
    }

    @Override
    protected void onExecute(@NotNull CommandSender sender, @NotNull CommandResult result) {
        Player player = (Player) sender;
        ChallengeType challengeType = result.length() >= 2 ? plugin.getChallengeManager().getChallengeType(result.getArg(1)) : null;

        if (challengeType == null) {
            plugin.getChallengeManager().getMainMenu().open(player, 1);
        }
        else {
            challengeType.getMenu().open(player, 1);
        }
    }
}
