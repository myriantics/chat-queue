package net.myriantics.chat_queue;

import net.minecraft.client.network.ClientPlayNetworkHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ChatQueueCore {

    public static HashMap<String, ArrayList<String>> PREFIXED_QUEUES = new HashMap<>();

    public static String RAW_CHAT_MESSAGE_PREFIX = "";

    public static final String[] VALID_MESSAGE_PREFIXES = {
            "shout",
            "pc"
    };

    // time is stored in milliseconds
    public static final Map<String, Long> QUEUE_DELAYS = Map.of(
            RAW_CHAT_MESSAGE_PREFIX, 3000L,
            VALID_MESSAGE_PREFIXES[0], 60000L,
            VALID_MESSAGE_PREFIXES[1], 1000L
    );

    public static HashMap<String, Long> QUEUE_LAST_SENT_TIMES = new HashMap<>();

    public static int clearAllQueues() {
        int clearedQueuesAmount = 0;
        // don't clear the map because it causes a crash - clear all values instead
        for (ArrayList<String> queue : PREFIXED_QUEUES.values()) {
            if (!queue.isEmpty()) {
                // update tracking variable - used in command feedback
                clearedQueuesAmount++;
                queue.clear();
            }
        }
        return clearedQueuesAmount;
    }

    public static void updateLastSentTime(String prefix) {
        QUEUE_LAST_SENT_TIMES.put(prefix, System.currentTimeMillis());
    }

    public static void addEntryToBaseQueue(String message) {
        addEntryToPrefixedQueue(RAW_CHAT_MESSAGE_PREFIX, message);
    }

    public static void addEntryToPrefixedQueue(String prefix, String message) {
        PREFIXED_QUEUES.get(prefix).add(message);
    }

    public static void sendNextQueuedMessage(String prefix, ClientPlayNetworkHandler handler) {
        if (prefix.isEmpty()) {
            handler.sendChatMessage(PREFIXED_QUEUES.get(prefix).remove(0));
        } else {
            handler.sendChatCommand(PREFIXED_QUEUES.get(prefix).remove(0));
        }
        ChatQueueClient.LOGGER.info("Queue Size: " + PREFIXED_QUEUES.get(prefix).size());
    }

    public static int clearSpecificPrefixedQueue(String prefix) {
        int size = PREFIXED_QUEUES.get(prefix).size();
        PREFIXED_QUEUES.get(prefix).clear();
        ChatQueueClient.LOGGER.info("Cleared prefixed queue " + (prefix.isEmpty() ? "raw_chat" : prefix));
        // Size is used in command feedback
        return size;
    }

    public static ArrayList<String> getPrefixedQueue(String prefix) {
        return PREFIXED_QUEUES.get(prefix);
    }

    /*
    public static boolean isModEnabledOnServer() {
        final ClientPlayNetworkHandler network = MinecraftClient.getInstance().getNetworkHandler();
        if(network == null || network.getServerInfo() == null) return false;

        final String address = network.getServerInfo().address;
        return address.endsWith("hoplite.gg") || address.contains(".hoplite");
    }
    */

    public static boolean isPrefixedQueueOnCooldown(String prefix) {
        // oopsy haha
        if (QUEUE_LAST_SENT_TIMES.get(prefix) == null || QUEUE_DELAYS.get(prefix) == null) return false;

        return (System.currentTimeMillis() - QUEUE_LAST_SENT_TIMES.get(prefix)) < QUEUE_DELAYS.get(prefix);
    }
}
