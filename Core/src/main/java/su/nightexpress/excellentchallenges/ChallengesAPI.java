package su.nightexpress.excellentchallenges;

import su.nightexpress.excellentchallenges.challenge.ChallengeManager;
import org.jetbrains.annotations.NotNull;

public class ChallengesAPI {

    public static final ChallengesPlugin PLUGIN = ChallengesPlugin.getPlugin(ChallengesPlugin.class);

    @NotNull
    public static ChallengeManager getChallengeManager() {
        return PLUGIN.getChallengeManager();
    }
}
