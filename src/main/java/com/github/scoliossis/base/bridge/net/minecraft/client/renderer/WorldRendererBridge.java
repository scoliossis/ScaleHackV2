package com.github.scoliossis.base.bridge.net.minecraft.client.renderer;

public interface WorldRendererBridge {
    static WorldRendererBridge from(Object instance) {
        return (WorldRendererBridge) instance;
    }

    boolean bridge$isDrawing();
}
