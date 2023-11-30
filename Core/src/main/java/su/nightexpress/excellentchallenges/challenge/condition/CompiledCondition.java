package su.nightexpress.excellentchallenges.challenge.condition;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class CompiledCondition<P, E> {

    private final Condition<P, E> condition;
    private final Operator        operator;
    private final P value;

    public CompiledCondition(
        @NotNull Condition<P, E> condition,
        @NotNull Operator operator,
        @NotNull P value
    ) {
        this.condition = condition;
        this.value = value;
        this.operator = operator;
    }

    public boolean test(@NotNull Player player) {
        E extracted = this.condition.getExtractor().apply(player);
        return this.condition.getTester().apply(extracted, this.getValue(), this.getOperator());
    }

    @NotNull
    public String toRaw() {
        String prefix = "[" + this.getCondition().getName() + "]";
        String oper = this.getOperator().getRaw();
        String value;
        if (this.getValue() instanceof Number[] array) {
            value = String.join(",", Arrays.stream(array).map(String::valueOf).toList());
        }
        else {
            value = String.valueOf(this.getValue());
        }
        return prefix + " " + oper + " " + value;
    }

    @NotNull
    public Condition<?, ?> getCondition() {
        return condition;
    }

    @NotNull
    public Operator getOperator() {
        return operator;
    }

    @NotNull
    public P getValue() {
        return value;
    }
}
