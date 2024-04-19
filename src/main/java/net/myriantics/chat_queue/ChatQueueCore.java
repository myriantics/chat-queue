package net.myriantics.chat_queue;

import com.mojang.authlib.GameProfile;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.fabricmc.fabric.api.client.message.v1.ClientSendMessageEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.message.MessageType;
import net.minecraft.network.message.SignedMessage;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;
import java.util.ArrayList;

public class ChatQueueCore implements ClientTickEvents.StartTick, ClientSendMessageEvents.Chat, ClientReceiveMessageEvents.Game, ClientReceiveMessageEvents.Chat{

    private static boolean SkipNextSentMessage = false;

    private static ArrayList<String> UnconfirmedMessages = new ArrayList<>();

    private static ArrayList<String> QueuedMessages = new ArrayList<>();
    private static final Text MessageSendFailKey = Text.literal("second");
    private static long OldestQueuedMessageSentTimeMillis;
    private static final int MessageDelayMillis = 3000;
    //private static final int UnsentMessageDecayTimerMillis = 20000;


    @Override
    public void onReceiveGameMessage(Text message, boolean overlay) {
        if(!overlay /*add another check for message fail key here (reference chat filter mods)*/) {
            sendDebugMessage("Recieved fail-confirm message!");
        }
        /*if(message.contains(Text.literal(UnconfirmedMessages.get(0)))) {
            UnconfirmedMessages.remove(0);
        } else if (message.contains(MessageSendFailKey)){
            TransferLatestMessageToQueue();
        }*/
    }

    @Override
    public void onReceiveChatMessage(Text message, @Nullable SignedMessage signedMessage, @Nullable GameProfile sender, MessageType.Parameters params, Instant receptionTimestamp) {
        sendDebugMessage("Recieved chat message!");
        /*if(message.contains(Text.literal(UnconfirmedMessages.get(0)))) {
            UnconfirmedMessages.remove(0);
        }*/
    }

    @Override
    public void onSendChatMessage(String message) {
        sendDebugMessage("Sent chat message!");
        /*if(!SkipNextSentMessage && isModEnabledOnServer()) {
            if(UnconfirmedMessages.isEmpty() || QueuedMessages.isEmpty()) {
                OldestQueuedMessageSentTimeMillis = System.currentTimeMillis();
            }
            UnconfirmedMessages.add(message);
        }
        SkipNextSentMessage = false;*/
    }

    public static boolean sendNextQueuedMessage(MinecraftClient client) {
        if(client.player != null && !QueuedMessages.isEmpty()) {
            client.player.networkHandler.sendChatMessage(QueuedMessages.get(0));
            return true;
        }
        return false;
    }

    public static boolean hasCoolDownExpired() {
        return (System.currentTimeMillis() - OldestQueuedMessageSentTimeMillis) >= MessageDelayMillis;
    }

    public static void TransferLatestMessageToQueue() {
        QueuedMessages.add(UnconfirmedMessages.remove(0));
    }

    public static ArrayList<String> getUnconfirmedMessages() {
        return UnconfirmedMessages;
    }

    public static ArrayList<String> getQueuedMessages() {
        return QueuedMessages;
    }

    public static boolean isModEnabledOnServer() {
        final ClientPlayNetworkHandler network = MinecraftClient.getInstance().getNetworkHandler();
        if(network == null || network.getServerInfo() == null) return false;

        final String address = network.getServerInfo().address;
        return address.endsWith("hoplite.gg") || address.contains(".hoplite");
    }

    @Override
    public void onStartTick(MinecraftClient client) {
        if(hasCoolDownExpired() && client.getNetworkHandler() != null) {
            sendNextQueuedMessage(client);
        }
    }

    private static void sendDebugMessage(String message) {
        MinecraftClient.getInstance().player.sendMessage(Text.literal(message));
    }
}
