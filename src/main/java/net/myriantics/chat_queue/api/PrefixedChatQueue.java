package net.myriantics.chat_queue.api;

import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.message.SentMessage;
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

        return ChatQueueCore.getCurrentTimeMillis() - lastSentTimeMillis < queueDelayMillis;
    }

    public long getTimeUntilNextSendMillis() {
        long time = queueDelayMillis - (ChatQueueCore.getCurrentTimeMillis() - lastSentTimeMillis);
        return time;
    }

    public boolean isTimeOutOfBounds() {
        long time = getTimeUntilNextSendMillis();
        return time <= 0 || time > queueDelayMillis;
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
    }

    @Override
    public void clear() {
        super.clear();
        // reset last sent time
        lastSentTimeMillis = -1;
        // unpauses itself after it's cleared because that seems apt
        //
        ChatQueueCore.updatePrimedQueue(null);
        this.setPaused(false);
    }

    @Override
    public String remove(int index) {
        String success = super.remove(index);

        // update last sent time
        this.updateLastSentTime();

        ChatQueueCore.updatePrimedQueue(null);
        return success;
    }

    @Override
    public boolean remove(Object o) {
        boolean success = super.remove(o);

        // update last sent time
        this.updateLastSentTime();

        ChatQueueCore.updatePrimedQueue(null);
        return success;
    }

    @Override
    public boolean add(String s) {
        boolean success = super.add(s);
        ChatQueueCore.updatePrimedQueue(null);
        return success;
    }
}
