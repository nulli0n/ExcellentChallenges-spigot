package su.nightexpress.excellentchallenges.challenge.difficulty;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nexmedia.engine.api.config.JYML;
import su.nexmedia.engine.utils.Colorizer;
import su.nexmedia.engine.utils.StringUtil;
import su.nexmedia.engine.utils.values.UniInt;

import java.util.HashMap;
import java.util.Map;

public class Difficulty {

    public static final String DEF_CHILD   = "childish";
    public static final String DEF_EASY    = "easy";
    public static final String DEF_MEDIUM  = "medium";
    public static final String DEF_HARD    = "hard";
    public static final String DEF_EXTREME = "extreme";

    private final String id;
    private final String name;
    //@Deprecated private final String nameFormat;
    private final UniInt levels;
    private final Map<String, DifficultyModifier> modifiers;

    public Difficulty(@NotNull String id, @NotNull String name,
                      //@NotNull String nameFormat,
                      @NotNull UniInt levels,
                      @NotNull Map<String, DifficultyModifier> modifiers) {
        this.id = id.toLowerCase();
        this.name = Colorizer.apply(name);
        //this.nameFormat = Colorizer.apply(nameFormat);
        this.levels = levels;
        this.modifiers = modifiers;
    }

    @NotNull
    public static Difficulty read(@NotNull JYML cfg, @NotNull String path, @NotNull String id) {
        String name = cfg.getString(path + ".Name", StringUtil.capitalizeUnderscored(id));
        //String nameFormat = cfg.getString(path + ".Challenge_Name_Format", Placeholders.CHALLENGE_NAME);
        UniInt levels = UniInt.read(cfg, path + ".Levels");
        Map<String, DifficultyModifier> modifiers = new HashMap<>();
        for (String modId : cfg.getSection(path + ".Modifiers")) {
            DifficultyModifier modifier = DifficultyModifier.read(cfg, path + ".Modifiers." + modId);
            modifiers.put(modId.toLowerCase(), modifier);
        }

        return new Difficulty(id, name, levels, modifiers);
    }

    public void write(@NotNull JYML cfg, @NotNull String path) {
        cfg.set(path + ".Name", this.getName());
        //cfg.set(path + ".Challenge_Name_Format", this.getNameFormat());
        this.getLevels().write(cfg, path + ".Levels");

        cfg.remove(path + ".Modifiers");
        this.getModifiers().forEach((id, modifier) -> {
            modifier.write(cfg, path + ".Modifiers." + id);
        });
    }

    /*@NotNull
    public String formatChallengeName(@NotNull GeneratedChallenge challenge) {
        return this.getNameFormat().replace(Placeholders.CHALLENGE_NAME, challenge.getName());
    }*/

    public int createLevel() {
        return this.getLevels().roll();
    }

    @Nullable
    public DifficultyModifier getModifier(@NotNull String id) {
        return this.getModifiers().get(id.toLowerCase());
    }

    @NotNull
    public String getId() {
        return id;
    }

    @NotNull
    public String getName() {
        return name;
    }

    /*@NotNull
    @Deprecated
    public String getNameFormat() {
        return nameFormat;
    }*/

    @NotNull
    public UniInt getLevels() {
        return levels;
    }

    @NotNull
    public Map<String, DifficultyModifier> getModifiers() {
        return modifiers;
    }
}
