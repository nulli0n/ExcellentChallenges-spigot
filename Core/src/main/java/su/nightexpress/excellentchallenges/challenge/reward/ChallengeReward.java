package su.nightexpress.excellentchallenges.challenge.reward;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import su.nexmedia.engine.api.manager.IPlaceholder;
import su.nexmedia.engine.utils.Colorizer;
import su.nexmedia.engine.utils.PlayerUtil;
import su.nightexpress.excellentchallenges.Placeholders;

import java.util.List;
import java.util.function.UnaryOperator;

public class ChallengeReward implements IPlaceholder {

    private final String       id;
    private final String       name;
    private final List<String> commands;

    public ChallengeReward(@NotNull String id, @NotNull String name, @NotNull List<String> commands) {
        this.id = id.toLowerCase();
        this.name = Colorizer.apply(name);
        this.commands = commands;
    }

    @Override
    @NotNull
    public UnaryOperator<String> replacePlaceholders() {
        return str -> str
            .replace(Placeholders.REWARD_ID, this.getId())
            .replace(Placeholders.REWARD_NAME, this.getName())
            ;
    }

    @NotNull
    public String getId() {
        return id;
    }

    @NotNull
    public String getName() {
        return name;
    }

    @NotNull
    public List<String> getCommands() {
        return commands;
    }

    public void give(@NotNull Player player) {
        this.getCommands().forEach(command -> PlayerUtil.dispatchCommand(player, command));
    }
}