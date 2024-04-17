package net.myriantics.chat_queue.events;

import net.fabricmc.fabric.api.client.message.v1.ClientSendMessageEvents;

public class ChatSentMessageRecorder implements ClientSendMessageEvents.Chat{
    @Override
    public void onSendChatMessage(String message) {
        //MessageSentChecker checker = new MessageSentChecker(message, System.currentTimeMillis());
    }
}
