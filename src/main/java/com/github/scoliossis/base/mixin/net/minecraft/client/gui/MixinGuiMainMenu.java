package com.github.scoliossis.base.mixin.net.minecraft.client.gui;

import com.github.scoliossis.base.screens.mainmenu.MainMenuMain;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiMainMenu.class)
public class MixinGuiMainMenu extends GuiScreen {
    @Inject(method="initGui", at= @At(value="RETURN"))
    private void initGui(CallbackInfo callbackInfo) {
        this.mc.displayGuiScreen(new MainMenuMain());
    }

    @Inject(method = "renderSkybox", at = @At("HEAD"), cancellable = true)
    private void impl$renderSkybox(int mouseX, int mouseY, float partialTicks, CallbackInfo ci) {
        ci.cancel();
    }
}
