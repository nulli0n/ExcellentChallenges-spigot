package su.nightexpress.excellentchallenges.challenge.menu;

import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import su.nexmedia.engine.api.config.JOption;
import su.nexmedia.engine.api.menu.AutoPaged;
import su.nexmedia.engine.api.menu.click.ClickHandler;
import su.nexmedia.engine.api.menu.click.ItemClick;
import su.nexmedia.engine.api.menu.impl.MenuOptions;
import su.nexmedia.engine.api.menu.impl.MenuViewer;
import su.nexmedia.engine.utils.*;
import su.nightexpress.excellentchallenges.challenge.GeneratedChallenge;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import su.nexmedia.engine.api.config.JYML;
import su.nexmedia.engine.api.menu.MenuItemType;
import su.nexmedia.engine.api.menu.impl.ConfigMenu;
import su.nexmedia.engine.api.menu.item.MenuItem;
import su.nightexpress.excellentchallenges.ExcellentChallengesPlugin;
import su.nightexpress.excellentchallenges.Placeholders;
import su.nightexpress.excellentchallenges.challenge.ChallengeCategory;
import su.nightexpress.excellentchallenges.challenge.reward.Reward;
import su.nightexpress.excellentchallenges.config.Config;
import su.nightexpress.excellentchallenges.data.object.ChallengeUser;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static su.nexmedia.engine.utils.Colors.*;
import static su.nightexpress.excellentchallenges.Placeholders.*;

public class CategoriesMenu extends ConfigMenu<ExcellentChallengesPlugin> implements AutoPaged<ChallengeCategory> {

    public static final String FILE = "categories.yml";

    private int[] categorySlots;
    private String categoryName;
    private List<String> categoryLore;

    public CategoriesMenu(@NotNull ExcellentChallengesPlugin plugin) {
        super(plugin, new JYML(plugin.getDataFolder() + Config.DIR_MENU, FILE));

        this.registerHandler(MenuItemType.class)
            .addClick(MenuItemType.CLOSE, ClickHandler.forClose(this));

        this.load();

        this.getItems().forEach(menuItem -> menuItem.getOptions().addDisplayModifier((viewer, item) -> {
            ItemReplacer.create(item).readMeta().replacePlaceholderAPI(viewer.getPlayer()).writeMeta();
        }));
    }

    @Override
    public boolean isCodeCreation() {
        return true;
    }

    @Override
    @NotNull
    protected MenuOptions createDefaultOptions() {
        return new MenuOptions("Challenges", 27, InventoryType.CHEST, 1);
    }

    @Override
    @NotNull
    protected List<MenuItem> createDefaultItems() {
        return super.createDefaultItems();
    }

    @Override
    protected void loadAdditional() {
        this.categorySlots = new JOption<int[]>("Category.Slots",
            (cfg, path, def) -> StringUtil.getIntArray(cfg.getString(path, "10,13,16")),
            () -> new int[] {10, 13, 16}
        ).setWriter(JYML::setIntArray).read(cfg);

        this.categoryName = JOption.create("Category.Name",
            LIGHT_YELLOW + BOLD + CATEGORY_NAME + " Challenges"
        ).read(cfg);

        this.categoryLore = JOption.create("Category.Lore",
            Arrays.asList(
                "",
                LIGHT_YELLOW + BOLD + "Statistics:",
                LIGHT_YELLOW + "┃ " + GRAY + "Completed: " + LIGHT_YELLOW + GENERIC_COMPLETED + GRAY + "/" + LIGHT_YELLOW + GENERIC_TOTAL,
                LIGHT_YELLOW + "┃ " + GRAY + "Progress: " + LIGHT_YELLOW + GENERIC_PROGRESS + "%"
            )
        ).read(cfg);
    }

    @Override
    public void onPrepare(@NotNull MenuViewer viewer, @NotNull MenuOptions options) {
        super.onPrepare(viewer, options);

        this.getItemsForPage(viewer).forEach(this::addItem);
    }

    @Override
    public int[] getObjectSlots() {
        return this.categorySlots;
    }

    @Override
    @NotNull
    public List<ChallengeCategory> getObjects(@NotNull Player player) {
        return Config.CATEGORIES.get().values().stream().toList();
    }

    @Override
    @NotNull
    public ItemStack getObjectStack(@NotNull Player player, @NotNull ChallengeCategory category) {
        ItemStack item = category.getIcon();

        ChallengeUser user = plugin.getUserManager().getUserData(player);
        Set<GeneratedChallenge> challenges = user.getChallenges(category);

        ItemReplacer.create(item).hideFlags().trimmed()
            .setDisplayName(this.categoryName)
            .setLore(this.categoryLore)
            .replace(Placeholders.GENERIC_REWARDS, () -> category.getCompletionRewards().stream().map(Reward::getName).collect(Collectors.joining(", ")))
            .replace(Placeholders.GENERIC_UNFINISHED, () -> {
                return NumberUtil.format((int) challenges.stream().filter(Predicate.not(GeneratedChallenge::isCompleted)).count());
            })
            .replace(Placeholders.GENERIC_COMPLETED, () -> {
                return NumberUtil.format((int) challenges.stream().filter(GeneratedChallenge::isCompleted).count());
            })
            .replace(Placeholders.GENERIC_TOTAL, () -> {
                return NumberUtil.format(challenges.size());
            })
            .replace(Placeholders.GENERIC_PROGRESS, () -> NumberUtil.format(user.getProgressPercent(category)))
            .replace(Placeholders.GENERIC_REROLL_TOKENS, () -> NumberUtil.format(user.getRerollTokens(category)))
            .replace(category.replacePlaceholders())
            .replace(Colorizer::apply)
            .writeMeta();


        return item;
    }

    @Override
    @NotNull
    public ItemClick getObjectClick(@NotNull ChallengeCategory category) {
        return (viewer, event) -> {
            this.plugin.runTask(task -> {
                this.plugin.getChallengeManager().openChallengesMenu(viewer.getPlayer(), category);
            });
        };
    }
}
