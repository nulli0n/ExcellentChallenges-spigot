package su.nightexpress.excellentchallenges.challenge.difficulty;

import org.jetbrains.annotations.NotNull;

import java.util.function.BiFunction;

public enum ModifierAction {

    NONE((origin, mod) -> origin),
    ADD(Double::sum),
    MULTIPLY((origin, mod) -> origin * mod);

    private final BiFunction<Double, Double, Double> function;

    ModifierAction(@NotNull BiFunction<Double, Double, Double> function) {
        this.function = function;
    }

    public double calculate(double origin, double mod) {
        return this.function.apply(origin, mod);
    }
}
