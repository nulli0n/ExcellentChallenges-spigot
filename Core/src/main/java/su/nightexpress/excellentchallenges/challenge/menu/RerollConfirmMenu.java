package su.nightexpress.excellentchallenges.challenge.menu;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentchallenges.ChallengesPlugin;
import su.nightexpress.excellentchallenges.challenge.ChallengeCategory;
import su.nightexpress.excellentchallenges.config.Config;
import su.nightexpress.excellentchallenges.data.object.ChallengeUser;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.menu.MenuOptions;
import su.nightexpress.nightcore.menu.MenuViewer;
import su.nightexpress.nightcore.menu.impl.ConfigMenu;
import su.nightexpress.nightcore.menu.item.ItemHandler;
import su.nightexpress.nightcore.menu.item.MenuItem;
import su.nightexpress.nightcore.menu.link.Linked;
import su.nightexpress.nightcore.menu.link.ViewLink;
import su.nightexpress.nightcore.util.ItemReplacer;
import su.nightexpress.nightcore.util.ItemUtil;
import su.nightexpress.nightcore.util.NumberUtil;

import java.util.ArrayList;
import java.util.List;

import static su.nightexpress.excellentchallenges.Placeholders.*;
import static su.nightexpress.nightcore.util.text.tag.Tags.*;

public class RerollConfirmMenu extends ConfigMenu<ChallengesPlugin> implements Linked<ChallengeCategory> {

    public static final String FILE = "reroll_confirm.yml";

    private final ItemHandler acceptHandler;
    private final ItemHandler declineHandler;
    private final ViewLink<ChallengeCategory> link;

    public RerollConfirmMenu(@NotNull ChallengesPlugin plugin) {
        super(plugin, new FileConfig(plugin.getDataFolder() + Config.DIR_MENU, FILE));
        this.link = new ViewLink<>();

        this.addHandler(this.acceptHandler = new ItemHandler("confirmation_accept", (viewer, event) -> {
            Player player = viewer.getPlayer();
            ChallengeCategory category = this.getLink().get(player);
            this.plugin.getChallengeManager().rerollChallenges(player, category);
            this.runNextTick(() -> plugin.getChallengeManager().openChallengesMenu(player, category));
        }));

        this.addHandler(this.declineHandler = new ItemHandler("confirmation_decline", (viewer, event) -> {
            Player player = viewer.getPlayer();
            ChallengeCategory category = this.getLink().get(player);
            this.runNextTick(() -> plugin.getChallengeManager().openChallengesMenu(player, category));
        }));

        this.load();

        this.getItems().forEach(menuItem -> menuItem.getOptions().addDisplayModifier((viewer, item) -> {
            Player player = viewer.getPlayer();

            ChallengeCategory category = this.getLink().get(viewer);
            if (category == null) return;

            ChallengeUser user = plugin.getUserManager().getUserData(player);

            ItemReplacer.create(item).readMeta()
                .replace(GENERIC_TYPE, category::getName)
                .replace(GENERIC_REROLL_TOKENS, () -> NumberUtil.format(user.getRerollTokens(category)))
                .writeMeta();
        }));
    }

    @Override
    @NotNull
    public ViewLink<ChallengeCategory> getLink() {
        return this.link;
    }

    @Override
    @NotNull
    protected MenuOptions createDefaultOptions() {
        return new MenuOptions(BLACK.enclose("Reroll " + CATEGORY_NAME + " challenges?"), 9, InventoryType.CHEST);
    }

    @Override
    @NotNull
    protected List<MenuItem> createDefaultItems() {
        List<MenuItem> list = new ArrayList<>();

        ItemStack cancelStack = ItemUtil.getSkinHead("27548362a24c0fa8453e4d93e68c5969ddbde57bf6666c0319c1ed1e84d89065");
        ItemUtil.editMeta(cancelStack, meta -> {
            meta.setDisplayName(LIGHT_RED.enclose(BOLD.enclose("Cancel")));
        });

        ItemStack acceptStack = ItemUtil.getSkinHead("a79a5c95ee17abfef45c8dc224189964944d560f19a44f19f8a46aef3fee4756");
        ItemUtil.editMeta(acceptStack, meta -> {
            meta.setDisplayName(LIGHT_GREEN.enclose(BOLD.enclose("Accept")));
        });

        list.add(new MenuItem(cancelStack).setHandler(this.declineHandler).setSlots(0));
        list.add(new MenuItem(acceptStack).setHandler(this.acceptHandler).setSlots(8));

        return list;
    }

    @Override
    protected void loadAdditional() {

    }

    @Override
    public void onPrepare(@NotNull MenuViewer viewer, @NotNull MenuOptions options) {
        ChallengeCategory category = this.getLink().get(viewer);
        if (category != null) {
            options.setTitle(category.replacePlaceholders().apply(options.getTitle()));
        }
    }

    @Override
    protected void onReady(@NotNull MenuViewer viewer, @NotNull Inventory inventory) {

    }
}
