package su.nightexpress.excellentchallenges.challenge.menu;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.jetbrains.annotations.NotNull;
import su.nexmedia.engine.api.config.JYML;
import su.nexmedia.engine.api.menu.MenuItemType;
import su.nexmedia.engine.api.menu.impl.ConfigMenu;
import su.nexmedia.engine.api.menu.impl.MenuViewer;
import su.nexmedia.engine.utils.ItemUtil;
import su.nightexpress.excellentchallenges.ExcellentChallenges;
import su.nightexpress.excellentchallenges.Placeholders;
import su.nightexpress.excellentchallenges.challenge.ChallengeType;
import su.nightexpress.excellentchallenges.config.Config;
import su.nightexpress.excellentchallenges.config.Lang;
import su.nightexpress.excellentchallenges.data.object.ChallengeUser;

import java.util.Map;
import java.util.WeakHashMap;

public class RerollConfirmMenu extends ConfigMenu<ExcellentChallenges> {

    private static final Map<Player, ChallengeType> USER_DATA = new WeakHashMap<>();

    public RerollConfirmMenu(@NotNull ExcellentChallenges plugin) {
        super(plugin, JYML.loadOrExtract(plugin, Config.DIR_MENU + "reroll_confirmation.yml"));

        this.registerHandler(MenuItemType.class)
            .addClick(MenuItemType.CONFIRMATION_DECLINE, (viewer, event) -> {
                Player player = viewer.getPlayer();
                ChallengeType challengeType = USER_DATA.remove(player);
                if (challengeType == null) {
                    player.closeInventory();
                    return;
                }
                challengeType.getMenu().openNextTick(player, 1);
            })
            .addClick(MenuItemType.CONFIRMATION_ACCEPT, (viewer, event) -> {
                Player player = viewer.getPlayer();
                ChallengeType challengeType = USER_DATA.remove(player);
                if (challengeType == null) {
                    player.closeInventory();
                    return;
                }

                ChallengeUser user = plugin.getUserManager().getUserData(player);
                user.takeRerollTokens(challengeType, 1);
                user.getChallenges(challengeType).clear();
                plugin.getChallengeManager().createChallenges(player, challengeType);
                challengeType.getMenu().openNextTick(player, 1);
                plugin.getMessage(Lang.CHALLENGE_REROLL_DONE)
                    .replace(Placeholders.GENERIC_TYPE, challengeType.getName())
                    .send(player);
            });

        this.load();

        this.getItems().forEach(menuItem -> menuItem.getOptions().addDisplayModifier((viewer, item) -> {
            Player player = viewer.getPlayer();

            ChallengeType challengeType = USER_DATA.get(player);
            if (challengeType == null) return;

            ChallengeUser user = plugin.getUserManager().getUserData(player);

            ItemUtil.replace(item, str -> str
                .replace(Placeholders.GENERIC_TYPE, challengeType.getName())
                .replace(Placeholders.GENERIC_REROLL_TOKENS, String.valueOf(user.getRerollTokens(challengeType)))
            );
        }));
    }

    public boolean open(@NotNull Player player, @NotNull ChallengeType challengeType) {
        USER_DATA.put(player, challengeType);
        return this.open(player, 1);
    }

    @Override
    public void onClose(@NotNull MenuViewer viewer, @NotNull InventoryCloseEvent event) {
        super.onClose(viewer, event);
        USER_DATA.remove(viewer.getPlayer());
    }
}
