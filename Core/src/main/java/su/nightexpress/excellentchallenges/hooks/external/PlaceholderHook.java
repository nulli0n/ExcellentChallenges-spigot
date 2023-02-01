package su.nightexpress.excellentchallenges.hooks.external;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import su.nexmedia.engine.utils.CollectionsUtil;
import su.nexmedia.engine.utils.NumberUtil;
import su.nightexpress.excellentchallenges.ExcellentChallenges;
import su.nightexpress.excellentchallenges.ExcellentChallengesAPI;
import su.nightexpress.excellentchallenges.challenge.ChallengeType;
import su.nightexpress.excellentchallenges.challenge.type.ChallengeJobType;
import su.nightexpress.excellentchallenges.data.object.ChallengeUser;

public class PlaceholderHook {

    private static Expansion expansion;

    public static void setup() {
        if (expansion == null) {
            (expansion = new Expansion()).register();
        }
    }

    public static void shutdown() {
        if (expansion != null) {
            expansion.unregister();
            expansion = null;
        }
    }

    static class Expansion extends PlaceholderExpansion {

        @Override
        @NotNull
        public String getAuthor() {
            return ExcellentChallengesAPI.PLUGIN.getDescription().getAuthors().get(0);
        }

        @Override
        @NotNull
        public String getIdentifier() {
            return "excellentchallenges";
        }

        @Override
        @NotNull
        public String getVersion() {
            return ExcellentChallengesAPI.PLUGIN.getDescription().getVersion();
        }

        @Override
        public String onPlaceholderRequest(Player player, @NotNull String params) {
            if (player == null) return null;

            ExcellentChallenges plugin = ExcellentChallengesAPI.PLUGIN;
            ChallengeUser user = plugin.getUserManager().getUserData(player);

            if (params.startsWith("progress_")) {
                String type = params.replace("progress_", "");
                ChallengeType cType = plugin.getChallengeManager().getChallengeType(type);
                if (cType == null) return null;


                return NumberUtil.format(user.getProgressPercent(cType));
            }

            if (params.startsWith("reroll_tokens_")) {
                String type = params.substring("reroll_tokens_".length());
                ChallengeType cType = plugin.getChallengeManager().getChallengeType(type);
                if (cType == null) return null;

                return NumberUtil.format(user.getRerollTokens(cType));
            }

            if (params.startsWith("completed_")) {
                String type = params.replace("completed_", "");
                if (type.equalsIgnoreCase("all")) {
                    return NumberUtil.format(user.getCompletedChallengesAmount());
                }

                ChallengeType cType = plugin.getChallengeManager().getChallengeType(type);
                if (cType != null) {
                    return NumberUtil.format(user.getCompletedChallengesAmount(cType));
                }

                ChallengeJobType jobType = CollectionsUtil.getEnum(type, ChallengeJobType.class);
                if (jobType != null) {
                    return NumberUtil.format(user.getCompletedChallengesAmount(jobType));
                }
            }

            return null;
        }
    }
}
