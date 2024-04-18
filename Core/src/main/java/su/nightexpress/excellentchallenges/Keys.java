package su.nightexpress.excellentchallenges;

import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;

public class Keys {

    public static NamespacedKey brewingHolder;
    public static NamespacedKey entityTracked;

    public static void load(@NotNull ChallengesPlugin plugin) {
        brewingHolder = new NamespacedKey(plugin, "brewing_holder");
        entityTracked = new NamespacedKey(plugin, "entity_tracked");
    }
}
