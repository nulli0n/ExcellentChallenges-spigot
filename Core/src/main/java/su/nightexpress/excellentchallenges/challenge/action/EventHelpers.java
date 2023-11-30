package su.nightexpress.excellentchallenges.challenge.action;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BrewingStand;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockFertilizeEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerShearEntityEvent;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import org.bukkit.projectiles.ProjectileSource;
import su.nexmedia.engine.utils.*;
import su.nexmedia.engine.utils.blocktracker.PlayerBlockTracker;
import su.nightexpress.excellentchallenges.Keys;
import su.nightexpress.excellentchallenges.config.Config;
import su.nightexpress.excellentchallenges.hooks.HookId;
import su.nightexpress.excellentchallenges.hooks.external.MythicMobsHook;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class EventHelpers {

    public static final EventHelper<InventoryClickEvent, Material> ANVIL_RENAME  = (plugin, event, processor) -> {
        Inventory inventory = event.getInventory();
        if (inventory.getType() != InventoryType.ANVIL) return false;
        if (event.getRawSlot() != 2 || event.getClick() == ClickType.MIDDLE) return false;

        AnvilInventory anvil = (AnvilInventory) inventory;
        if (anvil.getRepairCost() <= 0) return false;

        ItemStack first = anvil.getItem(0);
        if (first == null || first.getType().isAir()) return false;

        ItemStack result = anvil.getItem(2);
        if (result == null || result.getType().isAir()) return false;

        String renameText = anvil.getRenameText();
        if (renameText == null) return false;

        String nameSource = Colorizer.restrip(ItemUtil.getItemName(first));
        String nameResult = Colorizer.restrip(renameText);
        if (nameSource.equalsIgnoreCase(nameResult)) return false;

        Player player = (Player) event.getWhoClicked();
        plugin.runTask(task -> {
            ItemStack result2 = anvil.getItem(2);
            if (result2 != null && !result2.getType().isAir()) return;

            processor.progressChallenge(player, result.getType(), result.getAmount());
        });
        return true;
    };

    public static final EventHelper<InventoryClickEvent, Material> ANVIL_REPAIR  = (plugin, event, processor) -> {
        Inventory inventory = event.getInventory();
        if (inventory.getType() != InventoryType.ANVIL) return false;
        if (event.getRawSlot() != 2 || event.getClick() == ClickType.MIDDLE) return false;

        AnvilInventory anvil = (AnvilInventory) inventory;
        if (anvil.getRepairCost() <= 0) return false;

        ItemStack first = anvil.getItem(0);
        if (first == null || first.getType().isAir()) return false;

        ItemStack result = anvil.getItem(2);
        if (result == null || result.getType().isAir()) return false;

        if (first.getType() != result.getType()) return false;

        int damageSource = 0;
        int damageResult = 0;

        if (first.getItemMeta() instanceof Damageable damageable) {
            damageSource = damageable.getDamage();
        }
        if (result.getItemMeta() instanceof Damageable damageable) {
            damageResult = damageable.getDamage();
        }
        if (damageSource == damageResult) return false;

        Player player = (Player) event.getWhoClicked();
        plugin.runTask(task -> {
            ItemStack result2 = anvil.getItem(2);
            if (result2 != null && !result2.getType().isAir()) return;

            processor.progressChallenge(player, result.getType(), 1);
        });
        return true;
    };

    public static final EventHelper<BlockBreakEvent, Material> BLOCK_BREAK = (plugin, event, processor) -> {
        Block block = event.getBlock();
        if (Config.OBJECTIVES_ANTI_GLITCH_TRACK_BLOCKS.get() && PlayerBlockTracker.isTracked(block)) return false;

        Player player = event.getPlayer();
        processor.progressChallenge(player, block.getType(), 1);
        return true;
    };

    public static final EventHelper<BlockFertilizeEvent, Material> BLOCK_FERTILIZE = (plugin, event, processor) -> {
        Player player = event.getPlayer();
        if (player == null) return false;

        event.getBlocks().forEach(blockState -> {
            processor.progressChallenge(player, blockState.getType(), 1);
        });
        return true;
    };

    public static final EventHelper<BlockPlaceEvent, Material> BLOCK_PLACE = (plugin, event, processor) -> {
        Block block = event.getBlockPlaced();

        processor.progressChallenge(event.getPlayer(), block.getType(), 1);
        return false;
    };

    public static final EventHelper<EntityDamageByEntityEvent, EntityDamageEvent.DamageCause> DAMAGE_INFLICT = (plugin, event, processor) -> {
        Entity damager = event.getDamager();
        if (EntityUtil.isNPC(damager)) return false;
        if (!(damager instanceof Player player)) return false;

        EntityDamageEvent.DamageCause cause = event.getCause();
        double damage = event.getDamage();
        processor.progressChallenge(player, cause, (int) damage);
        return true;
    };

    public static final EventHelper<EntityDamageEvent, EntityDamageEvent.DamageCause> DAMAGE_RECEIVE = (plugin, event, processor) -> {
        Entity victim = event.getEntity();
        if (EntityUtil.isNPC(victim)) return false;
        if (!(victim instanceof Player player)) return false;

        EntityDamageEvent.DamageCause cause = event.getCause();
        double damage = event.getDamage();
        processor.progressChallenge(player, cause, (int) damage);
        return true;
    };

    public static final EventHelper<EntityBreedEvent, EntityType> ENTITY_BREED = (plugin, event, processor) -> {
        LivingEntity breeder = event.getBreeder();
        if (!(breeder instanceof Player player)) return false;

        processor.progressChallenge(player, event.getEntity().getType(), 1);
        return true;
    };

    public static final EventHelper<EntityDeathEvent, EntityType> ENTITY_KILL = (plugin, event, processor) -> {
        LivingEntity entity = event.getEntity();
        if (PDCUtil.getBoolean(entity, Keys.ENTITY_TRACKED).orElse(false)) return false;

        Player killer = entity.getKiller();
        if (killer == null || EntityUtil.isNPC(killer)) return false;

        // Do not count MythicMobs here.
        if (EngineUtils.hasPlugin(HookId.MYTHIC_MOBS) && MythicMobsHook.isMythicMob(entity)) return false;

        processor.progressChallenge(killer, entity.getType(), 1);
        return true;
    };

    public static final EventHelper<EntityDeathEvent, EntityType> ENTITY_SHOOT = (plugin, event, processor) -> {
        LivingEntity entity = event.getEntity();
        if (PDCUtil.getBoolean(entity, Keys.ENTITY_TRACKED).orElse(false)) return false;

        Player killer = entity.getKiller();
        if (killer == null || EntityUtil.isNPC(killer)) return false;

        if (!(entity.getLastDamageCause() instanceof EntityDamageByEntityEvent ede)) return false;
        if (!(ede.getDamager() instanceof Projectile projectile)) return false;

        // Do not count MythicMobs here.
        if (EngineUtils.hasPlugin(HookId.MYTHIC_MOBS) && MythicMobsHook.isMythicMob(entity)) return false;

        processor.progressChallenge(killer, entity.getType(), 1);
        return true;
    };

    public static final EventHelper<PlayerShearEntityEvent, EntityType> ENTITY_SHEAR = (plugin, event, processor) -> {
        Player player = event.getPlayer();
        Entity entity = event.getEntity();

        processor.progressChallenge(player, entity.getType(), 1);
        return true;
    };

    public static final EventHelper<EntityTameEvent, EntityType> ENTITY_TAME = (plugin, event, processor) -> {
        Player player = (Player) event.getOwner();
        LivingEntity entity = event.getEntity();

        processor.progressChallenge(player, entity.getType(), 1);
        return true;
    };

    public static final EventHelper<PlayerItemConsumeEvent, Material> ITEM_CONSUME = (plugin, event, processor) -> {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();

        processor.progressChallenge(player, item.getType(), 1);
        return true;
    };

    public static final EventHelper<CraftItemEvent, Material> ITEM_CRAFT = (plugin, event, processor) -> {
        if (event.getClick() == ClickType.MIDDLE) return false;

        ItemStack item = event.getCurrentItem();
        if (item == null || item.getType().isAir()) return false;

        Player player = (Player) event.getWhoClicked();
        ItemStack craft = new ItemStack(item);
        Material type = craft.getType();

        // Идеальный вариант
        // Считаем до, считаем после, разницу записываем в прогресс хД
        boolean numberKey = event.getAction() == InventoryAction.HOTBAR_MOVE_AND_READD;

        if (event.isShiftClick() || numberKey) {
            int has = PlayerUtil.countItem(player, craft);
            plugin.getServer().getScheduler().runTask(plugin, () -> {
                int now = PlayerUtil.countItem(player, craft);
                int crafted = now - has;
                processor.progressChallenge(player, type, crafted);
            });
        }
        else {
            ItemStack cursor = event.getCursor();
            if (cursor != null && !cursor.getType().isAir() && (!cursor.isSimilar(craft) || cursor.getAmount() >= cursor.getMaxStackSize()))
                return false;

            processor.progressChallenge(player, type, 1);
        }
        return true;
    };

    public static final EventHelper<InventoryClickEvent, Material> ITEM_DISENCHANT = (plugin, event, processor) -> {
        Inventory inventory = event.getInventory();
        if (inventory.getType() != InventoryType.GRINDSTONE) return false;
        if (event.getRawSlot() != 2 || event.getClick() == ClickType.MIDDLE) return false;

        ItemStack result = inventory.getItem(2);
        if (result == null || result.getType().isAir()) return false;

        ItemStack source = inventory.getItem(0);
        if (source == null || result.getType().isAir()) return false;

        if (source.getEnchantments().size() == result.getEnchantments().size()) return false;

        Player player = (Player) event.getWhoClicked();
        processor.progressChallenge(player, result.getType(), 1);
        return true;
    };

    public static final EventHelper<EnchantItemEvent, Material> ITEM_ENCHANT = (plugin, event, processor) -> {
        Player player = event.getEnchanter();
        ItemStack item = event.getItem();

        processor.progressChallenge(player, item.getType(), 1);
        return true;
    };

    public static final EventHelper<PlayerFishEvent, Material> ITEM_FISH = (plugin, event, processor) -> {
        if (event.getState() != PlayerFishEvent.State.CAUGHT_FISH) return false;

        Entity caught = event.getCaught();
        if (!(caught instanceof Item item)) return false;

        Player player = event.getPlayer();
        ItemStack stack = item.getItemStack();
        processor.progressChallenge(player, stack.getType(), stack.getAmount());
        return true;
    };

    public static final EventHelper<FurnaceExtractEvent, Material> ITEM_FURNACE = (plugin, event, processor) -> {
        Player player = event.getPlayer();

        Material material = event.getItemType();
        int amount = event.getItemAmount();

        processor.progressChallenge(player, material, amount);
        return true;
    };

    public static final EventHelper<InventoryClickEvent, Material> ITEM_TRADE = (plugin, event, processor) -> {
        Inventory inventory = event.getInventory();
        if (inventory.getType() != InventoryType.MERCHANT) return false;

        MerchantInventory merchant = (MerchantInventory) inventory;
        MerchantRecipe recipe = merchant.getSelectedRecipe();
        if (recipe == null) return false;

        Player player = (Player) event.getWhoClicked();
        ItemStack result = recipe.getResult();
        int uses = recipe.getUses();
        int userHas = PlayerUtil.countItem(player, result);

        plugin.runTask(task -> {
            int uses2 = recipe.getUses();
            if (uses2 <= uses) return;

            int amount = 1;
            if (event.isShiftClick()) {
                int resultSize = result.getAmount();
                int userNow = PlayerUtil.countItem(player, result);
                int diff = userNow - userHas;
                amount = (int) ((double) diff / (double) resultSize);
            }

            processor.progressChallenge(player, result.getType(), amount);
        });
        return true;
    };

    public static final EventHelper<BrewEvent, PotionEffectType> POTION_BREW = (plugin, event, processor) -> {
        BrewerInventory inventory = event.getContents();

        BrewingStand stand = inventory.getHolder();
        if (stand == null) return false;

        String uuidRaw = PDCUtil.getString(stand, Keys.BREWING_HOLDER).orElse(null);
        UUID uuid = uuidRaw == null ? null : UUID.fromString(uuidRaw);
        if (uuid == null) return false;

        Player player = plugin.getServer().getPlayer(uuid);
        if (player == null) return false;

        int[] slots = new int[]{0, 1, 2};

        plugin.runTask(task -> {
            for (int slot : slots) {
                ItemStack item = inventory.getItem(slot);
                if (item == null || item.getType().isAir()) continue;

                ItemMeta meta = item.getItemMeta();
                if (!(meta instanceof PotionMeta potionMeta)) continue;

                PotionType potionType = potionMeta.getBasePotionType();
                for (PotionEffect effect : potionType.getPotionEffects()) {
                    processor.progressChallenge(player, effect.getType(), item.getAmount());
                }
                potionMeta.getCustomEffects().forEach(effect -> {
                    processor.progressChallenge(player, effect.getType(), item.getAmount());
                });
            }
        });
        return true;
    };

    public static final EventHelper<PlayerItemConsumeEvent, PotionEffectType> POTION_DRINK = (plugin, event, processor) -> {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();
        if (!(item.getItemMeta() instanceof PotionMeta potionMeta)) return false;

        Set<PotionEffectType> types = new HashSet<>();
        potionMeta.getBasePotionType().getPotionEffects().forEach(e -> types.add(e.getType()));
        potionMeta.getCustomEffects().forEach(e -> types.add(e.getType()));

        types.forEach(effectType -> {
            processor.progressChallenge(player, effectType, 1);
        });
        return true;
    };

    public static final EventHelper<ProjectileLaunchEvent, EntityType> PROJECTILE_LAUNCH = (plugin, event, processor) -> {
        Projectile projectile = event.getEntity();
        ProjectileSource source = projectile.getShooter();
        if (!(source instanceof Player player)) return false;

        processor.progressChallenge(player, projectile.getType(), 1);
        return true;
    };

    public static final EventHelper<InventoryClickEvent, Enchantment> ENCHANT_REMOVE = (plugin, event, processor) -> {
        Inventory inventory = event.getInventory();
        if (inventory.getType() != InventoryType.GRINDSTONE) return false;
        if (event.getRawSlot() != 2 || event.getClick() == ClickType.MIDDLE) return false;

        ItemStack result = inventory.getItem(2);
        if (result == null || result.getType().isAir()) return false;

        ItemStack source = inventory.getItem(0);
        if (source == null || result.getType().isAir()) return false;

        var sourceEnchants = new HashSet<>(source.getEnchantments().keySet());
        var resultEnchants = new HashSet<>(result.getEnchantments().keySet());
        if (sourceEnchants.size() == resultEnchants.size()) return false;

        sourceEnchants.removeAll(resultEnchants);

        Player player = (Player) event.getWhoClicked();
        sourceEnchants.forEach(enchantment -> {
            processor.progressChallenge(player, enchantment, 1);
        });
        return true;
    };

    public static final EventHelper<EnchantItemEvent, Enchantment> ENCHANT_GET = (plugin, event, processor) -> {
        Player player = event.getEnchanter();

        event.getEnchantsToAdd().keySet().forEach(enchantment -> {
            processor.progressChallenge(player, enchantment, 1);
        });
        return true;
    };
}
