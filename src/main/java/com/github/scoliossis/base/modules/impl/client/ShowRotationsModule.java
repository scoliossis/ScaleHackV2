package com.github.scoliossis.base.modules.impl.client;

import com.github.scoliossis.base.modules.ModuleCategory;
import com.github.scoliossis.base.modules.RegisterModule;
import com.github.scoliossis.base.modules.Module;

@RegisterModule(
        name = "Show Rotations",
        description = "you spin me right round baby right round",
        category = ModuleCategory.Client
)
public class ShowRotationsModule extends Module {
    /*
    private float yaw;
    private float pitch;
    private Float overrideYaw = null;
    private Float overridePitch = null;

    public ShowRotationsModule() {
        super("Show Rotations", "you spin me right round baby right round", ModuleCategory.VISUALS);
    }

    @SubscribeEvent
    public void onPostMotion(MotionEvent.Post event) {
        this.yaw = event.getYaw();
        this.pitch = event.getPitch();

        if (this.overrideYaw != null) {
            this.yaw = this.overrideYaw;
            this.overrideYaw = null;
        }

        if (this.overridePitch != null) {
            this.pitch = this.overridePitch;
            this.overridePitch = null;
        }
    }
     */

    @Override
    protected void onEnable() {

    }

    @Override
    protected void onDisable() {

    }
}
