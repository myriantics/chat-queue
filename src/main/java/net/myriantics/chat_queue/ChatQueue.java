package net.myriantics.chat_queue;

import net.fabricmc.api.ClientModInitializer;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.fabricmc.fabric.api.client.message.v1.ClientSendMessageEvents;
import net.minecraft.client.MinecraftClient;
import net.myriantics.chat_queue.events.ChatSentMessageRecorder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ChatQueue implements ClientModInitializer {
	public static final String MOD_ID = "chat_queue";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitializeClient() {
		ClientTickEvents.START_CLIENT_TICK.register(new ChatQueueCore());
		ClientReceiveMessageEvents.GAME.register(new ChatQueueCore());
		ClientSendMessageEvents.CHAT.register(new ChatQueueCore());
	}
}