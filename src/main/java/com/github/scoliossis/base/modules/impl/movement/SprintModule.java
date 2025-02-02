package com.github.scoliossis.base.modules.impl.movement;

import com.github.scoliossis.base.modules.Module;
import com.github.scoliossis.base.modules.ModuleCategory;
import com.github.scoliossis.base.modules.ModuleManager;
import com.github.scoliossis.base.modules.RegisterModule;
import com.github.scoliossis.base.utils.C;

@RegisterModule(
        name = "Sprint",
        description = "idek what this does mane",
        category = ModuleCategory.Movement
)
public class SprintModule extends Module {
    @Override
    protected void onEnable() {

    }

    @Override
    protected void onDisable() {

    }

    public static boolean isSprinting() {
        return C.p().isSprinting() || (ModuleManager.isEnabled(SprintModule.class) && shouldSprint());
    }

    public static boolean shouldSprint() {
        return true;
    }
}
