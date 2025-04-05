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


    public static final LangString COMMAND_CATEGORY_DESC = LangString.of("Command.Category.Desc",
        "View challenges.");

    public static final LangString COMMAND_CATEGORY_USAGE = LangString.of("Command.Category.Usage",
        "[player]");

    public static final LangString COMMAND_CATEGORY_DONE = LangString.of("Command.Category.Done",
        LIGHT_GRAY.wrap("Opened " + LIGHT_YELLOW.wrap(CATEGORY_NAME) + " challenges GUI for " + LIGHT_YELLOW.wrap(PLAYER_NAME) + "."));


    public static final LangString COMMAND_RESET_DESC = LangString.of("Command.Reset.Desc",
        "Resets current player's challenges.");

    public static final LangString COMMAND_RESET_USAGE = LangString.of("Command.Reset.Usage",
        "<player> [type]");

    public static final LangText COMMAND_RESET_DONE = LangText.of("Command.Reset.Done",
        LIGHT_GRAY.wrap("Reset " + LIGHT_YELLOW.wrap(GENERIC_TYPE) + " challenges for " + LIGHT_YELLOW.wrap(PLAYER_NAME) + "!"));



    public static final LangString COMMAND_RESET_ALL_DESC = LangString.of("Command.ResetAll.Desc",
        "Resets challenges for all players.");

    public static final LangString COMMAND_RESET_ALL_USAGE = LangString.of("Command.ResetAll.Usage",
        "[type]");

    public static final LangText COMMAND_RESET_ALL_DONE = LangText.of("Command.ResetAll.Done",
        LIGHT_GRAY.wrap("Reset " + LIGHT_YELLOW.wrap(GENERIC_TYPE) + " challenges for " + LIGHT_YELLOW.wrap("all players") + "!"));


    public static final LangString COMMAND_REROLL_TOKENS_DESC = LangString.of("Command.RerollTokens.Desc",
        "Manage Reroll Tokens.");

    public static final LangString COMMAND_REROLL_TOKENS_USAGE = LangString.of("Command.RerollTokens.Usage",
        "[help]");

    public static final LangString COMMAND_REROLL_TOKENS_SUB_USAGE = LangString.of("Command.RerollTokens.SubUsage",
        "<player> <category> <amount>");


    public static final LangString COMMAND_REROLL_TOKENS_GIVE_DESC = LangString.of("Command.RerollTokens.Give.Desc",
        "Give Reroll Tokens to a player.");

    public static final LangText COMMAND_REROLL_TOKENS_GIVE_DONE = LangText.of("Command.RerollTokens.Give.Done",
        LIGHT_GRAY.wrap("Given " + LIGHT_YELLOW.wrap("x" + GENERIC_AMOUNT + " Reroll Tokens ") + "to " + LIGHT_YELLOW.wrap(PLAYER_NAME) + "!"));


    public static final LangString COMMAND_REROLL_TOKENS_REMOVE_DESC = LangString.of("Command.RerollTokens.Take.Desc",
        "Take Reroll Tokens off a player.");

    public static final LangText COMMAND_REROLL_TOKENS_REMOVE_DONE = LangText.of("Command.RerollTokens.Take.Done",
        LIGHT_GRAY.wrap("Taken " + LIGHT_YELLOW.wrap("x" + GENERIC_AMOUNT + " Reroll Tokens ") + "off " + LIGHT_YELLOW.wrap(PLAYER_NAME) + "!"));


    public static final LangString COMMAND_REROLL_TOKENS_SET_DESC = LangString.of("Command.RerollTokens.Set.Desc",
        "Set Reroll Tokens for a player.");

    public static final LangText COMMAND_REROLL_TOKENS_SET_DONE = LangText.of("Command.RerollTokens.Set.Done",
        LIGHT_GRAY.wrap("Set " + LIGHT_YELLOW.wrap("x" + GENERIC_AMOUNT + " Reroll Tokens ") + "for " + LIGHT_YELLOW.wrap(PLAYER_NAME) + "!"));


    public static final LangText CHALLENGE_REROLL_DONE = LangText.of("Challenge.Reroll.Done",
        SOUND.wrap(Sound.ENTITY_PLAYER_LEVELUP) +
            LIGHT_GRAY.wrap(LIGHT_YELLOW.wrap(GENERIC_TYPE) + " challenges are rerolled!"));

    public static final LangText CHALLENGE_REROLL_ERROR_CONDITION = LangText.of("Challenge.Reroll.Error.Condition",
        LIGHT_GRAY.wrap("You can't reroll challenges now."));

    public static final LangText CHALLENGE_REROLL_ERROR_NO_TOKENS = LangText.of("Challenge.Reroll.Error.NoTokens",
        LIGHT_GRAY.wrap("You don't have " + LIGHT_RED.wrap("Reroll Tokens") + "!"));


    public static final LangText CHALLENGE_NOTIFY_PROGRESS = LangText.of("Challenge.Notify.Progress",
        OUTPUT.wrap(OutputType.ACTION_BAR) +
            LIGHT_GRAY.wrap(CHALLENGE_TYPE + ": " + LIGHT_YELLOW.wrap(OBJECTIVE_NAME + " " + OBJECTIVE_PROGRESS_CURRENT) + "/" + LIGHT_YELLOW.wrap(OBJECTIVE_PROGRESS_MAX)));


    public static final LangText CHALLENGE_NOTIFY_COMPLETED = LangText.of("Challenge.Notify.Completed",
        OUTPUT.wrap(OutputType.ACTION_BAR) +
            LIGHT_GRAY.wrap(CHALLENGE_TYPE + ": " + LIGHT_GREEN.wrap(BOLD.wrap("COMPLETED!"))));


    public static final LangText ERROR_CATEGORY_INVALID = LangText.of("Error.InvalidCategory",
        LIGHT_GRAY.wrap("Invalid challenges category!"));
}
