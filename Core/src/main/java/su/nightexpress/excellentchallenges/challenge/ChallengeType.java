package su.nightexpress.excellentchallenges.challenge;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import su.nexmedia.engine.api.config.JOption;
import su.nexmedia.engine.api.config.JYML;
import su.nexmedia.engine.api.manager.ICleanable;
import su.nexmedia.engine.hooks.Hooks;
import su.nexmedia.engine.utils.Colorizer;
import su.nightexpress.excellentchallenges.ExcellentChallengesAPI;
import su.nightexpress.excellentchallenges.Placeholders;
import su.nightexpress.excellentchallenges.challenge.menu.ChallengesListMenu;
import su.nightexpress.excellentchallenges.challenge.reward.ChallengeReward;
import su.nightexpress.excellentchallenges.config.Config;

import java.util.*;
import java.util.stream.Collectors;

public class ChallengeType implements ICleanable {

    private final String                   id;
    private final String                   name;
    private final long                     refreshTime;
    private final int[]                    levels;
    private final boolean                  uniqueGenerators;
    private final Map<String, Integer>     amountPerRank;
    private final Map<String, Set<String>> challengesPerRank;
    private final Set<String> completionRewards;

    private ChallengesListMenu menu;

    public ChallengeType(@NotNull String id, @NotNull String name, long refreshTime, boolean uniqueGenerators, int[] levels,
                         @NotNull Map<String, Integer> amountPerRank,
                         @NotNull Map<String, Set<String>> challengesPerRank,
                         @NotNull Set<String> completionRewards) {
        this.id = id.toLowerCase();
        this.name = Colorizer.apply(name);
        this.refreshTime = refreshTime;
        this.uniqueGenerators = uniqueGenerators;
        this.levels = levels;
        this.amountPerRank = amountPerRank;
        this.challengesPerRank = challengesPerRank;
        this.completionRewards = completionRewards;
    }

    @NotNull
    public static ChallengeType read(@NotNull JYML cfg, @NotNull String path, @NotNull String id) {
        String name = cfg.getString(path + ".Name", id);
        long refreshTime = cfg.getLong(path + ".Refresh_Time");
        boolean uniqueGenerators = cfg.getBoolean(path + ".Unique_Generators");
        int[] levels = new int[2];
        levels[0] = Math.max(1, cfg.getInt(path + ".Levels.Min"));
        levels[1] = Math.max(levels[0], cfg.getInt(path + ".Levels.Max"));

        Map<String, Integer> amountPerRank = new HashMap<>();
        for (String sRank : cfg.getSection(path + ".Amount_Per_Rank")) {
            amountPerRank.put(sRank.toLowerCase(), cfg.getInt(path + ".Amount_Per_Rank." + sRank));
        }
        Map<String, Set<String>> challengesPerRank = new HashMap<>();
        for (String sRank : cfg.getSection(path + ".Challenge_Ids_Per_Rank")) {
            challengesPerRank.put(sRank.toLowerCase(), cfg.getStringSet(path + ".Challenge_Ids_Per_Rank." + sRank));
        }

        Set<String> completionRewards = JOption.create(path + ".Completion_Rewards", new HashSet<>(),
            "A list of reward identifiers that will be given to a player, when he completes all current challenges of this type",
            "Reward configuration files with their identifiers are located in '" + Config.DIR_REWARDS + "' sub-folder.",
            "Use '" + Placeholders.GENERIC_REWARDS + "' placeholder in 'challenges_types.yml' menu config to display reward names.")
            .read(cfg);

        return new ChallengeType(id, name, refreshTime, uniqueGenerators, levels, amountPerRank, challengesPerRank, completionRewards);
    }

    public static void write(@NotNull ChallengeType type, @NotNull JYML cfg, @NotNull String path) {
        cfg.set(path + ".Name", type.getName());
        cfg.set(path + ".Refresh_Time", type.getRefreshTime());
        cfg.set(path + ".Unique_Generators", type.isUniqueGenerators());
        cfg.set(path + ".Levels.Min", type.getLevels()[0]);
        cfg.set(path + ".Levels.Max", type.getLevels()[1]);
        cfg.remove(path + ".Amount_Per_Rank");
        type.getAmountPerRank().forEach((rank, amount) -> cfg.set(path + ".Amount_Per_Rank." + rank, amount));
        cfg.remove(path + ".Challenge_Ids_Per_Rank");
        type.getChallengesPerRank().forEach((rank, amount) -> cfg.set(path + ".Challenge_Ids_Per_Rank." + rank, amount));
        cfg.set(path + ".Completion_Rewards", type.getCompletionRewardIds());
    }

    @Override
    public void clear() {
        if (this.menu != null) {
            this.menu.clear();
            this.menu = null;
        }
    }

    @NotNull
    public ChallengesListMenu getMenu() {
        if (this.menu == null) {
            JYML cfg = JYML.loadOrExtract(ExcellentChallengesAPI.PLUGIN, Config.DIR_MENU + "challenges_" + this.getId() + ".yml");
            this.menu = new ChallengesListMenu(ExcellentChallengesAPI.PLUGIN, cfg, this);
        }
        return menu;
    }

    @NotNull
    public String getId() {
        return id;
    }

    @NotNull
    public String getName() {
        return name;
    }

    public long getRefreshTime() {
        return refreshTime;
    }

    public boolean isUniqueGenerators() {
        return uniqueGenerators;
    }

    public int[] getLevels() {
        return levels;
    }

    @NotNull
    public Map<String, Integer> getAmountPerRank() {
        return amountPerRank;
    }

    public int getAmountPerRank(@NotNull Player player) {
        return Hooks.getGroupValueInt(player, this.getAmountPerRank(), false);
    }

    @NotNull
    public Map<String, Set<String>> getChallengesPerRank() {
        return challengesPerRank;
    }

    @NotNull
    public Set<String> getChallengesPerRank(@NotNull String rank) {
        return this.getChallengesPerRank().getOrDefault(rank.toLowerCase(), this.getChallengesPerRank().get(Placeholders.DEFAULT));
    }

    @NotNull
    public Set<String> getCompletionRewardIds() {
        return completionRewards;
    }

    @NotNull
    public Set<ChallengeReward> getCompletionRewards() {
        return this.getCompletionRewardIds().stream().map(id -> ExcellentChallengesAPI.getChallengeManager().getReward(id))
            .filter(Objects::nonNull).collect(Collectors.toSet());
    }
}
