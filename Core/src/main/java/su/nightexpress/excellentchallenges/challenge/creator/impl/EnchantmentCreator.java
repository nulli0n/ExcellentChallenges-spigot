package su.nightexpress.excellentchallenges.challenge.creator.impl;

import com.google.common.collect.Sets;
import su.nightexpress.excellentchallenges.challenge.action.ActionType;
import su.nightexpress.excellentchallenges.challenge.action.ActionTypes;
import org.bukkit.enchantments.Enchantment;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentchallenges.ChallengesPlugin;
import su.nightexpress.excellentchallenges.challenge.creator.CreatorManager;
import su.nightexpress.nightcore.util.wrapper.UniInt;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class EnchantmentCreator extends AbstractCreator<Enchantment> {

    public EnchantmentCreator(@NotNull ChallengesPlugin plugin) {
        super(plugin);
    }

    @Override
    public void create() {

        this.createGetRemoveEnchantGenerators();
    }

    /*@NotNull
    @Override
    public ItemStack getIcon(@NotNull Enchantment object) {
        return new ItemStack(Material.ENCHANTED_BOOK);
    }

    @NotNull
    @Override
    public List<String> getNames(@NotNull ActionType<?, Enchantment> actionType) {
        return new ArrayList<>();
    }*/

    @NotNull
    @Override
    public Set<String> getConditions(@NotNull ActionType<?, Enchantment> actionType) {
        return Sets.newHashSet(
            CreatorManager.CONDITIONS_SERVER_TIME,
            CreatorManager.CONDITIONS_WORLD
        );
    }

    @NotNull
    @Override
    public Set<String> getRewards(@NotNull ActionType<?, Enchantment> actionType) {
        return Sets.newHashSet(CreatorManager.REWARDS_MONEY, CreatorManager.REWARDS_ITEMS);
    }

    @NotNull
    @Override
    public UniInt getMinProgress(@NotNull ActionType<?, Enchantment> actionType) {
        return UniInt.of(1, 3);
    }

    @NotNull
    @Override
    public UniInt getMaxProgress(@NotNull ActionType<?, Enchantment> actionType) {
        return UniInt.of(4, 7);
    }

    private void createGetRemoveEnchantGenerators() {
        Set<Enchantment> enchantments = Stream.of(Enchantment.values())
            .filter(Predicate.not(Enchantment::isCursed))
            .filter(Predicate.not(Enchantment::isTreasure))
            .collect(Collectors.toSet());

        Map<String, Set<Enchantment>> map = new HashMap<>();
        map.put(VARIOUS, enchantments);
        this.createGenerator(ActionTypes.ENCHANT_GET, map);
        this.createGenerator(ActionTypes.ENCHANT_REMOVE, map);
    }
}
