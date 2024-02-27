package su.nightexpress.excellentchallenges.challenge.reward;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentchallenges.Placeholders;
import su.nightexpress.nightcore.config.ConfigValue;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.util.Players;
import su.nightexpress.nightcore.util.StringUtil;
import su.nightexpress.nightcore.util.placeholder.Placeholder;
import su.nightexpress.nightcore.util.placeholder.PlaceholderMap;

import java.util.ArrayList;
import java.util.List;

public class Reward implements Placeholder {

    private final String       id;
    private final String       name;
    private final List<String> commands;

    private final PlaceholderMap placeholderMap;

    public Reward(@NotNull String id, @NotNull String name, @NotNull List<String> commands) {
        this.id = id.toLowerCase();
        this.name = name;
        this.commands = commands;

        this.placeholderMap = new PlaceholderMap()
            .add(Placeholders.REWARD_ID, this::getId)
            .add(Placeholders.REWARD_NAME, this::getName);
    }

    @NotNull
    public static Reward read(@NotNull FileConfig cfg, @NotNull String path, @NotNull String id) {
        String name = ConfigValue.create(path + ".Name", StringUtil.capitalizeUnderscored(id)).read(cfg);
        List<String> commands = ConfigValue.create(path + ".Commands", new ArrayList<>()).read(cfg);

        return new Reward(id, name, commands);
    }


    public void write(@NotNull FileConfig cfg, @NotNull String path) {
        cfg.set(path + ".Name", this.getName());
        cfg.set(path + ".Commands", this.getCommands());
    }

    @Override
    @NotNull
    public PlaceholderMap getPlaceholders() {
        return this.placeholderMap;
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
        this.getCommands().forEach(command -> Players.dispatchCommand(player, command));
    }
}