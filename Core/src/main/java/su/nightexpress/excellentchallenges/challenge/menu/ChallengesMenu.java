package su.nightexpress.excellentchallenges.challenge.menu;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import su.nexmedia.engine.api.config.JOption;
import su.nexmedia.engine.api.config.JYML;
import su.nexmedia.engine.api.menu.AutoPaged;
import su.nexmedia.engine.api.menu.MenuItemType;
import su.nexmedia.engine.api.menu.click.ClickHandler;
import su.nexmedia.engine.api.menu.click.ItemClick;
import su.nexmedia.engine.api.menu.impl.ConfigMenu;
import su.nexmedia.engine.api.menu.impl.MenuOptions;
import su.nexmedia.engine.api.menu.impl.MenuViewer;
import su.nexmedia.engine.api.menu.item.MenuItem;
import su.nexmedia.engine.api.menu.link.Linked;
import su.nexmedia.engine.api.menu.link.ViewLink;
import su.nexmedia.engine.utils.*;
import su.nightexpress.excellentchallenges.ExcellentChallengesPlugin;
import su.nightexpress.excellentchallenges.Perms;
import su.nightexpress.excellentchallenges.Placeholders;
import su.nightexpress.excellentchallenges.challenge.ChallengeCategory;
import su.nightexpress.excellentchallenges.challenge.GeneratedChallenge;
import su.nightexpress.excellentchallenges.challenge.generator.Generator;
import su.nightexpress.excellentchallenges.challenge.type.RerollCondition;
import su.nightexpress.excellentchallenges.config.Config;
import su.nightexpress.excellentchallenges.config.Lang;
import su.nightexpress.excellentchallenges.data.object.ChallengeUser;

import java.util.*;
import java.util.function.Predicate;

import static su.nexmedia.engine.utils.Colors.*;
import static su.nightexpress.excellentchallenges.Placeholders.*;

