package su.nightexpress.excellentchallenges.config;

import org.bukkit.Sound;
import su.nexmedia.engine.api.lang.LangColors;
import su.nexmedia.engine.api.lang.LangKey;
import su.nexmedia.engine.lang.EngineLang;
import su.nightexpress.excellentchallenges.Placeholders;

public class Lang extends EngineLang implements LangColors {

    public static final LangKey COMMAND_OPEN_DESC  = new LangKey("Command.Open.Desc", "Open challenges GUI.");
    public static final LangKey COMMAND_OPEN_USAGE = new LangKey("Command.Open.Usage", "[type]");

    public static final LangKey COMMAND_RESET_DESC  = new LangKey("Command.Reset.Desc", "Resets current player's challenges.");
    public static final LangKey COMMAND_RESET_USAGE = new LangKey("Command.Reset.Usage", "<player> [type]");
    public static final LangKey COMMAND_RESET_DONE  = new LangKey("Command.Reset.Done", "Reset " + GREEN + Placeholders.GENERIC_TYPE + GRAY +" challenges for " + GREEN + Placeholders.Player.NAME + GRAY + "!");

    public static final LangKey COMMAND_REROLL_TOKENS_DESC      = LangKey.of("Command.RerollTokens.Desc", "Manage Reroll Tokens.");
    public static final LangKey COMMAND_REROLL_TOKENS_USAGE     = LangKey.of("Command.RerollTokens.Usage", "[help]");
    public static final LangKey COMMAND_REROLL_TOKENS_SUB_USAGE = LangKey.of("Command.RerollTokens.SubUsage", "<player> <type> <amount>");

    public static final LangKey COMMAND_REROLL_TOKENS_GIVE_DESC = LangKey.of("Command.RerollTokens.Give.Desc", "Give Reroll Tokens to a player.");
    public static final LangKey COMMAND_REROLL_TOKENS_GIVE_DONE = LangKey.of("Command.RerollTokens.Give.Done", "Given " + GREEN + "x" + Placeholders.GENERIC_AMOUNT + " Reroll Tokens " + GRAY + "to " + GREEN + Placeholders.Player.NAME + GRAY + "!");

    public static final LangKey COMMAND_REROLL_TOKENS_TAKE_DESC = LangKey.of("Command.RerollTokens.Take.Desc", "Take Reroll Tokens off a player.");
    public static final LangKey COMMAND_REROLL_TOKENS_TAKE_DONE = LangKey.of("Command.RerollTokens.Take.Done", "Taken " + GREEN + "x" + Placeholders.GENERIC_AMOUNT + " Reroll Tokens " + GRAY + "off " + GREEN + Placeholders.Player.NAME + GRAY + "!");

    public static final LangKey COMMAND_REROLL_TOKENS_SET_DESC = LangKey.of("Command.RerollTokens.Set.Desc", "Set Reroll Tokens for a player.");
    public static final LangKey COMMAND_REROLL_TOKENS_SET_DONE = LangKey.of("Command.RerollTokens.Set.Done", "Set " + GREEN + "x" + Placeholders.GENERIC_AMOUNT + " Reroll Tokens " + GRAY + "for " + GREEN + Placeholders.Player.NAME + GRAY + "!");

    public static final LangKey CHALLENGE_REROLL_DONE            = LangKey.of("Challenge.Reroll.Done", "<! sound:\"" + Sound.ENTITY_PLAYER_LEVELUP.name() + "\" !>" + GREEN + Placeholders.GENERIC_TYPE + GRAY + " challenges are rerolled!");
    public static final LangKey CHALLENGE_REROLL_ERROR_CONDITION = LangKey.of("Challenge.Reroll.Error.Condition", RED + "You can't reroll challenges now.");
    public static final LangKey CHALLENGE_REROLL_ERROR_NO_TOKENS = LangKey.of("Challenge.Reroll.Error.NoTokens", "You don't have " + RED + "Reroll Tokens" + GRAY + "!");

    public static final LangKey CHALLENGE_NOTIFY_PROGRESS  = new LangKey("Challenge.Notify.Progress", "<! type:\"action_bar\" !> " + GRAY + Placeholders.CHALLENGE_NAME + ": " + YELLOW + Placeholders.OBJECTIVE_NAME + " " + YELLOW + Placeholders.OBJECTIVE_PROGRESS_CURRENT + GRAY + "/" + YELLOW + Placeholders.OBJECTIVE_PROGRESS_MAX);
    public static final LangKey CHALLENGE_NOTIFY_COMPLETED = new LangKey("Challenge.Notify.Completed", "<! type:\"action_bar\" !> " + GRAY + Placeholders.CHALLENGE_NAME + ": " + GREEN + "&lCOMPLETED!");
}
