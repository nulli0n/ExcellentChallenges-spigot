package su.nightexpress.excellentchallenges.challenge.action;

import org.bukkit.Material;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentchallenges.ChallengesPlugin;
import su.nightexpress.nightcore.config.ConfigValue;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.util.StringUtil;

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

    public boolean loadSettings(@NotNull ChallengesPlugin plugin) {
        FileConfig config = plugin.getConfig();
        String path = "Challenges.Action_Types." + this.getName();
        if (!ConfigValue.create(path + ".Enabled", true).read(config)) {
            return false;
        }
        this.setDisplayName(ConfigValue.create(path + ".DisplayName", this.getDisplayName()).read(config));
        this.setIcon(ConfigValue.create(path + ".Icon", this.getIcon()).read(config));
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
        this.displayName = displayName;
    }

    @NotNull
    public ItemStack getIcon() {
        return new ItemStack(this.icon);
    }

    @NotNull
    public ActionType<E, O> setIcon(@NotNull ItemStack icon) {
        this.icon = new ItemStack(icon);
        return this;
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
