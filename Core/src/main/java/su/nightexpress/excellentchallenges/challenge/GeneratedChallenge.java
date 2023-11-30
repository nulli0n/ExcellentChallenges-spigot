package su.nightexpress.excellentchallenges.challenge;

import su.nightexpress.excellentchallenges.challenge.condition.ConditionConfig;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import su.nexmedia.engine.api.placeholder.Placeholder;
import su.nexmedia.engine.api.placeholder.PlaceholderMap;
import su.nexmedia.engine.utils.NumberUtil;
import su.nexmedia.engine.utils.values.UniInt;
import su.nightexpress.excellentchallenges.ExcellentChallengesPlugin;
import su.nightexpress.excellentchallenges.Placeholders;
import su.nightexpress.excellentchallenges.challenge.action.ActionType;
import su.nightexpress.excellentchallenges.challenge.condition.CompiledCondition;
import su.nightexpress.excellentchallenges.challenge.difficulty.Difficulty;
import su.nightexpress.excellentchallenges.challenge.generator.Generator;
import su.nightexpress.excellentchallenges.challenge.reward.Reward;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.UnaryOperator;

public class GeneratedChallenge implements Placeholder {

    //private final ExcellentChallengesPlugin plugin;
    private final ActionType<?, ?>          actionType;
    private final Generator         generator;
    private final ChallengeCategory type;
    private final Difficulty        difficulty;
    private final int                                                 level;
    private final Map<String, UniInt>                                       objectives;
    private final Map<ConditionConfig, List<List<CompiledCondition<?, ?>>>> conditions;
    private final List<Reward>                                              rewards;
    private final long                                                      dateCreated;

    private final PlaceholderMap placeholderMap;

    public GeneratedChallenge(
        @NotNull ExcellentChallengesPlugin plugin,
        @NotNull ActionType<?, ?> actionType,
        @NotNull Generator generator,
        @NotNull ChallengeCategory type,
        @NotNull Difficulty difficulty,
        int level,
        @NotNull Map<String, UniInt> objectives,
        @NotNull Map<ConditionConfig, List<List<CompiledCondition<?, ?>>>> conditions,
        @NotNull List<Reward> rewards,
        long dateCreated
    ) {
        //this.plugin = plugin;
        this.actionType = actionType;
        this.generator = generator;
        this.type = type;
        this.difficulty = difficulty;
        this.level = level;
        this.objectives = objectives;
        this.conditions = conditions;
        this.rewards = rewards;
        this.dateCreated = dateCreated;

        this.placeholderMap = new PlaceholderMap()
            .add(Placeholders.CHALLENGE_DIFFICULTY, () -> this.getDifficulty().getName())
            .add(Placeholders.CHALLENGE_LEVEL, () -> NumberUtil.format(this.getLevel()))
            .add(Placeholders.CHALLENGE_LEVEL_ROMAN, () -> NumberUtil.toRoman(this.getLevel()))
            .add(Placeholders.CHALLENGE_PROGRESS_PERCENT, () -> NumberUtil.format(this.getCompletionPercent()))
            .add(Placeholders.CHALLENGE_TYPE, () -> this.getActionType().getDisplayName());
    }

    public static GeneratedChallenge create(
        @NotNull Generator generator,
        @NotNull ChallengeCategory type,
        @NotNull Difficulty difficulty,
        int level,
        @NotNull Map<String, UniInt> objectives,
        @NotNull List<ConditionConfig> conditionConfigs,
        @NotNull List<Reward> rewards,
        long dateCreated
    ) {

        Map<ConditionConfig, List<List<CompiledCondition<?, ?>>>> conditionMap = new LinkedHashMap<>();

        conditionConfigs.forEach(conditionInfo -> {
            List<CompiledCondition<?, ?>> compiledConditions = new ArrayList<>();

            conditionInfo.getConditionMap().forEach((sId, list) -> {
                list.forEach(pair -> compiledConditions.add(pair.getFirst().compile(pair.getSecond())));
            });
            conditionMap.computeIfAbsent(conditionInfo, k -> new ArrayList<>()).add(compiledConditions);
        });

        return new GeneratedChallenge(
            generator.plugin(), generator.getType(), generator, type, difficulty, level,
            objectives, conditionMap, rewards, dateCreated
        );
    }

