package su.nightexpress.excellentchallenges.challenge.menu;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import su.nexmedia.engine.api.config.JYML;
import su.nexmedia.engine.api.menu.MenuItemType;
import su.nexmedia.engine.api.menu.impl.ConfigMenu;
import su.nexmedia.engine.api.menu.impl.MenuOptions;
import su.nexmedia.engine.api.menu.impl.MenuViewer;
import su.nexmedia.engine.api.menu.item.MenuItem;
import su.nexmedia.engine.api.menu.link.Linked;
import su.nexmedia.engine.api.menu.link.ViewLink;
import su.nexmedia.engine.utils.Colorizer;
import su.nexmedia.engine.utils.ItemReplacer;
import su.nexmedia.engine.utils.ItemUtil;
import su.nexmedia.engine.utils.NumberUtil;
import su.nightexpress.excellentchallenges.ExcellentChallengesPlugin;
import su.nightexpress.excellentchallenges.Placeholders;
import su.nightexpress.excellentchallenges.challenge.ChallengeCategory;
import su.nightexpress.excellentchallenges.config.Config;
import su.nightexpress.excellentchallenges.config.Lang;
import su.nightexpress.excellentchallenges.data.object.ChallengeUser;

import java.util.ArrayList;
import java.util.List;

import static su.nexmedia.engine.utils.Colors.*;


public class RerollConfirmMenu extends ConfigMenu<ExcellentChallengesPlugin> implements Linked<ChallengeCategory> {

    public static final String FILE = "reroll_confirm.yml";

    private final ViewLink<ChallengeCategory> link;

    public RerollConfirmMenu(@NotNull ExcellentChallengesPlugin plugin) {
        super(plugin, new JYML(plugin.getDataFolder() + Config.DIR_MENU, FILE));
        this.link = new ViewLink<>();

        this.registerHandler(MenuItemType.class)
            .addClick(MenuItemType.CONFIRMATION_DECLINE, (viewer, event) -> {
                Player player = viewer.getPlayer();
                ChallengeCategory category = this.link.get(viewer);
                if (category == null) {
                    player.closeInventory();
                    return;
                }
                this.plugin.runTask(task -> {
                    this.plugin.getChallengeManager().openChallengesMenu(player, category);
                });
            })
            .addClick(MenuItemType.CONFIRMATION_ACCEPT, (viewer, event) -> {
                Player player = viewer.getPlayer();
                ChallengeCategory category = this.link.get(viewer);
                if (category == null) {
                    player.closeInventory();
                    return;
                }

                ChallengeUser user = plugin.getUserManager().getUserData(player);
                user.takeRerollTokens(category, 1);
                user.getChallenges(category).clear();
                plugin.getChallengeManager().createChallenges(player, category);
                this.plugin.runTask(task -> {
                    this.plugin.getChallengeManager().openChallengesMenu(player, category);
                });
                plugin.getMessage(Lang.CHALLENGE_REROLL_DONE)
                    .replace(Placeholders.GENERIC_TYPE, category.getName())
                    .send(player);
            });

        this.load();

        this.getItems().forEach(menuItem -> menuItem.getOptions().addDisplayModifier((viewer, item) -> {
            Player player = viewer.getPlayer();

            ChallengeCategory category = this.link.get(viewer);
            if (category == null) return;

            ChallengeUser user = plugin.getUserManager().getUserData(player);

            ItemReplacer.create(item).readMeta()
                .replace(Placeholders.GENERIC_TYPE, category::getName)
                .replace(Placeholders.GENERIC_REROLL_TOKENS, () -> NumberUtil.format(user.getRerollTokens(category)))
                .writeMeta();
        }));
    }

    @Override
    public boolean isCodeCreation() {
        return true;
    }

    @Override
    @NotNull
    protected MenuOptions createDefaultOptions() {
        return new MenuOptions("Reroll " + Placeholders.CATEGORY_NAME + " challenges?", 9, InventoryType.CHEST);
    }

    @Override
    @NotNull
    protected List<MenuItem> createDefaultItems() {
        List<MenuItem> list = new ArrayList<>();

        ItemStack cancelStack = ItemUtil.createCustomHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmViNTg4YjIxYTZmOThhZDFmZjRlMDg1YzU1MmRjYjA1MGVmYzljYWI0MjdmNDYwNDhmMThmYzgwMzQ3NWY3In19fQ==");
        ItemUtil.mapMeta(cancelStack, meta -> {
            meta.setDisplayName(Colorizer.apply(RED + BOLD + "Cancel"));
        });

        ItemStack acceptStack = ItemUtil.createCustomHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTc5YTVjOTVlZTE3YWJmZWY0NWM4ZGMyMjQxODk5NjQ5NDRkNTYwZjE5YTQ0ZjE5ZjhhNDZhZWYzZmVlNDc1NiJ9fX0=");
        ItemUtil.mapMeta(acceptStack, meta -> {
            meta.setDisplayName(Colorizer.apply(GREEN + BOLD + "Accept"));
        });

        list.add(new MenuItem(cancelStack).setType(MenuItemType.CONFIRMATION_DECLINE).setSlots(0));
        list.add(new MenuItem(acceptStack).setType(MenuItemType.CONFIRMATION_ACCEPT).setSlots(8));

        return list;
    }

    @Override
    @NotNull
    public ViewLink<ChallengeCategory> getLink() {
        return this.link;
    }

    @Override
    public void onPrepare(@NotNull MenuViewer viewer, @NotNull MenuOptions options) {
        super.onPrepare(viewer, options);

        ChallengeCategory category = this.getLink().get(viewer);
        if (category != null) {
            options.setTitle(category.replacePlaceholders().apply(options.getTitle()));
        }
    }
}
