package net.myriantics.chat_queue.event;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.myriantics.chat_queue.ChatQueueCore;
import net.myriantics.chat_queue.api.PrefixedChatQueue;

public class CQStartClientTickEvent implements ClientTickEvents.StartTick{
    @Override
    public void onStartTick(MinecraftClient minecraftClient) {
        // silence, IDE warnings
        if (minecraftClient.player == null) return;

        for (PrefixedChatQueue queue : ChatQueueCore.getActiveChatQueues()) {

            // only process this if selected queue is not on cooldown & selected queue has entries
            if (queue.hasEntries() && !queue.isOnCooldown()) {

                // send the message / command - also removes message from list / map
                queue.sendNextMessage(minecraftClient.player.networkHandler);

                ChatQueueCore.updatePrimedQueue();

                // update last sent time
                queue.updateLastSentTime();
            }
        }
    }

}
