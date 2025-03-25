package net.myriantics.chat_queue.command.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.myriantics.chat_queue.ChatQueueCore;
import net.myriantics.chat_queue.command.CQCommands;

import java.util.ArrayList;

public class GetSpecificQueueContentsCommand {
    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        dispatcher.register(
                ClientCommandManager.literal("getqueuecontents")
                        .then(ClientCommandManager.argument("target_queue_command_prefix", StringArgumentType.string())
                                .executes(context -> {
                                    String specifiedQueueParameter = StringArgumentType.getString(context, "target_queue_command_prefix");

                                    String targetQueuePrefix = specifiedQueueParameter.equals("raw_chat") ? "" : specifiedQueueParameter;

                                    ArrayList<String> selectedQueue = ChatQueueCore.getPrefixedQueue(targetQueuePrefix);

                                    // if there's no queue corresponding to string, then fail command
                                    if (selectedQueue == null) {
                                        context.getSource().sendError(Text.translatable("commands.get_specific_queue_contents.fail.missing", specifiedQueueParameter));
                                        return 0;
                                    }

                                    // if the selected queue is empty, fail
                                    if (selectedQueue.isEmpty()) {
                                        context.getSource().sendError(Text.translatable("commands.get_specific_queue_contents.fail.empty", specifiedQueueParameter));
                                        return 0;
                                    }


                                    // yoink the number of messages to fetch
                                    // can only display last 10 messages
                                    int fetchedMessages = Math.min(selectedQueue.size(), 10);

                                    // send corresponding chat message
                                    if (targetQueuePrefix.isEmpty()) {
                                        context.getSource().sendFeedback(Text.translatable("commands.get_specific_queue_contents.success.raw_chat", fetchedMessages));
                                    } else {
                                        context.getSource().sendFeedback(Text.translatable("commands.get_specific_queue_contents.success.prefix", fetchedMessages, targetQueuePrefix));
                                    }

                                    // dump all queued messages into chat
                                    // only display in bursts of 10 - also don't exceed selected queue's size
                                    for (int i = 0; i < fetchedMessages; i++) {
                                        String queuedMessage = selectedQueue.get(i);
                                        context.getSource().sendFeedback(Text.literal(queuedMessage));
                                    }

                                    return 1;
                                })
        ));
    }

}
