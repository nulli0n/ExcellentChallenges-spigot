package su.nightexpress.excellentchallenges.challenge.condition;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nexmedia.engine.utils.TriFunction;

import java.util.function.Function;

public class Condition<P, E> {

    private final String                               name;
    private final Function<String, P>                  parser;
    private final Function<Player, E>                  extractor;
    private final TriFunction<E, P, Operator, Boolean> tester;

    public Condition(@NotNull String name,
                     @NotNull Function<String, P> parser,
                     @NotNull Function<Player, E> extractor,
                     @NotNull TriFunction<E, P, Operator, Boolean> tester
    ) {
        this.name = name.toLowerCase();
        this.parser = parser;
        this.extractor = extractor;
        this.tester = tester;
    }

    @Nullable
    public CompiledCondition<P, E> compile(@NotNull String str) {
        String[] split = str.split(" ");
        if (split.length < 3) return null;

        P value = this.getParser().apply(split[2]);
        if (value == null) return null;

        Operator operator = Operator.fromString(split[1]).orElse(Operator.EQUAL);
        return new CompiledCondition<>(this, operator, value);
    }

    @NotNull
    public String getName() {
        return name;
    }

    @NotNull
    public Function<String, P> getParser() {
        return parser;
    }

    @NotNull
    public Function<Player, E> getExtractor() {
        return extractor;
    }

    @NotNull
    public TriFunction<E, P, Operator, Boolean> getTester() {
        return tester;
    }
}
