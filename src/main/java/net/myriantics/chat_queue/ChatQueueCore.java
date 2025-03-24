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

    public static void clearAllQueues() {
        PREFIXED_QUEUES.clear();
    }

    public static void updateLastSentTime(String prefix) {
        QUEUE_LAST_SENT_TIMES.put(prefix, System.currentTimeMillis());
    }

    public static void addEntryToBaseQueue(String message) {
        addEntryToPrefixedQueue(RAW_CHAT_MESSAGE_PREFIX, message);
    }

    public static void addEntryToPrefixedQueue(String prefix, String message) {
        ChatQueueClient.LOGGER.info("Added message to queue! " + message);
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
