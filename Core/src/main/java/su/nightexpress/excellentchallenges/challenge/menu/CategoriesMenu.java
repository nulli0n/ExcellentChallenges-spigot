package su.nightexpress.excellentchallenges.challenge.menu;

import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentchallenges.ChallengesPlugin;
import su.nightexpress.excellentchallenges.challenge.ChallengeCategory;
import su.nightexpress.excellentchallenges.challenge.GeneratedChallenge;
import su.nightexpress.excellentchallenges.challenge.reward.Reward;
import su.nightexpress.excellentchallenges.config.Config;
import su.nightexpress.excellentchallenges.data.object.ChallengeUser;
import su.nightexpress.nightcore.config.ConfigValue;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.menu.MenuOptions;
import su.nightexpress.nightcore.menu.MenuViewer;
import su.nightexpress.nightcore.menu.api.AutoFill;
import su.nightexpress.nightcore.menu.api.AutoFilled;
import su.nightexpress.nightcore.menu.impl.ConfigMenu;
import su.nightexpress.nightcore.menu.item.MenuItem;
import su.nightexpress.nightcore.util.ItemReplacer;
import su.nightexpress.nightcore.util.NumberUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static su.nightexpress.excellentchallenges.Placeholders.*;
import static su.nightexpress.nightcore.util.text.tag.Tags.*;

public class CategoriesMenu extends ConfigMenu<ChallengesPlugin> implements AutoFilled<ChallengeCategory> {

    public static final String FILE = "categories.yml";

    private int[] categorySlots;
    private String categoryName;
    private List<String> categoryLore;

    public CategoriesMenu(@NotNull ChallengesPlugin plugin) {
        super(plugin, new FileConfig(plugin.getDataFolder() + Config.DIR_MENU, FILE));

        this.load();

        this.getItems().forEach(menuItem -> menuItem.getOptions().addDisplayModifier((viewer, item) -> {
            ItemReplacer.create(item).readMeta().replacePlaceholderAPI(viewer.getPlayer()).writeMeta();
        }));
    }

    @Override
    @NotNull
    protected MenuOptions createDefaultOptions() {
        return new MenuOptions(BLACK.enclose("Challenges"), 27, InventoryType.CHEST, 1);
    }

    @Override
    @NotNull
    protected List<MenuItem> createDefaultItems() {
        return new ArrayList<>();
    }

    @Override
    protected void loadAdditional() {
        this.categorySlots = ConfigValue.create("Category.Slots", new int[] {10, 13, 16}).read(cfg);

        this.categoryName = ConfigValue.create("Category.Name",
            LIGHT_YELLOW.enclose(BOLD.enclose(CATEGORY_NAME + " Challenges"))
        ).read(cfg);

        this.categoryLore = ConfigValue.create("Category.Lore",
            Arrays.asList(
                "",
                LIGHT_YELLOW.enclose(BOLD.enclose("Statistics:")),
                LIGHT_YELLOW.enclose("┃ " + LIGHT_GRAY.enclose("Completed: ") + GENERIC_COMPLETED + LIGHT_GRAY.enclose("/") + GENERIC_TOTAL),
                LIGHT_YELLOW.enclose("┃ " + LIGHT_GRAY.enclose("Progress: ") + GENERIC_PROGRESS + "%")
            )
        ).read(cfg);
    }

    @Override
    public void onPrepare(@NotNull MenuViewer viewer, @NotNull MenuOptions options) {
        this.autoFill(viewer);
    }

    @Override
    protected void onReady(@NotNull MenuViewer viewer, @NotNull Inventory inventory) {

    }

    @Override
    public void onAutoFill(@NotNull MenuViewer viewer, @NotNull AutoFill<ChallengeCategory> autoFill) {
        autoFill.setSlots(this.categorySlots);
        autoFill.setItems(this.plugin.getChallengeManager().getCategories().stream().toList());
        autoFill.setItemCreator(category -> {
            ItemStack item = category.getIcon();

            ChallengeUser user = plugin.getUserManager().getUserData(viewer.getPlayer());
            Set<GeneratedChallenge> challenges = user.getChallenges(category);

            ItemReplacer.create(item).hideFlags().trimmed()
                .setDisplayName(this.categoryName)
                .setLore(this.categoryLore)
                .replace(GENERIC_REWARDS, () -> category.getCompletionRewards().stream().map(Reward::getName).collect(Collectors.joining(", ")))
                .replace(GENERIC_UNFINISHED, () -> {
                    return NumberUtil.format((int) challenges.stream().filter(Predicate.not(GeneratedChallenge::isCompleted)).count());
                })
                .replace(GENERIC_COMPLETED, () -> {
                    return NumberUtil.format((int) challenges.stream().filter(GeneratedChallenge::isCompleted).count());
                })
                .replace(GENERIC_TOTAL, () -> {
                    return NumberUtil.format(challenges.size());
                })
                .replace(GENERIC_PROGRESS, () -> NumberUtil.format(user.getProgressPercent(category)))
                .replace(GENERIC_REROLL_TOKENS, () -> NumberUtil.format(user.getRerollTokens(category)))
                .replace(category.replacePlaceholders())
                .writeMeta();


            return item;
        });
        autoFill.setClickAction(category -> (viewer1, event) -> {
            this.runNextTick(() -> {
                this.plugin.getChallengeManager().openChallengesMenu(viewer1.getPlayer(), category);
            });
        });
    }
}
