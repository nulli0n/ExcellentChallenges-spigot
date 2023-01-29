package su.nightexpress.excellentchallenges.command;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import su.nexmedia.engine.api.command.AbstractCommand;
import su.nightexpress.excellentchallenges.ExcellentChallenges;
import su.nightexpress.excellentchallenges.Perms;
import su.nightexpress.excellentchallenges.challenge.ChallengeType;
import su.nightexpress.excellentchallenges.config.Config;
import su.nightexpress.excellentchallenges.config.Lang;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OpenCommand extends AbstractCommand<ExcellentChallenges> {

    public OpenCommand(@NotNull ExcellentChallenges plugin) {
        super(plugin, new String[]{"open"}, Perms.COMMAND_OPEN);
    }

    @Override
    @NotNull
    public String getUsage() {
        return plugin.getMessage(Lang.COMMAND_OPEN_USAGE).getLocalized();
    }

    @Override
    @NotNull
    public String getDescription() {
        return plugin.getMessage(Lang.COMMAND_OPEN_DESC).getLocalized();
    }

    @Override
    public boolean isPlayerOnly() {
        return true;
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
    protected void onExecute(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args, @NotNull Map<String, String> flags) {
        Player player = (Player) sender;
        ChallengeType challengeType = args.length >= 2 ? plugin.getChallengeManager().getChallengeType(args[1]) : null;

        if (challengeType == null) {
            plugin.getChallengeManager().getMainMenu().open(player, 1);
        }
        else {
            challengeType.getMenu().open(player, 1);
        }
    }
}
