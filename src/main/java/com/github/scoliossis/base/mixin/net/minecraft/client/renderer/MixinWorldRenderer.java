package com.github.scoliossis.base.mixin.net.minecraft.client.renderer;

import com.github.scoliossis.base.bridge.net.minecraft.client.renderer.WorldRendererBridge;
import net.minecraft.client.renderer.WorldRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(WorldRenderer.class)
public class MixinWorldRenderer implements WorldRendererBridge {
    @Shadow private boolean isDrawing;

    public boolean bridge$isDrawing() {
        return this.isDrawing;
    }
}
