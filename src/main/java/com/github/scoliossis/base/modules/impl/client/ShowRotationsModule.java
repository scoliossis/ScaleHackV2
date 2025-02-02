package com.github.scoliossis.base.modules.impl.client;

import com.github.scoliossis.base.Events.SubscribeEvent;
import com.github.scoliossis.base.Events.impl.MotionEvent;
import com.github.scoliossis.base.modules.Module;
import com.github.scoliossis.base.modules.ModuleCategory;
import com.github.scoliossis.base.modules.RegisterModule;

@RegisterModule(
        name = "Show Rotations",
        description = "you spin me right round baby right round",
        category = ModuleCategory.Client
)
public class ShowRotationsModule extends Module {
    public static float yaw;
    public static float pitch;
    private Float overrideYaw = null;
    private Float overridePitch = null;

    @SubscribeEvent
    public void onPostMotion(MotionEvent.Post event) {
        yaw = event.getYaw();
        pitch = event.getPitch();

        if (this.overrideYaw != null) {
            yaw = this.overrideYaw;
            this.overrideYaw = null;
        }

        if (this.overridePitch != null) {
            pitch = this.overridePitch;
            this.overridePitch = null;
        }
    }
    @Override
    protected void onEnable() {

    }

    @Override
    protected void onDisable() {

    }
}
