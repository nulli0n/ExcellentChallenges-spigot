package su.nightexpress.excellentchallenges.challenge;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.excellentchallenges.ExcellentChallengesPlugin;
import su.nightexpress.excellentchallenges.config.Perms;
import su.nightexpress.excellentchallenges.api.event.PlayerChallengeCompleteEvent;
import su.nightexpress.excellentchallenges.api.event.PlayerChallengeObjectiveEvent;
import su.nightexpress.excellentchallenges.challenge.action.ActionType;
import su.nightexpress.excellentchallenges.challenge.condition.ConditionConfig;
import su.nightexpress.excellentchallenges.challenge.difficulty.Difficulty;
import su.nightexpress.excellentchallenges.challenge.generator.Generator;
import su.nightexpress.excellentchallenges.challenge.listener.ChallengeListener;
import su.nightexpress.excellentchallenges.challenge.menu.CategoriesMenu;
import su.nightexpress.excellentchallenges.challenge.menu.ChallengesMenu;
import su.nightexpress.excellentchallenges.challenge.menu.RerollConfirmMenu;
import su.nightexpress.excellentchallenges.challenge.reward.Reward;
import su.nightexpress.excellentchallenges.challenge.type.RerollCondition;
import su.nightexpress.excellentchallenges.config.Config;
import su.nightexpress.excellentchallenges.config.Lang;
import su.nightexpress.excellentchallenges.data.object.ChallengeUser;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.manager.AbstractManager;
import su.nightexpress.nightcore.util.NumberUtil;
import su.nightexpress.nightcore.util.random.Rnd;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static su.nightexpress.excellentchallenges.Placeholders.*;

public class ChallengeManager extends AbstractManager<ExcellentChallengesPlugin> {

    private final Map<String, Generator>       generatorMap;
    private final Map<String, ConditionConfig> conditionMap;
    private final Map<String, Reward>          rewardMap;

    private CategoriesMenu    categoriesMenu;
    private ChallengesMenu    challengesMenu;
    private RerollConfirmMenu rerollConfirmMenu;

    public ChallengeManager(@NotNull ExcellentChallengesPlugin plugin) {
        super(plugin);
        this.generatorMap = new HashMap<>();
        this.conditionMap = new HashMap<>();
        this.rewardMap = new HashMap<>();
    }

    @Override
    public void onLoad() {
        // Load conditions.
        for (FileConfig cfg : FileConfig.loadAll(plugin.getDataFolder() + Config.DIR_CONDITIONS, true)) {
            cfg.getSection("").forEach(sId -> {
                ConditionConfig conditionConfig = ConditionConfig.read(cfg, sId, sId);
                this.conditionMap.put(conditionConfig.getId(), conditionConfig);
            });

        }
        this.plugin.info("Loaded " + this.getConditionMap().size() + " conditions.");


        // Load rewards.
        for (FileConfig cfg : FileConfig.loadAll(plugin.getDataFolder() + Config.DIR_REWARDS, true)) {
            cfg.getSection("").forEach(sId -> {
                Reward reward = Reward.read(cfg, sId, sId);
                this.rewardMap.put(reward.getId(), reward);
            });
        }
        this.plugin.info("Loaded " + this.getRewardMap().size() + " rewards.");


        int combis = 0;
        // Load generators.
        for (FileConfig cfg : FileConfig.loadAll(plugin.getDataFolder() + Config.DIR_GENERATORS, true)) {
            Generator generator = new Generator(plugin, cfg.getFile());
            if (generator.load()) {
                this.generatorMap.put(generator.getId(), generator);

                for (var object : generator.getObjectiveList().getObjectMap().values()) {
                    for (var set : object.getItems().values()) {
                        if (set.size() > 1) {
                            combis += set.size() * (set.size() - 1);
                        }
                    }
                }
            }
            else this.plugin.error("Challenge generator not loaded: " + cfg.getFile().getName());
        }
        this.plugin.info("Loaded " + this.getGeneratorMap().size() + " generators.");
        this.plugin.info("Challenge combinations: " + NumberUtil.format(combis));


        this.categoriesMenu = new CategoriesMenu(this.plugin);
        this.challengesMenu = new ChallengesMenu(this.plugin);
        this.rerollConfirmMenu = new RerollConfirmMenu(this.plugin);

        this.addListener(new ChallengeListener(this));
    }

