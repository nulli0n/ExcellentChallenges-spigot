package su.nightexpress.excellentchallenges.challenge.creator.impl;

import com.google.common.collect.Sets;
import su.nightexpress.excellentchallenges.challenge.action.ActionType;
import su.nightexpress.excellentchallenges.challenge.action.ActionTypes;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentchallenges.ChallengesPlugin;
import su.nightexpress.excellentchallenges.challenge.creator.CreatorManager;
import su.nightexpress.nightcore.util.wrapper.UniInt;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.bukkit.potion.PotionEffectType.*;

public class PotionTypeCreator extends AbstractCreator<PotionEffectType> {

    public PotionTypeCreator(@NotNull ChallengesPlugin plugin) {
        super(plugin);
    }

    @Override
    public void create() {

        this.createPotionGenerators();
    }

    /*@NotNull
    @Override
    public ItemStack getIcon(@NotNull PotionEffectType object) {
        ItemStack stack = new ItemStack(Material.POTION);
        ItemUtil.mapMeta(stack, meta -> {
            if (!(meta instanceof PotionMeta potionMeta)) return;
            potionMeta.setColor(Color.fromRGB(Rnd.get(255), Rnd.get(255), Rnd.get(255)));
        });
        return stack;
    }

    @NotNull
    @Override
    public List<String> getNames(@NotNull ActionType<?, PotionEffectType> actionType) {
        return new ArrayList<>();
    }*/

    @NotNull
    @Override
    public Set<String> getConditions(@NotNull ActionType<?, PotionEffectType> actionType) {
        return Sets.newHashSet(
            CreatorManager.CONDITIONS_SERVER_TIME,
            CreatorManager.CONDITIONS_PLAYER,
            CreatorManager.CONDITIONS_WORLD
        );
    }

    @NotNull
    @Override
    public Set<String> getRewards(@NotNull ActionType<?, PotionEffectType> actionType) {
        return Sets.newHashSet(CreatorManager.REWARDS_MONEY, CreatorManager.REWARDS_ITEMS);
    }

    @NotNull
    @Override
    public UniInt getMinProgress(@NotNull ActionType<?, PotionEffectType> actionType) {
        return UniInt.of(2, 5);
    }

    @NotNull
    @Override
    public UniInt getMaxProgress(@NotNull ActionType<?, PotionEffectType> actionType) {
        return UniInt.of(6, 12);
    }

    private void createPotionGenerators() {
        Map<String, Set<PotionEffectType>> map = new HashMap<>();

        map.put("positive", Sets.newHashSet(
            HEAL, FIRE_RESISTANCE, REGENERATION, INCREASE_DAMAGE,
            SPEED, NIGHT_VISION, INVISIBILITY, WATER_BREATHING,
            SLOW_FALLING, JUMP
        ));

        map.put("negative", Sets.newHashSet(
            POISON, WEAKNESS, HARM, SLOW
        ));

        this.createGenerator(ActionTypes.POTION_BREW, map);
        this.createGenerator(ActionTypes.POTION_DRINK, map);
    }
}
