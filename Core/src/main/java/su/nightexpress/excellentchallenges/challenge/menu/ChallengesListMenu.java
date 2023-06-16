package su.nightexpress.excellentchallenges.challenge.menu;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import su.nexmedia.engine.api.config.JYML;
import su.nexmedia.engine.api.menu.AutoPaged;
import su.nexmedia.engine.api.menu.MenuItemType;
import su.nexmedia.engine.api.menu.click.ClickHandler;
import su.nexmedia.engine.api.menu.click.ItemClick;
import su.nexmedia.engine.api.menu.impl.ConfigMenu;
import su.nexmedia.engine.api.menu.impl.MenuOptions;
import su.nexmedia.engine.api.menu.impl.MenuViewer;
import su.nexmedia.engine.lang.LangManager;
import su.nexmedia.engine.utils.Colorizer;
import su.nexmedia.engine.utils.ItemUtil;
import su.nexmedia.engine.utils.NumberUtil;
import su.nexmedia.engine.utils.TimeUtil;
import su.nightexpress.excellentchallenges.ExcellentChallenges;
import su.nightexpress.excellentchallenges.Perms;
import su.nightexpress.excellentchallenges.Placeholders;
import su.nightexpress.excellentchallenges.challenge.Challenge;
import su.nightexpress.excellentchallenges.challenge.ChallengeType;
import su.nightexpress.excellentchallenges.challenge.config.ChallengeTemplate;
import su.nightexpress.excellentchallenges.challenge.generator.ChallengeGenerator;
import su.nightexpress.excellentchallenges.challenge.reward.ChallengeReward;
import su.nightexpress.excellentchallenges.challenge.task.MenuUpdateTask;
import su.nightexpress.excellentchallenges.challenge.type.RerollCondition;
import su.nightexpress.excellentchallenges.config.Config;
import su.nightexpress.excellentchallenges.config.Lang;
import su.nightexpress.excellentchallenges.data.object.ChallengeUser;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ChallengesListMenu extends ConfigMenu<ExcellentChallenges> implements AutoPaged<Challenge> {

    private static final String PLACEHOLDER_WORLDS  = "%worlds%";
    private static final String PLACEHOLDER_REWARDS = "%rewards%";

    private final ChallengeType challengeType;
    private final int[]         challengesSlots;
    private final String        formatActiveName;
    private final List<String>  formatActiveLore;
    private final String        formatCompletedName;
    private final List<String>  formatCompletedLore;
    private final List<String>  formatWorlds;
    private final List<String>  formatRewards;

    private MenuUpdateTask updateTask;

    public ChallengesListMenu(@NotNull ExcellentChallenges plugin, @NotNull JYML cfg, @NotNull ChallengeType challengeType) {
        super(plugin, cfg);
        this.challengeType = challengeType;

        this.challengesSlots = cfg.getIntArray("Challenges.Slots");
        this.formatActiveName = Colorizer.apply(cfg.getString("Challenges.Format.Challenge_Active.Name", Placeholders.CHALLENGE_NAME));
        this.formatActiveLore = Colorizer.apply(cfg.getStringList("Challenges.Format.Challenge_Active.Lore"));
        this.formatCompletedName = Colorizer.apply(cfg.getString("Challenges.Format.Challenge_Completed.Name", Placeholders.CHALLENGE_NAME));
        this.formatCompletedLore = Colorizer.apply(cfg.getStringList("Challenges.Format.Challenge_Completed.Lore"));
        this.formatWorlds = Colorizer.apply(cfg.getStringList("Challenges.Format.Worlds"));
        this.formatRewards = Colorizer.apply(cfg.getStringList("Challenges.Format.Rewards"));

        this.registerHandler(MenuItemType.class)
            .addClick(MenuItemType.RETURN, (viewer, event) -> {
                plugin.getChallengeManager().getMainMenu().openNextTick(viewer, 1);
            })
            .addClick(MenuItemType.PAGE_NEXT, ClickHandler.forNextPage(this))
            .addClick(MenuItemType.PAGE_PREVIOUS, ClickHandler.forPreviousPage(this));

        this.registerHandler(ButtonType.class)
            .addClick(ButtonType.REROLL, (viewer, event) -> {
                Player player = viewer.getPlayer();
                if (!player.hasPermission(Perms.REROLL)) {
                    plugin.getMessage(Lang.ERROR_PERMISSION_DENY).send(player);
                    return;
                }

                ChallengeUser user = plugin.getUserManager().getUserData(player);
                int tokens = user.getRerollTokens(this.challengeType);
                if (tokens <= 0) {
                    plugin.getMessage(Lang.CHALLENGE_REROLL_ERROR_NO_TOKENS).send(player);
                    return;
                }

                RerollCondition condition = Config.REROLL_CONDITION.get();
                if (condition == RerollCondition.ALL_COMPLETED) {
                    if (user.getChallenges(this.challengeType).stream().anyMatch(Predicate.not(Challenge::isCompleted))) {
                        plugin.getMessage(Lang.CHALLENGE_REROLL_ERROR_CONDITION).send(player);
                        return;
                    }
                }
                else if (condition == RerollCondition.ALL_UNFINISHED) {
                    if (user.getChallenges(this.challengeType).stream().anyMatch(Challenge::isCompleted)) {
                        plugin.getMessage(Lang.CHALLENGE_REROLL_ERROR_CONDITION).send(player);
                        return;
                    }
                }

                plugin.getChallengeManager().getRerollConfirmMenu().open(player, this.challengeType);
            });

        this.load();

        this.updateTask = new MenuUpdateTask(this.plugin, this);
        this.updateTask.start();

        this.getItems().forEach(menuItem -> {
            menuItem.getOptions().addDisplayModifier((viewer, item) -> {
                ChallengeUser user = plugin.getUserManager().getUserData(viewer.getPlayer());

                ItemUtil.replace(item, str -> str
                    .replace(Placeholders.GENERIC_REROLL_TOKENS, NumberUtil.format(user.getRerollTokens(this.challengeType)))
                );
            });
        });
    }

    @Override
    public void onPrepare(@NotNull MenuViewer viewer, @NotNull MenuOptions options) {
        super.onPrepare(viewer, options);
        this.getItemsForPage(viewer).forEach(this::addItem);
    }

    enum ButtonType {
        REROLL,
    }

    @Override
    public void clear() {
        if (this.updateTask != null) {
            this.updateTask.stop();
            this.updateTask = null;
        }
        super.clear();
    }

    @Override
    public int[] getObjectSlots() {
        return this.challengesSlots;
    }

    @Override
    @NotNull
    public List<Challenge> getObjects(@NotNull Player player) {
        this.plugin.getChallengeManager().updateChallenges(player, false);
        ChallengeUser user = plugin.getUserManager().getUserData(player);
        return user.getChallenges(this.challengeType).stream().sorted(Comparator.comparingInt(Challenge::getLevel)).toList();
    }

    @Override
    @NotNull
    public ItemStack getObjectStack(@NotNull Player player, @NotNull Challenge challenge) {
        ChallengeTemplate template = plugin.getChallengeManager().getTemplate(challenge.getTemplateId());
        ChallengeGenerator generator = plugin.getChallengeManager().getGenerator(challenge.getGeneratorId());
        if (template == null || generator == null) return new ItemStack(Material.AIR);

        boolean isCompleted = challenge.isCompleted();
        ItemStack item = isCompleted ? generator.getIconCompleted() : generator.getIconActive();

        ItemMeta meta = item.getItemMeta();
        if (meta == null) return item;


        ChallengeUser user = plugin.getUserManager().getUserData(player);
        long refreshTime = user.getRefreshTime(this.challengeType);

        List<String> lore2 = new ArrayList<>();
        for (String line : (isCompleted ? this.formatCompletedLore : this.formatActiveLore)) {

            if (line.equalsIgnoreCase(PLACEHOLDER_WORLDS)) {
                if (challenge.getWorlds().isEmpty()) continue;

                String worlds = challenge.getWorlds().stream()
                    .map(name -> plugin.getServer().getWorld(name)).filter(Objects::nonNull)
                    .map(LangManager::getWorld).collect(Collectors.joining(", "));
                List<String> formatWorlds = new ArrayList<>(this.formatWorlds);
                formatWorlds.replaceAll(str -> str.replace(Placeholders.GENERIC_WORLD, worlds));
                lore2.addAll(formatWorlds);

                continue;
            }

            if (line.equalsIgnoreCase(PLACEHOLDER_REWARDS)) {
                List<ChallengeReward> rewards = challenge.getRewards().stream()
                    .map(rewardId -> plugin.getChallengeManager().getReward(rewardId)).filter(Objects::nonNull)
                    .toList();
                if (rewards.isEmpty()) continue;

                for (String line2 : this.formatRewards) {
                    if (line2.contains(Placeholders.REWARD_NAME)) {
                        rewards.forEach(reward -> {
                            lore2.add(line2.replace(Placeholders.REWARD_NAME, reward.getName()));
                        });
                        continue;
                    }
                    lore2.add(line2);
                }
                continue;
            }

            if (line.contains(Placeholders.OBJECTIVE_NAME)) {
                challenge.getObjectives().forEach((objId, objVal) -> {
                    lore2.add(challenge.replacePlaceholders(objId).apply(line));
                });
                continue;
            }

            lore2.add(line
                .replace(Placeholders.CHALLENGE_REFRESH_TIME, TimeUtil.formatTimeLeft(refreshTime == 0 ? System.currentTimeMillis() : refreshTime))
            );
        }
        meta.setDisplayName(isCompleted ? this.formatCompletedName : this.formatActiveName);
        meta.setLore(lore2);
        item.setItemMeta(meta);

        ItemUtil.replace(item, template.replacePlaceholders());
        ItemUtil.replace(item, challenge.replacePlaceholders());
        return item;
    }

    @Override
    @NotNull
    public ItemClick getObjectClick(@NotNull Challenge challenge) {
        return (viewer, event) -> {

        };
    }
}
