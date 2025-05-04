package net.myriantics.chat_queue;

import net.myriantics.chat_queue.api.PrefixedChatQueue;

import java.util.*;

public class ChatQueueCore {

    private static final HashMap<String, PrefixedChatQueue> PREFIXED_CHAT_QUEUES = new HashMap<>();

    public static final String RAW_CHAT_MESSAGE_PREFIX = "";

    private static PrefixedChatQueue nextPrimedQueue;

    public static int clearAllQueues() {
        int clearedQueuesAmount = 0;
        // don't clear the map because it causes a crash - clear all values instead
        for (PrefixedChatQueue queue : PREFIXED_CHAT_QUEUES.values()) {
            if (queue.hasEntries()) {
                // update tracking variable - used in command feedback
                clearedQueuesAmount++;
                queue.clear();
            }
        }
        updatePrimedQueue();
        return clearedQueuesAmount;
    }

    public static int clearSpecificPrefixedQueue(String prefix) {
        PrefixedChatQueue queue = getPrefixedQueue(prefix);

        // null protection go brrrt
        if (queue == null) return 0;

        int size = queue.size();
        queue.clear();
        ChatQueueClient.LOGGER.info("Cleared prefixed queue " + (prefix.isEmpty() ? "raw_chat" : prefix));
        updatePrimedQueue();
        // Size is used in command feedback
        return size;
    }

    public static List<String> getValidCommandPrefixes() {
        // "" is omitted here because it causes problems if it's not
        return List.copyOf(PREFIXED_CHAT_QUEUES.keySet().stream().filter((prefix) -> !prefix.equals(RAW_CHAT_MESSAGE_PREFIX)).toList());
    }

    public static PrefixedChatQueue getPrefixedQueue(String commandPrefix) {
        return PREFIXED_CHAT_QUEUES.get(commandPrefix);
    }

    public static void registerChatQueue(String commandPrefix, PrefixedChatQueue queue) {
        PREFIXED_CHAT_QUEUES.put(commandPrefix, queue);
    }

    public static List<PrefixedChatQueue> getActiveChatQueues() {
        return List.copyOf(PREFIXED_CHAT_QUEUES.values());
    }

    public static String getNextQueuedMessage() {
        String nextQueuedMessage = null;

        if (nextPrimedQueue == null) return null;

        if (!nextPrimedQueue.isEmpty()) {
            String commandPrefix = nextPrimedQueue.isRawChatQueue() ? "" : nextPrimedQueue.getCommandPrefix() + " ";
            nextQueuedMessage = nextPrimedQueue.getTimeUntilNextSendMillis() + " " + commandPrefix + nextPrimedQueue.get(0);
        }

        return nextQueuedMessage;
    }

    public static void updatePrimedQueue() {
        long lowestQueueDelayMillis = Long.MAX_VALUE;
        PrefixedChatQueue selectedQueue = null;
        for (PrefixedChatQueue queue : getActiveChatQueues()) {
            if (queue.getTimeUntilNextSendMillis() < lowestQueueDelayMillis) {
                lowestQueueDelayMillis = queue.getTimeUntilNextSendMillis();
                selectedQueue = queue;
            }
        }

        if (selectedQueue != null && !selectedQueue.isEmpty()) {
            nextPrimedQueue = selectedQueue;
        } else {
            nextPrimedQueue = null;
        }

    }

    /*
    public static boolean isModEnabledOnServer() {
        final ClientPlayNetworkHandler network = MinecraftClient.getInstance().getNetworkHandler();
        if(network == null || network.getServerInfo() == null) return false;

        final String address = network.getServerInfo().address;
        return address.endsWith("hoplite.gg") || address.contains(".hoplite");
    }
    */
}
