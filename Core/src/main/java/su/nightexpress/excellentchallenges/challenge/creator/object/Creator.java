package su.nightexpress.excellentchallenges.challenge.creator.object;

import su.nightexpress.excellentchallenges.ExcellentChallengesPlugin;
import su.nightexpress.excellentchallenges.challenge.action.ActionType;
import su.nightexpress.excellentchallenges.challenge.generator.Generator;
import su.nightexpress.excellentchallenges.challenge.generator.object.GenAmountObject;
import su.nightexpress.excellentchallenges.challenge.generator.object.GenObjectiveObject;
import su.nightexpress.excellentchallenges.challenge.generator.object.ObjectList;
import su.nightexpress.excellentchallenges.config.Config;
import org.jetbrains.annotations.NotNull;
import su.nexmedia.engine.api.config.JYML;

import java.io.File;

public class Creator<O> {

    private final ExcellentChallengesPlugin plugin;
    private final ActionType<?, O>          actionType;
    private final String              name;

    private final ObjectList<GenObjectiveObject> objectives;
    private final ObjectList<GenAmountObject>    conditions;
    private final ObjectList<GenAmountObject>    rewards;

    public Creator(@NotNull ExcellentChallengesPlugin plugin, @NotNull ActionType<?, O> actionType, @NotNull String name) {
        this.plugin = plugin;
        this.actionType = actionType;
        this.name = name;

        this.objectives = new ObjectList<>();
        this.conditions = new ObjectList<>();
        this.rewards = new ObjectList<>();
    }

    @NotNull
    public ActionType<?, O> getActionType() {
        return actionType;
    }

    public ObjectiveCreator<O> objectiveCreator() {
        return new ObjectiveCreator<>(this.actionType, this.objectives.getObjectMap().size() + 1);
    }

    public ObjectListCreator conditionsCreator() {
        return new ObjectListCreator(this.conditions.getObjectMap().size() + 1);
    }

    public ObjectListCreator rewardsCreator() {
        return new ObjectListCreator(this.rewards.getObjectMap().size() + 1);
    }

    public Creator<O> createObjective(@NotNull ObjectiveCreator<O> creator) {
        this.objectives.add(creator.pack());
        return this;
    }

    public Creator<O> createCondition(@NotNull ObjectListCreator creator) {
        this.conditions.add(creator.pack());
        return this;
    }

    public Creator<O> createReward(@NotNull ObjectListCreator creator) {
        this.rewards.add(creator.pack());
        return this;
    }

    public Generator createGenerator() {
        File file = new File(plugin.getDataFolder() + Config.DIR_GENERATORS + this.getActionType().getName(), this.name + ".yml");
        if (file.exists()) return null;

        JYML cfg = new JYML(file);
        Generator generator = new Generator(plugin, cfg);
        generator.setType(this.actionType);
        generator.getObjectiveList().getObjectMap().putAll(this.objectives.getObjectMap());
        generator.getConditionList().getObjectMap().putAll(this.conditions.getObjectMap());
        generator.getRewardList().getObjectMap().putAll(this.rewards.getObjectMap());
        generator.save();
        return generator;
    }
}