    @Override
    protected void onShutdown() {
        if (this.rerollConfirmMenu != null) this.rerollConfirmMenu.clear();
        if (this.challengesMenu != null) this.challengesMenu.clear();
        if (this.categoriesMenu != null) this.categoriesMenu.clear();

        this.getGeneratorMap().clear();
        this.getConditionMap().clear();
        this.getRewardMap().clear();
        //this.getNamesMap().clear();
    }

    @NotNull
    public Map<String, Difficulty> getDifficultyMap() {
        return Config.DIFFICULTIES.get();
    }

    @NotNull
    public Collection<Difficulty> getDifficulties() {
        return this.getDifficultyMap().values();
    }

    @Nullable
    public Difficulty getDifficulty(@NotNull String id) {
        return this.getDifficultyMap().get(id.toLowerCase());
    }

    @NotNull
    public Map<String, Generator> getGeneratorMap() {
        return this.generatorMap;
    }

    @NotNull
    public Collection<Generator> getGenerators() {
        return this.getGeneratorMap().values();
    }

    @Nullable
    public Generator getGenerator(@NotNull String id) {
        return this.getGeneratorMap().get(id.toLowerCase());
    }

    @Nullable
    public ChallengeCategory getChallengeType(@NotNull String id) {
        return Config.CATEGORIES.get().get(id.toLowerCase());
    }

    @NotNull
    public Map<String, ConditionConfig> getConditionMap() {
        return this.conditionMap;
    }

    @NotNull
    public Collection<ConditionConfig> getConditions() {
        return this.getConditionMap().values();
    }

    @Nullable
    public ConditionConfig getCondition(@NotNull String id) {
        return this.getConditionMap().get(id.toLowerCase());
    }

    @NotNull
    public Map<String, Reward> getRewardMap() {
        return this.rewardMap;
    }

    @NotNull
    public Collection<Reward> getRewards() {
        return this.getRewardMap().values();
    }

    @Nullable
    public Reward getReward(@NotNull String id) {
        return this.getRewardMap().get(id.toLowerCase());
    }

    @NotNull
    public CategoriesMenu getCategoriesMenu() {
        return categoriesMenu;
    }

    @NotNull
    public ChallengesMenu getChallengesMenu() {
        return challengesMenu;
    }

    @NotNull
    public RerollConfirmMenu getRerollConfirmMenu() {
        return rerollConfirmMenu;
    }

    public void openChallengesMenu(@NotNull Player player, @NotNull ChallengeCategory category) {
        this.getChallengesMenu().open(player, category);
    }

    public void openRerollMenu(@NotNull Player player, @NotNull ChallengeCategory category) {
        this.getRerollConfirmMenu().open(player, category);
    }

    public boolean isDisabledWorld(@NotNull String name) {
        return Config.OBJECTIVES_DISABLED_WORLDS.get().stream().anyMatch(disabled -> disabled.equalsIgnoreCase(name));
    }

    public boolean tryRerollChallenges(@NotNull Player player, @NotNull ChallengeCategory category) {
        if (!player.hasPermission(Perms.REROLL)) {
            Lang.ERROR_NO_PERMISSION.getMessage().send(player);
            return false;
        }

        ChallengeUser user = plugin.getUserManager().getUserData(player);
        int tokens = user.getRerollTokens(category);
        if (tokens <= 0) {
            Lang.CHALLENGE_REROLL_ERROR_NO_TOKENS.getMessage().send(player);
            return false;
        }

        RerollCondition condition = Config.REROLL_CONDITION.get();
        if (condition == RerollCondition.ALL_COMPLETED) {
            if (user.getChallenges(category).stream().anyMatch(Predicate.not(GeneratedChallenge::isCompleted))) {
                Lang.CHALLENGE_REROLL_ERROR_CONDITION.getMessage().send(player);
                return false;
            }
        }
        else if (condition == RerollCondition.ALL_UNFINISHED) {
            if (user.getChallenges(category).stream().anyMatch(GeneratedChallenge::isCompleted)) {
                Lang.CHALLENGE_REROLL_ERROR_CONDITION.getMessage().send(player);
                return false;
            }
        }

        this.openRerollMenu(player, category);
        return true;
    }

    public boolean rerollChallenges(@NotNull Player player, @NotNull ChallengeCategory category) {
        ChallengeUser user = plugin.getUserManager().getUserData(player);
        user.removeRerollTokens(category, 1);
        user.getChallenges(category).clear();
        this.createChallenges(player, category);

        Lang.CHALLENGE_REROLL_DONE.getMessage()
            .replace(GENERIC_TYPE, category.getName())
            .send(player);

        return true;
    }

