package su.nightexpress.excellentchallenges.challenge.difficulty;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.config.ConfigValue;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.util.StringUtil;
import su.nightexpress.nightcore.util.wrapper.UniInt;

public class DifficultyValue {

    private final UniInt         value;
    private final String         modifierId;
    private final ModifierAction modifierAction;

    public DifficultyValue(@NotNull UniInt value, @NotNull String modifierId, @NotNull ModifierAction modifierAction) {
        this.value = value;
        this.modifierId = modifierId;
        this.modifierAction = modifierAction;
    }

    @NotNull
    public static DifficultyValue readOrCreate(@NotNull FileConfig cfg, @NotNull String path, @NotNull DifficultyValue def,
                                               @Nullable String... comments) {
        return ConfigValue.create(path,
            (cfg1, path1, def1) -> read(cfg1, path1),
            (cfg1, path1, val) -> val.write(cfg1, path1),
            () -> def,
            comments).read(cfg);
    }

    @NotNull
    public static DifficultyValue read(@NotNull FileConfig cfg, @NotNull String path) {
        UniInt value = UniInt.read(cfg, path);

        String modifierId = ConfigValue.create(path + ".Difficulty_Modifier.Id",
            "null",
            "Determines the difficulty modifier used to adjust value generated from 'Min' and 'Max' settings.",
            "You can create & edit modifiers in the config.yml for each difficulty individually."
        ).read(cfg);

        ModifierAction modifierAction = ConfigValue.create(path + ".Difficulty_Modifier.Action",
            ModifierAction.class, ModifierAction.NONE,
            "Sets action to perform between value generated from 'Min' and 'Max' and value from difficulty modifier.",
            "Available actions: " + StringUtil.inlineEnum(ModifierAction.class, ", ")
        ).read(cfg);

        return new DifficultyValue(value, modifierId, modifierAction);
    }

    public void write(@NotNull FileConfig cfg, @NotNull String path) {
        this.getValue().write(cfg, path);
        cfg.set(path + ".Difficulty_Modifier.Id", this.getModifierId());
        cfg.set(path + ".Difficulty_Modifier.Action", this.getModifierAction().name());
    }

    public int createValue(@NotNull Difficulty difficulty, int level) {
        DifficultyModifier modifier = difficulty.getModifier(this.getModifierId());
        if (modifier == null) {
            if (this.getModifierAction() == ModifierAction.MULTIPLY) modifier = DifficultyModifier.BASE_1;
            else modifier = DifficultyModifier.ZERO;
        }

        double value = this.getValue().roll();
        double mod = modifier.createModifier(level);

        return (int) this.getModifierAction().calculate(value, mod);
    }

    @NotNull
    public UniInt getValue() {
        return value;
    }

    @NotNull
    public String getModifierId() {
        return modifierId;
    }

    @NotNull
    public ModifierAction getModifierAction() {
        return modifierAction;
    }
}
