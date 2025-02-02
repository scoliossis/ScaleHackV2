package com.github.scoliossis.base.mixin.net.minecraft.client.gui;

import com.github.scoliossis.base.utils.RenderUtil;
import net.minecraft.client.gui.GuiSlot;
import net.minecraft.client.renderer.Tessellator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiSlot.class)
public class MixinGuiSlot {
    @Shadow public int left;
    @Shadow public int top;
    @Shadow public int right;
    @Shadow public int bottom;

    /**
     * @author Lifix
     * @reason scissor
     */
    @Overwrite(remap = false)
    protected void drawContainerBackground(Tessellator tessellator) {
        // nuh uh
    }

    /**
     * @author Lifix
     * @reason scissor
     */
    @Overwrite
    protected void overlayBackground(int startY, int endY, int startAlpha, int endAlpha) {
        // nuh uh
    }

    @Inject(method = "drawScreen", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiSlot;" +
            "drawContainerBackground(Lnet/minecraft/client/renderer/Tessellator;)V", shift = At.Shift.AFTER))
    private void impl$drawScreen$drawSelectionBox$pre(int mouseXIn, int mouseYIn, float p_148128_3_, CallbackInfo ci) {
        RenderUtil.scissorBeginMatrix(this.left, this.top, this.right, this.bottom);
    }

    @Inject(method = "drawScreen", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiSlot;" +
            "drawSelectionBox(IIII)V", shift = At.Shift.AFTER))
    private void impl$drawScreen$drawSelectionBox$post(int mouseXIn, int mouseYIn, float p_148128_3_, CallbackInfo ci) {
        RenderUtil.scissorEndMatrix();
    }
}
