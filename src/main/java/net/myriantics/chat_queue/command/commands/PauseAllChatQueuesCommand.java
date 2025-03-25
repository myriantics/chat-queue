package net.myriantics.chat_queue.command.commands;

import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.text.Text;
import net.myriantics.chat_queue.ChatQueueCore;

public class PauseAllChatQueuesCommand {

    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        dispatcher.register(
                ClientCommandManager.literal("pauseallqueues")
                        .executes(context -> {
                            int clearedQueuesAmount = ChatQueueCore.clearAllQueues();

                            if (clearedQueuesAmount > 0) {
                                context.getSource().sendFeedback(Text.translatable("commands.clear_all_queues.success", clearedQueuesAmount));
                                return 1;
                            }

                            context.getSource().sendError(Text.translatable("commands.clear_all_queues.fail"));
                            return 0;
                        })
        );
    }
}
