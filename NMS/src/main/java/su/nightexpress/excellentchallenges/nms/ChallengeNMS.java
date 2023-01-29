package su.nightexpress.excellentchallenges.nms;

import org.bukkit.block.BrewingStand;
import org.jetbrains.annotations.NotNull;

public abstract class ChallengeNMS {

    public abstract boolean canBrew(@NotNull BrewingStand stand);
}
