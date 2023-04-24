package su.nightexpress.excellentchallenges.command;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import su.nexmedia.engine.api.command.AbstractCommand;
import su.nexmedia.engine.utils.CollectionsUtil;
import su.nightexpress.excellentchallenges.ExcellentChallenges;
import su.nightexpress.excellentchallenges.Perms;
import su.nightexpress.excellentchallenges.Placeholders;
import su.nightexpress.excellentchallenges.challenge.ChallengeType;
import su.nightexpress.excellentchallenges.config.Config;
import su.nightexpress.excellentchallenges.config.Lang;
import su.nightexpress.excellentchallenges.data.object.ChallengeUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ResetCommand extends AbstractCommand<ExcellentChallenges> {

    public ResetCommand(@NotNull ExcellentChallenges plugin) {
        super(plugin, new String[]{"reset"}, Perms.COMMAND_RESET);
    }

    @Override
    @NotNull
    public String getUsage() {
        return plugin.getMessage(Lang.COMMAND_RESET_USAGE).getLocalized();
    }

    @Override
    @NotNull
    public String getDescription() {
        return plugin.getMessage(Lang.COMMAND_RESET_DESC).getLocalized();
    }

    @Override
    public boolean isPlayerOnly() {
        return false;
    }

    @Override
    @NotNull
    public List<String> getTab(@NotNull Player player, int arg, @NotNull String[] args) {
        if (arg == 1) {
            return CollectionsUtil.playerNames(player);
        }
        if (arg == 2) {
            return new ArrayList<>(Config.CHALLENGES_TYPES.get().keySet());
        }
        return super.getTab(player, arg, args);
    }

    @Override
    protected void onExecute(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args, @NotNull Map<String, String> flags) {
        if (args.length < 2) {
            this.printUsage(sender);
            return;
        }

        ChallengeUser user = plugin.getUserManager().getUserData(args[1]);
        if (user == null) {
            this.errorPlayer(sender);
            return;
        }

        String type = args.length >= 3 ? args[2] : null;
        ChallengeType cType = type != null ? plugin.getChallengeManager().getChallengeType(type) : null;
        ChallengeType[] types = cType != null ? new ChallengeType[]{cType} : Config.CHALLENGES_TYPES.get().values().toArray(new ChallengeType[0]);

        for (ChallengeType type2 : types) {
            user.getChallenges(type2).clear();

            plugin.getMessage(Lang.COMMAND_RESET_DONE)
                .replace(Placeholders.GENERIC_TYPE, type2.getName())
                .replace(Placeholders.Player.NAME, user.getName())
                .send(sender);
        }

        Player player = user.getPlayer();
        if (player != null) {
            plugin.getChallengeManager().updateChallenges(player, false);
        }
    }
}
