package su.nightexpress.excellentchallenges.challenge;

import org.jetbrains.annotations.NotNull;
import su.nexmedia.engine.api.manager.IPlaceholder;
import su.nexmedia.engine.utils.NumberUtil;
import su.nexmedia.engine.utils.StringUtil;
import su.nexmedia.engine.utils.random.Rnd;
import su.nightexpress.excellentchallenges.ExcellentChallengesAPI;
import su.nightexpress.excellentchallenges.Placeholders;
import su.nightexpress.excellentchallenges.challenge.config.ChallengeTemplate;
import su.nightexpress.excellentchallenges.challenge.generator.ChallengeGenerator;
import su.nightexpress.excellentchallenges.challenge.type.ChallengeJobType;

import java.util.*;
import java.util.function.UnaryOperator;

public class Challenge implements IPlaceholder {

    private final String             typeId;
    private final String             templateId;
    private final String             generatorId;
    private final ChallengeJobType   jobType;
    private final String             name;
    private final List<String>       description;
    private final int                level;
    private final Map<String, int[]> objectives;
    private final Set<String>        worlds;
    private final Set<String>        rewards;
    private final long               dateCreated;

    public Challenge(@NotNull String typeId, @NotNull String templateId, @NotNull String generatorId, @NotNull ChallengeJobType jobType,
                     @NotNull String name, @NotNull List<String> description, int level, long dateCreated,
                     @NotNull Map<String, int[]> objectives,
                     @NotNull Set<String> worlds, @NotNull Set<String> rewards) {
        this.typeId = typeId.toLowerCase();
        this.templateId = templateId.toLowerCase();
        this.generatorId = generatorId.toLowerCase();
        this.jobType = jobType;
        this.name = StringUtil.color(name);
        this.description = description;
        this.level = Math.max(1, level);
        this.dateCreated = dateCreated;
        this.objectives = objectives;
        this.worlds = worlds;
        this.rewards = rewards;
    }

    /*@NotNull
    public static Challenge from(@NotNull ChallengeTemplate template) {
        ChallengeGenerator generator = template.pickGenerator();
        if (generator == null) {
            throw new IllegalStateException("No challenge generator found from '" + template.getId() + "' template!");
        }
        return from(template, generator, null);
    }*/

    @NotNull
    public static Challenge from(@NotNull ChallengeTemplate template, @NotNull ChallengeGenerator generator, @NotNull ChallengeType type) {
        String typeId = type.getId();
        String configId = template.getId();
        String generatorId = generator.getId();
        ChallengeJobType jobType = template.getJobType();
        String name = generator.pickName();
        if (name == null) {
            throw new IllegalStateException("No challenge name available! Template = '" + template.getId() + "', Generator = '" + generator.getId() + "'.");
        }
        List<String> description = generator.getDescription();
        int level;
        //if (type != null) {
        level = generator.pickLevel();
        if (level > type.getLevels()[1] || level < type.getLevels()[0]) {
            level = Rnd.get(type.getLevels());
        }
        if (level < generator.getLevelMin()) level = generator.getLevelMin();
        if (level > generator.getLevelMin()) level = generator.getLevelMax();
        //}
        //else {
        //    level = generator.pickLevel();
        //}
        long dateCreated = System.currentTimeMillis();

        Set<String> objectivesList = generator.pickObjectives(level);
        Map<String, Integer> objectives = generator.pickObjectivesProgress(objectivesList, level);
        if (objectives.isEmpty()) {
            throw new IllegalStateException("No challenge objectives available! Template = '" + template.getId() + "', Generator = '" + generator.getId() + "'.");
        }

        Map<String, int[]> objectives2 = new HashMap<>();
        objectives.forEach((oName, value) -> {
            objectives2.put(oName, new int[]{0, value});
        });

        Set<String> worlds = new HashSet<>();
        Set<String> rewards = generator.pickRewards(level);

        int conditions = generator.pickConditionsAmount(level);
        if (conditions > 0) {
            worlds.addAll(generator.pickConditionWorlds(level));
            conditions--;
        }

        return new Challenge(typeId, configId, generatorId, jobType, name, description, level, dateCreated, objectives2, worlds, rewards);
    }

