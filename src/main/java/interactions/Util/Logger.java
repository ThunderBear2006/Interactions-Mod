package interactions.Util;

import interactions.InteractionsMod;

import java.text.MessageFormat;

public class Logger {
    private static String ModStringID;

    public static void init(String modStringID) {
        ModStringID = modStringID;
    }

    /**
     * Prints a message to the console with ModStringID and the supplied type as a prefix
     * @param message The message, can be formatted with {0} {1}...
     * @param args A list of arguments for formatting
     */
    public static void print(String message, Object... args) {
        MessageFormat msg = new MessageFormat(message);
        System.out.println(ModStringID + "|" + msg.format(args));
    }

    /**
     * Prints a debug message with the ModStringID and DEBUG type as a prefix to the console.
     * Does nothing if InteractionsMod.DebugEnabled is False
     * @param message The message, can be formatted with {0} {1}...
     * @param args A list of arguments for formatting
     */
    public static void printDebug(String message, Object... args) {
        if (!InteractionsMod.settings.DebugMode)
            return;
        print(message, args);
    }

    public static void printError(String message, Object... args) {
        printError(message, new StackTraceElement[0], args);
    }

    public static void printError(String message, StackTraceElement[] stackTrace, Object... args) {
        MessageFormat msg = new MessageFormat(message);
        System.err.println(ModStringID + msg.format(args));
        for (StackTraceElement element : stackTrace) {
            System.err.println(element);
        }
    }
}
