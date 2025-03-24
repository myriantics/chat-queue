package net.myriantics.chat_queue.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.CommandRegistryAccess;
import net.myriantics.chat_queue.command.commands.ClearAllQueuesCommand;
import net.myriantics.chat_queue.command.commands.ClearQueueCommand;

public class CQCommands implements ClientCommandRegistrationCallback{

    @Override
    public void register(CommandDispatcher<FabricClientCommandSource> dispatcher, CommandRegistryAccess registryAccess) {
        ClearAllQueuesCommand.register(dispatcher);
        ClearQueueCommand.register(dispatcher);
    }
}
