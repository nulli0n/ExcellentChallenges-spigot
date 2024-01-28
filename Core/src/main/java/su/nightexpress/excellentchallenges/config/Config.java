package su.nightexpress.excellentchallenges.config;

import org.bukkit.event.entity.CreatureSpawnEvent;
import su.nexmedia.engine.api.config.JOption;
import su.nexmedia.engine.utils.StringUtil;
import su.nightexpress.excellentchallenges.Perms;
import su.nightexpress.excellentchallenges.challenge.ChallengeCategory;
import su.nightexpress.excellentchallenges.challenge.creator.ConfigDefaults;
import su.nightexpress.excellentchallenges.challenge.difficulty.Difficulty;
import su.nightexpress.excellentchallenges.challenge.type.RerollCondition;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class Config {

    public static final String DIR_MENU       = "/menu/";
    public static final String DIR_GENERATORS = "/generators/";
    public static final String DIR_CONDITIONS = "/conditions/";
    public static final String DIR_REWARDS    = "/rewards/";

    //public static final String NAMES_FILE = "names.yml";

    public static final JOption<Map<String, Difficulty>> DIFFICULTIES = JOption.forMap("Challenges.Difficulties",
        (cfg, path, id) -> Difficulty.read(cfg, path + "." + id, id),
        ConfigDefaults::defaultDifficulties,
        "Here you can create your own difficulties for challenges."
    ).setWriter((cfg, path, map) -> map.forEach((id, diff) -> diff.write(cfg, path + "." + id)));

    public static final JOption<Map<String, ChallengeCategory>> CATEGORIES = JOption.forMap("Challenges.Types",
        String::toLowerCase,
        (cfg, path, id) -> ChallengeCategory.read(cfg, path + "." + id, id),
        LinkedHashMap::new,
        ConfigDefaults.defaultCategories(),
        "Here you can create your own challenge categories."
    ).setWriter((cfg, path, map) -> map.forEach((id, type) -> type.write(cfg, path + "." + id)));

    public static final JOption<Boolean> REROLL_ENABLED = JOption.create("Challenges.Reroll.Enabled", true,
        "Enables the 'Reroll' feature.",
        "This feature allows players to reroll their challenges using Reroll Tokens.",
        "Players must have '" + Perms.REROLL.getName() + "' permission to be able to reroll.");

    public static final JOption<RerollCondition> REROLL_CONDITION = JOption.create("Challenges.Reroll.Condition",
        RerollCondition.class, RerollCondition.ANYTIME,
        "Sets when challenges can be rerolled.", "Allowed values:",
        RerollCondition.ALL_COMPLETED.name() + " - When all challenges are completed.",
        RerollCondition.ALL_UNFINISHED.name() + " - When all challenges are unfinished.",
        RerollCondition.ANYTIME.name() + " - At anytime.");

    public static final JOption<Boolean> OBJECTIVES_ANTI_GLITCH_TRACK_BLOCKS = JOption.create("Objectives.Anti_Glitch.Track_Player_Blocks", true,
        "Sets whether plugin should store data of player placed blocks and prevent them",
        "from counting into challenge progress.");

    public static final JOption<Set<CreatureSpawnEvent.SpawnReason>> OBJECTIVES_ANTI_GLITCH_ENTITY_SPAWN_REASONS = new JOption<>(
        "Objectives.Anti_Glitch.Tracked_Entity_Spawn",
        (cfg, path, def) -> cfg.getStringSet(path).stream()
            .map(raw -> StringUtil.getEnum(raw, CreatureSpawnEvent.SpawnReason.class).orElse(null))
            .filter(Objects::nonNull).collect(Collectors.toSet()),
        Set.of(
            CreatureSpawnEvent.SpawnReason.SPAWNER, CreatureSpawnEvent.SpawnReason.SPAWNER_EGG,
            CreatureSpawnEvent.SpawnReason.EGG, CreatureSpawnEvent.SpawnReason.DISPENSE_EGG,
            CreatureSpawnEvent.SpawnReason.BREEDING,
            CreatureSpawnEvent.SpawnReason.BUILD_IRONGOLEM, CreatureSpawnEvent.SpawnReason.BUILD_SNOWMAN
        ),
        "A list of SpawnReasons for entities to be tracked and not counted in challenge progress.",
        "This option will help you to prevent challenge progress abusing via Spawn Eggs, Spawners, etc.",
        "https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/event/entity/CreatureSpawnEvent.SpawnReason.html"
    ).setWriter((cfg, path, set) -> cfg.set(path, set.stream().map(Enum::name).toList()));


}
