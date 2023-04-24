package su.nightexpress.excellentchallenges.challenge.handler.impl;

import org.bukkit.block.BrewingStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.inventory.BrewEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.BrewerInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import org.jetbrains.annotations.NotNull;
import su.nexmedia.engine.utils.PDCUtil;
import su.nightexpress.excellentchallenges.ExcellentChallenges;
import su.nightexpress.excellentchallenges.Keys;
import su.nightexpress.excellentchallenges.challenge.handler.ChallengeHandler;
import su.nightexpress.excellentchallenges.challenge.type.ChallengeJobType;

import java.util.UUID;

public class PotionBrewHandler extends ChallengeHandler {

    public PotionBrewHandler(@NotNull ExcellentChallenges plugin) {
        super(plugin, ChallengeJobType.POTION_BREW);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onChallengePotionBrew(BrewEvent e) {
        BrewerInventory bInventory = e.getContents();

        BrewingStand stand = bInventory.getHolder();
        if (stand == null) return;

        String uuidRaw = PDCUtil.getString(stand, Keys.BREWING_HOLDER).orElse(null);
        UUID uuid = uuidRaw == null ? null : UUID.fromString(uuidRaw);
        if (uuid == null) return;

        Player player = plugin.getServer().getPlayer(uuid);
        if (player == null) return;

        int[] slots = new int[]{0, 1, 2};

        plugin.getServer().getScheduler().runTask(plugin, () -> {
            for (int slot : slots) {
                ItemStack item = bInventory.getItem(slot);
                if (item == null || item.getType().isAir()) continue;

                ItemMeta meta = item.getItemMeta();
                if (!(meta instanceof PotionMeta potionMeta)) continue;

                PotionType potionType = potionMeta.getBasePotionData().getType();
                PotionEffectType effectType = potionType.getEffectType();
                if (effectType != null) {
                    this.progressChallenge(player, effectType.getName(), item.getAmount());
                }
                potionMeta.getCustomEffects().forEach(effect -> {
                    this.progressChallenge(player, effect.getType().getName(), item.getAmount());
                });
            }
        });
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onChallengeInventoryHandler(InventoryClickEvent e) {
        Inventory inventory = e.getInventory();
        Player player = (Player) e.getWhoClicked();

        if (inventory.getType() == InventoryType.BREWING) {
            BrewerInventory bInventory = (BrewerInventory) inventory;

            BrewingStand stand = bInventory.getHolder();
            if (stand == null) return;

            boolean canBrew1 = plugin.getChallengeNMS().canBrew(stand);
            if (canBrew1) return;

            plugin.getServer().getScheduler().runTask(plugin, () -> {
                boolean canBrew2 = plugin.getChallengeNMS().canBrew(stand);
                if (!canBrew2) return;

                BrewingStand stand2 = bInventory.getHolder();
                PDCUtil.set(stand2, Keys.BREWING_HOLDER, player.getUniqueId().toString());
                stand2.update();
            });
        }
    }
}
