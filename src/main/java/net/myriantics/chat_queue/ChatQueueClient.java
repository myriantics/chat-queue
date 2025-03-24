package net.myriantics.chat_queue;

import net.fabricmc.api.ClientModInitializer;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.message.v1.ClientSendMessageEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.myriantics.chat_queue.event.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ChatQueueClient implements ClientModInitializer {
	public static final String MOD_ID = "chat_queue";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitializeClient() {
		ClientTickEvents.START_CLIENT_TICK.register(new CQStartClientTickEvent());
		ClientPlayConnectionEvents.JOIN.register(new CQServerJoinEvent());
		ClientPlayConnectionEvents.DISCONNECT.register(new CQServerDisconnectEvent());


		LOGGER.info("ChatQueue has initialized!");
	}
}