    @Override
    @NotNull
    public PlaceholderMap getPlaceholders() {
        return this.placeholderMap;
    }

    @NotNull
    public UnaryOperator<String> replacePlaceholders(@NotNull String objId) {
        return line -> {
            line = this.replacePlaceholders().apply(line);
            line = line
                .replace(Placeholders.OBJECTIVE_NAME, this.getActionType().getObjectLocalizedName(objId))
                .replace(Placeholders.OBJECTIVE_PROGRESS_CURRENT, NumberUtil.format(this.getObjectiveProgress(objId)))
                .replace(Placeholders.OBJECTIVE_PROGRESS_MAX, NumberUtil.format(this.getObjectiveRequirement(objId)))
                .replace(Placeholders.OBJECTIVE_PROGRESS_PERCENT, NumberUtil.format(this.getCompletionPercent(objId)))
            ;
            return line;
        };
    }

    public boolean isCompleted() {
        return this.getObjectives().values().stream().allMatch(arr -> arr.getMinValue() >= arr.getMaxValue());
    }

    public boolean isCompleted(@NotNull String objective) {
        return this.getObjectiveProgress(objective) >= this.getObjectiveRequirement(objective);
    }

    public double getCompletionPercent() {
        double has = this.getObjectives().values().stream().mapToInt(UniInt::getMinValue).sum();
        double need = this.getObjectives().values().stream().mapToInt(UniInt::getMaxValue).sum();

        return has / need * 100D;
    }

    public double getObjectivesProgress() {
        return this.getObjectives().values().stream().mapToInt(UniInt::getMinValue).sum();
    }

    public double getObjectiesRequirement() {
        return this.getObjectives().values().stream().mapToInt(UniInt::getMaxValue).sum();
    }

    public double getCompletionPercent(@NotNull String objective) {
        double has = this.getObjectiveProgress(objective);
        double need = this.getObjectiveRequirement(objective);

        return has / need * 100D;
    }

    public boolean hasObjective(@NotNull String id) {
        return this.getObjectives().containsKey(id.toLowerCase());
    }

    public int getObjectiveProgress(@NotNull String id) {
        return this.getObjectiveValue(id).getMinValue();
    }

    public int getObjectiveRequirement(@NotNull String id) {
        return this.getObjectiveValue(id).getMaxValue();
    }

    @NotNull
    public UniInt getObjectiveValue(@NotNull String id) {
        return this.getObjectives().getOrDefault(id.toLowerCase(), UniInt.of(0, 0));
    }

    public void setObjectiveValue(@NotNull String id, @NotNull UniInt value) {
        this.getObjectives().put(id.toLowerCase(), value);
    }

    public boolean addObjectiveProgress(@NotNull String id, int amount) {
        if (!this.hasObjective(id) || this.isCompleted(id) || this.isCompleted()) return false;

        UniInt progress = this.getObjectiveValue(id);
        int current = this.getObjectiveProgress(id);
        int max = this.getObjectiveRequirement(id);

        int updated = Math.min(max, current + amount);
        this.setObjectiveValue(id, UniInt.of(updated, max));
        return true;
    }

    public boolean checkConditions(@NotNull Player player) {
        var lists = this.conditions.values();

        return lists.stream()
            .allMatch(section -> section.stream()
                .anyMatch(condis -> condis.stream()
                    .allMatch(condition -> condition.test(player)))
            );
    }

    @NotNull
    public ActionType<?, ?> getActionType() {
        return actionType;
    }

    @NotNull
    public Generator getGenerator() {
        return generator;
    }

    @NotNull
    public ChallengeCategory getType() {
        return type;
    }

    @NotNull
    public Difficulty getDifficulty() {
        return difficulty;
    }

    public int getLevel() {
        return level;
    }

    @NotNull
    public Map<String, UniInt> getObjectives() {
        return objectives;
    }

    @NotNull
    public Map<ConditionConfig, List<List<CompiledCondition<?, ?>>>> getConditions() {
        return conditions;
    }

    @NotNull
    public List<Reward> getRewards() {
        return rewards;
    }

    public long getDateCreated() {
        return dateCreated;
    }
}
