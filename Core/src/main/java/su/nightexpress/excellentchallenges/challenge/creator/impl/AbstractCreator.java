package su.nightexpress.excellentchallenges.challenge.creator.impl;

import su.nightexpress.excellentchallenges.Placeholders;
import su.nightexpress.excellentchallenges.challenge.action.ActionType;
import su.nightexpress.excellentchallenges.config.Config;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentchallenges.ChallengesPlugin;
import su.nightexpress.excellentchallenges.challenge.creator.object.Creator;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.util.Lists;
import su.nightexpress.nightcore.util.wrapper.UniInt;

import java.util.*;

public abstract class AbstractCreator<O> {

    protected static final String VARIOUS = "various";

    protected final ChallengesPlugin plugin;

    public AbstractCreator(@NotNull ChallengesPlugin plugin) {
        this.plugin = plugin;
    }

    public abstract void create();

    @NotNull
    public abstract Set<String> getConditions(@NotNull ActionType<?, O> actionType);

    @NotNull
    public abstract Set<String> getRewards(@NotNull ActionType<?, O> actionType);

    @NotNull
    public abstract UniInt getMinProgress(@NotNull ActionType<?, O> actionType);

    @NotNull
    public abstract UniInt getMaxProgress(@NotNull ActionType<?, O> actionType);

    public void createGenerator(@NotNull ActionType<?, O> actionType,
                                @NotNull Map<String, Set<O>> map) {

        Map<String, List<String>> conditionIds = new HashMap<>();
        for (String fileName : this.getConditions(actionType)) {
            FileConfig cfg = new FileConfig(plugin.getDataFolder() + Config.DIR_CONDITIONS, fileName + ".yml");
            conditionIds.put(fileName, new ArrayList<>(cfg.getSection("")));
        }

        Map<String, List<String>> rewardIds = new HashMap<>();
        for (String fileName : this.getRewards(actionType)) {
            FileConfig cfg = new FileConfig(plugin.getDataFolder() + Config.DIR_REWARDS, fileName + ".yml");
            rewardIds.put(fileName, new ArrayList<>(cfg.getSection("")));
        }

        for (var entry : map.entrySet()) {
            String prefix = entry.getKey();
            //String nameListId = actionType.getName() + NAMES_LIST_SUFFIX;
            List<O> materialGroup = entry.getValue().stream().sorted(Comparator.comparing(actionType::getObjectName)).toList();

            Creator<O> creator = new Creator<>(plugin, actionType, actionType.getName() + "_" + prefix);
            boolean doSplit = prefix.equalsIgnoreCase(VARIOUS) || materialGroup.size() >= 8;

            for (List<O> list : Lists.split(materialGroup, doSplit ? 8 : materialGroup.size())) {
                UniInt min = this.getMinProgress(actionType);
                UniInt max = this.getMaxProgress(actionType);

                creator.createObjective(creator.objectiveCreator()
                    .weight(50)
                    .addItems(Set.of(Placeholders.WILDCARD), list)
                    .amountObjectives(1, 4)
                    .progress(min.roll(), max.roll())
                );
            }

            for (var conditionEntry : conditionIds.entrySet()) {
                creator.createCondition(creator.conditionsCreator()
                    .weight(50)
                    .addItemsRaw(Set.of(Placeholders.WILDCARD), conditionEntry.getValue())
                    .amountConditions(0, 1)
                );
            }

            for (var rewardEntry : rewardIds.entrySet()) {
                creator.createReward(creator.rewardsCreator()
                    .weight(50)
                    .addItemsRaw(Set.of(Placeholders.WILDCARD), rewardEntry.getValue())
                    .amountRewards(1, 1)
                );
            }

            creator.createGenerator();
        }
    }
}
