package su.nightexpress.excellentchallenges.challenge.creator.object;

import su.nightexpress.excellentchallenges.challenge.difficulty.DifficultyModifier;
import su.nightexpress.excellentchallenges.challenge.difficulty.DifficultyValue;
import su.nightexpress.excellentchallenges.challenge.difficulty.ModifierAction;
import su.nightexpress.excellentchallenges.challenge.generator.object.GenAmountObject;
import org.jetbrains.annotations.NotNull;
import su.nexmedia.engine.utils.values.UniInt;

import java.util.*;

public class ObjectListCreator {

    protected int                      index;
    protected double                   weight;
    protected Map<String, Set<String>> items;
    protected DifficultyValue          amount;

    public ObjectListCreator(int index) {
        this.index = index;
        this.items = new HashMap<>();
    }

    @NotNull
    public ObjectListCreator weight(double weight) {
        this.weight = weight;
        return this;
    }

    public final ObjectListCreator addItemsRaw(@NotNull Set<String> diffs, @NotNull Collection<String> items) {
        for (String difficulty : diffs) {
            this.items.computeIfAbsent(difficulty, k -> new HashSet<>()).addAll(items);
        }
        return this;
    }

    public ObjectListCreator amountConditions(int min, int max) {
        return this.amount(min, max, DifficultyModifier.DEF_CONDITIONS_AMOUNT_ID);
    }

    public ObjectListCreator amountRewards(int min, int max) {
        return this.amount(min, max, DifficultyModifier.DEF_REWARDS_AMOUNT_ID);
    }

    public ObjectListCreator amount(int min, int max, @NotNull String id) {
        this.amount = new DifficultyValue(UniInt.of(min, max), id, ModifierAction.ADD);
        return this;
    }

    public GenAmountObject pack() {
        return new GenAmountObject(String.valueOf(index), weight, items, amount);
    }
}
