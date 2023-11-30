package su.nightexpress.excellentchallenges.challenge.condition;

import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.stream.Stream;

public enum Operator {

    EQUAL("="),
    NOT_EQUAL("!="),
    GREATER(">"),
    SMALLER("<"),
    EACH("%"),
    EACH_NOT("!%");

    private final String raw;

    Operator(@NotNull String raw) {
        this.raw = raw;
    }

    @NotNull
    public String getRaw() {
        return raw;
    }

    @NotNull
    public static Optional<Operator> fromString(@NotNull String str) {
        return Stream.of(Operator.values()).filter(operator -> operator.getRaw().equalsIgnoreCase(str))
            .findFirst();
    }
}