public class ChallengesMenu extends ConfigMenu<ExcellentChallengesPlugin>
    implements AutoPaged<GeneratedChallenge>, Linked<ChallengeCategory> {

    public static final String FILE = "challenges.yml";

    private static final String PLACEHOLDER_OBJECTIVES = "%objectives%";
    private static final String PLACEHOLDER_CONDITIONS = "%conditions%";
    private static final String PLACEHOLDER_REWARDS    = "%rewards%";

    private static final String CYAN = "#9af7ff";
    private static final String LIME = "#a5ff9a";
    private static final String GOLD = "#ebff9a";

    private int[]        challengeSlots;
    private String       formatActiveName;
    private List<String> formatActiveLore;
    private String       formatCompletedName;
    private List<String> formatCompletedLore;
    private List<String> formatObjectives;
    private List<String> formatConditions;
    private List<String> formatRewards;

    private final ViewLink<ChallengeCategory> viewLink;

    public ChallengesMenu(@NotNull ExcellentChallengesPlugin plugin) {
        super(plugin, new JYML(plugin.getDataFolder() + Config.DIR_MENU, FILE));
        this.viewLink = new ViewLink<>();

        this.registerHandler(MenuItemType.class)
            .addClick(MenuItemType.RETURN, (viewer, event) -> {
                plugin.getChallengeManager().getCategoriesMenu().openNextTick(viewer, 1);
            })
            .addClick(MenuItemType.PAGE_NEXT, ClickHandler.forNextPage(this))
            .addClick(MenuItemType.PAGE_PREVIOUS, ClickHandler.forPreviousPage(this));

        this.registerHandler(ButtonType.class)
            .addClick(ButtonType.REROLL, (viewer, event) -> {
                ChallengeCategory category = this.getLink().get(viewer);
                if (category == null) return;

                Player player = viewer.getPlayer();
                if (!player.hasPermission(Perms.REROLL)) {
                    plugin.getMessage(Lang.ERROR_PERMISSION_DENY).send(player);
                    return;
                }

                ChallengeUser user = plugin.getUserManager().getUserData(player);
                int tokens = user.getRerollTokens(category);
                if (tokens <= 0) {
                    plugin.getMessage(Lang.CHALLENGE_REROLL_ERROR_NO_TOKENS).send(player);
                    return;
                }

                RerollCondition condition = Config.REROLL_CONDITION.get();
                if (condition == RerollCondition.ALL_COMPLETED) {
                    if (user.getChallenges(category).stream().anyMatch(Predicate.not(GeneratedChallenge::isCompleted))) {
                        plugin.getMessage(Lang.CHALLENGE_REROLL_ERROR_CONDITION).send(player);
                        return;
                    }
                }
                else if (condition == RerollCondition.ALL_UNFINISHED) {
                    if (user.getChallenges(category).stream().anyMatch(GeneratedChallenge::isCompleted)) {
                        plugin.getMessage(Lang.CHALLENGE_REROLL_ERROR_CONDITION).send(player);
                        return;
                    }
                }

                plugin.getChallengeManager().getRerollConfirmMenu().open(player, category, 1);
            });

        this.load();

        this.getItems().forEach(menuItem -> {
            menuItem.getOptions().addDisplayModifier((viewer, item) -> {
                ChallengeCategory category = this.getLink().get(viewer);
                ChallengeUser user = plugin.getUserManager().getUserData(viewer.getPlayer());

                ItemReplacer.create(item).readMeta()
                    .replacePlaceholderAPI(viewer.getPlayer())
                    .replace(Placeholders.GENERIC_REROLL_TOKENS, () -> NumberUtil.format(user.getRerollTokens(category)))
                    .replace(Colorizer::apply)
                    .writeMeta();
            });
        });
    }

    @Override
    public boolean isCodeCreation() {
        return true;
    }

    @Override
    @NotNull
    protected MenuOptions createDefaultOptions() {
        return new MenuOptions(CATEGORY_NAME + " Challenges", 54, InventoryType.CHEST, 1);
    }

    @Override
    @NotNull
    protected List<MenuItem> createDefaultItems() {
        List<MenuItem> list = new ArrayList<>();

        ItemStack rerollStack = ItemUtil.createCustomHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjQyMzI4OTUxMGM1NGI2N2RmMDIzNTgwOTc5YzQ2NWQwNDgxYzc2OWM4NjViZjRiNDY1Y2Y0Nzg3NDlmMWM0ZiJ9fX0=");
        ItemUtil.mapMeta(rerollStack, meta -> {
            meta.setDisplayName(LIGHT_YELLOW + BOLD + "Reroll Challenges " + GRAY + "(1 Token)");
            meta.setLore(Arrays.asList(
                LIGHT_YELLOW + "▪ " + GRAY + "Tokens: " + LIGHT_YELLOW + Placeholders.GENERIC_REROLL_TOKENS,
                LIGHT_YELLOW + "▪ " + GRAY + "Purchase at: " + LIGHT_YELLOW + "store.samplesmp.com",
                "",
                LIGHT_YELLOW + BOLD + "Description:",
                GRAY + "Use this button to " + LIGHT_YELLOW + "replace" + GRAY + " your",
                GRAY + "current challenges.",
                "",
                RED + "[!] " + GRAY + "All challenge progress will be " + RED + "lost" + GRAY + "."
            ));
        });

        ItemStack returnStack = ItemUtil.createCustomHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmU5YWU3YTRiZTY1ZmNiYWVlNjUxODEzODlhMmY3ZDQ3ZTJlMzI2ZGI1OWVhM2ViNzg5YTkyYzg1ZWE0NiJ9fX0=");
        ItemUtil.mapMeta(returnStack, meta -> {
            meta.setDisplayName(GRAY + "(↓) " + WHITE + "Back to Categories");
        });

        list.add(new MenuItem(rerollStack).setType(ButtonType.REROLL).setSlots(4));
        list.add(new MenuItem(returnStack).setType(MenuItemType.RETURN).setSlots(49));

        return list;
    }

    @Override
    protected void loadAdditional() {
        this.challengeSlots = new JOption<int[]>("Challenges.Slots",
            (cfg, path, def) -> StringUtil.getIntArray(cfg.getString(path, "19,21,23,25,31")),
            () -> new int[] {19, 21, 23, 25, 31}
        ).setWriter(JYML::setIntArray).read(cfg);

        this.formatActiveName = JOption.create("Challenges.Format.Challenge_Active.Name",
            CYAN + BOLD + "Challenge: " + GRAY + CHALLENGE_TYPE
        ).read(cfg);

        this.formatActiveLore = JOption.create("Challenges.Format.Challenge_Active.Lore",
            Arrays.asList(
                CYAN + "▪ " + GRAY + "Difficulty: " + CYAN + CHALLENGE_DIFFICULTY,
                CYAN + "▪ " + GRAY + "Level: " + CYAN + CHALLENGE_LEVEL,
                "",
                PLACEHOLDER_OBJECTIVES,
                PLACEHOLDER_CONDITIONS,
                PLACEHOLDER_REWARDS,
                "",
                LIME + "✔" + GRAY + " You completed " + LIME + CHALLENGE_PROGRESS_PERCENT + "% " + GRAY + "of the challenge.",
                LIME + "⌚" + GRAY + " Finish it in: " + LIME + CHALLENGE_REFRESH_TIME
            )
        ).read(cfg);

        this.formatCompletedName = JOption.create("Challenges.Format.Challenge_Completed.Name",
            CYAN + BOLD + "Challenge: " + GRAY + CHALLENGE_TYPE
        ).read(cfg);

        this.formatCompletedLore = JOption.create("Challenges.Format.Challenge_Completed.Lore",
            Arrays.asList(
                CYAN + "▪ " + GRAY + "Difficulty: " + CYAN + CHALLENGE_DIFFICULTY,
                CYAN + "▪ " + GRAY + "Level: " + CYAN + CHALLENGE_LEVEL,
                "",
                PLACEHOLDER_OBJECTIVES,
                PLACEHOLDER_REWARDS,
                "",
                LIME + "✔" + GRAY + " You completed " + LIME + CHALLENGE_PROGRESS_PERCENT + "% " + GRAY + "of the challenge."
            )
        ).read(cfg);

        this.formatObjectives = JOption.create("Challenges.Format.Objectives",
            Arrays.asList(
                CYAN + BOLD + "Objectives:",
                CYAN + "┃ " + GRAY + OBJECTIVE_NAME + ": " + CYAN + OBJECTIVE_PROGRESS_CURRENT + GRAY + "/" + CYAN + OBJECTIVE_PROGRESS_MAX
            )
        ).read(cfg);

        this.formatConditions = JOption.create("Challenges.Format.Conditions",
            Arrays.asList(
                "",
                RED + BOLD + "Requirements:",
                RED + "[❗] " + GRAY + CONDITION_DESCRIPTION
            )
        ).read(cfg);

        this.formatRewards = JOption.create("Challenges.Format.Rewards",
            Arrays.asList(
                "",
                GOLD + "[$] " + GRAY + "Complete this challenge to receive:",
                GOLD + "◈ %reward_name%"
            )
        ).read(cfg);
    }

    @Override
    @NotNull
    public ViewLink<ChallengeCategory> getLink() {
        return this.viewLink;
    }

    @Override
    public void onPrepare(@NotNull MenuViewer viewer, @NotNull MenuOptions options) {
        super.onPrepare(viewer, options);

        ChallengeCategory category = this.getLink().get(viewer);
        if (category != null) {
            options.setTitle(category.replacePlaceholders().apply(options.getTitle()));
        }

        this.getItemsForPage(viewer).forEach(this::addItem);
    }

    private enum ButtonType {
        REROLL
    }

    @Override
    public int[] getObjectSlots() {
        return this.challengeSlots;
    }

    @Override
    @NotNull
    public List<GeneratedChallenge> getObjects(@NotNull Player player) {
        this.plugin.getChallengeManager().updateChallenges(player, false);

        ChallengeCategory category = this.getLink().get(player);
        if (category == null) return Collections.emptyList();

        ChallengeUser user = plugin.getUserManager().getUserData(player);
        return user.getChallenges(category).stream().sorted(Comparator.comparingInt(GeneratedChallenge::getLevel)).toList();
    }

    @Override
    @NotNull
    public ItemStack getObjectStack(@NotNull Player player, @NotNull GeneratedChallenge challenge) {
        ItemStack item = challenge.getActionType().getIcon();

        ChallengeCategory category = this.getLink().get(player);
        ChallengeUser user = plugin.getUserManager().getUserData(player);
        Generator generator = challenge.getGenerator();
        boolean isCompleted = challenge.isCompleted();
        long refreshTime = user.getRefreshTime(category);

        List<String> objectives = new ArrayList<>();
        List<String> conditions = new ArrayList<>();
        List<String> rewards = new ArrayList<>();

        for (String line : this.formatObjectives) {
            if (line.contains(Placeholders.OBJECTIVE_NAME)) {
                for (var objId : challenge.getObjectives().keySet()) {
                    objectives.add(challenge.replacePlaceholders(objId).apply(line));
                }
            }
            else objectives.add(line);
        }

        if (!challenge.getConditions().isEmpty()) {
            for (String line : this.formatConditions) {
                if (line.contains(Placeholders.CONDITION_NAME) || line.contains(Placeholders.CONDITION_DESCRIPTION)) {
                    for (var conditionConfig : challenge.getConditions().keySet()) {
                        conditions.add(conditionConfig.replacePlaceholders().apply(line));
                    }
                }
                else conditions.add(line);
            }
        }

        if (!challenge.getRewards().isEmpty()) {
            for (String line : this.formatRewards) {
                if (line.contains(Placeholders.REWARD_NAME)) {
                    for (var reward : challenge.getRewards()) {
                        rewards.add(reward.replacePlaceholders().apply(line));
                    }
                }
                else rewards.add(line);
            }
        }

        ItemReplacer.create(item).hideFlags().trimmed()
            .setDisplayName(isCompleted ? this.formatCompletedName : this.formatActiveName)
            .setLore(isCompleted ? this.formatCompletedLore : this.formatActiveLore)
            .replace(Placeholders.CHALLENGE_REFRESH_TIME, () -> {
                return TimeUtil.formatTimeLeft(refreshTime == 0 ? System.currentTimeMillis() : refreshTime);
            })
            .replaceLoreExact(PLACEHOLDER_OBJECTIVES, objectives)
            .replaceLoreExact(PLACEHOLDER_CONDITIONS, conditions)
            .replaceLoreExact(PLACEHOLDER_REWARDS, rewards)
            .replace(challenge.replacePlaceholders())
            .replace(Colorizer::apply)
            .writeMeta();
        return item;
    }

    @Override
    @NotNull
    public ItemClick getObjectClick(@NotNull GeneratedChallenge challenge) {
        return (viewer, event) -> {

        };
    }
}
