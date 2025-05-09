package net.myriantics.chat_queue.event;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.myriantics.chat_queue.ChatQueueCore;
import net.myriantics.chat_queue.api.PrefixedChatQueue;

import java.util.ArrayList;

public class CQServerJoinEvent implements ClientPlayConnectionEvents.Join {
    @Override
    public void onPlayReady(ClientPlayNetworkHandler handler, PacketSender sender, MinecraftClient client) {
        // initialize queues
        /*for (String string : ChatQueueCore.QUEUE_DELAYS.keySet()) {
            ChatQueueCore.PREFIXED_QUEUES.put(string, new ArrayList<>());
        }*/

        ChatQueueCore.registerChatQueue(ChatQueueCore.RAW_CHAT_MESSAGE_PREFIX, new PrefixedChatQueue(ChatQueueCore.RAW_CHAT_MESSAGE_PREFIX, 3000L));
        ChatQueueCore.registerChatQueue("shout",new PrefixedChatQueue("shout", 60000L));
        ChatQueueCore.registerChatQueue("pc", new PrefixedChatQueue("pc", 1000L));
    }
}
