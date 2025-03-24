package net.myriantics.chat_queue.event;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.myriantics.chat_queue.ChatQueueCore;

public class CQStartClientTickEvent implements ClientTickEvents.StartTick{
    @Override
    public void onStartTick(MinecraftClient minecraftClient) {
        // silence, IDE warnings
        if (minecraftClient.player == null) return;

        for (String prefix : ChatQueueCore.PREFIXED_QUEUES.keySet()) {

            // only process this if selected queue is not on cooldown & selected queue has entries
            if (!ChatQueueCore.PREFIXED_QUEUES.get(prefix).isEmpty() && !ChatQueueCore.isPrefixedQueueOnCooldown(prefix)) {

                // send the message / command - also removes message from list / map
                ChatQueueCore.sendNextQueuedMessage(prefix, minecraftClient.player.networkHandler);

                // update last sent time
                ChatQueueCore.updateLastSentTime(prefix);
            }
        }
    }

}
