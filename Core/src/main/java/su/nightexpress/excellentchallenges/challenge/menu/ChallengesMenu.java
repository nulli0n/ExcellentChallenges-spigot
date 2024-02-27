package su.nightexpress.excellentchallenges.challenge.menu;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentchallenges.ExcellentChallengesPlugin;
import su.nightexpress.excellentchallenges.Placeholders;
import su.nightexpress.excellentchallenges.challenge.ChallengeCategory;
import su.nightexpress.excellentchallenges.challenge.GeneratedChallenge;
import su.nightexpress.excellentchallenges.config.Config;
import su.nightexpress.excellentchallenges.data.object.ChallengeUser;
import su.nightexpress.nightcore.config.ConfigValue;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.menu.MenuOptions;
import su.nightexpress.nightcore.menu.MenuViewer;
import su.nightexpress.nightcore.menu.api.AutoFill;
import su.nightexpress.nightcore.menu.api.AutoFilled;
import su.nightexpress.nightcore.menu.impl.ConfigMenu;
import su.nightexpress.nightcore.menu.item.ItemHandler;
import su.nightexpress.nightcore.menu.item.MenuItem;
import su.nightexpress.nightcore.menu.link.Linked;
import su.nightexpress.nightcore.menu.link.ViewLink;
import su.nightexpress.nightcore.util.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import static su.nightexpress.excellentchallenges.Placeholders.*;
import static su.nightexpress.nightcore.util.text.tag.Tags.*;

