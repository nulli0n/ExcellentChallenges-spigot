package su.nightexpress.excellentchallenges.challenge;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentchallenges.ChallengesAPI;
import su.nightexpress.excellentchallenges.challenge.reward.Reward;
import su.nightexpress.excellentchallenges.config.Config;
import su.nightexpress.nightcore.config.ConfigValue;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.util.Lists;
import su.nightexpress.nightcore.util.NumberUtil;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class ChallengeMilestone {

    private final int milestone;
    private final boolean      repeatable;
    private final List<String> rewards;

    public ChallengeMilestone(int milestone, boolean repeatable, @NotNull List<String> rewards) {
        this.milestone = milestone;
        this.repeatable = repeatable;
        this.rewards = rewards;
    }

    @NotNull
    public static ChallengeMilestone read(@NotNull FileConfig config, @NotNull String path, @NotNull String id) {
        int milestone = NumberUtil.getInteger(id, 0);

        boolean repeatable = ConfigValue.create(path + ".Repeatable", false,
            "Sets whether or not players can complete this milestone multiple times."
        ).read(config);

        List<String> rewards = ConfigValue.create(path + ".Rewards", Lists.newList(),
            "List of reward identifiers that will be given to a player upon milestone completion.",
            "Reward configuration files with their identifiers are located in the '" + Config.DIR_REWARDS + "' directory."
        ).read(config);

        return new ChallengeMilestone(milestone, repeatable, rewards);
    }

    public void write(@NotNull FileConfig config, @NotNull String path) {
        config.set(path + ".Repeatable", this.repeatable);
        config.set(path + ".Rewards", this.rewards);
    }

    @NotNull
    public Set<Reward> getCompletionRewards() {
        return this.rewards.stream().map(id -> ChallengesAPI.getChallengeManager().getReward(id))
            .filter(Objects::nonNull).collect(Collectors.toSet());
    }

    public int getMilestone() {
        return milestone;
    }

    public boolean isRepeatable() {
        return repeatable;
    }

    @NotNull
    public List<String> getRewards() {
        return rewards;
    }
}