    public void updateChallenges(@NotNull Player player, boolean force) {
        Collection<ChallengeCategory> challengeCategories = Config.CATEGORIES.get().values();
        challengeCategories.forEach(type -> this.updateChallenges(player, type, force));
    }

    public void updateChallenges(@NotNull Player player, @NotNull ChallengeCategory type, boolean force) {
        ChallengeUser user = this.plugin.getUserManager().getUserData(player);

        if (!force) {
            if (user.hasChallenges(type) && !user.isTimeForNewChallenges(type)) return;
        }

        this.createChallenges(player, type);
    }

    public void createChallenges(@NotNull Player player) {
        Collection<ChallengeCategory> challengeCategories = Config.CATEGORIES.get().values();
        challengeCategories.forEach(type -> {
            this.createChallenges(player, type);
        });
    }

    @NotNull
    public Set<GeneratedChallenge> createChallenges(@NotNull Player player, @NotNull ChallengeCategory category) {
        ChallengeUser user = plugin.getUserManager().getUserData(player);
        Set<GeneratedChallenge> generated = new HashSet<>();

        int amount = category.getAmountPerRank(player);
        if (amount <= 0) return generated;

        //String rank = Players.getPermissionGroup(player);
        Set<Generator> generators = new HashSet<>(this.getGenerators());

        generators.removeIf(generator -> {
            return category.getExcludedGenerators().stream().anyMatch(ex -> generator.getId().startsWith(ex));
        });
        if (generators.isEmpty()) {
            return generated;
        }

        Map<Difficulty, Double> difficultyMap = new HashMap<>();
        category.getDifficulties().forEach((id, chance) -> {
            Difficulty difficulty = this.getDifficulty(id);
            if (difficulty == null || chance <= 0D) return;

            difficultyMap.put(difficulty, chance);
        });
        if (difficultyMap.isEmpty()) {
            this.plugin.error("No difficulty available for '" + category.getId() + "' category!");
            return generated;
        }

        while (amount > 0 && !generators.isEmpty()) {
            Generator generator = Rnd.get(generators);
            Difficulty difficulty = Rnd.getByWeight(difficultyMap);

            generators.remove(generator);
            if (!generator.hasObjectives(difficulty)) {
                //this.plugin.warn("Generator " + generator.getId() + " has no objectives for " + difficulty.getId() + " difficulty.");
                continue;
            }
            if (category.isUniqueTypes()) generators.removeIf(g -> g.getType() == generator.getType());

            try {
                generated.add(generator.generate(category, difficulty));
            }
            // Something went really wrong
            catch (IllegalStateException exception) {
                this.plugin.error("--- CHALLENGE GENERATION ERROR REPORT ---");
                this.plugin.error(exception.getMessage());
                this.plugin.error("> Challenge Type: " + category.getId());
                this.plugin.error("> Generator: " + generator.getId());
                this.plugin.error("> Difficulty: " + difficulty.getId());
                this.plugin.error("> Stacktrace: ");
                exception.printStackTrace();
            }
            // No objectives for selected difficutly, go next
            catch (NoSuchElementException ignored) {

            }

            amount--;
        }

        Set<GeneratedChallenge> challenges = user.getChallenges(category.getId());
        challenges.clear();
        challenges.addAll(generated);
        user.updateRefreshTime(category);
        this.plugin.getUserManager().saveAsync(user);

        return generated;
    }

    public <O> void progressChallenge(@NotNull Player player,
                                      @NotNull ActionType<?, O> type,
                                      @NotNull O objective,
                                      int amount) {
        String worldName = player.getWorld().getName();
        if (this.isDisabledWorld(worldName)) return;

        String objectName = type.getObjectName(objective);
        ChallengeUser user = this.plugin.getUserManager().getUserData(player);
        Set<GeneratedChallenge> challenges = user.getChallengesMap().values().stream().flatMap(Collection::stream)
            .filter(chal -> chal.getActionType() == type)
            .filter(chal -> chal.checkConditions(player))
            .filter(chal -> chal.addObjectiveProgress(objectName, amount))
            .collect(Collectors.toSet());
        if (challenges.isEmpty()) return;

        challenges.forEach(challenge -> {
            if (challenge.isCompleted()) {
                PlayerChallengeCompleteEvent event = new PlayerChallengeCompleteEvent(player, user, challenge);
                plugin.getPluginManager().callEvent(event);
            }
            else {
                PlayerChallengeObjectiveEvent event = new PlayerChallengeObjectiveEvent(player, user, challenge, objectName, amount);
                plugin.getPluginManager().callEvent(event);
            }
        });

        this.plugin.getUserManager().saveAsync(user);
    }
}
