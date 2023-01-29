package su.nightexpress.excellentchallenges.challenge.menu;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import su.nexmedia.engine.api.config.JYML;
import su.nexmedia.engine.api.menu.AbstractMenu;
import su.nexmedia.engine.api.menu.MenuClick;
import su.nexmedia.engine.api.menu.MenuItem;
import su.nexmedia.engine.api.menu.MenuItemType;
import su.nexmedia.engine.utils.ItemUtil;
import su.nightexpress.excellentchallenges.ExcellentChallenges;
import su.nightexpress.excellentchallenges.Placeholders;
import su.nightexpress.excellentchallenges.challenge.ChallengeType;
import su.nightexpress.excellentchallenges.config.Config;
import su.nightexpress.excellentchallenges.config.Lang;
import su.nightexpress.excellentchallenges.data.object.ChallengeUser;

import java.util.Map;
import java.util.WeakHashMap;

public class RerollConfirmMenu extends AbstractMenu<ExcellentChallenges> {

    private static final Map<Player, ChallengeType> USER_DATA = new WeakHashMap<>();

    public RerollConfirmMenu(@NotNull ExcellentChallenges plugin) {
        super(plugin, JYML.loadOrExtract(plugin, Config.DIR_MENU + "reroll_confirmation.yml"), "");

        MenuClick click = (player, type, e) -> {
            if (type instanceof MenuItemType type2) {
                ChallengeType challengeType = USER_DATA.remove(player);
                if (challengeType == null) {
                    player.closeInventory();
                    return;
                }

                if (type2 == MenuItemType.CONFIRMATION_DECLINE) {
                    challengeType.getMenu().open(player, 1);
                }
                else if (type2 == MenuItemType.CONFIRMATION_ACCEPT) {
                    ChallengeUser user = plugin.getUserManager().getUserData(player);
                    user.takeRerollTokens(challengeType, 1);
                    user.getChallenges(challengeType).clear();
                    plugin.getChallengeManager().createChallenges(player, challengeType);
                    challengeType.getMenu().open(player, 1);
                    plugin.getMessage(Lang.CHALLENGE_REROLL_DONE)
                        .replace(Placeholders.GENERIC_TYPE, challengeType.getName())
                        .send(player);
                }
            }
        };

        for (String sId : cfg.getSection("Content")) {
            MenuItem menuItem = cfg.getMenuItem("Content." + sId, MenuItemType.class);
            if (menuItem.getType() != null) {
                menuItem.setClickHandler(click);
            }
            this.addItem(menuItem);
        }
    }

    public boolean open(@NotNull Player player, @NotNull ChallengeType challengeType) {
        USER_DATA.put(player, challengeType);
        return this.open(player, 1);
    }

    @Override
    public void onClose(@NotNull Player player, @NotNull InventoryCloseEvent e) {
        super.onClose(player, e);
        USER_DATA.remove(player);
    }

    @Override
    public void onItemPrepare(@NotNull Player player, @NotNull MenuItem menuItem, @NotNull ItemStack item) {
        super.onItemPrepare(player, menuItem, item);

        ChallengeType challengeType = USER_DATA.get(player);
        if (challengeType == null) return;

        ChallengeUser user = plugin.getUserManager().getUserData(player);

        ItemUtil.replace(item, str -> str
            .replace(Placeholders.GENERIC_TYPE, challengeType.getName())
            .replace(Placeholders.GENERIC_REROLL_TOKENS, String.valueOf(user.getRerollTokens(challengeType)))
        );
    }

    @Override
    public boolean cancelClick(@NotNull InventoryClickEvent inventoryClickEvent, @NotNull SlotType slotType) {
        return true;
    }
}
