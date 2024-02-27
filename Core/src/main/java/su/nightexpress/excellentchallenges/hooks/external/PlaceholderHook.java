package su.nightexpress.excellentchallenges.hooks.external;

import su.nightexpress.excellentchallenges.challenge.ChallengeCategory;
import su.nightexpress.excellentchallenges.challenge.action.ActionType;
import su.nightexpress.excellentchallenges.data.object.ChallengeUser;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentchallenges.ExcellentChallengesPlugin;
import su.nightexpress.nightcore.util.NumberUtil;

public class PlaceholderHook {

    private static Expansion expansion;

    public static void setup(@NotNull ExcellentChallengesPlugin plugin) {
        if (expansion == null) {
            expansion = new Expansion(plugin);
            expansion.register();
        }
    }

    public static void shutdown() {
        if (expansion != null) {
            expansion.unregister();
            expansion = null;
        }
    }

    private static class Expansion extends PlaceholderExpansion {

        private final ExcellentChallengesPlugin plugin;

        public Expansion(@NotNull ExcellentChallengesPlugin plugin) {
            this.plugin = plugin;
        }

        @Override
        @NotNull
        public String getAuthor() {
            return plugin.getDescription().getAuthors().get(0);
        }

        @Override
        @NotNull
        public String getIdentifier() {
            return plugin.getDescription().getName().toLowerCase();
        }

        @Override
        @NotNull
        public String getVersion() {
            return plugin.getDescription().getVersion();
        }

        @Override
        public boolean persist() {
            return true;
        }

        @Override
        public String onPlaceholderRequest(Player player, @NotNull String params) {
            if (player == null) return null;

            ChallengeUser user = plugin.getUserManager().getUserData(player);

            if (params.startsWith("progress_")) {
                String type = params.replace("progress_", "");
                ChallengeCategory cType = plugin.getChallengeManager().getChallengeType(type);
                if (cType == null) return null;


                return NumberUtil.format(user.getProgressPercent(cType));
            }

            if (params.startsWith("reroll_tokens_")) {
                String type = params.substring("reroll_tokens_".length());
                ChallengeCategory cType = plugin.getChallengeManager().getChallengeType(type);
                if (cType == null) return null;

                return NumberUtil.format(user.getRerollTokens(cType));
            }

            if (params.startsWith("completed_")) {
                String type = params.replace("completed_", "");
                if (type.equalsIgnoreCase("all")) {
                    return NumberUtil.format(user.getCompletedChallengesAmount());
                }

                ChallengeCategory category = plugin.getChallengeManager().getChallengeType(type);
                if (category != null) {
                    return NumberUtil.format(user.getCompletedChallengesAmount(category));
                }

                ActionType<?, ?> actionType = plugin.getActionRegistry().getActionType(type);
                if (actionType != null) {
                    return NumberUtil.format(user.getCompletedChallengesAmount(actionType));
                }
            }

            return null;
        }
    }
}
