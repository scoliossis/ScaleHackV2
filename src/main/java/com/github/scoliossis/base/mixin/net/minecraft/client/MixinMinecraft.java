package com.github.scoliossis.base.mixin.net.minecraft.client;

import com.github.scoliossis.base.Events.Bus;
import com.github.scoliossis.base.Events.impl.ClientTickEvent;
import com.github.scoliossis.base.Events.impl.KeyPressEvent;
import com.github.scoliossis.base.bridge.net.minecraft.client.MinecraftBridge;
import com.github.scoliossis.base.bridge.net.minecraft.util.SessionBridge;
import com.github.scoliossis.base.bridge.net.minecraft.util.TimerBridge;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.Session;
import net.minecraft.util.Timer;
import org.lwjgl.input.Keyboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;

@Mixin(Minecraft.class)
public class MixinMinecraft implements MinecraftBridge {
    @Unique ArrayList<Integer> scale$pressedKeys = new ArrayList<>();
    @Shadow private Timer timer;
    @Shadow public WorldClient theWorld;
    @Shadow public GuiScreen currentScreen;
    @Shadow public GameSettings gameSettings;
    @Shadow private Session session;

    public TimerBridge bridge$getTimer() {
        return TimerBridge.from(this.timer);
    }

    public void bridge$setSession(SessionBridge session) {
        this.session = (Session) session;
    }

    @Inject(method = "runTick", at = @At("HEAD"))
    public void impl$runTick$head(CallbackInfo ci) {
        Bus.post(new ClientTickEvent());
        // awesome key released event real
        for (Integer key : this.scale$pressedKeys) {
            if (!Keyboard.isKeyDown(key)) {
                Bus.post(new KeyPressEvent.Released(key, Keyboard.getEventCharacter()));
            }
        }

        this.scale$pressedKeys.removeIf(k -> !Keyboard.isKeyDown(k));
    }


    @Inject(method = "runTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;dispatchKeypresses()V"))
    public void impl$runTick$dispatchKeypresses(CallbackInfo ci) {
        if (Keyboard.getEventKeyState()) {
            if (!this.scale$pressedKeys.contains(Keyboard.getEventKey())) {
                Bus.post(new KeyPressEvent.Pressed(Keyboard.getEventKey(),
                        Keyboard.getEventCharacter()));
                this.scale$pressedKeys.add(Keyboard.getEventKey());
            }
        }
    }

    @Inject(method = "getLimitFramerate", at = @At("HEAD"), cancellable = true)
    public void impl$getLimitFramerate(CallbackInfoReturnable<Integer> cir) {
        cir.setReturnValue(this.theWorld == null && this.currentScreen != null
                ? 360 : this.gameSettings.limitFramerate);
    }
}
