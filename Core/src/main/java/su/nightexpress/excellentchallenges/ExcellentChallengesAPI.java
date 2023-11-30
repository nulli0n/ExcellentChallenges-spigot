package su.nightexpress.excellentchallenges;

import su.nightexpress.excellentchallenges.challenge.ChallengeManager;
import org.jetbrains.annotations.NotNull;

public class ExcellentChallengesAPI {

    public static final ExcellentChallengesPlugin PLUGIN = ExcellentChallengesPlugin.getPlugin(ExcellentChallengesPlugin.class);

    @NotNull
    public static ChallengeManager getChallengeManager() {
        return PLUGIN.getChallengeManager();
    }
}
