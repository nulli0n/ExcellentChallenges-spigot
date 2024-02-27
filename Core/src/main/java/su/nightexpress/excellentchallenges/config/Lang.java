package su.nightexpress.excellentchallenges.config;

import org.bukkit.Sound;
import su.nightexpress.nightcore.core.CoreLang;
import su.nightexpress.nightcore.language.entry.LangString;
import su.nightexpress.nightcore.language.entry.LangText;
import su.nightexpress.nightcore.language.message.OutputType;

import static su.nightexpress.nightcore.util.text.tag.Tags.*;
import static su.nightexpress.nightcore.language.tag.MessageTags.*;
import static su.nightexpress.excellentchallenges.Placeholders.*;

public class Lang extends CoreLang {

    public static final LangString COMMAND_OPEN_DESC = LangString.of("Command.Open.Desc",
        "Open challenges GUI.");

    public static final LangString COMMAND_OPEN_USAGE = LangString.of("Command.Open.Usage",
        "[type]");


    public static final LangString COMMAND_RESET_DESC = LangString.of("Command.Reset.Desc",
        "Resets current player's challenges.");

    public static final LangString COMMAND_RESET_USAGE = LangString.of("Command.Reset.Usage",
        "<player> [type]");

    public static final LangText COMMAND_RESET_DONE = LangText.of("Command.Reset.Done",
        LIGHT_GRAY.enclose("Reset " + LIGHT_YELLOW.enclose(GENERIC_TYPE) + " challenges for " + LIGHT_YELLOW.enclose(PLAYER_NAME) + "!"));


    public static final LangString COMMAND_REROLL_TOKENS_DESC = LangString.of("Command.RerollTokens.Desc",
        "Manage Reroll Tokens.");

    public static final LangString COMMAND_REROLL_TOKENS_USAGE = LangString.of("Command.RerollTokens.Usage",
        "[help]");

    public static final LangString COMMAND_REROLL_TOKENS_SUB_USAGE = LangString.of("Command.RerollTokens.SubUsage",
        "<player> <category> <amount>");


    public static final LangString COMMAND_REROLL_TOKENS_GIVE_DESC = LangString.of("Command.RerollTokens.Give.Desc",
        "Give Reroll Tokens to a player.");

    public static final LangText COMMAND_REROLL_TOKENS_GIVE_DONE = LangText.of("Command.RerollTokens.Give.Done",
        LIGHT_GRAY.enclose("Given " + LIGHT_YELLOW.enclose("x" + GENERIC_AMOUNT + " Reroll Tokens ") + "to " + LIGHT_YELLOW.enclose(PLAYER_NAME) + "!"));


    public static final LangString COMMAND_REROLL_TOKENS_REMOVE_DESC = LangString.of("Command.RerollTokens.Take.Desc",
        "Take Reroll Tokens off a player.");

    public static final LangText COMMAND_REROLL_TOKENS_REMOVE_DONE = LangText.of("Command.RerollTokens.Take.Done",
        LIGHT_GRAY.enclose("Taken " + LIGHT_YELLOW.enclose("x" + GENERIC_AMOUNT + " Reroll Tokens ") + "off " + LIGHT_YELLOW.enclose(PLAYER_NAME) + "!"));


    public static final LangString COMMAND_REROLL_TOKENS_SET_DESC = LangString.of("Command.RerollTokens.Set.Desc",
        "Set Reroll Tokens for a player.");

    public static final LangText COMMAND_REROLL_TOKENS_SET_DONE = LangText.of("Command.RerollTokens.Set.Done",
        LIGHT_GRAY.enclose("Set " + LIGHT_YELLOW.enclose("x" + GENERIC_AMOUNT + " Reroll Tokens ") + "for " + LIGHT_YELLOW.enclose(PLAYER_NAME) + "!"));


    public static final LangText CHALLENGE_REROLL_DONE = LangText.of("Challenge.Reroll.Done",
        SOUND.enclose(Sound.ENTITY_PLAYER_LEVELUP) +
            LIGHT_GRAY.enclose(LIGHT_YELLOW.enclose(GENERIC_TYPE) + " challenges are rerolled!"));

    public static final LangText CHALLENGE_REROLL_ERROR_CONDITION = LangText.of("Challenge.Reroll.Error.Condition",
        LIGHT_GRAY.enclose("You can't reroll challenges now."));

    public static final LangText CHALLENGE_REROLL_ERROR_NO_TOKENS = LangText.of("Challenge.Reroll.Error.NoTokens",
        LIGHT_GRAY.enclose("You don't have " + LIGHT_RED.enclose("Reroll Tokens") + "!"));


    public static final LangText CHALLENGE_NOTIFY_PROGRESS = LangText.of("Challenge.Notify.Progress",
        OUTPUT.enclose(OutputType.ACTION_BAR) +
            LIGHT_GRAY.enclose(CHALLENGE_TYPE + ": " + LIGHT_YELLOW.enclose(OBJECTIVE_NAME + " " + OBJECTIVE_PROGRESS_CURRENT) + "/" + LIGHT_YELLOW.enclose(OBJECTIVE_PROGRESS_MAX)));


    public static final LangText CHALLENGE_NOTIFY_COMPLETED = LangText.of("Challenge.Notify.Completed",
        OUTPUT.enclose(OutputType.ACTION_BAR) +
            LIGHT_GRAY.enclose(CHALLENGE_TYPE + ": " + LIGHT_GREEN.enclose(BOLD.enclose("COMPLETED!"))));


    public static final LangText ERROR_CATEGORY_INVALID = LangText.of("Error.InvalidCategory",
        LIGHT_GRAY.enclose("Invalid challenges category!"));
}
