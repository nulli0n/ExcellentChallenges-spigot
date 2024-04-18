package su.nightexpress.excellentchallenges.challenge.action;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.excellentchallenges.ChallengesAPI;
import su.nightexpress.nightcore.language.LangAssets;
import su.nightexpress.nightcore.util.StringUtil;

public class ObjectFormatters {

    public static final ObjectFormatter<Material> MATERIAL = new ObjectFormatter<>() {

        @NotNull
        @Override
        public String getName(@NotNull Material object) {
            return object.name();
        }

        @NotNull
        @Override
        public String getLocalizedName(@NotNull Material material) {
            return LangAssets.get(material);
        }

        @Nullable
        @Override
        public Material parseObject(@NotNull String name) {
            return Material.getMaterial(name.toUpperCase());
        }
    };

    public static final ObjectFormatter<EntityType> ENITITY_TYPE = new ObjectFormatter<>() {
        @NotNull
        @Override
        public String getName(@NotNull EntityType type) {
            return type.name();
        }

        @NotNull
        @Override
        public String getLocalizedName(@NotNull EntityType type) {
            return LangAssets.get(type);
        }

        @Nullable
        @Override
        public EntityType parseObject(@NotNull String name) {
            return StringUtil.getEnum(name, EntityType.class).orElse(null);
        }
    };

    public static final ObjectFormatter<PotionEffectType> POTION_TYPE = new ObjectFormatter<>() {

        @NotNull
        @Override
        public String getName(@NotNull PotionEffectType object) {
            /*if (Version.isAtLeast(Version.V1_20_R3)) {
                return object.getKey().getKey();
            }*/
            return object.getName();
        }

        @NotNull
        @Override
        public String getLocalizedName(@NotNull PotionEffectType object) {
            return LangAssets.get(object);
        }

        @Nullable
        @Override
        public PotionEffectType parseObject(@NotNull String name) {
            /*if (Version.isAtLeast(Version.V1_20_R3)) {
                return BukkitThing.fromRegistry(Registry.EFFECT, name);
            }*/
            return PotionEffectType.getByName(name.toUpperCase());
        }
    };

    public static final ObjectFormatter<Enchantment> ENCHANTMENT = new ObjectFormatter<>() {
        @NotNull
        @Override
        public String getName(@NotNull Enchantment object) {
            return object.getKey().getKey();
        }

        @NotNull
        @Override
        public String getLocalizedName(@NotNull Enchantment object) {
            return LangAssets.get(object);
        }

        @Nullable
        @Override
        public Enchantment parseObject(@NotNull String name) {
            return Enchantment.getByKey(NamespacedKey.minecraft(name.toLowerCase()));
        }
    };

    public static final ObjectFormatter<EntityDamageEvent.DamageCause> DAMAGE_CAUSE = new ObjectFormatter<>() {
        @NotNull
        @Override
        public String getName(@NotNull EntityDamageEvent.DamageCause object) {
            return object.name();
        }

        @NotNull
        @Override
        public String getLocalizedName(@NotNull EntityDamageEvent.DamageCause object) {
            return ChallengesAPI.PLUGIN.getLangManager().getEnum(object);
        }

        @Nullable
        @Override
        public EntityDamageEvent.DamageCause parseObject(@NotNull String name) {
            return StringUtil.getEnum(name, EntityDamageEvent.DamageCause.class).orElse(null);
        }
    };
}
