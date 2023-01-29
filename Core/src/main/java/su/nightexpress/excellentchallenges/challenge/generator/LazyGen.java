package su.nightexpress.excellentchallenges.challenge.generator;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

public class LazyGen {

    public static void go() {
        ChallengeGenerator.lazyCreate("blocks_grass", "grass_block", "podzol", "dirt");
        ChallengeGenerator.lazyCreate("blocks_mycelium", "mycelium");
        ChallengeGenerator.lazyCreate("blocks_sand", "sand", "red_sand", "gravel");
        ChallengeGenerator.lazyCreate("blocks_nether_2", "crimson_nylium", "warped_nylium");
        ChallengeGenerator.lazyCreate("blocks_nether_3", "soul_sand", "glowstone");
        ChallengeGenerator.lazyCreate("blocks_log_1", "oak_log", "birch_log", "spruce_log");
        ChallengeGenerator.lazyCreate("blocks_log_2", "jungle_log", "acacia_log", "dark_oak_log");
        ChallengeGenerator.lazyCreate("blocks_log_3", "crimson_stem", "warped_stem");
        ChallengeGenerator.lazyCreate("blocks_log_4", "mushroom_stem", "mangrove_stem");
        ChallengeGenerator.lazyCreate("blocks_leaves_1", "oak_leaves", "birch_leaves", "spruce_leaves");
        ChallengeGenerator.lazyCreate("blocks_leaves_2", "jungle_leaves", "acacia_leaves", "dark_oak_leaves");
        ChallengeGenerator.lazyCreate("blocks_leaves_3", "azalea_leaves", "flowering_azalea_leaves");
        ChallengeGenerator.lazyCreate("blocks_leaves_4", "mangrove_roots", "mangrove_leaves");
        ChallengeGenerator.lazyCreate("blocks_mushrooms_1", "red_mushroom_block", "brown_mushroom_block");
        ChallengeGenerator.lazyCreate("blocks_saplings_1", "oak_sapling", "birch_sapling", "spruce_sapling");
        ChallengeGenerator.lazyCreate("blocks_saplings_2", "jungle_sapling", "acacia_sapling", "dark_oak_sapling");
        ChallengeGenerator.lazyCreate("blocks_mushrooms_2", "red_mushroom", "brown_mushroom");
        ChallengeGenerator.lazyCreate("blocks_mushrooms_3", "crimson_fungus", "warped_fungus");
        ChallengeGenerator.lazyCreate("blocks_flowers_1", "grass", "fern", "dead_bush");
        ChallengeGenerator.lazyCreate("blocks_flowers_2", "dandelion", "poppy", "blue_orchid");
        ChallengeGenerator.lazyCreate("blocks_flowers_3", "red_tulip", "orange_tulip", "white_tulip", "pink_tulip");
        ChallengeGenerator.lazyCreate("blocks_bamboo", "bamboo", "sugar_cane");
        ChallengeGenerator.lazyCreate("blocks_farm_1", "carrots", "potatoes", "beetroots", "wheats");

        Map<String, Set<String>> mobs = new HashMap<>();
        for (EntityType entityType : EntityType.values()) {
            if (!entityType.isSpawnable() || !entityType.isAlive()) continue;

            ChallengeGenerator.lazyCreate("mobs_" + entityType.name().toLowerCase(), entityType.name().toLowerCase());
        }

        ChallengeGenerator.lazyCreate("items_food_1", "apple", "carrot", "potato", "beetroot");
        ChallengeGenerator.lazyCreate("items_food_2", "bread", "cake", "cookie", "pumkpin_pie");
        ChallengeGenerator.lazyCreate("items_food_3", "cooked_beef", "cooked_porkchop", "cooked_mutton", "cooked_rabbit");
        ChallengeGenerator.lazyCreate("items_food_4", "cooked_cod", "cooked_salmon");
        ChallengeGenerator.lazyCreate("items_food_5", "sweet_berries", "glow_berries");
        ChallengeGenerator.lazyCreate("items_food_6", "golden_apple", "golden_carrot");

        ChallengeGenerator.lazyCreate("items_tools_1", "wooden_axe", "wooden_sword", "wooden_shovel", "wooden_pickaxe", "wooden_hoe");
        ChallengeGenerator.lazyCreate("items_tools_2", "stone_axe", "stone_sword", "stone_shovel", "stone_pickaxe", "stone_hoe");
        ChallengeGenerator.lazyCreate("items_tools_3", "golden_axe", "golden_sword", "golden_shovel", "golden_pickaxe", "golden_hoe");
        ChallengeGenerator.lazyCreate("items_tools_4", "iron_axe", "iron_sword", "iron_shovel", "iron_pickaxe", "iron_hoe");
        ChallengeGenerator.lazyCreate("items_tools_5", "diamond_axe", "diamond_sword", "diamond_shovel", "diamond_pickaxe", "diamond_hoe");
        ChallengeGenerator.lazyCreate("items_tools_6", "netherite_axe", "netherite_sword", "netherite_shovel", "netherite_pickaxe", "netherite_hoe");

        ChallengeGenerator.lazyCreate("items_armors_1", "leather_helmet", "leather_chestplate", "leather_leggings", "leather_boots");
        ChallengeGenerator.lazyCreate("items_armors_2", "chainmail_helmet", "chainmail_chestplate", "chainmail_leggings", "chainmail_boots");
        ChallengeGenerator.lazyCreate("items_armors_3", "golden_helmet", "golden_chestplate", "golden_leggings", "golden_boots");
        ChallengeGenerator.lazyCreate("items_armors_4", "iron_helmet", "iron_chestplate", "iron_leggings", "iron_boots");
        ChallengeGenerator.lazyCreate("items_armors_5", "diamond_helmet", "diamond_chestplate", "diamond_leggings", "diamond_boots");
        ChallengeGenerator.lazyCreate("items_armors_6", "netherite_helmet", "netherite_chestplate", "netherite_leggings", "netherite_boots");

        ChallengeGenerator.lazyCreate("enchants", Stream.of(Enchantment.values()).map(e -> e.getKey().getKey()).toList().toArray(new String[0]));
        ChallengeGenerator.lazyCreate("potions", Stream.of(PotionEffectType.values()).map(PotionEffectType::getName).toList().toArray(new String[0]));
        ChallengeGenerator.lazyCreate("damages", Stream.of(EntityDamageEvent.DamageCause.values()).map(e -> e.name().toLowerCase()).toList().toArray(new String[0]));
    }
}
