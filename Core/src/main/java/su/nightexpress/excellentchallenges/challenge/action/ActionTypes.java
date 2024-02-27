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
import org.bukkit.potion.PotionEffectType;
import su.nightexpress.nightcore.util.ItemUtil;

public class ActionTypes {

    public static final ActionType<InventoryClickEvent, Material> ANVIL_RENAME = ActionType.create(
        "rename_item", ObjectFormatters.MATERIAL, EventHelpers.ANVIL_RENAME
        ).setIcon(ItemUtil.getSkinHead("692d5df805c239022fe1b45f940882bf40b559671937dc71fbc96f630250ebc4"));

    public static final ActionType<InventoryClickEvent, Material> ANVIL_REPAIR = ActionType.create(
        "repair_item", ObjectFormatters.MATERIAL, EventHelpers.ANVIL_REPAIR
    ).setIcon(ItemUtil.getSkinHead("55fddec36655d3b371c79d6113354877577089c1fcb1b3d8e00af32612f26f28"));

    public static final ActionType<BlockBreakEvent, Material> BLOCK_BREAK = ActionType.create(
        "block_break", ObjectFormatters.MATERIAL, EventHelpers.BLOCK_BREAK
    ).setIcon(ItemUtil.getSkinHead("1e1d4bc469d29d22a7ef6d21a61b451291f21bf51fd167e7fd07b719512e87a1"));

    public static final ActionType<BlockFertilizeEvent, Material> BLOCK_FERTILIZE = ActionType.create(
        "block_fertilize", ObjectFormatters.MATERIAL, EventHelpers.BLOCK_FERTILIZE
    ).setIcon(ItemUtil.getSkinHead("b451845943fd0c07f629711e3401a71a31cd371cc3cb36f3f9637b0e759cc48a"));

    public static final ActionType<BlockPlaceEvent, Material> BLOCK_PLACE = ActionType.create(
        "block_place", ObjectFormatters.MATERIAL, EventHelpers.BLOCK_PLACE
    ).setIcon(ItemUtil.getSkinHead("c0d20f6033a58626841c80dc25e604fc22f8dba8eadad7de921330ff26e1659c"));

    public static final ActionType<EntityDamageByEntityEvent, EntityDamageEvent.DamageCause> DAMAGE_INFLICT = ActionType.create(
        "inflict_damage", ObjectFormatters.DAMAGE_CAUSE, EventHelpers.DAMAGE_INFLICT
    ).setIcon(ItemUtil.getSkinHead("ea5fb227a87a953e7225fbe6c1f4fb4884d9c023c1906654b8dca3a9290a54d3"));

    public static final ActionType<EntityDamageEvent, EntityDamageEvent.DamageCause> DAMAGE_RECEIVE = ActionType.create(
        "receive_damage", ObjectFormatters.DAMAGE_CAUSE, EventHelpers.DAMAGE_RECEIVE
    ).setIcon(ItemUtil.getSkinHead("f25def6560cacfeeaa03886e525324ddddae49eb6eb749dc6df207ca3a135ad5"));

    public static final ActionType<EntityBreedEvent, EntityType> ENTITY_BREED = ActionType.create(
        "breed_entity", ObjectFormatters.ENITITY_TYPE, EventHelpers.ENTITY_BREED
    ).setIcon(ItemUtil.getSkinHead("319b7fcd8ab72e293edbdb1c615d658908d2ed354880eb6964634a4657898c60"));

    public static final ActionType<EntityDeathEvent, EntityType> ENTITY_KILL = ActionType.create(
        "kill_entity", ObjectFormatters.ENITITY_TYPE, EventHelpers.ENTITY_KILL
    ).setIcon(ItemUtil.getSkinHead("783aaaee22868cafdaa1f6f4a0e56b0fdb64cd0aeaabd6e83818c312ebe66437"));

    public static final ActionType<EntityDeathEvent, EntityType> ENTITY_SHOOT = ActionType.create(
        "shoot_entity", ObjectFormatters.ENITITY_TYPE, EventHelpers.ENTITY_SHOOT
    ).setIcon(ItemUtil.getSkinHead("c787b7afb5a59953975bba2473749b601d54d6f93ceac7a02ac69aae7f9b8"));

    public static final ActionType<PlayerShearEntityEvent, EntityType> ENTITY_SHEAR = ActionType.create(
        "shear_entity", ObjectFormatters.ENITITY_TYPE, EventHelpers.ENTITY_SHEAR
    ).setIcon(ItemUtil.getSkinHead("a723893df4cfb9c7240fc47b560ccf6ddeb19da9183d33083f2c71f46dad290a"));

