package su.nightexpress.excellentchallenges.challenge.creator;

import com.google.common.collect.Sets;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.inventory.EquipmentSlot;
import org.jetbrains.annotations.NotNull;
import su.nexmedia.engine.api.config.JYML;
import su.nexmedia.engine.api.manager.AbstractManager;
import su.nexmedia.engine.lang.LangManager;
import su.nexmedia.engine.utils.NumberUtil;
import su.nexmedia.engine.utils.Pair;
import su.nexmedia.engine.utils.StringUtil;
import su.nightexpress.excellentchallenges.ExcellentChallengesPlugin;
import su.nightexpress.excellentchallenges.Placeholders;
import su.nightexpress.excellentchallenges.challenge.condition.Condition;
import su.nightexpress.excellentchallenges.challenge.condition.ConditionConfig;
import su.nightexpress.excellentchallenges.challenge.condition.Conditions;
import su.nightexpress.excellentchallenges.challenge.condition.Operator;
import su.nightexpress.excellentchallenges.challenge.creator.impl.*;
import su.nightexpress.excellentchallenges.challenge.reward.Reward;
import su.nightexpress.excellentchallenges.config.Config;

import java.io.File;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class CreatorManager extends AbstractManager<ExcellentChallengesPlugin> {

    public static final String CONDITIONS_WORLD       = "world";
    public static final String CONDITIONS_SERVER_TIME = "server_time";
    public static final String CONDITIONS_PLAYER      = "player";
    public static final String CONDITIONS_ARMOR       = "armor";
    public static final String CONDITIONS_WEAPON      = "weapon";

    public static final String REWARDS_MONEY = "money";
    public static final String REWARDS_ITEMS = "items";

    public CreatorManager(@NotNull ExcellentChallengesPlugin plugin) {
        super(plugin);
    }

    @Override
    protected void onLoad() {
        this.plugin.info("Creating fresh randomized default configs...");

        this.createConditions();
        this.createRewards();
        this.createGenerators();
    }

    @Override
    protected void onShutdown() {

    }

    private void createGenerators() {
        File file = new File(plugin.getDataFolder(), Config.DIR_GENERATORS);
        if (file.exists()) return;

        new ItemMaterialCreator(this.plugin).create();
        new BlockMaterialCreator(this.plugin).create();
        new EntityTypeCreator(this.plugin).create();
        new DamageCauseCreator(this.plugin).create();
        new PotionTypeCreator(this.plugin).create();
        new EnchantmentCreator(this.plugin).create();
    }

    private void createConditions() {
        File file = new File(plugin.getDataFolder(), Config.DIR_CONDITIONS);
        if (file.exists()) return;

        this.createTimeCondition("time_morning", 8, 12);
        this.createTimeCondition("time_day", 12, 18);
        this.createTimeCondition("time_evening", 18, 22);
        this.createTimeCondition("time_night", 22, 4);

        this.createArmorCondition(EquipmentSlot.HEAD, Sets.newHashSet(
            Material.IRON_HELMET, Material.GOLDEN_HELMET,
            Material.CHAINMAIL_HELMET, Material.DIAMOND_HELMET,
            Material.NETHERITE_HELMET, Material.LEATHER_HELMET, Material.TURTLE_HELMET
        ));

        this.createArmorCondition(EquipmentSlot.CHEST, Sets.newHashSet(
            Material.IRON_CHESTPLATE, Material.GOLDEN_CHESTPLATE,
            Material.CHAINMAIL_CHESTPLATE, Material.DIAMOND_CHESTPLATE,
            Material.NETHERITE_CHESTPLATE, Material.LEATHER_CHESTPLATE
        ));

        this.createArmorCondition(EquipmentSlot.LEGS, Sets.newHashSet(
            Material.IRON_LEGGINGS, Material.GOLDEN_LEGGINGS,
            Material.CHAINMAIL_LEGGINGS, Material.DIAMOND_LEGGINGS,
            Material.NETHERITE_LEGGINGS, Material.LEATHER_LEGGINGS
        ));

        this.createArmorCondition(EquipmentSlot.FEET, Sets.newHashSet(
            Material.IRON_BOOTS, Material.GOLDEN_BOOTS,
            Material.CHAINMAIL_BOOTS, Material.DIAMOND_BOOTS,
            Material.NETHERITE_BOOTS, Material.LEATHER_BOOTS
        ));

        this.createWeaponCondition(Tag.ITEMS_SWORDS.getValues());
        this.createWeaponCondition(Tag.ITEMS_AXES.getValues());

        this.createPlayerHealthCondition(75D);
        this.createPlayerHealthCondition(50D);
        this.createPlayerHealthCondition(25D);

        this.createWorldStormCondition();

        this.createPlayerLevelCondition(5);
        this.createPlayerLevelCondition(10);
        this.createPlayerLevelCondition(15);
        this.createPlayerLevelCondition(20);
        this.createPlayerLevelCondition(25);
        this.createPlayerLevelCondition(30);
    }

    private void createRewards() {
        File file = new File(plugin.getDataFolder(), Config.DIR_REWARDS);
        if (file.exists()) return;

        this.createMoneyReward("money_250", 250);
        this.createMoneyReward("money_500", 500);
        this.createMoneyReward("money_750", 750);
        this.createMoneyReward("money_1000", 1000);

        this.createItemReward(Material.IRON_INGOT, 16);
        this.createItemReward(Material.IRON_INGOT, 32);
        this.createItemReward(Material.IRON_INGOT, 64);

        this.createItemReward(Material.GOLD_INGOT, 16);
        this.createItemReward(Material.GOLD_INGOT, 32);
        this.createItemReward(Material.GOLD_INGOT, 64);

        this.createItemReward(Material.NETHERITE_INGOT, 2);
        this.createItemReward(Material.NETHERITE_INGOT, 4);
        this.createItemReward(Material.NETHERITE_INGOT, 6);

        this.createItemReward(Material.ARROW, 16);
        this.createItemReward(Material.ARROW, 32);
        this.createItemReward(Material.ARROW, 64);

        this.createItemReward(Material.DIAMOND, 4);
        this.createItemReward(Material.DIAMOND, 8);
        this.createItemReward(Material.DIAMOND, 16);

        this.createItemReward(Material.EMERALD, 1);
        this.createItemReward(Material.EMERALD, 4);
        this.createItemReward(Material.EMERALD, 8);

        this.createItemReward(Material.ENCHANTED_GOLDEN_APPLE, 2);
        this.createItemReward(Material.ENCHANTED_GOLDEN_APPLE, 4);

        this.createItemReward(Material.EXPERIENCE_BOTTLE, 8);
        this.createItemReward(Material.EXPERIENCE_BOTTLE, 16);
        this.createItemReward(Material.EXPERIENCE_BOTTLE, 32);

        this.createItemReward(Material.SPIDER_SPAWN_EGG, 3);
        this.createItemReward(Material.CREEPER_SPAWN_EGG, 3);
        this.createItemReward(Material.ZOMBIE_SPAWN_EGG, 3);
        this.createItemReward(Material.COW_SPAWN_EGG, 3);
        this.createItemReward(Material.PIG_SPAWN_EGG, 3);
        this.createItemReward(Material.SHEEP_SPAWN_EGG, 3);
        this.createItemReward(Material.SKELETON_SPAWN_EGG, 3);
        this.createItemReward(Material.RABBIT_SPAWN_EGG, 3);
        this.createItemReward(Material.HORSE_SPAWN_EGG, 3);
        this.createItemReward(Material.CAVE_SPIDER_SPAWN_EGG, 3);
        this.createItemReward(Material.CHICKEN_SPAWN_EGG, 3);
    }

    private void createTimeCondition(@NotNull String id, int hFrom, int hTo) {
        LocalTime from = LocalTime.of(hFrom, 0);
        LocalTime to = LocalTime.of(hTo, 0);

        var model = Conditions.SERVER_TIME;
        String fromRaw = "[" + model.getName() + "] " + Operator.GREATER.getRaw() + " " + from.format(DateTimeFormatter.ISO_LOCAL_TIME);
        String toRaw = "[" + model.getName() + "] " + Operator.SMALLER.getRaw() + " " + to.format(DateTimeFormatter.ISO_LOCAL_TIME);

        JYML cfg = new JYML(plugin.getDataFolder() + Config.DIR_CONDITIONS, CONDITIONS_SERVER_TIME + ".yml");
        ConditionConfig conditionConfig = new ConditionConfig(id, id, new ArrayList<>(), new HashMap<>());
        conditionConfig.setName(StringUtil.capitalizeUnderscored(id));
        conditionConfig.setDescription(Arrays.asList(
            "Must be done between " + from.getHour() + " and " + to.getHour() + " real time hours!"
        ));
        conditionConfig.getConditionMap().put(Placeholders.DEFAULT, List.of(Pair.of(model, fromRaw), Pair.of(model, toRaw)));
        conditionConfig.write(cfg, id);
        cfg.saveChanges();
    }

    private void saveCondition(@NotNull String file, @NotNull String id,
                               @NotNull Condition<?,?> condition, @NotNull String raw,
                               @NotNull String description) {

        JYML cfg = new JYML(plugin.getDataFolder() + Config.DIR_CONDITIONS, file + ".yml");
        ConditionConfig conditionConfig = new ConditionConfig(id, id, new ArrayList<>(), new HashMap<>());
        conditionConfig.setName(StringUtil.capitalizeUnderscored(id));
        conditionConfig.setDescription(Arrays.asList(description));
        conditionConfig.getConditionMap().put(Placeholders.DEFAULT, List.of(Pair.of(condition, raw)));
        conditionConfig.write(cfg, id);
        cfg.saveChanges();
    }

    private void createWorldStormCondition() {
        var condition = Conditions.WORLD_STORM;
        String raw = "[" + condition.getName() + "] " + Operator.EQUAL.getRaw() + " true";

        this.saveCondition(CONDITIONS_WORLD, condition.getName(), condition, raw,
            "There must be a rain in the world!");
    }

    private void createArmorCondition(@NotNull EquipmentSlot slot, @NotNull Set<Material> materials) {
        this.createEquipmentCondition(CONDITIONS_ARMOR, slot, materials);
    }

    private void createWeaponCondition(@NotNull Set<Material> materials) {
        this.createEquipmentCondition(CONDITIONS_WEAPON, EquipmentSlot.HAND, materials);
    }

    private void createEquipmentCondition(@NotNull String file, @NotNull EquipmentSlot slot, @NotNull Set<Material> materials) {
        var condition = switch (slot) {
            case HAND -> Conditions.HAND_ITEM;
            case OFF_HAND -> Conditions.OFFHAND_ITEM;
            case HEAD -> Conditions.HEAD_ITEM;
            case CHEST -> Conditions.CHEST_ITEM;
            case LEGS -> Conditions.LEGS_ITEM;
            case FEET -> Conditions.FEET_ITEM;
        };

        //JYML cfg = new JYML(plugin.getDataFolder() + Config.DIR_CONDITIONS, file + ".yml");

        for (Material material : materials) {
            String id = condition.getName() + "_" + material.name().toLowerCase();
            String raw = "[" + condition.getName() + "] " + Operator.EQUAL.getRaw() + " " + material.name();

            this.saveCondition(file, id, condition, raw,
                "You must have " + LangManager.getMaterial(material) + " equipped!");

            /*ConditionConfig conditionConfig = new ConditionConfig(id, "", new ArrayList<>(), new HashMap<>());
            conditionConfig.setName(StringUtil.capitalizeUnderscored(id));
            conditionConfig.setDescription(Arrays.asList(
                "You must have " + LangManager.getMaterial(material) + " equipped!"
            ));
            conditionConfig.getConditionMap().put(Placeholders.DEFAULT, List.of(Pair.of(condition, raw)));
            conditionConfig.write(cfg, id);*/
        }
        //cfg.saveChanges();
    }

    private void createPlayerHealthCondition(double health) {
        String id = "heatlh_" + NumberUtil.format(health);
        var condition = Conditions.PLAYER_HEALTH;
        String raw = "[" + condition.getName() + "] " + Operator.SMALLER.getRaw() + " " + health + "%";

        this.saveCondition(CONDITIONS_PLAYER, id, condition, raw,
            "You must have no more than " + NumberUtil.format(health) + "% of health!");

        /*JYML cfg = new JYML(plugin.getDataFolder() + Config.DIR_CONDITIONS, CONDITIONS_PLAYER + ".yml");
        ConditionConfig conditionConfig = new ConditionConfig(id, id, new ArrayList<>(), new HashMap<>());
        conditionConfig.setName(StringUtil.capitalizeUnderscored(id));
        conditionConfig.setDescription(Arrays.asList(
            "You must have no more than " + NumberUtil.format(health) + "% of health!"
        ));
        conditionConfig.getConditionMap().put(Placeholders.DEFAULT, List.of(Pair.of(condition, raw)));
        conditionConfig.write(cfg, id);
        cfg.saveChanges();*/
    }

    private void createPlayerLevelCondition(int level) {
        String id = "level_" + level;
        var condition = Conditions.PLAYER_LEVEL;
        String raw = "[" + condition.getName() + "] " + Operator.GREATER.getRaw() + " " + level;

        this.saveCondition(CONDITIONS_PLAYER, id, condition, raw,
            "You must have " + NumberUtil.format(level) + " XP levels!");

        /*JYML cfg = new JYML(plugin.getDataFolder() + Config.DIR_CONDITIONS, CONDITIONS_PLAYER + ".yml");
        ConditionConfig conditionConfig = new ConditionConfig(id, id, new ArrayList<>(), new HashMap<>());
        conditionConfig.setName(StringUtil.capitalizeUnderscored(id));
        conditionConfig.setDescription(Arrays.asList(
            "You must have " + NumberUtil.format(level) + " XP levels!"
        ));
        conditionConfig.getConditionMap().put(Placeholders.DEFAULT, List.of(Pair.of(condition, raw)));
        conditionConfig.write(cfg, id);
        cfg.saveChanges();*/
    }

    private void createMoneyReward(@NotNull String id, double money) {
        JYML cfg = new JYML(plugin.getDataFolder() + Config.DIR_REWARDS, REWARDS_MONEY + ".yml");

        Reward reward = new Reward(
            id, "$" + NumberUtil.format(money),
            Arrays.asList("eco give " + Placeholders.PLAYER_NAME + " " + NumberUtil.format(money))
        );
        reward.write(cfg, id);
        cfg.saveChanges();
    }

    private void createItemReward(Material material, int amount) {
        JYML cfg = new JYML(plugin.getDataFolder() + Config.DIR_REWARDS, REWARDS_ITEMS + ".yml");

        Reward reward = new Reward(
            material.getKey().getKey() + "_" + amount,
            "x" + NumberUtil.format(amount) + " " + LangManager.getMaterial(material),
            Arrays.asList("give " + Placeholders.PLAYER_NAME + " " + material.getKey().getKey() + " " + amount)
        );
        reward.write(cfg, reward.getId());
        cfg.saveChanges();
    }
}
