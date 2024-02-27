package su.nightexpress.excellentchallenges.challenge.generator.object;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentchallenges.Placeholders;
import su.nightexpress.excellentchallenges.challenge.difficulty.Difficulty;
import su.nightexpress.nightcore.config.ConfigValue;
import su.nightexpress.nightcore.config.FileConfig;

import java.util.*;

public class GenObject {

    protected final String                   id;
    protected final double                   weight;
    protected final Map<String, Set<String>> items;

    public GenObject(@NotNull String id, double weight,
                     @NotNull Map<String, Set<String>> items) {
        this.id = id.toLowerCase();
        this.weight = weight;
        this.items = items;
    }

    @NotNull
    public static GenObject read(@NotNull FileConfig cfg, @NotNull String path, @NotNull String id) {
        double weight = ConfigValue.create(path + ".Weight", 50D,
            "Determines the chance for this object to be picked in generation prior to others.",
            "The higher the value, the greater the chance."
        ).read(cfg);

        Map<String, Set<String>> items = ConfigValue.create(path + ".Items",
            (cfg2, path2, def) -> {
                Map<String, Set<String>> map = new HashMap<>();
                for (String diffs : cfg2.getSection(path2)) {
                    for (String diffId : diffs.split(",")) {
                        map.put(diffId.toLowerCase(), cfg2.getStringSet(path2 + "." + diffs)/*.stream().map(String::toLowerCase).collect(Collectors.toSet())*/);
                    }
                }
                return map;
            },
            (cfg2, path2, map) -> map.forEach((sId, set) -> cfg2.set(path2 + "." + sId, set)),
            () -> {
                Map<String, Set<String>> map = new HashMap<>();
                map.put(Placeholders.WILDCARD, new HashSet<>());
                return map;
            },
            "A list of items for random selection depends on difficulty.",
            "Items:",
            "-- easy,hard:",
            "-- - item1",
            "-- - item2",
            "You can provide multiple difficulties for the same list - split them with a comma.",
            "You can use '" + Placeholders.WILDCARD + "' to make list available for any difficult."
        ).read(cfg);

        return new GenObject(id, weight, items);
    }

    public void write(@NotNull FileConfig cfg, @NotNull String path) {
        cfg.set(path + ".Weight", this.getWeight());
        cfg.remove(path + ".Items");

        Map<String, Set<String>> reversed = new HashMap<>();
        Map<String, Set<String>> copyItemMap = new HashMap<>(this.getItems());
        Set<String> diffs = new HashSet<>(this.getItems().keySet());

        for (String diff : diffs) {
            Map<String, Set<String>> copy = new HashMap<>(copyItemMap);

            Set<String> originItems = copy.remove(diff);
            if (originItems == null) continue;

            StringBuilder fusionName = new StringBuilder(diff);

            for (var entry : new HashMap<>(copy).entrySet()) {
                Set<String> otherItems = entry.getValue();

                if (originItems.containsAll(otherItems) && otherItems.containsAll(originItems)) {
                    fusionName.append(",").append(entry.getKey());
                    copy.remove(entry.getKey());
                    copyItemMap.remove(entry.getKey());
                }
            }

            reversed.put(fusionName.toString(), originItems);
        }

        /*this.getItems()*/reversed.forEach((difId, items) -> {
            cfg.set(path + ".Items." + difId, items);
        });
    }

    @NotNull
    public Set<String> getItems(@NotNull Difficulty difficulty) {
        return this.getItems(difficulty.getId());
    }

    @NotNull
    public Set<String> getItems(@NotNull String diffId) {
        return this.getItems().getOrDefault(diffId.toLowerCase(), this.getItems().getOrDefault(Placeholders.WILDCARD, Collections.emptySet()));
    }

    @NotNull
    public String getId() {
        return id;
    }

    public double getWeight() {
        return weight;
    }

    @NotNull
    public Map<String, Set<String>> getItems() {
        return items;
    }
}
