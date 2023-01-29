package su.nightexpress.excellentchallenges.challenge.config;

import org.jetbrains.annotations.NotNull;
import su.nexmedia.engine.utils.Scaler;
import su.nightexpress.excellentchallenges.Placeholders;
import su.nightexpress.excellentchallenges.challenge.generator.ChallengeGenerator;

public class ChallengeScaler extends Scaler {

    public ChallengeScaler(@NotNull ChallengeGenerator generator, @NotNull String path) {
        super(generator.getConfig(), path, Placeholders.GENERIC_LEVEL, generator.getLevelMin(), generator.getLevelMax());
    }
}
