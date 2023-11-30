package su.nightexpress.excellentchallenges.challenge;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import su.nexmedia.engine.api.config.JOption;
import su.nexmedia.engine.api.config.JYML;
import su.nexmedia.engine.api.placeholder.Placeholder;
import su.nexmedia.engine.api.placeholder.PlaceholderMap;
import su.nexmedia.engine.utils.Colorizer;
import su.nexmedia.engine.utils.PlayerRankMap;
import su.nightexpress.excellentchallenges.ExcellentChallengesAPI;
import su.nightexpress.excellentchallenges.Placeholders;
import su.nightexpress.excellentchallenges.challenge.reward.Reward;
import su.nightexpress.excellentchallenges.config.Config;

import java.util.*;
import java.util.stream.Collectors;

public class ChallengeCategory implements Placeholder {

    private final String                 id;
    private final String                 name;
    private final ItemStack              icon;
    private final long                   refreshTime;
    private final boolean                uniqueTypes;
    private final Map<String, Double>    difficulties;
    private final PlayerRankMap<Integer> amountPerRank;
    private final Set<String>            excludedGenerators;
    private final Set<String>            completionRewards;

    private final PlaceholderMap placeholderMap;

    public ChallengeCategory(@NotNull String id,
                             @NotNull String name,
                             @NotNull ItemStack icon,
                             long refreshTime,
                             boolean uniqueTypes,
                             @NotNull Map<String, Double> difficulties,
                             @NotNull PlayerRankMap<Integer> amountPerRank,
                             @NotNull Set<String> excludedGenerators,
                             @NotNull Set<String> completionRewards) {
        this.id = id.toLowerCase();
        this.name = Colorizer.apply(name);
        this.icon = icon;
        this.refreshTime = refreshTime;
        this.uniqueTypes = uniqueTypes;
        this.difficulties = difficulties;
        this.amountPerRank = amountPerRank;
        this.excludedGenerators = excludedGenerators.stream().map(String::toLowerCase).collect(Collectors.toSet());
        this.completionRewards = completionRewards;

        this.placeholderMap = new PlaceholderMap()
            .add(Placeholders.CATEGORY_NAME, this::getName);
    }

    @NotNull
    public static ChallengeCategory read(@NotNull JYML cfg, @NotNull String path, @NotNull String id) {
        String name = cfg.getString(path + ".Name", id);

        ItemStack icon = JOption.create(path + ".Icon", new ItemStack(Material.DIAMOND),
            "Sets icon to display in Categories GUI."
        ).read(cfg);

        long refreshTime = JOption.create(path + ".Refresh_Time", 86400,
            "Amount of seconds to reroll (replace) all challenges in the category.",
            "The timer is individual for each player.."
        ).read(cfg);

        boolean uniqueTypes = JOption.create(path + ".Unique_Types", true,
            "Sets whether or not all challenges generated for this category must have diffrent action types."
        ).read(cfg);

        Map<String, Double> difficulties = JOption.forMap(path + ".Difficulties",
            (cfg2, path2, id2) -> cfg2.getDouble(path2 + "." + id2),
            Map.of(),
            "Sets chances for difficulties to be picked in generation."
        ).setWriter((cfg2, path2, map) -> map.forEach((id2, chance) -> cfg2.set(path2 + "." + id2, chance))).read(cfg);

        PlayerRankMap<Integer> amountPerRank = PlayerRankMap.read(cfg, path + ".Amount_Per_Rank", Integer.class);

        Set<String> excludedGenerators = JOption.create(path + ".Excluded_Generators",
            Set.of(),
            "Here you can provide generator names that will be excluded when generating new challenges.",
            "All generators which name starts with any of values in the list will be skipped.",
            "Exlcluded_Generators:",
            "- craft_item_",
            "- smelt_item_"
        ).read(cfg);

        Set<String> completionRewards = JOption.create(path + ".Completion_Rewards", new HashSet<>(),
            "A list of reward identifiers that will be given to a player, when he completes all current challenges of this type",
            "Reward configuration files with their identifiers are located in '" + Config.DIR_REWARDS + "' sub-folder.",
            "Use '" + Placeholders.GENERIC_REWARDS + "' placeholder in 'categories.yml' menu config to display reward names.")
            .read(cfg);

        return new ChallengeCategory(id, name, icon, refreshTime, uniqueTypes, difficulties, amountPerRank, excludedGenerators, completionRewards);
    }

    public void write(@NotNull JYML cfg, @NotNull String path) {
        cfg.set(path + ".Name", this.getName());
        cfg.setItem(path + ".Icon", this.getIcon());
        cfg.set(path + ".Refresh_Time", this.getRefreshTime());
        cfg.set(path + ".Unique_Types", this.isUniqueTypes());
        cfg.remove(path + ".Difficulties");
        this.getDifficulties().forEach((sId, chance) -> {
            cfg.set(path + ".Difficulties." + sId, chance);
        });
        cfg.remove(path + ".Amount_Per_Rank");
        this.getAmountPerRank().write(cfg, path + ".Amount_Per_Rank");
        cfg.set(path + ".Excluded_Generators", this.getExcludedGenerators());
        cfg.set(path + ".Completion_Rewards", this.getCompletionRewardIds());
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
    public ItemStack getIcon() {
        return new ItemStack(icon);
    }

    public long getRefreshTime() {
        return refreshTime;
    }

    public boolean isUniqueTypes() {
        return uniqueTypes;
    }

    @NotNull
    public Map<String, Double> getDifficulties() {
        return difficulties;
    }

    @NotNull
    public PlayerRankMap<Integer> getAmountPerRank() {
        return amountPerRank;
    }

    public int getAmountPerRank(@NotNull Player player) {
        return this.getAmountPerRank().getBestValue(player, 0);
    }

    @NotNull
    public Set<String> getExcludedGenerators() {
        return excludedGenerators;
    }

    @NotNull
    public Set<String> getCompletionRewardIds() {
        return completionRewards;
    }

    @NotNull
    public Set<Reward> getCompletionRewards() {
        return this.getCompletionRewardIds().stream().map(id -> ExcellentChallengesAPI.getChallengeManager().getReward(id))
            .filter(Objects::nonNull).collect(Collectors.toSet());
    }
}
