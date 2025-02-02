package com.github.scoliossis.base.mixin.net.minecraft.client.gui;

import com.github.scoliossis.base.modules.ModuleManager;
import com.github.scoliossis.base.modules.impl.client.HUDModule;
import com.github.scoliossis.base.modules.impl.client.NickHiderModule;
import com.github.scoliossis.base.utils.font.CustomFontRenderer;
import com.github.scoliossis.base.utils.font.FontRegistry;
import net.minecraft.client.gui.FontRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.awt.*;

@Mixin(FontRenderer.class)
public abstract class MixinFontRenderer {
    @Shadow protected abstract void renderStringAtPos(final String p0, final boolean p1);
    @Shadow public abstract int getStringWidth(final String p0);

    @Shadow protected float posX;

    @Shadow protected float posY;

    @Shadow private int textColor;

    @Shadow private float red;

    @Shadow private float blue;

    @Shadow private float green;

    @Shadow private float alpha;

    @Unique
    private boolean scale$shouldUseCustomFont() {
        return ModuleManager.isEnabled(HUDModule.class);
    }

    @Unique
    private CustomFontRenderer scale$getCustomFont() {
        return FontRegistry.getSpaceGroteskRegular18px();
    }

    @Inject(method = "renderStringAtPos", at = @At("HEAD"), cancellable = true)
    private void impl$renderStringAtPos(String text, boolean shadow, CallbackInfo ci) {
        // real.
        int color = new Color(this.red, this.green, this.blue, this.alpha).getRGB();

        // fix text to hide nick
        String text2 = NickHiderModule.fixText(text);

        // drop text all fancy
        if (this.scale$shouldUseCustomFont()) this.scale$getCustomFont().drawString(text2, this.posX, this.posY, color, shadow);
        else if (!text.equals(text2)) this.renderStringAtPos(text2, shadow);
        else return;
        ci.cancel();
    }

    @Inject(method = "getStringWidth", at = @At("RETURN"), cancellable = true)
    private void impl$getStringWidth(String text, CallbackInfoReturnable<Integer> cir) {
        String text2 = NickHiderModule.fixText(text);

        if (this.scale$shouldUseCustomFont()) cir.setReturnValue(this.scale$getCustomFont().getStringWidth(text2));
        else if (!text.equals(text2)) cir.setReturnValue(this.getStringWidth(text2));
    }
}
