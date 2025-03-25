package net.myriantics.chat_queue.command;

import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.text.Text;
import net.myriantics.chat_queue.command.commands.ClearAllQueuesCommand;
import net.myriantics.chat_queue.command.commands.ClearSpecificQueueCommand;
import net.myriantics.chat_queue.command.commands.GetSpecificQueueContentsCommand;

public class CQCommands implements ClientCommandRegistrationCallback{

    @Override
    public void register(CommandDispatcher<FabricClientCommandSource> dispatcher, CommandRegistryAccess registryAccess) {
        ClearAllQueuesCommand.register(dispatcher);
        ClearSpecificQueueCommand.register(dispatcher);
        GetSpecificQueueContentsCommand.register(dispatcher);
    }
}
