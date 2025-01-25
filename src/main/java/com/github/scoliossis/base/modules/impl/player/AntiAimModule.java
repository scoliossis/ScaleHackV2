package com.github.scoliossis.base.modules.impl.player;

import com.github.scoliossis.base.modules.Module;
import com.github.scoliossis.base.modules.ModuleCategory;
import com.github.scoliossis.base.modules.RegisterModule;

@RegisterModule(
        name = "Anti Aim",
        description = "aim where??",
        category = ModuleCategory.Player
)
public class AntiAimModule extends Module {
    /*
    private final Option<Boolean> serverSide = new Option<>("Server-Side", false);

    public AntiAimModule() {
        super("Anti Aim", "aim where??", ModuleCategory.PLAYER);
    }

    @SubscribeEvent
    public void onRotation(RotationEvent event) {
        float yaw = (float) (Math.random() * 360f);
        float pitch = (float) (Math.random() * 180f) - 90f;

        if (this.serverSide.getValue()) {
            event.setYaw(yaw);
            event.setPitch(pitch);
        }
        else {
            ShowRotationsModule showRotations = ScaleHack.getInstance().getModuleManager()
                    .get(ShowRotationsModule.class);
            showRotations.setOverrideYaw(yaw);
            showRotations.setOverridePitch(pitch);
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
