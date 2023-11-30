package su.nightexpress.excellentchallenges.challenge.creator;

import org.jetbrains.annotations.NotNull;
import su.nexmedia.engine.utils.ItemUtil;
import su.nexmedia.engine.utils.PlayerRankMap;
import su.nexmedia.engine.utils.values.UniInt;
import su.nightexpress.excellentchallenges.Placeholders;
import su.nightexpress.excellentchallenges.challenge.ChallengeCategory;
import su.nightexpress.excellentchallenges.challenge.difficulty.Difficulty;
import su.nightexpress.excellentchallenges.challenge.difficulty.DifficultyModifier;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;

public class ConfigDefaults {

    @NotNull
    public static Map<String, ChallengeCategory> defaultCategories() {
        Map<String, ChallengeCategory> map = new LinkedHashMap<>();

        ChallengeCategory daily = new ChallengeCategory(
            "daily", "Daily",
            ItemUtil.createCustomHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjhhYzYzMmU3YjJkMjBjOWNjZTVjYzllNjQ4NmE3ZGZkOTMzYWVlYjM1MTNmZWJkMjMyMDMxZTBlNzU4YjAwZSJ9fX0="),
            86400,
            true,
            Map.of(Difficulty.DEF_CHILD, 70D, Difficulty.DEF_EASY, 50D, Difficulty.DEF_MEDIUM, 25D),
            new PlayerRankMap<>(Map.of(Placeholders.DEFAULT, 5, "donor", 7)),
            new HashSet<>(),
            new HashSet<>()
        );

        ChallengeCategory weekly = new ChallengeCategory(
            "weekly", "Weekly",
            ItemUtil.createCustomHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTkzZGZiM2FlODE3Nzc4NGE2NDU3NzlkY2EyYzEyZGZiYTEyNThjMjAyYWZkYzA0ZDg3ZDgwZjJjZWNlYTFkNyJ9fX0="),
            604800,
            true,
            Map.of(Difficulty.DEF_EASY, 45D, Difficulty.DEF_MEDIUM, 60D, Difficulty.DEF_HARD, 50D, Difficulty.DEF_EXTREME, 15D),
            new PlayerRankMap<>(Map.of(Placeholders.DEFAULT, 5, "donor", 7)),
            new HashSet<>(),
            new HashSet<>()
        );

        ChallengeCategory monthly = new ChallengeCategory(
            "monthly", "Monthly",
            ItemUtil.createCustomHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzBjMDAyM2JmOGQ4NzIxZTAyMjlkYWZkZmI5ZDgzZWVlYTRkNGI2ZTFmMmRkMTQ5MDEyMGMxY2MxMTdjYmM3OCJ9fX0="),
            2628288,
            true,
            Map.of(Difficulty.DEF_MEDIUM, 35D, Difficulty.DEF_HARD, 50D, Difficulty.DEF_EXTREME, 60D),
            new PlayerRankMap<>(Map.of(Placeholders.DEFAULT, 5, "donor", 7)),
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
