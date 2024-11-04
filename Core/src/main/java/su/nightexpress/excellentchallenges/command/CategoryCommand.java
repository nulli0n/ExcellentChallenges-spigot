package su.nightexpress.excellentchallenges.command;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentchallenges.ChallengesPlugin;
import su.nightexpress.excellentchallenges.Placeholders;
import su.nightexpress.excellentchallenges.challenge.ChallengeCategory;
import su.nightexpress.excellentchallenges.config.Lang;
import su.nightexpress.excellentchallenges.config.Perms;
import su.nightexpress.nightcore.command.CommandResult;
import su.nightexpress.nightcore.command.impl.PluginCommand;
import su.nightexpress.nightcore.util.Players;

import java.util.List;

public class CategoryCommand extends PluginCommand<ChallengesPlugin> {

    private final ChallengeCategory category;

    public CategoryCommand(@NotNull ChallengesPlugin plugin, @NotNull ChallengeCategory category) {
        super(plugin, category.getCommandAliases(), Perms.COMMAND_OPEN);
        this.category = category;

        this.setDescription(Lang.COMMAND_CATEGORY_DESC);
        this.setUsage(Lang.COMMAND_CATEGORY_USAGE);
    }

    @Override
    @NotNull
    public List<String> getTab(@NotNull Player player, int arg, @NotNull String[] args) {
        if (arg == 1 && player.hasPermission(Perms.COMMAND_OPEN_OTHERS)) {
            return Players.playerNames(player);
        }
        return super.getTab(player, arg, args);
    }

    @Override
    protected void onExecute(@NotNull CommandSender sender, @NotNull CommandResult result) {
        if (result.length() >= 2 && !sender.hasPermission(Perms.COMMAND_OPEN_OTHERS)) {
            this.errorPermission(sender);
            return;
        }

        Player player = Players.getPlayer(result.getArg(1, sender.getName()));
        if (player == null) {
            this.errorPlayer(sender);
            return;
        }

        this.plugin.getChallengeManager().openChallengesMenu(player, this.category);

        if (player != sender) {
            Lang.COMMAND_CATEGORY_DONE.getMessage()
                .replace(category.replacePlaceholders())
                .replace(Placeholders.forPlayer(player))
                .send(sender);
        }
    }
}
