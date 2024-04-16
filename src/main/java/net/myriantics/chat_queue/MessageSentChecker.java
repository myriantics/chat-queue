package net.myriantics.chat_queue;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;

public class MessageSentChecker implements ClientReceiveMessageEvents.Game, ClientTickEvents.StartTick{

    private final String StoredMessage;
    private long MessageSentTime;
    private final int delayMillis = 3000;
    private boolean MessageSendConfirmed = false;
    private boolean MessageFailConfirmed = false;
    private final Text MessageSendFailKey = Text.literal("second");

    public MessageSentChecker(String message) {
        ClientReceiveMessageEvents.GAME.register(this);
        ClientTickEvents.START_CLIENT_TICK.register(this);

        this.StoredMessage = message;
        resetSentTime();
    }

    @Override
    public void onReceiveGameMessage(Text message, boolean overlay) {
        if(!MessageSendConfirmed && !MessageFailConfirmed) {
            if(message.contains(Text.literal(this.getMessage()))) {
                this.MessageSendConfirmed = true;
            } else if (message.contains(MessageSendFailKey)){
                this.MessageFailConfirmed = true;
            }
        }
    }

    public String getMessage() {
        return this.StoredMessage;
    }

    private boolean hasCoolDownExpired() {
        return (System.currentTimeMillis() - this.MessageSentTime) >= delayMillis;
    }

    private void resetSentTime() {
        this.MessageSentTime = System.currentTimeMillis();
    }

    @Override
    public void onStartTick(MinecraftClient client) {
        if(!MessageSendConfirmed && client.player != null && this.StoredMessage != null) {
            if(MessageFailConfirmed && hasCoolDownExpired()){
                resetSentTime();
                MinecraftClient.getInstance().player.networkHandler.sendChatMessage(StoredMessage);
            }
        }
    }
}
