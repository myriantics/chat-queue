package net.myriantics.chat_queue.events;

import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.minecraft.text.Text;
import net.myriantics.chat_queue.ChatQueueCore;

public class RecievedMessageProcessor implements ClientReceiveMessageEvents.Game{
    @Override
    public void onReceiveGameMessage(Text message, boolean overlay) {
    }
}
