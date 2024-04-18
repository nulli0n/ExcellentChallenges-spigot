package su.nightexpress.excellentchallenges.challenge.creator.impl;

import com.google.common.collect.Sets;
import su.nightexpress.excellentchallenges.challenge.action.ActionType;
import su.nightexpress.excellentchallenges.challenge.action.ActionTypes;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentchallenges.ChallengesPlugin;
import su.nightexpress.excellentchallenges.challenge.creator.CreatorManager;
import su.nightexpress.nightcore.util.wrapper.UniInt;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import static org.bukkit.event.entity.EntityDamageEvent.DamageCause.*;

public class DamageCauseCreator extends AbstractCreator<DamageCause> {

    public DamageCauseCreator(@NotNull ChallengesPlugin plugin) {
        super(plugin);
    }

    @Override
    public void create() {

        this.createDamageInflictGenerators();
        this.createDamageReceiveGenerators();
    }

    /*@NotNull
    @Override
    public ItemStack getIcon(@NotNull DamageCause object) {
        Material material = switch (object) {
            case CONTACT -> Material.CACTUS;
            case ENTITY_EXPLOSION, BLOCK_EXPLOSION -> Material.TNT;
            case PROJECTILE -> Material.ARROW;
            case SUFFOCATION -> Material.SAND;
            case FIRE, FIRE_TICK -> Material.FLINT_AND_STEEL;
            case LAVA -> Material.LAVA_BUCKET;
            case DROWNING -> Material.WATER_BUCKET;
            case STARVATION -> Material.ROTTEN_FLESH;
            case POISON -> Material.SPIDER_EYE;
            case MAGIC -> Material.FERMENTED_SPIDER_EYE;
            case WITHER -> Material.WITHER_ROSE;
            default -> Material.IRON_SWORD;
        };

        return new ItemStack(material);
    }

    @NotNull
    @Override
    public List<String> getNames(@NotNull ActionType<?, DamageCause> actionType) {
        return new ArrayList<>();
    }*/

    @NotNull
    @Override
    public Set<String> getConditions(@NotNull ActionType<?, DamageCause> actionType) {
        return Sets.newHashSet(
            CreatorManager.CONDITIONS_SERVER_TIME,
            CreatorManager.CONDITIONS_ARMOR,
            CreatorManager.CONDITIONS_WEAPON,
            CreatorManager.CONDITIONS_PLAYER,
            CreatorManager.CONDITIONS_WORLD
        );
    }

    @NotNull
    @Override
    public Set<String> getRewards(@NotNull ActionType<?, DamageCause> actionType) {
        return Sets.newHashSet(CreatorManager.REWARDS_MONEY, CreatorManager.REWARDS_ITEMS);
    }

    @NotNull
    @Override
    public UniInt getMinProgress(@NotNull ActionType<?, DamageCause> actionType) {
        if (actionType == ActionTypes.DAMAGE_RECEIVE) {
            return UniInt.of(20, 40);
        }
        return UniInt.of(150, 300);
    }

    @NotNull
    @Override
    public UniInt getMaxProgress(@NotNull ActionType<?, DamageCause> actionType) {
        if (actionType == ActionTypes.DAMAGE_RECEIVE) {
            return UniInt.of(50, 80);
        }
        return UniInt.of(350, 500);
    }

    private void createDamageInflictGenerators() {
        Map<String, Set<DamageCause>> map = new HashMap<>();

        Set<DamageCause> causes = new HashSet<>();
        causes.add(ENTITY_ATTACK);
        causes.add(ENTITY_SWEEP_ATTACK);
        map.put("attack", causes);

        this.createGenerator(ActionTypes.DAMAGE_INFLICT, map);
    }

    private void createDamageReceiveGenerators() {
        Map<String, Set<DamageCause>> map = new HashMap<>();

        Set<DamageCause> causes = Sets.newHashSet(
            CONTACT, ENTITY_ATTACK, ENTITY_EXPLOSION, ENTITY_SWEEP_ATTACK,
            PROJECTILE, SUFFOCATION, FALL, FIRE, FIRE_TICK,
            LAVA, DROWNING, BLOCK_EXPLOSION, LIGHTNING, STARVATION,
            POISON, MAGIC, WITHER, THORNS
        );
        map.put(VARIOUS, causes);

        this.createGenerator(ActionTypes.DAMAGE_RECEIVE, map);
    }
}
