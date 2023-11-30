package su.nightexpress.excellentchallenges.challenge.action;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockFertilizeEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.BrewEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.FurnaceExtractEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerShearEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;
import su.nexmedia.engine.utils.ItemUtil;

public class ActionTypes {

    private static final ItemStack ICON_TRADE = ItemUtil.createCustomHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzFmNzE0MTMzY2U3OGQxMTgxYzRkNWQzZTUzNzExZWNlMTBjNGM5YTI4MjAxMTg4ZWUxYTZmMzVjYzBmYTNjYSJ9fX0=");
    private static final ItemStack ICON_CRAFT   = ItemUtil.createCustomHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNGMzNjA0NTIwOGY5YjVkZGNmOGM0NDMzZTQyNGIxY2ExN2I5NGY2Yjk2MjAyZmIxZTUyNzBlZThkNTM4ODFiMSJ9fX0=");
    private static final ItemStack ICON_ZOMBIE  = ItemUtil.createCustomHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzgzYWFhZWUyMjg2OGNhZmRhYTFmNmY0YTBlNTZiMGZkYjY0Y2QwYWVhYWJkNmU4MzgxOGMzMTJlYmU2NjQzNyJ9fX0=");
    private static final ItemStack ICON_FURNACE = ItemUtil.createCustomHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDE3YjhiNDNmOGM0YjVjZmViOTE5YzlmOGZlOTNmMjZjZWI2ZDJiMTMzYzJhYjFlYjMzOWJkNjYyMWZkMzA5YyJ9fX0=");
    private static final ItemStack ICON_CAULDRON = ItemUtil.createCustomHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzVhZDcyOWY5MGViMWQwYWNiMTA1ZjU0MGZhN2UyMThiMjViOTM5YTAyNDM4YTBiYjhjZDVmNDI4MmYxODQwMSJ9fX0=");
    private static final ItemStack ICON_POTION = ItemUtil.createCustomHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTUyMjE4NTkyNjcwODUzNWRjMmRkNjAyNGNkZGU4MzE3ZWMzNTBlNzRjM2NhMzY0NWU5OTAyYjJjNzg3MGJhNSJ9fX0=");
    private static final ItemStack ICON_GRINDSTONE = ItemUtil.createCustomHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOGU5OWJmYTYxZmU1NTJmMWU2NjM2YjAzZmJlNDBmNGU0NzBjM2IzY2IxNGY3MGU5MDEyODEzNzkwZWFkNTY4ZiJ9fX0=");
    private static final ItemStack ICON_FISH = ItemUtil.createCustomHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTcxNTI4NzZiYzNhOTZkZDJhMjI5OTI0NWVkYjNiZWVmNjQ3YzhhNTZhYzg4NTNhNjg3YzNlN2I1ZDhiYiJ9fX0=");
    private static final ItemStack ICON_ENCHANT_BOOK = ItemUtil.createCustomHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjYyNjUxODc5ZDg3MDQ5OWRhNTBlMzQwMzY4MDBkZGZmZDUyZjNlNGUxOTkzYzVmYzBmYzgyNWQwMzQ0NmQ4YiJ9fX0=");
    private static final ItemStack ICON_ENCHANT_TABLE = ItemUtil.createCustomHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjJmNzkwMTZjYWQ4NGQxYWUyMTYwOWM0ODEzNzgyNTk4ZTM4Nzk2MWJlMTNjMTU2ODI3NTJmMTI2ZGNlN2EifX19");
    private static final ItemStack ICON_SNOWBALL = ItemUtil.createCustomHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTExNWM3OTY4ZWMzNzcxZWU5ZmY2YWU2YmNhMmQ1YmEzOTYyYWE3MjdhNGZhOGQzNzYwOGU0YzliZjE1MTJiYiJ9fX0=");
    private static final ItemStack ICON_BURGER = ItemUtil.createCustomHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTllZDIyNTU1YzY1NDk0NWI4YmU1YTBjZDQzYzBiYmU0MTUwZDczNWVjNzE5YjQ1YzU4ZjlhZDFmMTllYTVlZiJ9fX0=");
    private static final ItemStack ICON_WOLF = ItemUtil.createCustomHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNmNmMmE1MDA4MDNmNzI0NTI0MGRlY2ZiMDIyYTgyYTM0ZGFlNTI4YWE1ZTIxYmRlYWU0ZmY2NWQzYWI3MWYzNyJ9fX0=");
    private static final ItemStack ICON_SHEEP = ItemUtil.createCustomHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvN2I0NGZiOGEwODA1N2U0YTcyNzhhNmM1YWEyY2I2OTJmMmU3Y2ZlYTk2MGM2OGZjMGQ0ZDJlMjZlODhkZDM1OSJ9fX0=");
    private static final ItemStack ICON_PIG = ItemUtil.createCustomHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzMzYzUxNmE2YmM5Zjc1MjM5YTYyYzFhMjc2YzgyNTYwNGI4NzI5MDRmMmE4NzgwYmFmY2VlODA5MTE2ODQxNCJ9fX0=");
    private static final ItemStack ICON_KNIGHT = ItemUtil.createCustomHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzcxZDFkNGUxNGM1ZTM3OTI4NzE3ZTNmNzEzMTBhM2I5OTMzMGY1NzNlYjkyYjA2M2E0YjM2MWRmNTlhZDc4NyJ9fX0=");
    private static final ItemStack ICON_BUILDER = ItemUtil.createCustomHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzBkMjBmNjAzM2E1ODYyNjg0MWM4MGRjMjVlNjA0ZmMyMmY4ZGJhOGVhZGFkN2RlOTIxMzMwZmYyNmUxNjU5YyJ9fX0=");
    private static final ItemStack ICON_FARMER = ItemUtil.createCustomHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvN2E4YWIyMjY3YWU0NDNlODNiMDVlM2E4ZDY3YjhiNzYwYWRmYWFkMzM1YzkwNDczMzhjMGUxNzc0YTY1YzM3MiJ9fX0=");
    private static final ItemStack ICON_MINER = ItemUtil.createCustomHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMWUxZDRiYzQ2OWQyOWQyMmE3ZWY2ZDIxYTYxYjQ1MTI5MWYyMWJmNTFmZDE2N2U3ZmQwN2I3MTk1MTJlODdhMSJ9fX0=");
    private static final ItemStack ICON_ANVIL = ItemUtil.createCustomHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjkyZDVkZjgwNWMyMzkwMjJmZTFiNDVmOTQwODgyYmY0MGI1NTk2NzE5MzdkYzcxZmJjOTZmNjMwMjUwZWJjNCJ9fX0=");
    private static final ItemStack ICON_TARGET = ItemUtil.createCustomHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNWVjZDk3MjdlOWQ5ZjJjMDczOTkzNGVmMWU2M2QxZDg5YzkyMzY5NDIxY2FmYmFlYTczM2NjZTM1YzNiNjc2OCJ9fX0=");
    private static final ItemStack ICON_IRON = ItemUtil.createCustomHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDAwODgyMTU5MjhkZjBhMWVjZDM5NjUyMGY1ZTk0YjA2ODhjMGEzMjk5MDJhOThhMmJiYTZmYWZhN2M2MzRmZSJ9fX0=");

