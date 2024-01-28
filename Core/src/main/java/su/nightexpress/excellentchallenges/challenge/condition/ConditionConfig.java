package su.nightexpress.excellentchallenges.challenge.condition;

import org.jetbrains.annotations.NotNull;
import su.nexmedia.engine.api.config.JYML;
import su.nexmedia.engine.api.placeholder.Placeholder;
import su.nexmedia.engine.api.placeholder.PlaceholderMap;
import su.nexmedia.engine.utils.Colorizer;
import su.nexmedia.engine.utils.Pair;
import su.nexmedia.engine.utils.StringUtil;
import su.nightexpress.excellentchallenges.Placeholders;

import java.util.*;

public class ConditionConfig implements Placeholder {

    private final String id;
    private final Map<String, List<Pair<Condition<?, ?>, String>>> conditionMap;
    private final PlaceholderMap                                   placeholderMap;

    private String name;
    private List<String> description;

    public ConditionConfig(@NotNull String id,
                           @NotNull String name, @NotNull List<String> description,
                           @NotNull Map<String, List<Pair<Condition<?, ?>, String>>> conditionMap) {
        this.id = id.toLowerCase();
        this.setName(name);
        this.setDescription(description);
        this.conditionMap = conditionMap;

        this.placeholderMap = new PlaceholderMap()
            .add(Placeholders.CONDITION_ID, this::getId)
            .add(Placeholders.CONDITION_NAME, this::getName)
            .add(Placeholders.CONDITION_DESCRIPTION, () -> String.join("\n", this.getDescription()));
    }

    @NotNull
    public static ConditionConfig read(@NotNull JYML cfg, @NotNull String path, @NotNull String id) {
        String name = Colorizer.apply(cfg.getString(path + ".Name", StringUtil.capitalizeUnderscored(id)));
        List<String> description = Colorizer.apply(cfg.getStringList(path + ".Description"));

        Map<String, List<Pair<Condition<?, ?>, String>>> conditionMap = new HashMap<>();
        for (String sId : cfg.getSection(path + ".Conditions")) {
            List<Pair<Condition<?, ?>, String>> conditions = new ArrayList<>();

            List<String> list = cfg.getStringList(path + ".Conditions." + sId);
            for (String raw : list) {
                String conditionName = raw.split(" ")[0].replace("[", "").replace("]", "");
                Condition<?, ?> condition = Conditions.getByName(conditionName);
                if (condition == null) {
                    //plugin.error("Invalid condition: " + conditionName);
                    continue;
                }

                conditions.add(Pair.of(condition, raw));
            }
            conditionMap.put(sId.toLowerCase(), conditions);
        }

        return new ConditionConfig(id, name, description, conditionMap);
    }

    /*@Override
    public boolean load() {
        this.name = Colorizer.apply(cfg.getString("Name", StringUtil.capitalizeUnderscored(this.getId())));
        this.description = Colorizer.apply(cfg.getStringList("Description"));

        for (String sId : cfg.getSection("Conditions")) {
            List<Pair<Condition<?, ?>, String>> conditions = new ArrayList<>();

            List<String> list = cfg.getStringList("Conditions." + sId);
            for (String raw : list) {
                String conditionName = raw.split(" ")[0].replace("[", "").replace("]", "");
                Condition<?, ?> condition = Conditions.getByName(conditionName);
                if (condition == null) {
                    plugin.error("Invalid condition: " + conditionName);
                    continue;
                }

                conditions.add(Pair.of(condition, raw));
            }
            this.conditionMap.put(sId.toLowerCase(), conditions);
        }

        return true;
    }*/

    public void write(@NotNull JYML cfg, @NotNull String path) {
        cfg.set(path + ".Name", this.getName());
        cfg.set(path + ".Description", this.getDescription());
        cfg.remove(path + ".Conditions");
        this.getConditionMap().forEach((id, list) -> {
            cfg.set(path + ".Conditions." + id, list.stream().map(Pair::getSecond).toList());
        });
    }

    @Override
    @NotNull
    public PlaceholderMap getPlaceholders() {
        return this.placeholderMap;
    }

    @NotNull
    public List<Pair<Condition<?, ?>, String>> getConditions(@NotNull String id) {
        return this.getConditionMap().getOrDefault(id.toLowerCase(), Collections.emptyList());
    }

    @NotNull
    public String getId() {
        return id;
    }

    @NotNull
    public String getName() {
        return name;
    }

    public void setName(@NotNull String name) {
        this.name = Colorizer.apply(name);
    }

    @NotNull
    public List<String> getDescription() {
        return description;
    }

    public void setDescription(@NotNull List<String> description) {
        this.description = Colorizer.apply(description);
    }

    @NotNull
    public Map<String, List<Pair<Condition<?, ?>, String>>> getConditionMap() {
        return conditionMap;
    }
}
