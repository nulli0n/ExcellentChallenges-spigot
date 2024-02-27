package su.nightexpress.excellentchallenges.challenge.creator;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentchallenges.config.Perms;
import su.nightexpress.excellentchallenges.challenge.ChallengeCategory;
import su.nightexpress.excellentchallenges.challenge.difficulty.Difficulty;
import su.nightexpress.excellentchallenges.challenge.difficulty.DifficultyModifier;
import su.nightexpress.nightcore.util.ItemUtil;
import su.nightexpress.nightcore.util.RankMap;
import su.nightexpress.nightcore.util.wrapper.UniInt;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;

public class ConfigDefaults {

    @NotNull
    public static Map<String, ChallengeCategory> defaultCategories() {
        Map<String, ChallengeCategory> map = new LinkedHashMap<>();

        ChallengeCategory daily = new ChallengeCategory(
            "daily", "Daily",
            ItemUtil.getSkinHead("68ac632e7b2d20c9cce5cc9e6486a7dfd933aeeb3513febd232031e0e758b00e"),
            86400,
            true,
            Map.of(Difficulty.DEF_CHILD, 70D, Difficulty.DEF_EASY, 50D, Difficulty.DEF_MEDIUM, 25D),
            new RankMap<>(RankMap.Mode.RANK, Perms.PREFIX_CHALLENGES_AMOUNT + "daily.", 5, Map.of("donor", 7)),
            new HashSet<>(),
            new HashSet<>()
        );

        ChallengeCategory weekly = new ChallengeCategory(
            "weekly", "Weekly",
            ItemUtil.getSkinHead("a93dfb3ae8177784a645779dca2c12dfba1258c202afdc04d87d80f2cecea1d7"),
            604800,
            true,
            Map.of(Difficulty.DEF_EASY, 25D, Difficulty.DEF_MEDIUM, 60D, Difficulty.DEF_HARD, 50D, Difficulty.DEF_EXTREME, 15D),
            new RankMap<>(RankMap.Mode.RANK, Perms.PREFIX_CHALLENGES_AMOUNT + "weekly.", 5, Map.of("donor", 7)),
            new HashSet<>(),
            new HashSet<>()
        );

        ChallengeCategory monthly = new ChallengeCategory(
            "monthly", "Monthly",
            ItemUtil.getSkinHead("930423127bb0269c508333c6966578c1317db3368c1fbd0503dc38b642c5d193"),
            2628288,
            true,
            Map.of(Difficulty.DEF_MEDIUM, 35D, Difficulty.DEF_HARD, 50D, Difficulty.DEF_EXTREME, 80D),
            new RankMap<>(RankMap.Mode.RANK, Perms.PREFIX_CHALLENGES_AMOUNT + "monthly.", 5, Map.of("donor", 7)),
            new HashSet<>(),
            new HashSet<>()
        );

        map.put(daily.getId(), daily);
        map.put(weekly.getId(), weekly);
        map.put(monthly.getId(), monthly);

        return map;
    }

    @NotNull
    public static Map<String, Difficulty> defaultDifficulties() {
        Map<String, Difficulty> map = new LinkedHashMap<>();

        Difficulty child = new Difficulty(Difficulty.DEF_CHILD, "Childish", UniInt.of(1, 10),
            Map.of(
                DifficultyModifier.DEF_OBJECTIVE_AMOUNT_ID, new DifficultyModifier(0, 0),
                DifficultyModifier.DEF_OBJECTIVE_PROGRESS_ID, new DifficultyModifier(1.05, 0.05),
                DifficultyModifier.DEF_CONDITIONS_AMOUNT_ID, new DifficultyModifier(0, 0),
                DifficultyModifier.DEF_REWARDS_AMOUNT_ID, new DifficultyModifier(0, 0)
            )
        );

        Difficulty easy = new Difficulty(Difficulty.DEF_EASY, "Easy", UniInt.of(11, 20),
            Map.of(
                DifficultyModifier.DEF_OBJECTIVE_AMOUNT_ID, new DifficultyModifier(0, 0.05),
                DifficultyModifier.DEF_OBJECTIVE_PROGRESS_ID, new DifficultyModifier(1.25, 0.05),
                DifficultyModifier.DEF_CONDITIONS_AMOUNT_ID, new DifficultyModifier(0, 0),
                DifficultyModifier.DEF_REWARDS_AMOUNT_ID, new DifficultyModifier(0, 0)
            )
        );

        Difficulty medium = new Difficulty(Difficulty.DEF_MEDIUM, "Medium", UniInt.of(21, 30),
            Map.of(
                DifficultyModifier.DEF_OBJECTIVE_AMOUNT_ID, new DifficultyModifier(0, 0.05),
                DifficultyModifier.DEF_OBJECTIVE_PROGRESS_ID, new DifficultyModifier(2, 0.1),
                DifficultyModifier.DEF_CONDITIONS_AMOUNT_ID, new DifficultyModifier(0, 0),
                DifficultyModifier.DEF_REWARDS_AMOUNT_ID, new DifficultyModifier(0, 0.04)
            )
        );

        Difficulty hard = new Difficulty(Difficulty.DEF_HARD, "Hard", UniInt.of(21, 40),
            Map.of(
                DifficultyModifier.DEF_OBJECTIVE_AMOUNT_ID, new DifficultyModifier(0, 0.05),
                DifficultyModifier.DEF_OBJECTIVE_PROGRESS_ID, new DifficultyModifier(4, 0.2),
                DifficultyModifier.DEF_CONDITIONS_AMOUNT_ID, new DifficultyModifier(0, 0),
                DifficultyModifier.DEF_REWARDS_AMOUNT_ID, new DifficultyModifier(0, 0.04)
            )
        );

        Difficulty extreme = new Difficulty(Difficulty.DEF_EXTREME, "Extreme", UniInt.of(41, 50),
            Map.of(
                DifficultyModifier.DEF_OBJECTIVE_AMOUNT_ID, new DifficultyModifier(0, 0.05),
                DifficultyModifier.DEF_OBJECTIVE_PROGRESS_ID, new DifficultyModifier(5, 0.3),
                DifficultyModifier.DEF_CONDITIONS_AMOUNT_ID, new DifficultyModifier(0, 0),
                DifficultyModifier.DEF_REWARDS_AMOUNT_ID, new DifficultyModifier(0, 0.04)
            )
        );

        map.put(child.getId(), child);
        map.put(easy.getId(), easy);
        map.put(medium.getId(), medium);
        map.put(hard.getId(), hard);
        map.put(extreme.getId(), extreme);
        return map;
    }
}
