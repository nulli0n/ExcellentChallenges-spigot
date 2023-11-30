package su.nightexpress.excellentchallenges.challenge.difficulty;

import org.jetbrains.annotations.NotNull;
import su.nexmedia.engine.api.config.JYML;

public class DifficultyModifier {

    public static final DifficultyModifier BASE_1 = new DifficultyModifier(1D, 0D);
    public static final DifficultyModifier ZERO   = new DifficultyModifier(0D, 0D);

    public static final String DEF_OBJECTIVE_AMOUNT_ID   = "objective_amount";
    public static final String DEF_OBJECTIVE_PROGRESS_ID = "objective_progress";
    public static final String DEF_CONDITIONS_AMOUNT_ID  = "conditions_amount";
    public static final String DEF_REWARDS_AMOUNT_ID     = "rewards_amount";

    private final double base;
    private final double perLevel;

    public DifficultyModifier(double base, double perLevel) {
        this.base = base;
        this.perLevel = perLevel;
    }

    @NotNull
    public static DifficultyModifier read(@NotNull JYML cfg, @NotNull String path) {
        double base = cfg.getDouble(path + ".Base");
        double perLevel = cfg.getDouble(path + ".Per_Level");

        return new DifficultyModifier(base, perLevel);
    }

    public void write(@NotNull JYML cfg, @NotNull String path) {
        cfg.set(path + ".Base", this.getBase());
        cfg.set(path + ".Per_Level", this.getPerLevel());
    }

    public double createModifier(int level) {
        double perLevel = this.getPerLevel() * level;

        return this.getBase() + perLevel;
    }

    public double getBase() {
        return base;
    }

    public double getPerLevel() {
        return perLevel;
    }
}
