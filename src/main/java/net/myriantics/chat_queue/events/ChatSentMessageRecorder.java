package net.myriantics.chat_queue.events;

import net.fabricmc.fabric.api.client.message.v1.ClientSendMessageEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.myriantics.chat_queue.MessageSentChecker;

public class ChatSentMessageRecorder implements ClientSendMessageEvents.Chat{
    @Override
    public void onSendChatMessage(String message) {
        new MessageSentChecker(message);
    }
}
