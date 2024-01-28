package su.nightexpress.excellentchallenges.challenge;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nexmedia.engine.api.config.JYML;
import su.nexmedia.engine.api.manager.AbstractManager;
import su.nexmedia.engine.utils.EntityUtil;
import su.nexmedia.engine.utils.NumberUtil;
import su.nexmedia.engine.utils.PlayerUtil;
import su.nexmedia.engine.utils.random.Rnd;
import su.nightexpress.excellentchallenges.ExcellentChallengesPlugin;
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
import su.nightexpress.excellentchallenges.config.Config;
import su.nightexpress.excellentchallenges.data.object.ChallengeUser;

import java.util.*;
import java.util.stream.Collectors;

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
        for (JYML cfg : JYML.loadAll(plugin.getDataFolder() + Config.DIR_CONDITIONS, true)) {
            cfg.getSection("").forEach(sId -> {
                ConditionConfig conditionConfig = ConditionConfig.read(cfg, sId, sId);
                this.conditionMap.put(conditionConfig.getId(), conditionConfig);
            });

        }
        this.plugin.info("Loaded " + this.getConditionMap().size() + " conditions.");


        // Load rewards.
        for (JYML cfg : JYML.loadAll(plugin.getDataFolder() + Config.DIR_REWARDS, true)) {
            cfg.getSection("").forEach(sId -> {
                Reward reward = Reward.read(cfg, sId, sId);
                this.rewardMap.put(reward.getId(), reward);
            });
        }
        this.plugin.info("Loaded " + this.getRewardMap().size() + " rewards.");


        int combis = 0;
        // Load generators.
        for (JYML cfg : JYML.loadAll(plugin.getDataFolder() + Config.DIR_GENERATORS, true)) {
            Generator generator = new Generator(plugin, cfg);
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
        this.getChallengesMenu().open(player, category, 1);
    }

    public void updateChallenges(@NotNull Player player, boolean force) {
        Collection<ChallengeCategory> challengeCategories = Config.CATEGORIES.get().values();
        challengeCategories.forEach(type -> this.updateChallenges(player, type, force));
    }

    public void updateChallenges(@NotNull Player player, @NotNull ChallengeCategory type, boolean force) {
        if (EntityUtil.isNPC(player)) return;

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
        if (EntityUtil.isNPC(player)) return Collections.emptySet();

        ChallengeUser user = plugin.getUserManager().getUserData(player);
        Set<GeneratedChallenge> generated = new HashSet<>();

        int amount = category.getAmountPerRank(player);
        if (amount <= 0) return generated;

        String rank = PlayerUtil.getPermissionGroup(player);
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
        this.plugin.getUserManager().saveUser(user);

        return generated;
    }

    public <O> void progressChallenge(@NotNull Player player,
                                      @NotNull ActionType<?, O> type,
                                      @NotNull O objective,
                                      int amount) {
        if (EntityUtil.isNPC(player)) return;

        String objectName = type.getObjectName(objective);
        ChallengeUser user = this.plugin.getUserManager().getUserData(player);
        Set<GeneratedChallenge> challenges = user.getChallengesMap().values().stream().flatMap(Collection::stream)
            .filter(chal -> chal.getActionType() == type)
            .filter(chal -> chal.checkConditions(player))
            .filter(chal -> chal.addObjectiveProgress(objectName, amount))
            .collect(Collectors.toSet());

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

        this.plugin.getUserManager().saveUser(user);
    }
}
