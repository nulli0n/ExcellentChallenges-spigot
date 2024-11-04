package su.nightexpress.excellentchallenges.challenge;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentchallenges.ChallengesAPI;
import su.nightexpress.excellentchallenges.Placeholders;
import su.nightexpress.excellentchallenges.challenge.reward.Reward;
import su.nightexpress.excellentchallenges.config.Config;
import su.nightexpress.excellentchallenges.config.Perms;
import su.nightexpress.nightcore.config.ConfigValue;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.util.Lists;
import su.nightexpress.nightcore.util.NumberUtil;
import su.nightexpress.nightcore.util.RankMap;
import su.nightexpress.nightcore.util.placeholder.Placeholder;
import su.nightexpress.nightcore.util.placeholder.PlaceholderMap;

import java.util.*;
import java.util.stream.Collectors;

public class ChallengeCategory implements Placeholder {

    private final String              id;
    private final String              name;
    private final ItemStack           icon;
    private final String[]            commandAliases;
    private final long                refreshTime;
    private final boolean             uniqueTypes;
    private final Map<String, Double> difficulties;
    private final RankMap<Integer>    amountPerRank;
    private final Set<String>         excludedGenerators;
    private final Set<String>         completionRewards;
    private final TreeMap<Integer, ChallengeMilestone> milestones;

    private final PlaceholderMap placeholderMap;

    public ChallengeCategory(@NotNull String id,
                             @NotNull String name,
                             @NotNull ItemStack icon,
                             String[] commandAliases,
                             long refreshTime,
                             boolean uniqueTypes,
                             @NotNull Map<String, Double> difficulties,
                             @NotNull RankMap<Integer> amountPerRank,
                             @NotNull Set<String> excludedGenerators,
                             @NotNull Set<String> completionRewards,
                             @NotNull TreeMap<Integer, ChallengeMilestone> milestones) {
        this.id = id.toLowerCase();
        this.name = name;
        this.icon = icon;
        this.commandAliases = commandAliases;
        this.refreshTime = refreshTime;
        this.uniqueTypes = uniqueTypes;
        this.difficulties = difficulties;
        this.amountPerRank = amountPerRank;
        this.excludedGenerators = excludedGenerators.stream().map(String::toLowerCase).collect(Collectors.toSet());
        this.completionRewards = completionRewards;
        this.milestones = milestones;

        this.placeholderMap = new PlaceholderMap()
            .add(Placeholders.CATEGORY_NAME, this::getName);
    }

