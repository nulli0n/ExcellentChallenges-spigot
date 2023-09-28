package su.nightexpress.excellentchallenges.challenge.menu;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import su.nexmedia.engine.api.config.JYML;
import su.nexmedia.engine.api.menu.MenuItemType;
import su.nexmedia.engine.api.menu.impl.ConfigMenu;
import su.nexmedia.engine.api.menu.item.MenuItem;
import su.nexmedia.engine.utils.EngineUtils;
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

public class ChallengesMainMenu extends ConfigMenu<ExcellentChallenges> {

    public ChallengesMainMenu(@NotNull ExcellentChallenges plugin) {
        super(plugin, JYML.loadOrExtract(plugin, Config.DIR_MENU + "challenges_types.yml"));

        this.registerHandler(MenuItemType.class)
            .addClick(MenuItemType.CLOSE, (viewer, event) -> plugin.runTask(task -> viewer.getPlayer().closeInventory()));

        this.load();

        for (String sId : cfg.getSection("Types")) {
            ChallengeType challengeType = plugin.getChallengeManager().getChallengeType(sId);
            if (challengeType == null) {
                this.plugin.error("Invalid challenge category item: '" + sId + "' in 'challenges_types.yml'!");
                continue;
            }

            MenuItem menuItem = this.readItem("Types." + sId);
            menuItem.setClick((viewer, event) -> {
                challengeType.getMenu().openNextTick(viewer, 1);
            });
            menuItem.getOptions().addDisplayModifier((viewer, item) -> {
                Player player = viewer.getPlayer();
                ChallengeUser user = plugin.getUserManager().getUserData(player);
                Set<Challenge> challenges = user.getChallenges(challengeType);

                int incompleted = (int) challenges.stream().filter(Predicate.not(Challenge::isCompleted)).count();
                int completed = (int) challenges.stream().filter(Challenge::isCompleted).count();
                int total = challenges.size();

                ItemUtil.replace(item, line -> line
                    .replace(Placeholders.GENERIC_REWARDS, challengeType.getCompletionRewards().stream().map(ChallengeReward::getName).collect(Collectors.joining(", ")))
                    .replace(Placeholders.GENERIC_UNFINISHED, NumberUtil.format(incompleted))
                    .replace(Placeholders.GENERIC_COMPLETED, NumberUtil.format(completed))
                    .replace(Placeholders.GENERIC_TOTAL, NumberUtil.format(total))
                    .replace(Placeholders.GENERIC_PROGRESS, NumberUtil.format(user.getProgressPercent(challengeType)))
                    .replace(Placeholders.GENERIC_REROLL_TOKENS, NumberUtil.format(user.getRerollTokens(challengeType)))
                );
            });
            this.addItem(menuItem);
        }

        this.getItems().forEach(menuItem -> {
            menuItem.getOptions().addDisplayModifier((viewer, item) -> {
                ChallengeUser user = plugin.getUserManager().getUserData(viewer.getPlayer());

                if (EngineUtils.hasPlaceholderAPI()) {
                    ItemUtil.setPlaceholderAPI(viewer.getPlayer(), item);
                }
            });
        });
    }
}
