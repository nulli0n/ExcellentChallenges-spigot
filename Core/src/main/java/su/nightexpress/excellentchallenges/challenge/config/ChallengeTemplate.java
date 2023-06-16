package su.nightexpress.excellentchallenges.challenge.config;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nexmedia.engine.api.config.JYML;
import su.nexmedia.engine.api.manager.AbstractConfigHolder;
import su.nexmedia.engine.api.placeholder.Placeholder;
import su.nexmedia.engine.api.placeholder.PlaceholderMap;
import su.nexmedia.engine.utils.CollectionsUtil;
import su.nexmedia.engine.utils.Colorizer;
import su.nexmedia.engine.utils.random.Rnd;
import su.nightexpress.excellentchallenges.ExcellentChallenges;
import su.nightexpress.excellentchallenges.Placeholders;
import su.nightexpress.excellentchallenges.challenge.generator.ChallengeGenerator;
import su.nightexpress.excellentchallenges.challenge.type.ChallengeJobType;

import java.util.HashMap;
import java.util.Map;

public class ChallengeTemplate extends AbstractConfigHolder<ExcellentChallenges> implements Placeholder {

    private ChallengeJobType    jobType;
    private String              name;
    private Map<String, Double> generators;

    private final PlaceholderMap placeholderMap;

    public ChallengeTemplate(@NotNull ExcellentChallenges plugin, @NotNull JYML cfg) {
        super(plugin, cfg);
        this.generators = new HashMap<>();
        this.placeholderMap = new PlaceholderMap()
            .add(Placeholders.TEMPLATE_NAME, this::getName)
        ;
    }

    @Override
    public boolean load() {
        this.jobType = cfg.getEnum("Type", ChallengeJobType.class);
        if (this.jobType == null) {
            this.plugin.error("Invalid challenge job type!");
            return false;
        }

        this.name = Colorizer.apply(cfg.getString("Name", this.getId()));

        this.generators.clear();
        for (String sId : cfg.getSection("Generators")) {
            double chance = cfg.getDouble("Generators." + sId + ".Chance");
            if (chance <= 0D) continue;

            this.generators.put(sId.toLowerCase(), chance);
        }
        this.generators.keySet().removeIf(genId -> plugin.getChallengeManager().getGenerator(genId) == null);
        this.generators = CollectionsUtil.sortAscent(this.generators);
        return true;
    }

    @Override
    public void onSave() {

    }

    @Override
    @NotNull
    public PlaceholderMap getPlaceholders() {
        return this.placeholderMap;
    }

    @Nullable
    public ChallengeGenerator pickGenerator() {
        if (this.generators.isEmpty()) return null;

        String genId = Rnd.getByWeight(this.generators);
        return plugin.getChallengeManager().getGenerator(genId);
    }

    @NotNull
    public ChallengeJobType getJobType() {
        return jobType;
    }

    @NotNull
    public String getName() {
        return name;
    }

    @NotNull
    public Map<String, Double> getGenerators() {
        return generators;
    }
}
