package su.nightexpress.excellentchallenges.challenge.generator;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nexmedia.engine.api.config.JYML;
import su.nexmedia.engine.api.manager.AbstractConfigHolder;
import su.nexmedia.engine.utils.Colorizer;
import su.nexmedia.engine.utils.random.Rnd;
import su.nightexpress.excellentchallenges.ExcellentChallenges;
import su.nightexpress.excellentchallenges.ExcellentChallengesAPI;
import su.nightexpress.excellentchallenges.Placeholders;
import su.nightexpress.excellentchallenges.challenge.config.ChallengeScaler;
import su.nightexpress.excellentchallenges.config.Config;

import java.util.*;

public class ChallengeGenerator extends AbstractConfigHolder<ExcellentChallenges> {

    private Set<String>  names;
    private List<String> description;
    private ItemStack    iconActive;
    private ItemStack    iconCompleted;
    private int          levelMin;
    private int          levelMax;

    private       ChallengeScaler                objectivesAmountMin;
    private       ChallengeScaler                objectivesAmountMax;
    private final Set<GeneratorObject>           objectivesList;
    private final Map<String, ChallengeScaler[]> objectivesProgress;

    private       ChallengeScaler      conditionsAmountMin;
    private       ChallengeScaler      conditionsAmountMax;
    private final Set<GeneratorObject> conditionsWorlds;

    private       ChallengeScaler      rewardsAmountMin;
    private       ChallengeScaler      rewardsAmountMax;
    private final Set<GeneratorObject> rewardsList;

    public ChallengeGenerator(@NotNull ExcellentChallenges plugin, @NotNull JYML cfg) {
        super(plugin, cfg);

        this.objectivesList = new HashSet<>();
        this.objectivesProgress = new HashMap<>();
        this.conditionsWorlds = new HashSet<>();
        this.rewardsList = new HashSet<>();
    }

    public static void lazyCreate(@NotNull String id, @NotNull String... objectives) {
        JYML cfg = new JYML(ExcellentChallengesAPI.PLUGIN.getDataFolder() + Config.DIR_GENERATORS, id + ".yml");

        cfg.set("Names", Collections.singletonList(id));
        cfg.set("Description", new ArrayList<>());
        cfg.setItem("Icon.Active", new ItemStack(Material.BOOK));
        cfg.setItem("Icon.Completed", new ItemStack(Material.LIME_STAINED_GLASS_PANE));
        cfg.set("Level.Minimum", 1);
        cfg.set("Level.Maximum", 5);

        cfg.set("Objectives.Amount.Minimum", "1");
        cfg.set("Objectives.Amount.Maximum", "0 + " + Placeholders.GENERIC_LEVEL);
        for (String objective : objectives) {
            String path = "Objectives.List." + objective + ".";

            cfg.set(path + "Chance", 50D);
            cfg.set(path + "Level_Min", 1);
            cfg.set(path + "Level_Max", 5);

            String path2 = "Objectives.Progress." + objective + ".";
            cfg.set(path2 + "Minimum", "30 * " + Placeholders.GENERIC_LEVEL);
            cfg.set(path2 + "Maximum", "60 * " + Placeholders.GENERIC_LEVEL);
        }

        cfg.set("Conditions.Amount.Minimum", "0");
        cfg.set("Conditions.Amount.Maximum", "1");
        for (String world : new String[]{"world", "world_nether", "world_the_end"}) {
            String path = "Conditions.Worlds." + world + ".";

            cfg.set(path + "Chance", "0");
            cfg.set(path + "Level_Min", 1);
            cfg.set(path + "Level_Max", 5);
        }

        cfg.set("Rewards.Amount.Minimum", "1");
        cfg.set("Rewards.Amount.Maximum", "1");
        for (String reward : new String[]{"eco_250", "eco_500", "eco_750", "eco_1000"}) {
            String path = "Rewards.List." + reward + ".";

            cfg.set(path + "Chance", "50");
            cfg.set(path + "Level_Min", 1);
            cfg.set(path + "Level_Max", 5);
        }

        cfg.saveChanges();
    }

    @Override
    public boolean load() {
        this.names = Colorizer.apply(cfg.getStringSet("Names"));
        this.description = Colorizer.apply(cfg.getStringList("Description"));
        this.iconActive = cfg.getItem("Icon.Active");
        this.iconCompleted = cfg.getItem("Icon.Completed");
        this.levelMin = cfg.getInt("Level.Minimum", 1);
        this.levelMax = cfg.getInt("Level.Maximum", this.levelMin);

        this.objectivesAmountMin = new ChallengeScaler(this, "Objectives.Amount.Minimum");
        this.objectivesAmountMax = new ChallengeScaler(this, "Objectives.Amount.Maximum");
        this.loadObjects(this.objectivesList, "Objectives.List");
        this.objectivesProgress.clear();
        for (String sRaw : cfg.getSection("Objectives.Progress")) {
            String path2 = "Objectives.Progress." + sRaw + ".";

            ChallengeScaler pMin = new ChallengeScaler(this, path2 + "Minimum");
            ChallengeScaler pMax = new ChallengeScaler(this, path2 + "Maximum");

            for (String sId : sRaw.split(",")) {
                this.objectivesProgress.put(sId.toLowerCase(), new ChallengeScaler[]{pMin, pMax});
            }
        }

        this.conditionsAmountMin = new ChallengeScaler(this, "Conditions.Amount.Minimum");
        this.conditionsAmountMax = new ChallengeScaler(this, "Conditions.Amount.Maximum");
        this.loadObjects(this.conditionsWorlds, "Conditions.Worlds");

        this.rewardsAmountMin = new ChallengeScaler(this, "Rewards.Amount.Minimum");
        this.rewardsAmountMax = new ChallengeScaler(this, "Rewards.Amount.Maximum");
        this.loadObjects(this.rewardsList, "Rewards.List");

        return true;
    }

