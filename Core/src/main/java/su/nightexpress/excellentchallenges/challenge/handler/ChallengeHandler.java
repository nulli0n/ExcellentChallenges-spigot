package su.nightexpress.excellentchallenges.challenge.handler;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nexmedia.engine.api.manager.AbstractListener;
import su.nightexpress.excellentchallenges.ExcellentChallenges;
import su.nightexpress.excellentchallenges.challenge.type.ChallengeJobType;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public abstract class ChallengeHandler extends AbstractListener<ExcellentChallenges> {

    public static final Map<ChallengeJobType, ChallengeHandler> HANDLERS = new HashMap<>();

    protected final ChallengeJobType jobType;

    public ChallengeHandler(@NotNull ExcellentChallenges plugin, @NotNull ChallengeJobType jobType) {
        super(plugin);
        this.jobType = jobType;
    }

    @Nullable
    public static ChallengeHandler get(@NotNull ChallengeJobType jobType) {
        return HANDLERS.get(jobType);
    }

    public static void register(@NotNull ChallengeHandler handler) {
        HANDLERS.put(handler.getJobType(), handler);
        handler.setup();
    }

    public static void unregister(@NotNull ChallengeJobType jobType) {
        ChallengeHandler handler = HANDLERS.remove(jobType);
        if (handler != null) {
            handler.shutdown();
        }
    }

    public static void unregisterAll() {
        new HashSet<>(HANDLERS.keySet()).forEach(ChallengeHandler::unregister);
    }

    @NotNull
    public ChallengeJobType getJobType() {
        return jobType;
    }

    public void setup() {
        this.registerListeners();
    }

    public void shutdown() {
        this.unregisterListeners();
    }

    public void progressChallenge(@NotNull Player player, @NotNull String objective, int amount) {
        if (player.getGameMode() == GameMode.CREATIVE) return;

        this.plugin.getChallengeManager().progressChallenge(player, this.getJobType(), objective, amount);
    }
}
