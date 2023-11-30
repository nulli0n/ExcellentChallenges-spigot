package su.nightexpress.excellentchallenges.challenge.generator;

import su.nightexpress.excellentchallenges.Placeholders;
import su.nightexpress.excellentchallenges.challenge.GeneratedChallenge;
import su.nightexpress.excellentchallenges.challenge.condition.ConditionConfig;
import su.nightexpress.excellentchallenges.challenge.generator.object.GenObjectiveObject;
import org.jetbrains.annotations.NotNull;
import su.nexmedia.engine.api.config.JYML;
import su.nexmedia.engine.api.manager.AbstractConfigHolder;
import su.nexmedia.engine.utils.values.UniInt;
import su.nightexpress.excellentchallenges.ExcellentChallengesPlugin;
import su.nightexpress.excellentchallenges.challenge.ChallengeCategory;
import su.nightexpress.excellentchallenges.challenge.action.ActionType;
import su.nightexpress.excellentchallenges.challenge.difficulty.Difficulty;
import su.nightexpress.excellentchallenges.challenge.generator.object.GenAmountObject;
import su.nightexpress.excellentchallenges.challenge.generator.object.ObjectList;
import su.nightexpress.excellentchallenges.challenge.reward.Reward;

import java.util.*;
import java.util.stream.Collectors;

public class Generator extends AbstractConfigHolder<ExcellentChallengesPlugin> {

    private final ObjectList<GenObjectiveObject> objectiveList;
    private final ObjectList<GenAmountObject>    conditionList;
    private final ObjectList<GenAmountObject>    rewardList;

    private ActionType<?, ?> type;

    public Generator(@NotNull ExcellentChallengesPlugin plugin, @NotNull JYML cfg) {
        super(plugin, cfg);
        this.objectiveList = new ObjectList<>();
        this.conditionList = new ObjectList<>();
        this.rewardList = new ObjectList<>();
    }

    @Override
    public boolean load() {
        String typeName = cfg.getString("Type");
        this.type = typeName == null ? null : plugin.getActionRegistry().getActionType(typeName);
        if (this.type == null) throw new IllegalStateException("Invalid challenge type: '" + typeName + "' in generator '" + this.getId() + "'.");

        for (String sId : cfg.getSection("Objectives.List")) {
            GenObjectiveObject object = GenObjectiveObject.read(cfg, "Objectives.List." + sId, sId);
            this.getObjectiveList().add(object);
        }

        for (String sId : cfg.getSection("Conditions.List")) {
            GenAmountObject object = GenAmountObject.read(cfg, "Conditions.List." + sId, sId);
            this.getConditionList().add(object);
        }

        for (String sId : cfg.getSection("Rewards.List")) {
            GenAmountObject object = GenAmountObject.read(cfg, "Rewards.List." + sId, sId);
            this.getRewardList().add(object);
        }

        cfg.setComments("Objectives",
            "List of available objectives for 'Items' list:",
            Placeholders.WIKI_ACTION_TYPES
        );

        cfg.setComments("Conditions",
            "List of available conditions for 'Items' list:",
            plugin.getChallengeManager().getConditions().stream().map(ConditionConfig::getId).collect(Collectors.joining(", "))
        );

        cfg.setComments("Rewards",
            "List of available rewards for 'Items' list:",
            plugin.getChallengeManager().getRewards().stream().map(Reward::getId).collect(Collectors.joining(", "))
        );

        this.getObjectiveList().getObjectMap().forEach((section, objects) -> {
            objects.getItems().values().forEach(items -> {
                items.forEach(item -> {
                    if (this.getType().getObjectFormatter().parseObject(item) == null) {
                        this.plugin.warn("Invalid item '" + item + "' in '" + section + "' objectives section of '" + this.getId() + "' generator!");
                    }
                });
            });
        });

        this.cfg.saveChanges();
        return true;
    }

    @Override
    protected void onSave() {
        this.cfg.set("Type", this.getType().getName());

        this.getObjectiveList().getObjectMap().forEach((id, object) -> {
            object.write(cfg, "Objectives.List." + id);
        });

        this.getConditionList().getObjectMap().forEach((id, object) -> {
            object.write(cfg, "Conditions.List." + id);
        });

        this.getRewardList().getObjectMap().forEach((id, object) -> {
            object.write(cfg, "Rewards.List." + id);
        });
    }

    @NotNull
    public GeneratedChallenge generate(@NotNull ChallengeCategory challengeCategory, @NotNull Difficulty difficulty) {
        int level = difficulty.createLevel();
        if (level <= 0) throw new IllegalStateException("Difficulty '" + difficulty.getId() + "' produced invalid challenge level: '" + level + "'.");

        Map<String, UniInt> objectiveMap = new HashMap<>();
        Set<String> conditionIds = new HashSet<>();
        Set<String> rewardIds = new HashSet<>();

        GenObjectiveObject objectivesHolder = this.getObjectiveList().pickObject(difficulty);
        if (objectivesHolder == null) throw new NoSuchElementException("Could not pick objective!");

        for (String objectName : objectivesHolder.pickItems(difficulty, level)) {
            int progress = objectivesHolder.getProgress().createValue(difficulty, level);
            if (progress <= 0) {
                this.plugin.error("Could not generate value for objective '" + objectName + "' in generator '" + this.getId() + "' for difficulty '" + difficulty.getId() + "'.");
                continue;
            }

            objectiveMap.put(objectName, UniInt.of(0, progress));
        }
        if (objectiveMap.isEmpty()) {
            throw new IllegalStateException("No objectives generated!");
        }

        GenAmountObject conditionsHolder = this.getConditionList().pickObject(difficulty);
        if (conditionsHolder != null) {
            conditionIds.addAll(conditionsHolder.pickItems(difficulty, level));
        }

        GenAmountObject rewardsHolder = this.getRewardList().pickObject(difficulty);
        if (rewardsHolder != null) {
            rewardIds.addAll(rewardsHolder.pickItems(difficulty, level));
        }

        List<ConditionConfig> conditionConfigs = new ArrayList<>();
        for (String sId : conditionIds) {
            ConditionConfig conditionConfig = plugin.getChallengeManager().getCondition(sId);
            if (conditionConfig == null) continue;

            conditionConfigs.add(conditionConfig);
        }

        List<Reward> rewards = new ArrayList<>();
        for (String sId : rewardIds) {
            Reward reward = plugin.getChallengeManager().getReward(sId);
            if (reward == null) continue;

            rewards.add(reward);
        }

        long dateCreated = System.currentTimeMillis();

        /*List<String> names = plugin.getChallengeManager().getNames(objectivesHolder.getNamesListId());
        String name = Colorizer.apply((names.isEmpty() ? Placeholders.GENERIC_NAME : Rnd.get(names))
            .replace(Placeholders.GENERIC_NAME, objectivesHolder.getName()));

        List<String> description = new ArrayList<>();
        ItemStack icon = objectivesHolder.pickIcon();*/

        return GeneratedChallenge.create(
            this, challengeCategory, difficulty, level,
            objectiveMap, conditionConfigs, rewards, dateCreated
            //name, description, icon
        );
    }

    @NotNull
    public ActionType<?, ?> getType() {
        return type;
    }

    public void setType(@NotNull ActionType<?, ?> type) {
        this.type = type;
    }

    @NotNull
    public ObjectList<GenObjectiveObject> getObjectiveList() {
        return objectiveList;
    }

    @NotNull
    public ObjectList<GenAmountObject> getConditionList() {
        return conditionList;
    }

    @NotNull
    public ObjectList<GenAmountObject> getRewardList() {
        return rewardList;
    }
}
