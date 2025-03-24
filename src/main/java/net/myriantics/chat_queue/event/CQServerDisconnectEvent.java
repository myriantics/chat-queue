package net.myriantics.chat_queue.event;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.myriantics.chat_queue.ChatQueueCore;

public class CQServerDisconnectEvent implements ClientPlayConnectionEvents.Disconnect{
    @Override
    public void onPlayDisconnect(ClientPlayNetworkHandler handler, MinecraftClient client) {

        // clear all queues - we don't want this persisting between worlds / servers
        ChatQueueCore.clearAllQueues();
    }
}
