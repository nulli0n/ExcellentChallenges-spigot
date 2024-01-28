package su.nightexpress.excellentchallenges.challenge.action;

import org.bukkit.Material;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import su.nexmedia.engine.api.config.JOption;
import su.nexmedia.engine.api.config.JYML;
import su.nexmedia.engine.utils.Colorizer;
import su.nexmedia.engine.utils.StringUtil;
import su.nightexpress.excellentchallenges.ExcellentChallengesPlugin;

public class ActionType<E extends Event, O> {

    private final String             name;
    private final ObjectFormatter<O> objectFormatter;
    private final EventHelper<E, O>  eventHelper;

    private String displayName;
    private ItemStack icon;

    public ActionType(@NotNull String name,
                      @NotNull ItemStack icon,
                      @NotNull ObjectFormatter<O> objectFormatter,
                      @NotNull EventHelper<E, O> eventHelper) {
        this.name = name.toLowerCase();
        this.objectFormatter = objectFormatter;
        this.eventHelper = eventHelper;
        this.setDisplayName(StringUtil.capitalizeUnderscored(name));
        this.setIcon(icon);
    }

    @NotNull
    public static <E extends Event, O> ActionType<E, O> create(@NotNull String name,
                                                               @NotNull ObjectFormatter<O> objectFormatter,
                                                               @NotNull EventHelper<E, O> eventHelper) {
        return create(name, Material.MAP, objectFormatter, eventHelper);
    }

    @NotNull
    public static <E extends Event, O> ActionType<E, O> create(@NotNull String name,
                                                               @NotNull Material icon,
                                                               @NotNull ObjectFormatter<O> objectFormatter,
                                                               @NotNull EventHelper<E, O> eventHelper) {
        return create(name, new ItemStack(icon), objectFormatter, eventHelper);
    }

    @NotNull
    public static <E extends Event, O> ActionType<E, O> create(@NotNull String name,
                                                               @NotNull ItemStack icon,
                                                               @NotNull ObjectFormatter<O> objectFormatter,
                                                               @NotNull EventHelper<E, O> eventHelper) {
        return new ActionType<>(name, icon, objectFormatter, eventHelper);
    }

    public boolean loadSettings(@NotNull ExcellentChallengesPlugin plugin) {
        JYML config = plugin.getConfig();
        String path = "Challenges.Action_Types." + this.getName();
        if (!JOption.create(path + ".Enabled", true).read(config)) {
            return false;
        }
        this.setDisplayName(JOption.create(path + ".DisplayName", this.getDisplayName()).read(config));
        this.setIcon(JOption.create(path + ".Icon", this.getIcon()).read(config));
        config.saveChanges();
        return true;
    }

    @NotNull
    public String getName() {
        return name;
    }

    @NotNull
    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(@NotNull String displayName) {
        this.displayName = Colorizer.apply(displayName);
    }

    @NotNull
    public ItemStack getIcon() {
        return new ItemStack(this.icon);
    }

    public void setIcon(@NotNull ItemStack icon) {
        this.icon = new ItemStack(icon);
    }

    @NotNull
    public String getObjectName(@NotNull O object) {
        return this.objectFormatter.getName(object).toLowerCase();
    }

    @NotNull
    public String getObjectLocalizedName(@NotNull String object) {
        return this.objectFormatter.getLocalizedName(object);
    }

    @NotNull
    public EventHelper<E, O> getEventHelper() {
        return eventHelper;
    }

    @NotNull
    public ObjectFormatter<O> getObjectFormatter() {
        return objectFormatter;
    }
}
