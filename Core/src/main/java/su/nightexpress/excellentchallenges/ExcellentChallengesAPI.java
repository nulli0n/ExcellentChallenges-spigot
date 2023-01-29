package su.nightexpress.excellentchallenges;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentchallenges.challenge.ChallengeManager;

public class ExcellentChallengesAPI {

    public static final ExcellentChallenges PLUGIN = ExcellentChallenges.getPlugin(ExcellentChallenges.class);

    @NotNull
    public static ChallengeManager getChallengeManager() {
        return PLUGIN.getChallengeManager();
    }
}