    private void loadObjects(@NotNull Set<GeneratorObject> loadTo, @NotNull String path) {
        loadTo.clear();
        for (String sRaw : cfg.getSection(path)) {
            String path2 = path + "." + sRaw + ".";

            ChallengeScaler oChance = new ChallengeScaler(this, path2 + "Chance");
            int oMinLevel = cfg.getInt(path2 + "Level_Min");
            int oMaxLevel = cfg.getInt(path2 + "Level_Max");

            for (String sId : sRaw.split(",")) {
                loadTo.add(new GeneratorObject(sId.toLowerCase(), oChance, oMinLevel, oMaxLevel));
            }
        }
    }

    @Override
    public void onSave() {

    }

    @NotNull
    public List<String> getDescription() {
        return this.description;
    }

    @NotNull
    public ItemStack getIconActive() {
        return new ItemStack(iconActive);
    }

    @NotNull
    public ItemStack getIconCompleted() {
        return new ItemStack(iconCompleted);
    }

    public int getLevelMin() {
        return levelMin;
    }

    public int getLevelMax() {
        return levelMax;
    }

    @NotNull
    public Map<String, ChallengeScaler[]> getObjectivesProgress() {
        return objectivesProgress;
    }

    private int pickAmount(@NotNull ChallengeScaler smin, @NotNull ChallengeScaler smax, int level) {
        int min = Math.max(0, (int) smin.getValue(level));
        int max = Math.max(min, (int) smax.getValue(level));

        return Rnd.get(min, max);
    }

    @NotNull
    private Set<String> pickObjects(int level, int amount, @NotNull Set<GeneratorObject> from) {
        Set<String> list = new HashSet<>();
        Map<String, Double> mapChance = new HashMap<>();

        Set<GeneratorObject> source = new HashSet<>(from);
        source.removeIf(obj -> level < obj.getLevelMin() || level > obj.getLevelMax());
        source.forEach(obj -> mapChance.put(obj.getName(), obj.getChance().getValue(level)));

        while (amount > 0 && !mapChance.isEmpty() && mapChance.values().stream().mapToDouble(d -> d).sum() > 0) {
            String pick = Rnd.getByWeight(mapChance);
            list.add(pick);
            mapChance.remove(pick);
            amount--;
        }

        return list;
    }

    @Nullable
    public String pickName() {
        return Rnd.get(this.names);
    }

    public int pickLevel() {
        return Rnd.get(this.getLevelMin(), this.getLevelMax());
    }

    public int pickObjectivesAmount(int level) {
        return this.pickAmount(this.objectivesAmountMin, this.objectivesAmountMax, level);
    }

    @NotNull
    public Set<String> pickObjectives(int level) {
        int amount = this.pickObjectivesAmount(level);
        return this.pickObjects(level, amount, this.objectivesList);
    }

    public int pickObjectiveProgress(@NotNull String id, int level) {
        ChallengeScaler[] values = this.objectivesProgress.getOrDefault(id.toLowerCase(), this.objectivesProgress.get(Placeholders.DEFAULT));
        if (values == null) return 0;

        return (int) Rnd.getDouble(values[0].getValue(level), values[1].getValue(level));
    }

    @NotNull
    public Map<String, Integer> pickObjectivesProgress(@NotNull Set<String> objectives, int level) {
        Map<String, Integer> map = new HashMap<>();
        objectives.forEach(obj -> map.put(obj, this.pickObjectiveProgress(obj, level)));
        map.values().removeIf(score -> score <= 0D);
        return map;
    }

    public int pickConditionsAmount(int level) {
        return this.pickAmount(this.conditionsAmountMin, this.conditionsAmountMax, level);
    }

    @NotNull
    public Set<String> pickConditionWorlds(int level) {
        return this.pickObjects(level, 1, this.conditionsWorlds);
    }

    public int pickRewardsAmount(int level) {
        return this.pickAmount(this.rewardsAmountMin, this.rewardsAmountMax, level);
    }

    @NotNull
    public Set<String> pickRewards(int level) {
        return this.pickObjects(level, this.pickRewardsAmount(level), this.rewardsList);
    }
}
