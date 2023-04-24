package su.nightexpress.excellentchallenges.challenge.handler.impl;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.jetbrains.annotations.NotNull;
import su.nexmedia.playerblocktracker.PlayerBlockTracker;
import su.nightexpress.excellentchallenges.ExcellentChallenges;
import su.nightexpress.excellentchallenges.challenge.handler.ChallengeHandler;
import su.nightexpress.excellentchallenges.challenge.type.ChallengeJobType;
import su.nightexpress.excellentchallenges.config.Config;

public class BlockBreakHandler extends ChallengeHandler {

    public BlockBreakHandler(@NotNull ExcellentChallenges plugin) {
        super(plugin, ChallengeJobType.BLOCK_BREAK);
    }

    @Override
    public void setup() {
        super.setup();

        if (Config.OBJECTIVES_ANTI_GLITCH_TRACK_BLOCKS.get()) {
            PlayerBlockTracker.BLOCK_FILTERS.add(block -> {
                return plugin.getChallengeManager().getGenerators().stream().anyMatch(generator -> {
                    return generator.getObjectivesProgress().containsKey(block.getType().name().toLowerCase());
                });
            });
        }
    }

    /*
    Using MONITOR priority here for the best compatibility with protection plugins.
    PlayerBlockTracker is initialized after this handler registers its listeners,
    so we're safe to assume it won't untrack the block before this event handler proceed.
     */
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onChallengeBlockBreak(BlockBreakEvent e) {
        Block block = e.getBlock();
        if (PlayerBlockTracker.isTracked(block)) return;

        Player player = e.getPlayer();
        this.progressChallenge(player, block.getType().name(), 1);
    }
}
