package su.nightexpress.excellentchallenges;

import su.nexmedia.engine.api.server.JPermission;

public class Perms {

    private static final String PREFIX         = "excellentchallenges.";
    private static final String PREFIX_COMMAND = PREFIX + "command.";

    public static final JPermission PLUGIN  = new JPermission(PREFIX + Placeholders.WILDCARD, "Access to all the plugin features.");
    public static final JPermission COMMAND = new JPermission(PREFIX_COMMAND + Placeholders.WILDCARD, "Access to all the plugin commands.");

    public static final JPermission COMMAND_OPEN               = new JPermission(PREFIX_COMMAND + "open", "Access to the 'open' sub-command.");
    public static final JPermission COMMAND_RESET              = new JPermission(PREFIX_COMMAND + "reset", "Access to the 'reset' sub-command.");
    public static final JPermission COMMAND_REROLL_TOKENS      = new JPermission(PREFIX_COMMAND + "rerolltokens", "Access to the 'rerolltokens' command (without sub-commands).");
    public static final JPermission COMMAND_REROLL_TOKENS_GIVE = new JPermission(PREFIX_COMMAND + "rerolltokens.give", "Access to the 'rerolltokens give' command");
    public static final JPermission COMMAND_REROLL_TOKENS_SET  = new JPermission(PREFIX_COMMAND + "rerolltokens.set", "Access to the 'rerolltokens set' command.");
    public static final JPermission COMMAND_REROLL_TOKENS_TAKE = new JPermission(PREFIX_COMMAND + "rerolltokens.take", "Access to the 'rerolltokens take' command.");
    public static final JPermission COMMAND_RELOAD             = new JPermission(PREFIX_COMMAND + "reload", "Access to the 'reload' sub-command.");

    public static final JPermission REROLL = new JPermission(PREFIX + "reroll", "Allows to reroll challenges.");

    static {
        PLUGIN.addChildren(COMMAND, REROLL);

        COMMAND.addChildren(
            COMMAND_OPEN,
            COMMAND_REROLL_TOKENS, COMMAND_REROLL_TOKENS_GIVE, COMMAND_REROLL_TOKENS_SET, COMMAND_REROLL_TOKENS_TAKE,
            COMMAND_RESET,
            COMMAND_RELOAD
        );
    }
}
