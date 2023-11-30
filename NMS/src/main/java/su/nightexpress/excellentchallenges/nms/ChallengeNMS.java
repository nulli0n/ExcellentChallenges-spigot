package su.nightexpress.excellentchallenges.nms;

import org.bukkit.block.BrewingStand;
import org.jetbrains.annotations.NotNull;

public interface ChallengeNMS {

    boolean canBrew(@NotNull BrewingStand stand);
}
