package su.nightexpress.excellentchallenges.config;

import su.nightexpress.excellentchallenges.Placeholders;
import su.nightexpress.nightcore.util.wrapper.UniPermission;

public class Perms {

    public static final String PREFIX         = "excellentchallenges.";
    public static final String PREFIX_COMMAND = PREFIX + "command.";
    public static final String PREFIX_CHALLENGES_AMOUNT = PREFIX + "amount.";

    public static final UniPermission PLUGIN  = new UniPermission(PREFIX + Placeholders.WILDCARD, "Access to all the plugin features.");
    public static final UniPermission COMMAND = new UniPermission(PREFIX_COMMAND + Placeholders.WILDCARD, "Access to all the plugin commands.");

    public static final UniPermission COMMAND_OPEN               = new UniPermission(PREFIX_COMMAND + "open", "Access to the 'open' sub-command.");
    public static final UniPermission COMMAND_RESET              = new UniPermission(PREFIX_COMMAND + "reset", "Access to the 'reset' sub-command.");
    public static final UniPermission COMMAND_REROLL_TOKENS      = new UniPermission(PREFIX_COMMAND + "rerolltokens", "Access to the 'rerolltokens' command (without sub-commands).");
    public static final UniPermission COMMAND_REROLL_TOKENS_GIVE = new UniPermission(PREFIX_COMMAND + "rerolltokens.give", "Access to the 'rerolltokens give' command");
    public static final UniPermission COMMAND_REROLL_TOKENS_SET    = new UniPermission(PREFIX_COMMAND + "rerolltokens.set", "Access to the 'rerolltokens set' command.");
    public static final UniPermission COMMAND_REROLL_TOKENS_REMOVE = new UniPermission(PREFIX_COMMAND + "rerolltokens.take", "Access to the 'rerolltokens take' command.");
    public static final UniPermission COMMAND_RELOAD               = new UniPermission(PREFIX_COMMAND + "reload", "Access to the 'reload' sub-command.");

    public static final UniPermission REROLL = new UniPermission(PREFIX + "reroll", "Allows to reroll challenges.");

    static {
        PLUGIN.addChildren(COMMAND, REROLL);

        COMMAND.addChildren(
            COMMAND_OPEN,
            COMMAND_REROLL_TOKENS,
            COMMAND_REROLL_TOKENS_GIVE,
            COMMAND_REROLL_TOKENS_SET,
            COMMAND_REROLL_TOKENS_REMOVE,
            COMMAND_RESET,
            COMMAND_RELOAD
        );
    }
}