    public static final ActionType<EntityTameEvent, EntityType> ENTITY_TAME = ActionType.create(
        "tame_entity", ObjectFormatters.ENITITY_TYPE, EventHelpers.ENTITY_TAME
    ).setIcon(ItemUtil.getSkinHead("28d408842e76a5a454dc1c7e9ac5c1a8ac3f4ad34d6973b5275491dff8c5c251"));

    public static final ActionType<PlayerItemConsumeEvent, Material> ITEM_CONSUME = ActionType.create(
        "consume_item", ObjectFormatters.MATERIAL, EventHelpers.ITEM_CONSUME
    ).setIcon(ItemUtil.getSkinHead("10d71fe099e8bee600213ade9e38bc3b9217dd1dc3b44aab414635bcb68ac548"));

    public static final ActionType<CraftItemEvent, Material> ITEM_CRAFT = ActionType.create(
        "craft_item", ObjectFormatters.MATERIAL, EventHelpers.ITEM_CRAFT
    ).setIcon(ItemUtil.getSkinHead("4c36045208f9b5ddcf8c4433e424b1ca17b94f6b96202fb1e5270ee8d53881b1"));

    public static final ActionType<InventoryClickEvent, Material> ITEM_DISENCHANT = ActionType.create(
        "disenchant_item", ObjectFormatters.MATERIAL, EventHelpers.ITEM_DISENCHANT
    ).setIcon(ItemUtil.getSkinHead("8e99bfa61fe552f1e6636b03fbe40f4e470c3b3cb14f70e9012813790ead568f"));

    public static final ActionType<EnchantItemEvent, Material> ITEM_ENCHANT = ActionType.create(
        "enchant_item", ObjectFormatters.MATERIAL, EventHelpers.ITEM_ENCHANT
    ).setIcon(ItemUtil.getSkinHead("b2f79016cad84d1ae21609c4813782598e387961be13c15682752f126dce7a"));

    public static final ActionType<PlayerFishEvent, Material> ITEM_FISH = ActionType.create(
        "fish_item", ObjectFormatters.MATERIAL, EventHelpers.ITEM_FISH
    ).setIcon(ItemUtil.getSkinHead("1352df85a02d7fac5dca72dfbc6ba8ac0a7f96208bc8048247791ef2216f5c94"));

    public static final ActionType<FurnaceExtractEvent, Material> ITEM_FURNACE = ActionType.create(
        "smelt_item", ObjectFormatters.MATERIAL, EventHelpers.ITEM_FURNACE
    ).setIcon(ItemUtil.getSkinHead("53bf0b8859a1e57f3abd629c0c736e644e81651d4de034feea49f883f00e82b0"));

    public static final ActionType<InventoryClickEvent, Material> ITEM_TRADE = ActionType.create(
        "trade_item", ObjectFormatters.MATERIAL, EventHelpers.ITEM_TRADE
    ).setIcon(ItemUtil.getSkinHead("7e5995106d080f10b2052de08e355f34a2213904d9d32f6dc2d1b27bec753b74"));

    public static final ActionType<BrewEvent, PotionEffectType> POTION_BREW = ActionType.create(
        "brew_potion", ObjectFormatters.POTION_TYPE, EventHelpers.POTION_BREW
    ).setIcon(ItemUtil.getSkinHead("93a728ad8d31486a7f9aad200edb373ea803d1fc5fd4321b2e2a971348234443"));

    public static final ActionType<PlayerItemConsumeEvent, PotionEffectType> POTION_DRINK = ActionType.create(
        "drink_potion", ObjectFormatters.POTION_TYPE, EventHelpers.POTION_DRINK
    ).setIcon(ItemUtil.getSkinHead("5522185926708535dc2dd6024cdde8317ec350e74c3ca3645e9902b2c7870ba5"));

    public static final ActionType<ProjectileLaunchEvent, EntityType> PROJECTILE_LAUNCH = ActionType.create(
        "launch_projectile", ObjectFormatters.ENITITY_TYPE, EventHelpers.PROJECTILE_LAUNCH
    ).setIcon(ItemUtil.getSkinHead("1dfd7724c69a024dcfc60b16e00334ab5738f4a92bafb8fbc76cf15322ea0293"));

    public static final ActionType<InventoryClickEvent, Enchantment> ENCHANT_REMOVE = ActionType.create(
        "remove_enchant", ObjectFormatters.ENCHANTMENT, EventHelpers.ENCHANT_REMOVE
    ).setIcon(ItemUtil.getSkinHead("8e99bfa61fe552f1e6636b03fbe40f4e470c3b3cb14f70e9012813790ead568f"));

    public static final ActionType<EnchantItemEvent, Enchantment> ENCHANT_GET = ActionType.create(
        "get_enchant", ObjectFormatters.ENCHANTMENT, EventHelpers.ENCHANT_GET
    ).setIcon(ItemUtil.getSkinHead("b62651879d870499da50e34036800ddffd52f3e4e1993c5fc0fc825d03446d8b"));
}
