package su.nightexpress.excellentchallenges.challenge.handler.impl;

import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockPlaceEvent;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentchallenges.ExcellentChallenges;
import su.nightexpress.excellentchallenges.challenge.handler.ChallengeHandler;
import su.nightexpress.excellentchallenges.challenge.type.ChallengeJobType;

public class BlockPlaceHandler extends ChallengeHandler {

    public BlockPlaceHandler(@NotNull ExcellentChallenges plugin) {
        super(plugin, ChallengeJobType.BLOCK_PLACE);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onChallengeBlockPlace(BlockPlaceEvent e) {
        Block block = e.getBlock();

        this.progressChallenge(e.getPlayer(), block.getType().name(), 1);
    }
}
