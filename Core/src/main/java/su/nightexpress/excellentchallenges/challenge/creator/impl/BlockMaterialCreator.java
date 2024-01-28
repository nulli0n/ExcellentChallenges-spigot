package su.nightexpress.excellentchallenges.challenge.creator.impl;

import com.google.common.collect.Sets;
import su.nightexpress.excellentchallenges.challenge.action.ActionType;
import su.nightexpress.excellentchallenges.challenge.action.ActionTypes;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.jetbrains.annotations.NotNull;
import su.nexmedia.engine.utils.values.UniInt;
import su.nightexpress.excellentchallenges.ExcellentChallengesPlugin;
import su.nightexpress.excellentchallenges.challenge.creator.CreatorManager;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class BlockMaterialCreator extends AbstractCreator<Material> {

    public BlockMaterialCreator(@NotNull ExcellentChallengesPlugin plugin) {
        super(plugin);
    }

    @Override
    public void create() {

        this.createBlockBreakGenerators();
        this.createBlockFertilizeGenerators();
        this.createBlockPlaceGenerators();
    }

    @NotNull
    @Override
    public Set<String> getConditions(@NotNull ActionType<?, Material> actionType) {
        return Sets.newHashSet(
            CreatorManager.CONDITIONS_SERVER_TIME,
            CreatorManager.CONDITIONS_PLAYER,
            CreatorManager.CONDITIONS_WORLD
        );
    }

    @NotNull
    @Override
    public Set<String> getRewards(@NotNull ActionType<?, Material> actionType) {
        return Sets.newHashSet(CreatorManager.REWARDS_MONEY, CreatorManager.REWARDS_ITEMS);
    }

    /*@NotNull
    @Override
    public ItemStack getIcon(@NotNull Material object) {
        if (object == Material.POTATOES) object = Material.POTATO;
        else if (object == Material.CARROTS) object = Material.CARROT;
        else if (object == Material.BEETROOTS) object = Material.BEETROOT;
        else if (object == Material.MELON_STEM) object = Material.MELON;
        else if (object == Material.PUMPKIN_STEM) object = Material.PUMPKIN;

        return new ItemStack(object);
    }

    @NotNull
    @Override
    public List<String> getNames(@NotNull ActionType<?, Material> actionType) {
        return new ArrayList<>();
    }*/

    @NotNull
    @Override
    public UniInt getMinProgress(@NotNull ActionType<?, Material> actionType) {
        if (actionType == ActionTypes.BLOCK_FERTILIZE || actionType == ActionTypes.BLOCK_PLACE) {
            return UniInt.of(4, 8);
        }
        return UniInt.of(16, 22);
    }

    @NotNull
    @Override
    public UniInt getMaxProgress(@NotNull ActionType<?, Material> actionType) {
        if (actionType == ActionTypes.BLOCK_FERTILIZE || actionType == ActionTypes.BLOCK_PLACE) {
            return UniInt.of(10, 12);
        }
        return UniInt.of(24, 28);
    }

    private void createBlockBreakGenerators() {
        Map<String, Set<Material>> map = new HashMap<>();

        map.put("logs", Tag.LOGS.getValues());
        map.put("sand", Tag.SAND.getValues());
        map.put("leaves", Tag.LEAVES.getValues());
        map.put("flowers", Tag.FLOWERS.getValues());

        Set<Material> ores = new HashSet<>();
        ores.addAll(Tag.EMERALD_ORES.getValues());
        ores.addAll(Tag.DIAMOND_ORES.getValues());
        ores.addAll(Tag.COPPER_ORES.getValues());
        ores.addAll(Tag.COAL_ORES.getValues());
        ores.addAll(Tag.GOLD_ORES.getValues());
        ores.addAll(Tag.IRON_ORES.getValues());
        ores.addAll(Tag.LAPIS_ORES.getValues());
        ores.addAll(Tag.REDSTONE_ORES.getValues());
        map.put("ores", ores);

        map.put("dirt", Tag.DIRT.getValues());
        map.put("terracotta", Tag.TERRACOTTA.getValues());
        map.put("corals", Tag.CORALS.getValues());
        map.put("crops", Tag.CROPS.getValues());
        map.put("base_stone", Tag.BASE_STONE_OVERWORLD.getValues());
        map.put("nether_stone", Tag.BASE_STONE_NETHER.getValues());
        map.put("snow", Tag.SNOW.getValues());

        this.createGenerator(ActionTypes.BLOCK_BREAK, map);
    }

    private void createBlockFertilizeGenerators() {
        Map<String, Set<Material>> map = new HashMap<>();

        map.put("crops", Tag.CROPS.getValues());
        map.put("sapplings", Tag.SAPLINGS.getValues());
        map.put("flowers", Tag.FLOWERS.getValues());

        this.createGenerator(ActionTypes.BLOCK_FERTILIZE, map);
    }

    private void createBlockPlaceGenerators() {
        Map<String, Set<Material>> map = new HashMap<>();

        map.put("crops", Tag.CROPS.getValues());
        map.put("sapplings", Tag.SAPLINGS.getValues());

        this.createGenerator(ActionTypes.BLOCK_PLACE, map);
    }
}
