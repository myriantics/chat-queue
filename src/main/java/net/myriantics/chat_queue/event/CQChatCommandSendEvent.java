package net.myriantics.chat_queue.event;

import net.myriantics.chat_queue.ChatQueueClient;
import net.myriantics.chat_queue.ChatQueueCore;
import net.myriantics.chat_queue.api.PrefixedChatQueue;
import org.jetbrains.annotations.Nullable;

public class CQChatCommandSendEvent {

    public static boolean allowSendCommandMessage(String rawSentCommand) {
        PrefixedChatQueue queue = getCommandPrefix(rawSentCommand);

        // if the prefix isn't recognized, we don't care about the command
        if (queue == null) return true;

        // if there's no valid command prefix, we don't need to do any more CQ processing
        // also check to see if prefix is on cooldown
        if (queue.isOnCooldown()) {
            // add message to proper queue
            queue.add("/" + rawSentCommand);

            // we don't want this to go through if it's been added to the queue
            return false;
        }

        // Update last message sent time
        queue.updateLastSentTime();

        return true;
    }

    public static @Nullable PrefixedChatQueue getCommandPrefix(String rawSentCommand) {

        for (String testPrefix : ChatQueueCore.getValidCommandPrefixes()) {
            if (prefixMatches(rawSentCommand, testPrefix)) {
                return ChatQueueCore.getPrefixedQueue(testPrefix);
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
