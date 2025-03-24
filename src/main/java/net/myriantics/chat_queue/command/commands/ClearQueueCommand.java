package net.myriantics.chat_queue.command.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.network.ClientCommandSource;
import net.minecraft.command.argument.ArgumentTypes;
import net.minecraft.network.message.SentMessage;
import net.minecraft.server.command.CommandManager;
import net.myriantics.chat_queue.ChatQueueClient;
import net.myriantics.chat_queue.ChatQueueCore;

import java.util.ArrayList;

public class ClearQueueCommand {
    // Clears specified queue
    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        dispatcher.register(
                 ClientCommandManager.literal("clearqueue")
                         .then(ClientCommandManager.argument("specific_queue", StringArgumentType.string())
                                 .executes(context -> {
                                     String input = context.getInput().equals("base") ? "" : context.getInput();

                                     if (ChatQueueCore.getPrefixedQueue(input) != null) {
                                         ChatQueueCore.getPrefixedQueue(input);
                                         return 1;
                                     }
                                     return 0;
                                 }
                                 ))
        );
    }
}