public class ChallengesMenu extends ConfigMenu<ExcellentChallengesPlugin>
    implements AutoFilled<GeneratedChallenge>, Linked<ChallengeCategory> {

    public static final String FILE = "challenges.yml";

    private static final String PLACEHOLDER_OBJECTIVES = "%objectives%";
    private static final String PLACEHOLDER_CONDITIONS = "%conditions%";
    private static final String PLACEHOLDER_REWARDS    = "%rewards%";

    private int[]        challengeSlots;
    private boolean iconCompletedEnabled;
    private ItemStack iconCompleted;
    private String       formatActiveName;
    private List<String> formatActiveLore;
    private String       formatCompletedName;
    private List<String> formatCompletedLore;
    private List<String> formatObjectives;
    private List<String> formatConditions;
    private List<String> formatRewards;

    private final ItemHandler returnHandler;
    private final ItemHandler rerollHandler;
    private final ViewLink<ChallengeCategory> viewLink;

    public ChallengesMenu(@NotNull ExcellentChallengesPlugin plugin) {
        super(plugin, new FileConfig(plugin.getDataFolder() + Config.DIR_MENU, FILE));
        this.viewLink = new ViewLink<>();

        this.addHandler(this.returnHandler = ItemHandler.forReturn(this, (viewer, event) -> {
            this.runNextTick(() -> plugin.getChallengeManager().getCategoriesMenu().open(viewer));
        }));

        this.addHandler(this.rerollHandler = new ItemHandler("reroll", (viewer, event) -> {
            Player player = viewer.getPlayer();
            ChallengeCategory category = this.getLink().get(player);
            if (category == null) return;

            this.runNextTick(() -> {
                plugin.getChallengeManager().tryRerollChallenges(player, category);
            });
        }));

        this.load();

        this.getItems().forEach(menuItem -> {
            menuItem.getOptions().addDisplayModifier((viewer, item) -> {
                ChallengeCategory category = this.getLink().get(viewer);
                ChallengeUser user = plugin.getUserManager().getUserData(viewer.getPlayer());

                ItemReplacer.create(item).readMeta()
                    .replacePlaceholderAPI(viewer.getPlayer())
                    .replace(GENERIC_REROLL_TOKENS, () -> NumberUtil.format(user.getRerollTokens(category)))
                    .writeMeta();
            });
        });
    }

    @Override
    @NotNull
    protected MenuOptions createDefaultOptions() {
        return new MenuOptions(BLACK.enclose(CATEGORY_NAME + " Challenges"), 54, InventoryType.CHEST, 1);
    }

    @Override
    @NotNull
    protected List<MenuItem> createDefaultItems() {
        List<MenuItem> list = new ArrayList<>();

        ItemStack rerollStack = ItemUtil.getSkinHead("b423289510c54b67df023580979c465d0481c769c865bf4b465cf478749f1c4f");
        ItemUtil.editMeta(rerollStack, meta -> {
            meta.setDisplayName(LIGHT_YELLOW.enclose(BOLD.enclose("Reroll Challenges ")) + LIGHT_GRAY.enclose("(1 Token)"));
            meta.setLore(Arrays.asList(
                LIGHT_YELLOW.enclose("▪ " + LIGHT_GRAY.enclose("Tokens: ") + GENERIC_REROLL_TOKENS),
                LIGHT_YELLOW.enclose("▪ " + LIGHT_GRAY.enclose("Purchase at: ") + "store.samplesmp.com"),
                "",
                LIGHT_YELLOW.enclose(BOLD.enclose("Description:")),
                LIGHT_GRAY.enclose("Use this button to " + LIGHT_YELLOW.enclose("replace") + " your"),
                LIGHT_GRAY.enclose("current challenges."),
                "",
                LIGHT_GRAY.enclose(LIGHT_RED.enclose("[❗] ") + "All challenge progress will be " + LIGHT_RED.enclose("lost") + ".")
            ));
        });

        ItemStack returnStack = ItemUtil.getSkinHead("be9ae7a4be65fcbaee65181389a2f7d47e2e326db59ea3eb789a92c85ea46");
        ItemUtil.editMeta(returnStack, meta -> {
            meta.setDisplayName(LIGHT_YELLOW.enclose("Back to Categories"));
        });

        list.add(new MenuItem(rerollStack).setHandler(this.rerollHandler).setSlots(4));
        list.add(new MenuItem(returnStack).setHandler(this.returnHandler).setSlots(49));

        return list;
    }

    @Override
    protected void loadAdditional() {
        this.challengeSlots = ConfigValue.create("Challenges.Slots", new int[] {19, 21, 23, 25, 31}).read(cfg);

        this.iconCompletedEnabled = ConfigValue.create("Challenges.Completed.CustomIcon.Enabled",
            true,
            "Sets whether or not completed challenges will use a different icon.").read(cfg);

        this.iconCompleted = ConfigValue.create("Challenges.Completed.CustomIcon.Item",
            ItemUtil.getSkinHead("a79a5c95ee17abfef45c8dc224189964944d560f19a44f19f8a46aef3fee4756"),
            ""
        ).read(cfg);

        this.formatActiveName = ConfigValue.create("Challenges.Format.Challenge_Active.Name",
            LIGHT_CYAN.enclose(BOLD.enclose("Challenge: ")) + LIGHT_GRAY.enclose(CHALLENGE_TYPE)
        ).read(cfg);

        this.formatActiveLore = ConfigValue.create("Challenges.Format.Challenge_Active.Lore",
            Arrays.asList(
                LIGHT_CYAN.enclose("▪ " + LIGHT_GRAY.enclose("Difficulty: ") + CHALLENGE_DIFFICULTY),
                LIGHT_CYAN.enclose("▪ " + LIGHT_GRAY.enclose("Level: ") + CHALLENGE_LEVEL),
                "",
                PLACEHOLDER_OBJECTIVES,
                PLACEHOLDER_CONDITIONS,
                PLACEHOLDER_REWARDS,
                "",
                LIGHT_GRAY.enclose(LIGHT_GREEN.enclose("✔") + " You completed " + LIGHT_GREEN.enclose(CHALLENGE_PROGRESS_PERCENT + "% ") + "of the challenge."),
                LIGHT_GRAY.enclose(LIGHT_GREEN.enclose("⌚") + " Finish it in: " + LIGHT_GREEN.enclose(CHALLENGE_REFRESH_TIME))
            )
        ).read(cfg);

        this.formatCompletedName = ConfigValue.create("Challenges.Format.Challenge_Completed.Name",
            LIGHT_CYAN.enclose(BOLD.enclose("Challenge: ")) + LIGHT_GRAY.enclose(CHALLENGE_TYPE)
        ).read(cfg);

        this.formatCompletedLore = ConfigValue.create("Challenges.Format.Challenge_Completed.Lore",
            Arrays.asList(
                LIGHT_CYAN.enclose("▪ " + LIGHT_GRAY.enclose("Difficulty: ") + CHALLENGE_DIFFICULTY),
                LIGHT_CYAN.enclose("▪ " + LIGHT_GRAY.enclose("Level: ") + CHALLENGE_LEVEL),
                "",
                PLACEHOLDER_OBJECTIVES,
                PLACEHOLDER_REWARDS,
                "",
                LIGHT_GRAY.enclose(LIGHT_GREEN.enclose("✔") + " You completed " + LIGHT_GREEN.enclose(CHALLENGE_PROGRESS_PERCENT + "% ") + "of the challenge.")
            )
        ).read(cfg);

        this.formatObjectives = ConfigValue.create("Challenges.Format.Objectives",
            Arrays.asList(
                LIGHT_CYAN.enclose(BOLD.enclose("Objectives:")),
                LIGHT_CYAN.enclose("┃ " + LIGHT_GRAY.enclose(OBJECTIVE_NAME + ": ") + OBJECTIVE_PROGRESS_CURRENT + LIGHT_GRAY.enclose("/") + OBJECTIVE_PROGRESS_MAX)
            )
        ).read(cfg);

        this.formatConditions = ConfigValue.create("Challenges.Format.Conditions",
            Arrays.asList(
                "",
                LIGHT_RED.enclose(BOLD.enclose("Requirements:")),
                LIGHT_RED.enclose("[❗] ") + LIGHT_GRAY.enclose(CONDITION_DESCRIPTION)
            )
        ).read(cfg);

        this.formatRewards = ConfigValue.create("Challenges.Format.Rewards",
            Arrays.asList(
                "",
                LIGHT_YELLOW.enclose("[$] ") + LIGHT_GRAY.enclose("Complete this challenge to receive:"),
                LIGHT_YELLOW.enclose("◈ " + REWARD_NAME)
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
        ChallengeCategory category = this.getLink().get(viewer);
        if (category != null) {
            options.setTitle(category.replacePlaceholders().apply(options.getTitle()));
        }

        this.autoFill(viewer);
    }

    @Override
    protected void onReady(@NotNull MenuViewer viewer, @NotNull Inventory inventory) {

    }

    @Override
    public void onAutoFill(@NotNull MenuViewer viewer, @NotNull AutoFill<GeneratedChallenge> autoFill) {
        Player player = viewer.getPlayer();

        ChallengeCategory category = this.getLink().get(player);
        if (category == null) return;

        this.plugin.getChallengeManager().updateChallenges(player, false);

        ChallengeUser user = plugin.getUserManager().getUserData(player);

        autoFill.setSlots(this.challengeSlots);
        autoFill.setItems(user.getChallenges(category).stream().sorted(Comparator.comparingInt(GeneratedChallenge::getLevel)).toList());
        autoFill.setItemCreator(challenge -> {
            ItemStack item = challenge.getActionType().getIcon();

            boolean isCompleted = challenge.isCompleted();
            long refreshTime = user.getRefreshTime(category);

            if (isCompleted && this.iconCompletedEnabled) {
                item = new ItemStack(this.iconCompleted);
            }

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
                    return TimeUtil.formatDuration(refreshTime == 0 ? System.currentTimeMillis() : refreshTime);
                })
                .replaceLoreExact(PLACEHOLDER_OBJECTIVES, objectives)
                .replaceLoreExact(PLACEHOLDER_CONDITIONS, conditions)
                .replaceLoreExact(PLACEHOLDER_REWARDS, rewards)
                .replace(challenge.replacePlaceholders())
                .writeMeta();
            return item;
        });
    }
}