    public static final ActionType<InventoryClickEvent, Material> ANVIL_RENAME = ActionType.create(
        "rename_item", ICON_ANVIL, ObjectFormatters.MATERIAL, EventHelpers.ANVIL_RENAME
    );

    public static final ActionType<InventoryClickEvent, Material> ANVIL_REPAIR = ActionType.create(
        "repair_item", ICON_IRON, ObjectFormatters.MATERIAL, EventHelpers.ANVIL_REPAIR
    );

    public static final ActionType<BlockBreakEvent, Material> BLOCK_BREAK = ActionType.create(
        "block_break", ICON_MINER, ObjectFormatters.MATERIAL, EventHelpers.BLOCK_BREAK
    );

    public static final ActionType<BlockFertilizeEvent, Material> BLOCK_FERTILIZE = ActionType.create(
        "block_fertilize", ICON_FARMER, ObjectFormatters.MATERIAL, EventHelpers.BLOCK_FERTILIZE
    );

    public static final ActionType<BlockPlaceEvent, Material> BLOCK_PLACE = ActionType.create(
        "block_place", ICON_BUILDER, ObjectFormatters.MATERIAL, EventHelpers.BLOCK_PLACE
    );

    public static final ActionType<EntityDamageByEntityEvent, EntityDamageEvent.DamageCause> DAMAGE_INFLICT = ActionType.create(
        "inflict_damage", ICON_KNIGHT, ObjectFormatters.DAMAGE_CAUSE, EventHelpers.DAMAGE_INFLICT
    );

