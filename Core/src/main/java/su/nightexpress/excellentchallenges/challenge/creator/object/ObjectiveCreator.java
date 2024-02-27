package su.nightexpress.excellentchallenges.challenge.creator.object;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentchallenges.challenge.action.ActionType;
import su.nightexpress.excellentchallenges.challenge.difficulty.DifficultyModifier;
import su.nightexpress.excellentchallenges.challenge.difficulty.DifficultyValue;
import su.nightexpress.excellentchallenges.challenge.difficulty.ModifierAction;
import su.nightexpress.excellentchallenges.challenge.generator.object.GenObjectiveObject;
import su.nightexpress.nightcore.util.wrapper.UniInt;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public class ObjectiveCreator<O> extends ObjectListCreator {

    private final ActionType<?, O> actionType;

    protected DifficultyValue progress;

    public ObjectiveCreator(@NotNull ActionType<?, O> actionType, int index) {
        super(index);
        this.actionType = actionType;
    }

    @NotNull
    @Override
    public ObjectiveCreator<O> weight(double weight) {
        super.weight(weight);
        return this;
    }

    public final ObjectiveCreator<O> addItems(@NotNull Set<String> diffs, @NotNull Collection<O> items) {
        List<String> itemsRaw = items.stream().map(this.actionType::getObjectName).toList();
        this.addItemsRaw(diffs, itemsRaw);
        return this;
    }

    public ObjectiveCreator<O> amountObjectives(int min, int max) {
        this.amount(min, max, DifficultyModifier.DEF_OBJECTIVE_AMOUNT_ID);
        return this;
    }

    @NotNull
    public ObjectiveCreator<O> progress(int min, int max) {
        this.progress = new DifficultyValue(UniInt.of(min, max), DifficultyModifier.DEF_OBJECTIVE_PROGRESS_ID, ModifierAction.MULTIPLY);
        return this;
    }

    public GenObjectiveObject pack() {
        return new GenObjectiveObject(
            String.valueOf(index),
            weight,
            items, amount, progress
        );
    }
}
