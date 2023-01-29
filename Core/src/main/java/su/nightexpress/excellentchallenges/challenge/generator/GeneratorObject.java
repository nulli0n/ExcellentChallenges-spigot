package su.nightexpress.excellentchallenges.challenge.generator;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentchallenges.challenge.config.ChallengeScaler;

public class GeneratorObject {

    private final String          name;
    private final ChallengeScaler chance;
    private final int             levelMin;
    private final int             levelMax;

    public GeneratorObject(@NotNull String name, @NotNull ChallengeScaler chance, int levelMin, int levelMax) {
        this.name = name;
        this.chance = chance;
        this.levelMin = levelMin;
        this.levelMax = levelMax;
    }

    @NotNull
    public String getName() {
        return name;
    }

    @NotNull
    public ChallengeScaler getChance() {
        return chance;
    }

    public int getLevelMin() {
        return levelMin;
    }

    public int getLevelMax() {
        return levelMax;
    }
}
