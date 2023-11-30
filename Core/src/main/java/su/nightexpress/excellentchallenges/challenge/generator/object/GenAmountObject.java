package su.nightexpress.excellentchallenges.challenge.generator.object;

import org.jetbrains.annotations.NotNull;
import su.nexmedia.engine.api.config.JYML;
import su.nexmedia.engine.utils.random.Rnd;
import su.nightexpress.excellentchallenges.challenge.difficulty.Difficulty;
import su.nightexpress.excellentchallenges.challenge.difficulty.DifficultyValue;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class GenAmountObject extends GenObject {

    private final DifficultyValue amount;

    public GenAmountObject(@NotNull String id, double weight,
                           @NotNull Map<String, Set<String>> items,
                           @NotNull DifficultyValue amount) {
        super(id, weight, items);
        this.amount = amount;
    }

    @NotNull
    public static GenAmountObject read(@NotNull JYML cfg, @NotNull String path, @NotNull String id) {
        GenObject parent = GenObject.read(cfg, path, id);

        cfg.setComments(path + ".Amount",
            "Determines how many items from the 'Items' list will be picked on generation."
        );

        DifficultyValue amount = DifficultyValue.read(cfg, path + ".Amount");

        return new GenAmountObject(id, parent.getWeight(), parent.getItems(), amount);
    }

    @Override
    public void write(@NotNull JYML cfg, @NotNull String path) {
        super.write(cfg, path);

        this.getAmount().write(cfg, path + ".Amount");
    }

    @NotNull
    public Set<String> pickItems(@NotNull Difficulty difficulty, int level) {
        Set<String> originItems = new HashSet<>(this.getItems(difficulty));
        Set<String> items = new HashSet<>();

        int amount = this.getAmount().createValue(difficulty, level);
        if (amount <= 0) return items;

        while (amount > 0 && !originItems.isEmpty()) {
            String item = Rnd.get(originItems);

            items.add(item);
            amount--;
            originItems.remove(item);
        }

        return items;
    }

    @NotNull
    public DifficultyValue getAmount() {
        return amount;
    }
}
