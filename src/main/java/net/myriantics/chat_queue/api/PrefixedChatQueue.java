package net.myriantics.chat_queue.api;

import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.myriantics.chat_queue.ChatQueueClient;
import net.myriantics.chat_queue.ChatQueueCore;

import java.util.ArrayList;

public class PrefixedChatQueue extends ArrayList<String> {

    private final long queueDelayMillis;
    private final String commandPrefix;

    private boolean isPaused = false;
    private long lastSentTimeMillis = -1;

    public PrefixedChatQueue(String prefix, long queueDelayMillis) {
        this.commandPrefix = prefix;
        this.queueDelayMillis = queueDelayMillis;
    }

    public boolean isOnCooldown() {
        // only can be on cooldown if it's actually sent a message
        if (lastSentTimeMillis == -1) return false;

        return System.currentTimeMillis() - lastSentTimeMillis < queueDelayMillis;
    }

    public void setPaused(boolean paused) {
        this.isPaused = paused;
    }

    public boolean isPaused() {
        return this.isPaused;
    }

    public boolean hasEntries() {
        return !this.isEmpty();
    }

    public void clear() {
        super.clear();
        // unpauses itself after it's cleared because that seems apt
        this.setPaused(false);
    }

    public String getCommandPrefix() {
        return this.commandPrefix;
    }

    public boolean prefixMatches(String commandPrefix) {
        return this.commandPrefix.equals(commandPrefix);
    }

    public void updateLastSentTime() {
        this.lastSentTimeMillis = System.currentTimeMillis();
    }

    public boolean isRawChatQueue() {
        return commandPrefix.equals(ChatQueueCore.RAW_CHAT_MESSAGE_PREFIX);
    }

    public void sendNextMessage(ClientPlayNetworkHandler handler) {
        if (this.isRawChatQueue()) {
            handler.sendChatMessage(this.remove(0));
        } else {
            handler.sendChatCommand(this.remove(0));
        }
        ChatQueueClient.LOGGER.info("Queue Size: " + this.size());
    }
}
