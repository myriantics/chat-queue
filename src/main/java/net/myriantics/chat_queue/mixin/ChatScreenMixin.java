package net.myriantics.chat_queue.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.myriantics.chat_queue.ChatQueueClient;
import net.myriantics.chat_queue.event.CQChatMessageSendEvent;
import net.myriantics.chat_queue.event.CQChatCommandSendEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ChatScreen.class)
public abstract class ChatScreenMixin {

    @WrapOperation(
            method = "sendMessage",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayNetworkHandler;sendChatCommand(Ljava/lang/String;)V")
    )
    private void chat_queue$manualChatCommandSendOverride(ClientPlayNetworkHandler instance, String command, Operation<Void> original) {
        if (CQChatCommandSendEvent.allowSendCommandMessage(command)) original.call(instance, command);
    }

    @WrapOperation(
            method = "sendMessage",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayNetworkHandler;sendChatMessage(Ljava/lang/String;)V")
    )
    private void chat_queue$manualChatMessageSendOverride(ClientPlayNetworkHandler instance, String message, Operation<Void> original) {
        if (CQChatMessageSendEvent.allowSendChatMessage(message)) original.call(instance, message);
    }
}
