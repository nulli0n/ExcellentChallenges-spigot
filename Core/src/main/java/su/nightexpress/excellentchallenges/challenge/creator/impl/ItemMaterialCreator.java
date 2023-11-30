package su.nightexpress.excellentchallenges.challenge.creator.impl;

import com.google.common.collect.Sets;
import org.bukkit.inventory.*;
import su.nexmedia.engine.Version;
import su.nightexpress.excellentchallenges.challenge.action.ActionType;
import su.nightexpress.excellentchallenges.challenge.action.ActionTypes;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.enchantments.EnchantmentTarget;
import org.jetbrains.annotations.NotNull;
import su.nexmedia.engine.utils.values.UniInt;
import su.nightexpress.excellentchallenges.ExcellentChallengesPlugin;
import su.nightexpress.excellentchallenges.challenge.creator.CreatorManager;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ItemMaterialCreator extends AbstractCreator<Material> {

    public ItemMaterialCreator(@NotNull ExcellentChallengesPlugin plugin) {
        super(plugin);
    }

    @Override
    public void create() {
        //this.createNamesLists(ActionTypes.ITEM_CRAFT);

        this.createItemConsumeGenerators();
        this.createItemCraftGenerators();
        this.createItemEnchantGenerators();
        this.createItemFishGenerators();
        this.createItemSmeltGenerators();
        this.createItemTradeGenerators();
        this.createAnvilRenameGenerators();
    }

    /*@NotNull
    @Override
    public ItemStack getIcon(@NotNull Material object) {
        return new ItemStack(object);
    }

    @NotNull
    @Override
    public List<String> getNames(@NotNull ActionType<?, Material> actionType) {
        List<String> names = new ArrayList<>();
        if (actionType == ActionTypes.ITEM_CRAFT) {
            names.add("Crafting the Perfect " + Placeholders.GENERIC_NAME + " Set");
            names.add("Crafting the Ultimate " + Placeholders.GENERIC_NAME);
            names.add("Crafting the " + Placeholders.GENERIC_NAME + " Legacy");
            names.add("Crafting with Supreme " + Placeholders.GENERIC_NAME);
            names.add("Crafting Unique " + Placeholders.GENERIC_NAME);
            names.add("Crafting Supreme " + Placeholders.GENERIC_NAME);
            names.add("Craft Your " + Placeholders.GENERIC_NAME + " Suit");
            names.add("Craftsmanship in " + Placeholders.GENERIC_NAME + " Making");
            names.add("Creating Elegant " + Placeholders.GENERIC_NAME);
            names.add("Designing Spaces with " + Placeholders.GENERIC_NAME);
            names.add("Architectural Wonders with " + Placeholders.GENERIC_NAME);
            names.add("Building Marvels with " + Placeholders.GENERIC_NAME);
            names.add("Mastering the Art of " + Placeholders.GENERIC_NAME);
            names.add("Mastering the " + Placeholders.GENERIC_NAME + " Craft");
            names.add("Fashioning the Perfect " + Placeholders.GENERIC_NAME);
            names.add("Embellishing with " + Placeholders.GENERIC_NAME);
            names.add("The Art of " + Placeholders.GENERIC_NAME + " Crafting");
            names.add("Supreme " + Placeholders.GENERIC_NAME + " Ensemble");
            names.add("Elegant " + Placeholders.GENERIC_NAME + " Designs");
            names.add("The Legendary " + Placeholders.GENERIC_NAME + " Crafted");
            names.add(Placeholders.GENERIC_NAME + " Artistry Revealed");
            names.add(Placeholders.GENERIC_NAME + " Perfection Unveiled");
            names.add(Placeholders.GENERIC_NAME + " Mastery in Progress");
            names.add(Placeholders.GENERIC_NAME + " Mastery Unlocked");
            names.add(Placeholders.GENERIC_NAME + " Mastery Unleashed");
            names.add(Placeholders.GENERIC_NAME + " Masterpiece Unleashed");
            names.add(Placeholders.GENERIC_NAME + " Elegance Revealed");
            names.add(Placeholders.GENERIC_NAME + " Crafting Mastery");
        }
        return names;
    }*/

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

    @NotNull
    @Override
    public UniInt getMinProgress(@NotNull ActionType<?, Material> actionType) {
        if (actionType == ActionTypes.ITEM_DISENCHANT || actionType == ActionTypes.ITEM_ENCHANT
            || actionType == ActionTypes.ANVIL_RENAME || actionType == ActionTypes.ANVIL_REPAIR) {
            return UniInt.of(1, 5);
        }
        if (actionType == ActionTypes.ITEM_TRADE) {
            return UniInt.of(1, 3);
        }
        if (actionType == ActionTypes.ITEM_FISH) {
            return UniInt.of(5, 12);
        }
        return UniInt.of(8, 14);
    }

    @NotNull
    @Override
    public UniInt getMaxProgress(@NotNull ActionType<?, Material> actionType) {
        if (actionType == ActionTypes.ITEM_DISENCHANT || actionType == ActionTypes.ITEM_ENCHANT
            || actionType == ActionTypes.ANVIL_RENAME || actionType == ActionTypes.ANVIL_REPAIR) {
            return UniInt.of(6, 12);
        }
        if (actionType == ActionTypes.ITEM_TRADE) {
            return UniInt.of(4, 9);
        }
        return UniInt.of(16, 30);
    }

    private boolean isCraftable(@NotNull Material material, @NotNull Predicate<Recipe> predicate) {
        var iterator = this.plugin.getServer().recipeIterator();
        while (iterator.hasNext()) {
            Recipe recipe = iterator.next();
            if (!predicate.test(recipe)) continue;

            if (recipe.getResult().getType() == material) {
                return true;
            }
        }
        return false;
    }

    private void createItemCraftGenerators() {
        Map<String, Set<Material>> materials;

        if (Version.isAtLeast(Version.V1_20_R2)) {
            materials = getCraftableMaterials(recipe -> recipe instanceof CraftingRecipe);
        }
        else {
            materials = getCraftableMaterials(recipe -> recipe instanceof ShapedRecipe || recipe instanceof ShapelessRecipe);
        }

        this.createGenerator(ActionTypes.ITEM_CRAFT, materials);
    }

    private void createItemSmeltGenerators() {
        var materials = getCraftableMaterials(recipe -> recipe instanceof FurnaceRecipe);

        this.createGenerator(ActionTypes.ITEM_FURNACE, materials);
    }

    private void createItemEnchantGenerators() {
        Set<Material> enchantable = Stream.of(Material.values())
            .filter(material -> Stream.of(EnchantmentTarget.values()).anyMatch(t -> t.includes(material)))
            .filter(material -> material != Material.CARROT_ON_A_STICK && material != Material.WARPED_FUNGUS_ON_A_STICK)
            .filter(material -> material != Material.SHIELD)
            .filter(material -> !material.name().endsWith("_HEAD"))
            .collect(Collectors.toSet());

        this.createGenerator(ActionTypes.ITEM_DISENCHANT, createMaterialGroups(enchantable));
        this.createGenerator(ActionTypes.ITEM_ENCHANT, createMaterialGroups(enchantable));
    }

    private void createItemConsumeGenerators() {
        Map<String, Set<Material>> materials = new HashMap<>();
        materials.put(VARIOUS, Stream.of(Material.values()).filter(Material::isEdible).collect(Collectors.toSet()));

        this.createGenerator(ActionTypes.ITEM_CONSUME, materials);
    }

    private void createItemFishGenerators() {
        Map<String, Set<Material>> materials = new HashMap<>();
        materials.put("fish", Sets.newHashSet(
            Material.COD, Material.PUFFERFISH,
            Material.SALMON, Material.TROPICAL_FISH
        ));

        materials.put("treasure", Sets.newHashSet(
            Material.BOW,Material.SADDLE,
            Material.ENCHANTED_BOOK, Material.FISHING_ROD,
            Material.NAME_TAG, Material.NAUTILUS_SHELL
        ));

        materials.put("junk", Sets.newHashSet(
            Material.LILY_PAD, Material.BOWL,
            Material.FISHING_ROD, Material.ROTTEN_FLESH,
            Material.LEATHER, Material.LEATHER_BOOTS,
            Material.STICK, Material.STRING,
            Material.POTION, Material.BONE,
            Material.INK_SAC, Material.TRIPWIRE_HOOK,
            Material.BAMBOO, Material.COCOA_BEANS
        ));

        this.createGenerator(ActionTypes.ITEM_FISH, materials);
    }

    private void createItemTradeGenerators() {
        Map<String, Set<Material>> materials = new HashMap<>();
        materials.put("armorer", Sets.newHashSet(
            Material.EMERALD, Material.BELL,
            Material.IRON_HELMET, Material.IRON_CHESTPLATE,
            Material.IRON_LEGGINGS, Material.IRON_BOOTS,
            Material.CHAINMAIL_HELMET, Material.CHAINMAIL_CHESTPLATE,
            Material.CHAINMAIL_LEGGINGS, Material.CHAINMAIL_BOOTS,
            Material.SHIELD,
            Material.DIAMOND_HELMET, Material.DIAMOND_CHESTPLATE,
            Material.DIAMOND_LEGGINGS, Material.DIAMOND_BOOTS
        ));

        materials.put("butcher", Sets.newHashSet(
            Material.EMERALD, Material.RABBIT_STEW, Material.COOKED_CHICKEN, Material.COOKED_PORKCHOP
        ));

        materials.put("cartographer", Sets.newHashSet(
            Material.EMERALD, Material.MAP,
            Material.ITEM_FRAME, Material.GLOBE_BANNER_PATTERN,
            Material.BLACK_BANNER, Material.BLUE_BANNER,
            Material.BROWN_BANNER, Material.CYAN_BANNER,
            Material.GRAY_BANNER, Material.GREEN_BANNER,
            Material.LIGHT_BLUE_BANNER, Material.LIGHT_GRAY_BANNER,
            Material.LIME_BANNER, Material.MAGENTA_BANNER,
            Material.ORANGE_BANNER, Material.PINK_BANNER,
            Material.PURPLE_BANNER, Material.RED_BANNER,
            Material.WHITE_BANNER, Material.YELLOW_BANNER
        ));

        materials.put("cleric", Sets.newHashSet(
            Material.EMERALD, Material.REDSTONE,
            Material.LAPIS_LAZULI, Material.GLOWSTONE,
            Material.ENDER_PEARL, Material.EXPERIENCE_BOTTLE
        ));

        materials.put("farmer", Sets.newHashSet(
            Material.EMERALD, Material.BREAD,
            Material.PUMPKIN_PIE, Material.APPLE,
            Material.COOKIE, Material.SUSPICIOUS_STEW,
            Material.CAKE, Material.GOLDEN_CARROT, Material.GLISTERING_MELON_SLICE
        ));

        materials.put("fisherman", Sets.newHashSet(
            Material.EMERALD, Material.COD_BUCKET,
            Material.COOKED_COD, Material.CAMPFIRE,
            Material.COOKED_SALMON, Material.FISHING_ROD
        ));

        materials.put("fletcher", Sets.newHashSet(
            Material.EMERALD, Material.ARROW, Material.FLINT,
            Material.BOW, Material.CROSSBOW, Material.TIPPED_ARROW
        ));

        materials.put("leatherworker", Sets.newHashSet(
            Material.EMERALD,
            Material.LEATHER_HELMET, Material.LEATHER_CHESTPLATE,
            Material.LEATHER_LEGGINGS, Material.LEATHER_BOOTS,
            Material.LEATHER_HORSE_ARMOR, Material.SADDLE
        ));

        materials.put("librarian", Sets.newHashSet(
            Material.EMERALD, Material.ENCHANTED_BOOK,
            Material.BOOKSHELF, Material.LANTERN,
            Material.GLASS, Material.COMPASS, Material.CLOCK,
            Material.NAME_TAG
        ));

        materials.put("toolsmith", Sets.newHashSet(
            Material.EMERALD, Material.BELL,
            Material.STONE_AXE, Material.STONE_HOE, Material.STONE_PICKAXE, Material.STONE_SHOVEL,
            Material.IRON_AXE, Material.IRON_HOE, Material.IRON_PICKAXE, Material.IRON_SHOVEL,
            Material.DIAMOND_AXE, Material.DIAMOND_HOE, Material.DIAMOND_PICKAXE, Material.DIAMOND_SHOVEL
        ));

        this.createGenerator(ActionTypes.ITEM_TRADE, materials);
    }

    private void createAnvilRenameGenerators() {
        Map<String, Set<Material>> materials = new HashMap<>();
        materials.put("tools", Tag.ITEMS_TOOLS.getValues());
        materials.put("swords", Tag.ITEMS_SWORDS.getValues());
        materials.put("bows", Sets.newHashSet(Material.BOW, Material.CROSSBOW));
        materials.put("armors", Sets.newHashSet(
            Material.SHIELD, Material.TRIDENT,
            Material.IRON_HELMET, Material.IRON_CHESTPLATE,
            Material.IRON_LEGGINGS, Material.IRON_BOOTS,
            Material.GOLDEN_HELMET, Material.GOLDEN_CHESTPLATE,
            Material.GOLDEN_LEGGINGS, Material.GOLDEN_BOOTS,
            Material.CHAINMAIL_HELMET, Material.CHAINMAIL_CHESTPLATE,
            Material.CHAINMAIL_LEGGINGS, Material.CHAINMAIL_BOOTS,
            Material.DIAMOND_HELMET, Material.DIAMOND_CHESTPLATE,
            Material.DIAMOND_LEGGINGS, Material.DIAMOND_BOOTS,
            Material.NETHERITE_HELMET, Material.NETHERITE_CHESTPLATE,
            Material.NETHERITE_LEGGINGS, Material.NETHERITE_BOOTS,
            Material.LEATHER_HELMET, Material.LEATHER_CHESTPLATE,
            Material.LEATHER_LEGGINGS, Material.LEATHER_BOOTS,
            Material.TURTLE_HELMET
            ));
        this.createGenerator(ActionTypes.ANVIL_RENAME, materials);
        this.createGenerator(ActionTypes.ANVIL_REPAIR, materials);
    }

    @NotNull
    private Map<String, Set<Material>> getCraftableMaterials(@NotNull Predicate<Recipe> predicate) {
        Set<Material> craftable = Stream.of(Material.values())
            .filter(material -> isCraftable(material, predicate))
            .collect(Collectors.toSet());

        return createMaterialGroups(craftable);
    }

    @NotNull
    private Map<String, Set<Material>> createMaterialGroups(@NotNull Set<Material> materialSet) {
        Map<String, Set<Material>> byItemType = new HashMap<>();
        materialSet.forEach(material -> {
            String[] split = material.name().split("_");
            String prefix = split.length == 1 ? material.name() : split[split.length - 1];

            byItemType.computeIfAbsent(prefix.toLowerCase(), k -> new HashSet<>()).add(material);
        });

        Map<String, Set<Material>> materials = new HashMap<>();
        byItemType.forEach((key, set) -> {
            if (set.size() == 1) {
                materials.computeIfAbsent(VARIOUS, k -> new HashSet<>()).addAll(set);
            }
            else materials.computeIfAbsent(key, k -> new HashSet<>()).addAll(set);
        });

        return materials;
    }
}
