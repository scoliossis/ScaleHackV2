package com.github.scoliossis.base.modules.impl.visuals;

import com.github.scoliossis.base.modules.Module;
import com.github.scoliossis.base.modules.ModuleCategory;
import com.github.scoliossis.base.modules.RegisterModule;
import com.github.scoliossis.base.utils.C;

@RegisterModule(
        name = "Zoom",
        description = "optifine zoom for poorons",
        category = ModuleCategory.Render
)
public class ZoomModule extends Module {
    private float cachedFov = 60f;

    public void onEnable() {
        this.cachedFov = C.mc.gameSettings.fovSetting;
        C.mc.gameSettings.fovSetting = 10f;
        C.mc.gameSettings.smoothCamera = true;
    }

    public void onDisable() {
        C.mc.gameSettings.fovSetting = this.cachedFov;
        // NOT caching smoothcamera. if you had this on before, die.
        C.mc.gameSettings.smoothCamera = false;
    }
}
