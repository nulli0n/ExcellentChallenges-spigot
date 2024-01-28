package su.nightexpress.excellentchallenges.challenge.condition;

import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nexmedia.engine.utils.*;
import su.nightexpress.excellentchallenges.Placeholders;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.function.Function;

public class Conditions {

    private static final Map<String, Condition<?, ?>> REGISTRY = new HashMap<>();

    public static final Condition<String, String> WORLD_NAME = string("world",
        player -> player.getWorld().getName());

    public static final Condition<Boolean, Boolean> WORLD_STORM = register("world_storm",
        Boolean::parseBoolean,
        player -> player.getWorld().hasStorm(),
        (world, parsed, oper) -> {
            return world == parsed;
        });

    public static final Condition<LocalTime, LocalTime> SERVER_TIME = register("server_time",
        str -> LocalTime.parse(str, DateTimeFormatter.ISO_LOCAL_TIME).truncatedTo(ChronoUnit.MINUTES),
        player -> LocalTime.now().truncatedTo(ChronoUnit.MINUTES),
        (server, parsed, oper) -> {
            if (oper == Operator.GREATER) return server.isAfter(parsed);
            if (oper == Operator.SMALLER) return server.isBefore(parsed);
            if (oper == Operator.EQUAL) return server.equals(parsed);
            return false;
        });

    public static final Condition<Number, Number> PLAYER_LEVEL = numeric("player_level", Player::getLevel);

    public static final Condition<Pair<Double, Boolean>, Player> PLAYER_HEALTH = register("player_health",
        str -> {
            boolean percent = str.endsWith("%");
            double value = StringUtil.getDouble(str.replace("%", ""), 0D);
            return Pair.of(value, percent);
        },
        player -> player,
        (player, parsed, oper) -> {
            boolean percent = parsed.getSecond();
            double checkHealth = parsed.getFirst();
            double playerHealth = player.getHealth();

            if (percent) {
                playerHealth = (playerHealth / EntityUtil.getAttribute(player, Attribute.GENERIC_MAX_HEALTH)) * 100D;
            }

            if (oper == Operator.GREATER) return playerHealth > checkHealth;
            if (oper == Operator.SMALLER) return playerHealth < checkHealth;
            if (oper == Operator.EQUAL) return playerHealth == checkHealth;
            if (oper == Operator.NOT_EQUAL) return playerHealth != checkHealth;

            return false;
        });

    public static final Condition<Material, ItemStack> HAND_ITEM    = equipmentItem("hand_item", EquipmentSlot.HAND);
    public static final Condition<Material, ItemStack> OFFHAND_ITEM = equipmentItem("offhand_item", EquipmentSlot.OFF_HAND);
    public static final Condition<Material, ItemStack> HEAD_ITEM    = equipmentItem("head_item", EquipmentSlot.HEAD);
    public static final Condition<Material, ItemStack> CHEST_ITEM   = equipmentItem("chest_item", EquipmentSlot.CHEST);
    public static final Condition<Material, ItemStack> LEGS_ITEM    = equipmentItem("legs_item", EquipmentSlot.LEGS);
    public static final Condition<Material, ItemStack> FEET_ITEM    = equipmentItem("feet_item", EquipmentSlot.FEET);

    @NotNull
    public static <P, E> Condition<P, E> register(@NotNull String name,
                                                  @NotNull Function<String, P> parser,
                                                  @NotNull Function<Player, E> extractor,
                                                  @NotNull TriFunction<E, P, Operator, Boolean> tester) {
        Condition<P, E> condition = new Condition<>(name, parser, extractor, tester);
        return register(condition);
    }

    @NotNull
    public static Condition<Material, ItemStack> equipmentItem(@NotNull String name, @NotNull EquipmentSlot slot) {

        Function<String, Material> parser = str -> {
            Material material = Material.getMaterial(str.toUpperCase());
            return material == null ? Material.AIR : material;
        };

        Function<Player, ItemStack> extractor = player -> player.getInventory().getItem(slot);

        TriFunction<ItemStack, Material, Operator, Boolean> tester = (extracted, parsed, operator) -> {
            if (operator == Operator.EQUAL) {
                return extracted.getType() == parsed;
            }
            return false;
        };

        return register(name, parser, extractor, tester);
    }

    @NotNull
    public static Condition<Number, Number> numeric(@NotNull String name,
                                                    @NotNull Function<Player, Number> extractor) {

        Function<String, Number> parser = str -> StringUtil.getDouble(str, 0D);
        TriFunction<Number, Number, Operator, Boolean> tester = (eventValue, condValue, operator) -> {
            return switch (operator) {
                case EQUAL -> eventValue.doubleValue() == condValue.doubleValue();
                case NOT_EQUAL -> eventValue.doubleValue() != condValue.doubleValue();
                case GREATER -> eventValue.doubleValue() > condValue.doubleValue();
                case SMALLER -> eventValue.doubleValue() < condValue.doubleValue();
                case EACH -> eventValue.intValue() % condValue.intValue() == 0;
                case EACH_NOT -> eventValue.intValue() % condValue.intValue() != 0;
            };
        };

        return register(name, parser, extractor, tester);
    }

    @NotNull
    public static Condition<Number[], Number> numerics(@NotNull String name,
                                                       @NotNull Function<Player, Number> extractor) {

        Function<String, Number[]> parser = str -> {
            String[] split = StringUtil.noSpace(str).split(",");
            Integer[] array = new Integer[split.length];

            for(int index = 0; index < split.length; ++index) {
                try {
                    array[index] = Integer.parseInt(split[index]);
                } catch (NumberFormatException var5) {
                    array[index] = 0;
                }
            }

            return array;
        };
        TriFunction<Number, Number[], Operator, Boolean> tester = (eventValue, condValue, operator) -> {
            return Arrays.stream(condValue).anyMatch(number -> {
                return switch (operator) {
                    case EQUAL -> eventValue.doubleValue() == number.doubleValue();
                    case NOT_EQUAL -> eventValue.doubleValue() != number.doubleValue();
                    case GREATER -> eventValue.doubleValue() > number.doubleValue();
                    case SMALLER -> eventValue.doubleValue() < number.doubleValue();
                    case EACH -> eventValue.intValue() % number.intValue() == 0;
                    case EACH_NOT -> eventValue.intValue() % number.intValue() != 0;
                };
            });
        };

        return register(name, parser, extractor, tester);
    }

    @NotNull
    public static Condition<String, String> string(@NotNull String name, @NotNull Function<Player, String> extractor) {
        Function<String, String> parser = Colorizer::strip;
        TriFunction<String, String, Operator, Boolean> tester = (eventValue, condValue, operator) -> {
            if (operator == Operator.NOT_EQUAL) {
                return !eventValue.equalsIgnoreCase(condValue);
            }
            return eventValue.equalsIgnoreCase(condValue) || condValue.equalsIgnoreCase(Placeholders.WILDCARD);
        };

        return register(name, parser, extractor, tester);
    }

    @NotNull
    public static <C, E> Condition<C, E> register(@NotNull Condition<C, E> condition) {
        REGISTRY.put(condition.getName(), condition);
        return condition;
    }

    @Nullable
    public static Condition<?, ?> getByName(@NotNull String name) {
        return REGISTRY.get(name.toLowerCase());
    }

    @NotNull
    public static Set<Condition<?, ?>> getConditions() {
        return new HashSet<>(REGISTRY.values());
    }
}
