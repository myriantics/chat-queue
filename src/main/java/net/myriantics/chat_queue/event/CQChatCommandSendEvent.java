package net.myriantics.chat_queue.event;

import net.myriantics.chat_queue.ChatQueueClient;
import net.myriantics.chat_queue.ChatQueueCore;
import org.jetbrains.annotations.Nullable;

public class CQChatCommandSendEvent {

    public static boolean allowSendCommandMessage(String rawSentCommand) {
        String commandPrefix = getCommandPrefix(rawSentCommand);

        // if the prefix isn't recognized, we don't care about the command
        if (commandPrefix == null) return true;

        // if there's no valid command prefix, we don't need to do any more CQ processing
        // also check to see if prefix is on cooldown
        if (ChatQueueCore.isPrefixedQueueOnCooldown(commandPrefix)) {
            // add message to proper queue
            ChatQueueCore.addEntryToPrefixedQueue(commandPrefix, "/" + rawSentCommand);

            // we don't want this to go through if it's been added to the queue
            return false;
        }

        // Update last message sent time
        ChatQueueCore.updateLastSentTime(commandPrefix);

        return true;
    }

    public static @Nullable String getCommandPrefix(String rawSentCommand) {

        for (String testPrefix : ChatQueueCore.VALID_MESSAGE_PREFIXES) {
            if (prefixMatches(rawSentCommand, testPrefix)) {
                return testPrefix;
            }
        }

        return null;
    }

    private static boolean prefixMatches(String testString, String testPrefix) {
        char[] prefixArray = testPrefix.toCharArray();

        for (int i = 0; i < prefixArray.length; i++) {
            char testCharacter = prefixArray[i];

            // if at any point in the character array it doesnt match the test string prefix, return false
            if (testString.charAt(i) != testCharacter) return false;
        }

        // if it gets through the loop, the testString is prefixed with testPrefix
        return true;
    }
}
