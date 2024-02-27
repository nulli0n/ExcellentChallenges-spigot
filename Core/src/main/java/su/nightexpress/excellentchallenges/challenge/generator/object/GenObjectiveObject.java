package su.nightexpress.excellentchallenges.challenge.generator.object;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentchallenges.challenge.difficulty.DifficultyValue;
import su.nightexpress.nightcore.config.FileConfig;

import java.util.Map;
import java.util.Set;

public class GenObjectiveObject extends GenAmountObject {

    private final DifficultyValue progress;

    public GenObjectiveObject(@NotNull String id, double weight,
                              @NotNull Map<String, Set<String>> items,
                              @NotNull DifficultyValue amount,
                              @NotNull DifficultyValue progress
                              ) {
        super(id, weight, items, amount, false);
        this.progress = progress;
    }

    @NotNull
    public static GenObjectiveObject read(@NotNull FileConfig cfg, @NotNull String path, @NotNull String id) {
        GenAmountObject parent = GenAmountObject.read(cfg, path, id);

        cfg.setComments(path + ".Progress",
            "Determines the amount needs to be completed for each objective picked from the 'Items' list."
        );

        DifficultyValue progress = DifficultyValue.read(cfg, path + ".Progress");

        return new GenObjectiveObject(id, parent.getWeight(), /*name, nameListId, icons,*/ parent.getItems(), parent.getAmount(), progress);
    }

    @Override
    public void write(@NotNull FileConfig cfg, @NotNull String path) {
        super.write(cfg, path);

        this.getProgress().write(cfg, path + ".Progress");
    }

    @NotNull
    public DifficultyValue getProgress() {
        return progress;
    }
}