    public static final ActionType<EntityDamageEvent, EntityDamageEvent.DamageCause> DAMAGE_RECEIVE = ActionType.create(
        "receive_damage", ICON_KNIGHT, ObjectFormatters.DAMAGE_CAUSE, EventHelpers.DAMAGE_RECEIVE
    );

    public static final ActionType<EntityBreedEvent, EntityType> ENTITY_BREED = ActionType.create(
        "breed_entity", ICON_PIG, ObjectFormatters.ENITITY_TYPE, EventHelpers.ENTITY_BREED
    );

    public static final ActionType<EntityDeathEvent, EntityType> ENTITY_KILL = ActionType.create(
        "kill_entity", ICON_ZOMBIE, ObjectFormatters.ENITITY_TYPE, EventHelpers.ENTITY_KILL
    );

    public static final ActionType<EntityDeathEvent, EntityType> ENTITY_SHOOT = ActionType.create(
        "shoot_entity", ICON_TARGET, ObjectFormatters.ENITITY_TYPE, EventHelpers.ENTITY_SHOOT
    );

    public static final ActionType<PlayerShearEntityEvent, EntityType> ENTITY_SHEAR = ActionType.create(
        "shear_entity", ICON_SHEEP, ObjectFormatters.ENITITY_TYPE, EventHelpers.ENTITY_SHEAR
    );

    public static final ActionType<EntityTameEvent, EntityType> ENTITY_TAME = ActionType.create(
        "tame_entity", ICON_WOLF, ObjectFormatters.ENITITY_TYPE, EventHelpers.ENTITY_TAME
    );

    public static final ActionType<PlayerItemConsumeEvent, Material> ITEM_CONSUME = ActionType.create(
        "consume_item", ICON_BURGER, ObjectFormatters.MATERIAL, EventHelpers.ITEM_CONSUME
    );

    public static final ActionType<CraftItemEvent, Material> ITEM_CRAFT = ActionType.create(
        "craft_item", ICON_CRAFT, ObjectFormatters.MATERIAL, EventHelpers.ITEM_CRAFT
    );

    public static final ActionType<InventoryClickEvent, Material> ITEM_DISENCHANT = ActionType.create(
        "disenchant_item", ICON_GRINDSTONE, ObjectFormatters.MATERIAL, EventHelpers.ITEM_DISENCHANT
    );

    public static final ActionType<EnchantItemEvent, Material> ITEM_ENCHANT = ActionType.create(
        "enchant_item", ICON_ENCHANT_TABLE, ObjectFormatters.MATERIAL, EventHelpers.ITEM_ENCHANT
    );

    public static final ActionType<PlayerFishEvent, Material> ITEM_FISH = ActionType.create(
        "fish_item", ICON_FISH, ObjectFormatters.MATERIAL, EventHelpers.ITEM_FISH
    );

    public static final ActionType<FurnaceExtractEvent, Material> ITEM_FURNACE = ActionType.create(
        "smelt_item", ICON_FURNACE, ObjectFormatters.MATERIAL, EventHelpers.ITEM_FURNACE
    );

    public static final ActionType<InventoryClickEvent, Material> ITEM_TRADE = ActionType.create(
        "trade_item", ICON_TRADE, ObjectFormatters.MATERIAL, EventHelpers.ITEM_TRADE
    );

    public static final ActionType<BrewEvent, PotionEffectType> POTION_BREW = ActionType.create(
        "brew_potion", ICON_CAULDRON, ObjectFormatters.POTION_TYPE, EventHelpers.POTION_BREW
    );

    public static final ActionType<PlayerItemConsumeEvent, PotionEffectType> POTION_DRINK = ActionType.create(
        "drink_potion", ICON_POTION, ObjectFormatters.POTION_TYPE, EventHelpers.POTION_DRINK
    );

    public static final ActionType<ProjectileLaunchEvent, EntityType> PROJECTILE_LAUNCH = ActionType.create(
        "launch_projectile", ICON_SNOWBALL, ObjectFormatters.ENITITY_TYPE, EventHelpers.PROJECTILE_LAUNCH
    );

    public static final ActionType<InventoryClickEvent, Enchantment> ENCHANT_REMOVE = ActionType.create(
        "remove_enchant", ICON_GRINDSTONE, ObjectFormatters.ENCHANTMENT, EventHelpers.ENCHANT_REMOVE
    );

    public static final ActionType<EnchantItemEvent, Enchantment> ENCHANT_GET = ActionType.create(
        "get_enchant", ICON_ENCHANT_BOOK, ObjectFormatters.ENCHANTMENT, EventHelpers.ENCHANT_GET
    );
}
