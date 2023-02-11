package su.nightexpress.excellentchallenges.challenge.handler.impl;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockFertilizeEvent;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentchallenges.ExcellentChallenges;
import su.nightexpress.excellentchallenges.challenge.handler.ChallengeHandler;
import su.nightexpress.excellentchallenges.challenge.type.ChallengeJobType;

public class BlockFertilizeHandler extends ChallengeHandler {

    public BlockFertilizeHandler(@NotNull ExcellentChallenges plugin) {
        super(plugin, ChallengeJobType.BLOCK_FERTILIZE);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBlockFertilize(BlockFertilizeEvent e) {
        Player player = e.getPlayer();
        if (player == null) return;

        e.getBlocks().forEach(blockState -> {
            this.progressChallenge(player, blockState.getType().name(), 1);
        });
    }
}
