package su.nightexpress.excellentchallenges.challenge.menu;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import su.nexmedia.engine.api.config.JYML;
import su.nexmedia.engine.api.menu.AbstractMenu;
import su.nexmedia.engine.api.menu.MenuClick;
import su.nexmedia.engine.api.menu.MenuItem;
import su.nexmedia.engine.api.menu.MenuItemType;
import su.nexmedia.engine.utils.ItemUtil;
import su.nexmedia.engine.utils.NumberUtil;
import su.nightexpress.excellentchallenges.ExcellentChallenges;
import su.nightexpress.excellentchallenges.Placeholders;
import su.nightexpress.excellentchallenges.challenge.Challenge;
import su.nightexpress.excellentchallenges.challenge.ChallengeType;
import su.nightexpress.excellentchallenges.challenge.reward.ChallengeReward;
import su.nightexpress.excellentchallenges.config.Config;
import su.nightexpress.excellentchallenges.data.object.ChallengeUser;

import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ChallengesMainMenu extends AbstractMenu<ExcellentChallenges> {

    public ChallengesMainMenu(@NotNull ExcellentChallenges plugin) {
        super(plugin, JYML.loadOrExtract(plugin, Config.DIR_MENU + "challenges_types.yml"), "");

        MenuClick click = (player, type, e) -> {
            if (type instanceof MenuItemType type2) {
                this.onItemClickDefault(player, type2);
            }
        };

        for (String sId : cfg.getSection("Content")) {
            MenuItem menuItem = cfg.getMenuItem("Content." + sId, MenuItemType.class);

            if (menuItem.getType() != null) {
                menuItem.setClickHandler(click);
            }
            this.addItem(menuItem);
        }

        for (String sId : cfg.getSection("Types")) {
            MenuItem menuItem = cfg.getMenuItem("Types." + sId, MenuItemType.class);

            ChallengeType challengeType = plugin.getChallengeManager().getChallengeType(sId);
            if (challengeType != null) {
                menuItem.setClickHandler((player, type, e) -> {
                    challengeType.getMenu().open(player, 1);
                });
            }
            this.addItem(menuItem);
        }
    }

    @Override
    public void onItemPrepare(@NotNull Player player, @NotNull MenuItem menuItem, @NotNull ItemStack item) {
        super.onItemPrepare(player, menuItem, item);

        String type = menuItem.getId();
        ChallengeType challengeType = plugin.getChallengeManager().getChallengeType(type);
        if (challengeType == null) return;

        ChallengeUser user = plugin.getUserManager().getUserData(player);
        Set<Challenge> challenges = user.getChallenges(type);

        int incompleted = (int) challenges.stream().filter(Predicate.not(Challenge::isCompleted)).count();
        int completed = (int) challenges.stream().filter(Challenge::isCompleted).count();
        int total = challenges.size();

        ItemUtil.replace(item, line -> line
            .replace(Placeholders.GENERIC_REWARDS, challengeType.getCompletionRewards().stream().map(ChallengeReward::getName).collect(Collectors.joining(", ")))
            .replace(Placeholders.GENERIC_UNFINISHED, String.valueOf(incompleted))
            .replace(Placeholders.GENERIC_COMPLETED, String.valueOf(completed))
            .replace(Placeholders.GENERIC_TOTAL, String.valueOf(total))
            .replace(Placeholders.GENERIC_PROGRESS, NumberUtil.format(user.getProgressPercent(challengeType)))
            .replace(Placeholders.GENERIC_REROLL_TOKENS, String.valueOf(user.getRerollTokens(challengeType)))
        );
    }

    @Override
    public boolean cancelClick(@NotNull InventoryClickEvent e, @NotNull SlotType slotType) {
        return true;
    }
}
