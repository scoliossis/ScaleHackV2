package com.github.scoliossis.base.modules.impl.movement;

import com.github.scoliossis.base.modules.ModuleCategory;
import com.github.scoliossis.base.modules.RegisterModule;
import com.github.scoliossis.base.modules.Module;

@RegisterModule(
        name = "Flight",
        description = "\"nah i'm legit bro\"",
        category = ModuleCategory.Movement
)
public class FlightModule extends Module {
    /*
    private final NumberOption<Float> horizontalSpeed = new NumberOption<>("Horizontal Speed", 0.5f,
            -1f, 2f);
    private final NumberOption<Float> verticalSpeed = new NumberOption<>("Vertical Speed", 0.5f,
            -1f, 2f);

    @SubscribeEvent
    public void onMotionPre(MotionEvent.Pre event) {
        EntityPlayerSP player = ClientUtil.getPlayer();
        GameSettings settings = ClientUtil.getGame().gameSettings;

        MovementUtil.setMotion(this.horizontalSpeed.getValue());

        player.motionY = 0;
        if (settings.keyBindJump.isKeyDown()) player.motionY = this.verticalSpeed.getValue();
        if (settings.keyBindSneak.isKeyDown()) player.motionY = -this.verticalSpeed.getValue();
    }
     */

    @Override
    protected void onEnable() {

    }

    @Override
    protected void onDisable() {

    }
}
