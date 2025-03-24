package net.myriantics.chat_queue.command.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.myriantics.chat_queue.ChatQueueCore;
import net.myriantics.chat_queue.command.CQCommands;

public class ClearAllQueuesCommand {
    // Clears all queues
    // Run with no args - clears all queues
    // Run with string arg - clears specified queue
    // Run with list arg - clears all specified queues
    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        dispatcher.register(
                ClientCommandManager.literal("clearallqueues")
                        .executes(context -> {
                            int clearedQueuesAmount = ChatQueueCore.clearAllQueues();

                            if (clearedQueuesAmount > 0) {
                                CQCommands.sendClientChatMessage(MinecraftClient.getInstance(), Text.translatable("commands.clear_all_queues.success", clearedQueuesAmount));
                                return 1;
                            }

                            CQCommands.sendClientChatMessage(MinecraftClient.getInstance(), Text.translatable("commands.clear_all_queues.fail"));
                            return 0;
                        })
        );
    }
}
