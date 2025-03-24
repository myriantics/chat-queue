package net.myriantics.chat_queue.event;

import net.myriantics.chat_queue.ChatQueueClient;
import net.myriantics.chat_queue.ChatQueueCore;

import java.util.ArrayList;

public class CQChatMessageSendEvent {

    public static boolean allowSendChatMessage(String message) {
        // only cancel and send message to queue if queue list is not empty
        if (ChatQueueCore.isPrefixedQueueOnCooldown(ChatQueueCore.RAW_CHAT_MESSAGE_PREFIX)) {
            ChatQueueCore.addEntryToBaseQueue(message);

            return false;
        }

        // update last message sent time
        ChatQueueCore.updateLastSentTime(ChatQueueCore.RAW_CHAT_MESSAGE_PREFIX);

        return true;
    }
}
