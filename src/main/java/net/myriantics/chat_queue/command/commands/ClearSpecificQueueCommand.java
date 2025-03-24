package net.myriantics.chat_queue.command.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.myriantics.chat_queue.ChatQueueCore;
import net.myriantics.chat_queue.command.CQCommands;

import java.util.ArrayList;

public class ClearSpecificQueueCommand {
    // Clears specified queue
    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        dispatcher.register(
                 ClientCommandManager.literal("clearspecificqueue")
                         .then(ClientCommandManager.argument("target_queue_command_prefix", StringArgumentType.string())
                                 .executes(context -> {
                                     String specifiedQueueParameter = StringArgumentType.getString(context, "target_queue_command_prefix");

                                     String targetQueuePrefix = specifiedQueueParameter.equals("raw_chat") ? "" : specifiedQueueParameter;

                                     ArrayList<String> selectedQueue = ChatQueueCore.getPrefixedQueue(targetQueuePrefix);

                                     // if there's no queue corresponding to string, then fail command
                                     if (selectedQueue == null) {
                                         CQCommands.sendClientChatMessage(MinecraftClient.getInstance(), Text.translatable("commands.clear_specific_queue.fail.missing", targetQueuePrefix));
                                         return 0;
                                     }

                                     // if the selected queue is empty, fail
                                     if (selectedQueue.isEmpty()) {
                                         CQCommands.sendClientChatMessage(MinecraftClient.getInstance(), Text.translatable("commands.clear_specific_queue.fail.empty", specifiedQueueParameter));
                                         return 0;
                                     }

                                     // clear messages in target queue and note down number of cleared messages
                                     int clearedMessages = ChatQueueCore.clearSpecificPrefixedQueue(targetQueuePrefix);

                                     // send corresponding chat message
                                     if (targetQueuePrefix.isEmpty()) {
                                         CQCommands.sendClientChatMessage(MinecraftClient.getInstance(), Text.translatable("commands.clear_specific_queue.success.raw_chat", clearedMessages));
                                     } else {
                                         CQCommands.sendClientChatMessage(MinecraftClient.getInstance(), Text.translatable("commands.clear_specific_queue.success.prefix", targetQueuePrefix, clearedMessages));
                                     }

                                     return 1;
                                 }))
        );
    }
}
