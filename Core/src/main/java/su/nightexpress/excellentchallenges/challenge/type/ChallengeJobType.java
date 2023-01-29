package su.nightexpress.excellentchallenges.challenge.type;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nexmedia.engine.NexEngine;
import su.nexmedia.engine.hooks.Hooks;
import su.nexmedia.engine.hooks.external.MythicMobsHook;
import su.nexmedia.engine.lang.LangManager;
import su.nexmedia.engine.utils.CollectionsUtil;
import su.nightexpress.excellentchallenges.ExcellentChallengesAPI;
import su.nightexpress.excellentchallenges.Placeholders;
import su.nightexpress.excellentchallenges.challenge.handler.ChallengeHandler;
import su.nightexpress.excellentchallenges.config.Lang;

import java.util.function.UnaryOperator;

public enum ChallengeJobType {

    BLOCK_BREAK(Constants.OBJECTIVE_FORMATTER_MATERIAL),
    BLOCK_PLACE(Constants.OBJECTIVE_FORMATTER_MATERIAL),
    ITEM_CONSUME(Constants.OBJECTIVE_FORMATTER_MATERIAL),
    ITEM_CRAFT(Constants.OBJECTIVE_FORMATTER_MATERIAL),
    ITEM_COOK(Constants.OBJECTIVE_FORMATTER_MATERIAL),
    ITEM_ENCHANT(Constants.OBJECTIVE_FORMATTER_ENCHANT),
    ITEM_DISENCHANT(Constants.OBJECTIVE_FORMATTER_MATERIAL),
    ITEM_TRADE(Constants.OBJECTIVE_FORMATTER_MATERIAL),
    ITEM_FISH(Constants.OBJECTIVE_FORMATTER_MATERIAL),
    ENTITY_KILL(Constants.OBJECTIVE_FORMATTER_ENTITY),
    ENTITY_KILL_MYTHIC(Constants.OBJECTIVE_FORMATTER_MYTHIC),
    ENTITY_TAME(Constants.OBJECTIVE_FORMATTER_ENTITY),
    ENTITY_BREED(Constants.OBJECTIVE_FORMATTER_ENTITY),
    ENTITY_SHEAR(Constants.OBJECTIVE_FORMATTER_ENTITY),
    PROJECTILE_LAUNCH(Constants.OBJECTIVE_FORMATTER_ENTITY),
    POTION_BREW(Constants.OBJECTIVE_FORMATTER_POTION),
    ANVIL_RENAME(Constants.OBJECTIVE_FORMATTER_MATERIAL),
    DAMAGE_RECEIVE(Constants.OBJECTIVE_FORMATTER_DAMAGE_CAUSE),
    DAMAGE_INFLICT(Constants.OBJECTIVE_FORMATTER_DAMAGE_CAUSE),
    ;

    private final UnaryOperator<String> objectiveNameFormatter;

    ChallengeJobType(@NotNull UnaryOperator<String> objectiveNameFormatter) {
        this.objectiveNameFormatter = objectiveNameFormatter;
    }

    @Nullable
    public ChallengeHandler getHandler() {
        return ChallengeHandler.get(this);
    }

    public boolean isAvailable() {
        if (this == ENTITY_KILL_MYTHIC && !Hooks.hasMythicMobs()) {
            return false;
        }
        return true;
    }

    @NotNull
    public String formatObjective(@NotNull String obj) {
        if (obj.equalsIgnoreCase(Placeholders.WILDCARD)) {
            return ExcellentChallengesAPI.PLUGIN.getMessage(Lang.OTHER_ANY).getLocalized();
        }
        return this.objectiveNameFormatter.apply(obj);
    }

    private static class Constants {

        private static final UnaryOperator<String> OBJECTIVE_FORMATTER_MATERIAL = (obj -> {
            Material material = Material.getMaterial(obj.toUpperCase());
            if (material == null) return obj;

            return NexEngine.get().getLangManager().getEnum(material);
        });

        private static final UnaryOperator<String> OBJECTIVE_FORMATTER_ENTITY = (obj -> {
            EntityType type = CollectionsUtil.getEnum(obj, EntityType.class);
            if (type == null) return obj;

            return NexEngine.get().getLangManager().getEnum(type);
        });

        private static final UnaryOperator<String> OBJECTIVE_FORMATTER_POTION = (obj -> {
            PotionEffectType type = PotionEffectType.getByName(obj.toUpperCase());
            if (type == null) return obj;

            return LangManager.getPotionType(type);
        });

        private static final UnaryOperator<String> OBJECTIVE_FORMATTER_ENCHANT = (obj -> {
            Enchantment e = Enchantment.getByKey(NamespacedKey.minecraft(obj.toLowerCase()));
            if (e == null) return obj;

            return LangManager.getEnchantment(e);
        });

        private static final UnaryOperator<String> OBJECTIVE_FORMATTER_MYTHIC = (obj -> {
            return Hooks.hasMythicMobs() ? MythicMobsHook.getMobDisplayName(obj) : obj;
        });

        private static final UnaryOperator<String> OBJECTIVE_FORMATTER_DAMAGE_CAUSE = (obj -> {
            DamageCause cause = CollectionsUtil.getEnum(obj, DamageCause.class);
            if (cause == null) return obj;

            return ExcellentChallengesAPI.PLUGIN.getLangManager().getEnum(cause);
        });
    }
}
