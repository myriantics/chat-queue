package net.myriantics.chat_queue.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.ChatHud;
import net.myriantics.chat_queue.ChatQueueCore;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Debug(export = true)
@Mixin(ChatHud.class)
public abstract class ChatHudMixin {
    @Shadow protected abstract int getLineHeight();

    @ModifyExpressionValue(
            method = "render",
            at = @At(value = "INVOKE", target = "Ljava/util/List;size()I",
            ordinal = 0)
    )
    private int chat_queue$breakIntoChatRenderingMethod(int original) {
        if (original > 0) return original;

        return ChatQueueCore.getNextQueuedMessage() != null ? 1 : original;
    }

    @ModifyExpressionValue(
            method = "render",
            at = @At(value = "INVOKE", target = "Ljava/lang/Math;round(D)J")
    )
    private long chat_queue$renderNextQueuedMessage(long original, @Local(argsOnly = true) DrawContext context, @Local(ordinal = 7) int yOffset) {
        if (ChatQueueCore.getNextQueuedMessage() != null) {
            context.drawTextWithShadow(MinecraftClient.getInstance().textRenderer, ChatQueueCore.getNextQueuedMessage(), 0, (int) (yOffset + original + getLineHeight()), 16777215);
        }
        // y = m + getLineHeight + p
        return original;
    }
}
