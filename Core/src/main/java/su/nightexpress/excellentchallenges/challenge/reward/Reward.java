package su.nightexpress.excellentchallenges.challenge.reward;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import su.nexmedia.engine.api.config.JOption;
import su.nexmedia.engine.api.config.JYML;
import su.nexmedia.engine.api.placeholder.Placeholder;
import su.nexmedia.engine.api.placeholder.PlaceholderMap;
import su.nexmedia.engine.utils.Colorizer;
import su.nexmedia.engine.utils.PlayerUtil;
import su.nexmedia.engine.utils.StringUtil;
import su.nightexpress.excellentchallenges.Placeholders;

import java.util.ArrayList;
import java.util.List;

public class Reward implements Placeholder {

    private final String       id;
    private final String       name;
    private final List<String> commands;

    private final PlaceholderMap placeholderMap;

    public Reward(@NotNull String id, @NotNull String name, @NotNull List<String> commands) {
        this.id = id.toLowerCase();
        this.name = Colorizer.apply(name);
        this.commands = commands;

        this.placeholderMap = new PlaceholderMap()
            .add(Placeholders.REWARD_ID, this::getId)
            .add(Placeholders.REWARD_NAME, this::getName);
    }

    @NotNull
    public static Reward read(@NotNull JYML cfg, @NotNull String path, @NotNull String id) {
        String name = JOption.create(path + ".Name", StringUtil.capitalizeUnderscored(id)).read(cfg);
        List<String> commands = JOption.create(path + ".Commands", new ArrayList<>()).read(cfg);

        return new Reward(id, name, commands);
    }


    public void write(@NotNull JYML cfg, @NotNull String path) {
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
        this.getCommands().forEach(command -> PlayerUtil.dispatchCommand(player, command));
    }
}