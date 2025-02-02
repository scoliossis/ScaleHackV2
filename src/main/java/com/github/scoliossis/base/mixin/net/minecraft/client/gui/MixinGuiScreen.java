package com.github.scoliossis.base.mixin.net.minecraft.client.gui;

import com.github.scoliossis.base.screens.mainmenu.MainMenuBackground;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiScreen.class)
public class MixinGuiScreen extends Gui {
    @Shadow public int width;
    @Shadow public int height;

    @Inject(method = "drawBackground", at = @At("HEAD"), cancellable = true)
    private void impl$drawBackground(int tint, CallbackInfo ci) {
        ci.cancel();
        MainMenuBackground.drawBackground();
    }
}
