package net.myriantics.chat_queue;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.fabricmc.fabric.api.client.message.v1.ClientSendMessageEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

import java.util.ArrayList;

public class ChatQueueCore implements ClientTickEvents.StartTick, ClientSendMessageEvents.Chat, ClientReceiveMessageEvents.Game{

    private static boolean SkipNextSentMessage = false;

    private static ArrayList<String> UnconfirmedMessages = new ArrayList<>();

    private static ArrayList<String> QueuedMessages = new ArrayList<>();
    private static final Text MessageSendFailKey = Text.literal("second");
    private static long OldestQueuedMessageSentTimeMillis;
    private static int MessageDelayMillis = 3000;
    private static int UnsentMessageDecayTimerMillis = 20000;

    /*
    public ChatQueueCore() {
        ClientSendMessageEvents.CHAT.register(this);
        ClientReceiveMessageEvents.GAME.register(this);
    }*/

    @Override
    public void onStartTick(MinecraftClient client) {
        if(hasCoolDownExpired() && client.getNetworkHandler().getServerInfo() != null) {
            sendNextQueuedMessage(client);
        }
    }

    @Override
    public void onReceiveGameMessage(Text message, boolean overlay) {
        if(message.contains(Text.literal(UnconfirmedMessages.get(0)))) {
            UnconfirmedMessages.remove(0);
        } else if (message.contains(MessageSendFailKey)){
            TransferLatestMessageToQueue();
        }
    }

    @Override
    public void onSendChatMessage(String message) {
        if(!SkipNextSentMessage) {
            if(UnconfirmedMessages.isEmpty() || QueuedMessages.isEmpty()) {
                OldestQueuedMessageSentTimeMillis = System.currentTimeMillis();
            }
            UnconfirmedMessages.add(message);
        }
        SkipNextSentMessage = false;
    }

    private boolean sendNextQueuedMessage(MinecraftClient client) {
        if(client.player != null && !QueuedMessages.isEmpty()) {
            client.player.networkHandler.sendChatMessage(QueuedMessages.get(0));
            return true;
        }
        return false;
    }

    public static boolean hasCoolDownExpired() {
        return (System.currentTimeMillis() - OldestQueuedMessageSentTimeMillis) >= MessageDelayMillis;
    }

    private static void TransferLatestMessageToQueue() {
        QueuedMessages.add(UnconfirmedMessages.remove(0));
    }

    public ArrayList<String> getUnconfirmedMessages() {
        return UnconfirmedMessages;
    }

    public ArrayList<String> getQueuedMessages() {
        return QueuedMessages;
    }
}
