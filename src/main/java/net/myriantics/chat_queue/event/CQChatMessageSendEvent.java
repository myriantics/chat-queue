package net.myriantics.chat_queue.event;

import net.myriantics.chat_queue.ChatQueueClient;
import net.myriantics.chat_queue.ChatQueueCore;
import net.myriantics.chat_queue.api.PrefixedChatQueue;

import java.util.ArrayList;

public class CQChatMessageSendEvent {

    public static boolean allowSendChatMessage(String message) {
        PrefixedChatQueue queue = ChatQueueCore.getPrefixedQueue(ChatQueueCore.RAW_CHAT_MESSAGE_PREFIX);

        // null protection yay
        if (queue == null) return true;

        // only cancel and send message to queue if queue list is not empty
        if (queue.isOnCooldown()) {
            queue.add(message);
            return false;
        }

        // update last message sent time
        queue.updateLastSentTime();

        return true;
    }
}
