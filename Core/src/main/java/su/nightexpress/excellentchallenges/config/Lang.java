package su.nightexpress.excellentchallenges.config;

import org.bukkit.Sound;
import su.nexmedia.engine.api.lang.LangKey;
import su.nexmedia.engine.lang.EngineLang;

import static su.nexmedia.engine.utils.Colors.*;
import static su.nightexpress.excellentchallenges.Placeholders.*;

public class Lang extends EngineLang {

    public static final LangKey COMMAND_OPEN_DESC  = LangKey.of("Command.Open.Desc", "Open challenges GUI.");
    public static final LangKey COMMAND_OPEN_USAGE = LangKey.of("Command.Open.Usage", "[type]");

    public static final LangKey COMMAND_RESET_DESC  = LangKey.of("Command.Reset.Desc", "Resets current player's challenges.");
    public static final LangKey COMMAND_RESET_USAGE = LangKey.of("Command.Reset.Usage", "<player> [type]");
    public static final LangKey COMMAND_RESET_DONE  = LangKey.of("Command.Reset.Done", "Reset " + GREEN + GENERIC_TYPE + GRAY + " challenges for " + GREEN + PLAYER_NAME + GRAY + "!");

    public static final LangKey COMMAND_REROLL_TOKENS_DESC      = LangKey.of("Command.RerollTokens.Desc", "Manage Reroll Tokens.");
    public static final LangKey COMMAND_REROLL_TOKENS_USAGE     = LangKey.of("Command.RerollTokens.Usage", "[help]");
    public static final LangKey COMMAND_REROLL_TOKENS_SUB_USAGE = LangKey.of("Command.RerollTokens.SubUsage", "<player> <action> <amount>");

    public static final LangKey COMMAND_REROLL_TOKENS_GIVE_DESC = LangKey.of("Command.RerollTokens.Give.Desc", GRAY + "Give Reroll Tokens to a player.");
    public static final LangKey COMMAND_REROLL_TOKENS_GIVE_DONE = LangKey.of("Command.RerollTokens.Give.Done", GRAY + "Given " + GREEN + "x" + GENERIC_AMOUNT + " Reroll Tokens " + GRAY + "to " + GREEN + PLAYER_NAME + GRAY + "!");

    public static final LangKey COMMAND_REROLL_TOKENS_TAKE_DESC = LangKey.of("Command.RerollTokens.Take.Desc", GRAY + "Take Reroll Tokens off a player.");
    public static final LangKey COMMAND_REROLL_TOKENS_TAKE_DONE = LangKey.of("Command.RerollTokens.Take.Done", GRAY + "Taken " + GREEN + "x" + GENERIC_AMOUNT + " Reroll Tokens " + GRAY + "off " + GREEN + PLAYER_NAME + GRAY + "!");

    public static final LangKey COMMAND_REROLL_TOKENS_SET_DESC = LangKey.of("Command.RerollTokens.Set.Desc", GRAY + "Set Reroll Tokens for a player.");
    public static final LangKey COMMAND_REROLL_TOKENS_SET_DONE = LangKey.of("Command.RerollTokens.Set.Done", GRAY + "Set " + GREEN + "x" + GENERIC_AMOUNT + " Reroll Tokens " + GRAY + "for " + GREEN + PLAYER_NAME + GRAY + "!");

    public static final LangKey CHALLENGE_REROLL_DONE            = LangKey.of("Challenge.Reroll.Done", "<! sound:\"" + Sound.ENTITY_PLAYER_LEVELUP.name() + "\" !>" + GREEN + GENERIC_TYPE + GRAY + " challenges are rerolled!");
    public static final LangKey CHALLENGE_REROLL_ERROR_CONDITION = LangKey.of("Challenge.Reroll.Error.Condition", RED + "You can't reroll challenges now.");
    public static final LangKey CHALLENGE_REROLL_ERROR_NO_TOKENS = LangKey.of("Challenge.Reroll.Error.NoTokens", GRAY + "You don't have " + RED + "Reroll Tokens" + GRAY + "!");

    public static final LangKey CHALLENGE_NOTIFY_PROGRESS = LangKey.of("Challenge.Notify.Progress",
        "<! type:\"action_bar\" !> " + GRAY + CHALLENGE_TYPE + ": " + YELLOW + OBJECTIVE_NAME + " " + YELLOW + OBJECTIVE_PROGRESS_CURRENT + GRAY + "/" + YELLOW + OBJECTIVE_PROGRESS_MAX);

    public static final LangKey CHALLENGE_NOTIFY_COMPLETED = LangKey.of("Challenge.Notify.Completed",
        "<! type:\"action_bar\" !> " + GRAY + CHALLENGE_TYPE + ": " + GREEN + BOLD + "COMPLETED!");
}