    @NotNull
    public UnaryOperator<String> replacePlaceholders() {
        return line -> line
            .replace(Placeholders.CHALLENGE_NAME, this.getName())
            .replace(Placeholders.CHALLENGE_DESCRIPTION, String.join("\n", this.getDescription()))
            .replace(Placeholders.CHALLENGE_LEVEL, NumberUtil.format(this.getLevel()))
            .replace(Placeholders.CHALLENGE_LEVEL_ROMAN, NumberUtil.toRoman(this.getLevel()))
            .replace(Placeholders.CHALLENGE_PROGRESS_PERCENT, NumberUtil.format(this.getProgressPercent()))
            .replace(Placeholders.CHALLENGE_JOB_TYPE, ExcellentChallengesAPI.PLUGIN.getLangManager().getEnum(this.getJobType()))
            .replace(Placeholders.OBJECTIVE_PROGRESS_PERCENT, NumberUtil.format(this.getProgressPercent()))
            ;
    }

    @NotNull
    public UnaryOperator<String> replacePlaceholders(@NotNull String objId) {
        return line -> {
            line = this.replacePlaceholders().apply(line);
            line = line
                .replace(Placeholders.OBJECTIVE_NAME, this.getJobType().formatObjective(objId))
                .replace(Placeholders.OBJECTIVE_PROGRESS_CURRENT, NumberUtil.format(this.getObjectiveProgressCurrent(objId)))
                .replace(Placeholders.OBJECTIVE_PROGRESS_MAX, NumberUtil.format(this.getObjectiveProgressMax(objId)))
                .replace(Placeholders.OBJECTIVE_PROGRESS_PERCENT, NumberUtil.format(this.getProgressPercent(objId)))
            ;
            return line;
        };
    }

    @NotNull
    public String getTypeId() {
        return typeId;
    }

    @NotNull
    public String getTemplateId() {
        return templateId;
    }

    @NotNull
    public String getGeneratorId() {
        return generatorId;
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
    public List<String> getDescription() {
        return description;
    }

    public int getLevel() {
        return level;
    }

    public long getDateCreated() {
        return dateCreated;
    }

    @NotNull
    public Map<String, int[]> getObjectives() {
        return objectives;
    }

    @NotNull
    public Set<String> getWorlds() {
        return worlds;
    }

    public boolean isApplicableWorld(@NotNull String name) {
        return this.getWorlds().isEmpty() || this.getWorlds().contains(name.toLowerCase());
    }

    @NotNull
    public Set<String> getRewards() {
        return rewards;
    }

    public boolean isCompleted() {
        return this.getObjectives().values().stream().allMatch(arr -> arr[0] >= arr[1]);
    }

    public boolean isCompleted(@NotNull String objective) {
        return this.getObjectiveProgressCurrent(objective) >= this.getObjectiveProgressMax(objective);
    }

    public double getProgressPercent() {
        double has = this.getObjectives().values().stream().mapToInt(arr -> arr[0]).sum();
        double need = this.getObjectives().values().stream().mapToInt(arr -> arr[1]).sum();

        return has / need * 100D;
    }

    public double getProgressPercent(@NotNull String objective) {
        double has = this.getObjectiveProgressCurrent(objective);
        double need = this.getObjectiveProgressMax(objective);

        return has / need * 100D;
    }

    public boolean hasObjectiveExact(@NotNull String id) {
        return this.getObjectives().containsKey(id.toLowerCase());
    }

    public boolean hasObjective(@NotNull String id) {
        return this.hasObjectiveExact(id) || this.hasObjectiveExact(Placeholders.WILDCARD);
    }

    public int[] getObjectiveValue(@NotNull String id) {
        return this.getObjectives().getOrDefault(id.toLowerCase(), this.getObjectives().getOrDefault(Placeholders.WILDCARD, new int[2]));
    }

    public int getObjectiveProgressCurrent(@NotNull String id) {
        return this.getObjectiveValue(id)[0];
    }

    public int getObjectiveProgressMax(@NotNull String id) {
        return this.getObjectiveValue(id)[1];
    }

    public boolean addObjectiveProgress(@NotNull String id, int amount) {
        if (!this.hasObjectiveExact(id)) id = Placeholders.WILDCARD;
        if (!this.hasObjective(id) || this.isCompleted(id) || this.isCompleted()) return false;

        int[] progress = this.getObjectiveValue(id);
        int current = this.getObjectiveProgressCurrent(id);
        int max = this.getObjectiveProgressMax(id);

        progress[0] = Math.min(max, current + amount);
        return true;
    }
}