    @NotNull
    public static ChallengeCategory read(@NotNull FileConfig cfg, @NotNull String path, @NotNull String id) {
        String name = cfg.getString(path + ".Name", id);

        ItemStack icon = ConfigValue.create(path + ".Icon", new ItemStack(Material.DIAMOND),
            "Sets icon to display in Categories GUI."
        ).read(cfg);

        String[] commandAliases = ConfigValue.create(path + ".Command_Aliases",
            id + "challenges",
            "List of commands to be registered for a quick access to the Challenges GUI.",
            "Permissions are: '" + Perms.COMMAND_OPEN.getName() + "' and '" + Perms.COMMAND_OPEN_OTHERS.getName() + "'.",
            "[*] You MUST reboot your server to apply command changes."
        ).read(cfg).split(",");

        long refreshTime = ConfigValue.create(path + ".Refresh_Time", 86400,
            "Amount of seconds to reroll (replace) all challenges in the category.",
            "The timer is individual for each player."
        ).read(cfg);

        boolean uniqueTypes = ConfigValue.create(path + ".Unique_Types", true,
            "Sets whether or not all challenges generated for this category must have diffrent action types."
        ).read(cfg);

        Map<String, Double> difficulties = ConfigValue.forMap(path + ".Difficulties",
            (cfg2, path2, id2) -> cfg2.getDouble(path2 + "." + id2),
            (cfg2, path2, map) -> map.forEach((id2, chance) -> cfg2.set(path2 + "." + id2, chance)),
            Map.of(),
            "Sets chances for difficulties to be picked in generation."
        ).read(cfg);

        RankMap<Integer> amountPerRank = RankMap.readInt(cfg, path + ".Amount_Per_Rank", 5);

        Set<String> excludedGenerators = ConfigValue.create(path + ".Excluded_Generators",
            Set.of(),
            "Here you can provide generator names that will be excluded when generating new challenges.",
            "All generators which name starts with any of values in the list will be skipped.",
            "Exlcluded_Generators:",
            "- craft_item_",
            "- smelt_item_"
        ).read(cfg);

        Set<String> completionRewards = ConfigValue.create(path + ".Completion_Rewards",
                new HashSet<>(),
            "List of reward identifiers that will be given to a player, when he completes all current challenges of this type",
            "Reward configuration files with their identifiers are located in '" + Config.DIR_REWARDS + "' sub-folder.",
            "Use '" + Placeholders.GENERIC_REWARDS + "' placeholder in 'categories.yml' menu config to display reward names."
        ).read(cfg);

        TreeMap<Integer, ChallengeMilestone> milestones = ConfigValue.forTreeMap(path + ".Milestones",
            NumberUtil::getInteger,
            (cfg1, path1, key) -> ChallengeMilestone.read(cfg1, path1 + "." + key, key),
            (cfg1, path1, map) -> map.values().forEach(mile -> mile.write(cfg1, path1 + "." + mile.getMilestone())),
            () -> {
                TreeMap<Integer, ChallengeMilestone> map = new TreeMap<>();
                map.put(5, new ChallengeMilestone(5, false, Lists.newList("reward_1", "reward_2")));
                map.put(15, new ChallengeMilestone(15, true, Lists.newList("reward_1", "reward_2")));
                return map;
            },
            "Here you can create challenge milestones.",
            "Players will get specific rewards upon completion specific amount of challenges of this type."
        ).read(cfg);

//        cfg.getSection(path + ".Milestones").forEach(sId -> {
//            int milestone = NumberUtil.getInteger(sId);
//            if (milestone <= 0) return;
//
//            ChallengeMilestone mile = ChallengeMilestone.read(cfg, path + ".Milestones." + sId, milestone);
//            milestones.put(milestone, mile);
//        });

        return new ChallengeCategory(id, name, icon, commandAliases, refreshTime, uniqueTypes, difficulties, amountPerRank, excludedGenerators, completionRewards, milestones);
    }

    public void write(@NotNull FileConfig config, @NotNull String path) {
        config.set(path + ".Name", this.getName());
        config.setItem(path + ".Icon", this.getIcon());
        config.set(path + ".Command_Aliases", String.join(",", this.getCommandAliases()));
        config.set(path + ".Refresh_Time", this.getRefreshTime());
        config.set(path + ".Unique_Types", this.isUniqueTypes());
        config.remove(path + ".Difficulties");
        this.getDifficulties().forEach((sId, chance) -> {
            config.set(path + ".Difficulties." + sId, chance);
        });
        config.remove(path + ".Amount_Per_Rank");
        this.getAmountPerRank().write(config, path + ".Amount_Per_Rank");
        config.set(path + ".Excluded_Generators", this.getExcludedGenerators());
        config.set(path + ".Completion_Rewards", this.getCompletionRewardIds());

        config.remove(path + ".Milestones");
        this.milestones.forEach((mile, stone) -> stone.write(config, path + ".Milestones." + mile));
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

    public String[] getCommandAliases() {
        return commandAliases;
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
    public RankMap<Integer> getAmountPerRank() {
        return amountPerRank;
    }

    public int getAmountPerRank(@NotNull Player player) {
        return this.getAmountPerRank().getGreatest(player);
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
        return this.getCompletionRewardIds().stream().map(id -> ChallengesAPI.getChallengeManager().getReward(id))
            .filter(Objects::nonNull).collect(Collectors.toSet());
    }

    @NotNull
    public TreeMap<Integer, ChallengeMilestone> getMilestones() {
        return milestones;
    }

    @NotNull
    public Set<ChallengeMilestone> getMilestones(int amount) {
        if (amount == 0) return Collections.emptySet();

        System.out.println("amount = " + amount);

        return this.milestones.entrySet().stream().filter(entry -> {
            int milestone = entry.getKey();
            return milestone > 0 && amount % milestone == 0;
        }).map(Map.Entry::getValue).collect(Collectors.toSet());
    }
}
