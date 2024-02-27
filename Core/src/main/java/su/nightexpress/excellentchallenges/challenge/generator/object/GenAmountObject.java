package su.nightexpress.excellentchallenges.challenge.generator.object;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentchallenges.challenge.difficulty.Difficulty;
import su.nightexpress.excellentchallenges.challenge.difficulty.DifficultyValue;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.util.random.Rnd;

import java.util.*;

public class GenAmountObject extends GenObject {

    private final DifficultyValue amount;

    private boolean allowDuplicateSelection;

    public GenAmountObject(@NotNull String id, double weight,
                           @NotNull Map<String, Set<String>> items,
                           @NotNull DifficultyValue amount,
                           boolean allowDuplicateSelection) {
        super(id, weight, items);
        this.amount = amount;
        this.setAllowDuplicateSelection(false);
    }

    @NotNull
    public static GenAmountObject read(@NotNull FileConfig cfg, @NotNull String path, @NotNull String id) {
        GenObject parent = GenObject.read(cfg, path, id);

        cfg.setComments(path + ".Amount",
            "Determines how many items from the 'Items' list will be picked on generation."
        );

        DifficultyValue amount = DifficultyValue.read(cfg, path + ".Amount");

        return new GenAmountObject(id, parent.getWeight(), parent.getItems(), amount, false);
    }

    @Override
    public void write(@NotNull FileConfig cfg, @NotNull String path) {
        super.write(cfg, path);

        this.getAmount().write(cfg, path + ".Amount");
        //cfg.set(path + ".Allow_Duplicates", this.isAllowDuplicateSelection());
    }

    @NotNull
    public Collection<String> pickItems(@NotNull Difficulty difficulty, int level) {
        Set<String> originItems = new HashSet<>(this.getItems(difficulty));
        List<String> items = new ArrayList<>();

        int amount = this.getAmount().createValue(difficulty, level);
        if (amount <= 0) return items;

        while (amount > 0 && !originItems.isEmpty()) {
            String item = Rnd.get(originItems);

            items.add(item);
            amount--;
            if (!this.isAllowDuplicateSelection()) {
                originItems.remove(item);
            }
        }

        return items;
    }

    @NotNull
    public DifficultyValue getAmount() {
        return amount;
    }

    public void setAllowDuplicateSelection(boolean allowDuplicateSelection) {
        this.allowDuplicateSelection = allowDuplicateSelection;
    }

    public boolean isAllowDuplicateSelection() {
        return allowDuplicateSelection;
    }
}
