package com.github.scoliossis.base.modules.impl.player;

import com.github.scoliossis.base.Events.EventBus;
import com.github.scoliossis.base.Events.SubscribeEvent;
import com.github.scoliossis.base.Events.impl.MoveFlyingEvent;
import com.github.scoliossis.base.modules.Module;
import com.github.scoliossis.base.modules.ModuleCategory;
import com.github.scoliossis.base.modules.ModuleManager;
import com.github.scoliossis.base.modules.RegisterModule;
import com.github.scoliossis.base.utils.C;
import net.minecraft.util.MathHelper;

@RegisterModule(
        name = "Movement Fix",
        description = "the legitimacy",
        category = ModuleCategory.Player
)
public class MovementFixModule extends Module {
    public static boolean shouldMoveFix() {
        return ModuleManager.isEnabled(MovementFixModule.class);
    }

    @SubscribeEvent(EventBus.Priority.LOWEST)
    public void onMoveFlying(MoveFlyingEvent event) {
        //if (mode.getValue() == MovementFixMode.Awesome) {
        fixStrafe(event);
        //}
    }

    // pasted from idk where, but it was in mushroom so its mine now
    private void fixStrafe(MoveFlyingEvent event) {
        int dif = (int) ((MathHelper.wrapAngleTo180_float(C.p().rotationYaw - event.getYaw()
                - 158.5f) + 180.0f) / 45f);

        float strafe = event.getStrafe();
        float forward = event.getForward();

        float calcForward = 0;
        float calcStrafe = 0;
        switch (dif) {
            case 0:
                calcForward = forward;
                calcStrafe = strafe;
                break;
            case 1:
                calcForward += forward;
                calcStrafe -= forward;
                calcForward += strafe;
                calcStrafe += strafe;
                break;
            case 2:
                calcForward = strafe;
                calcStrafe = -forward;
                break;
            case 3:
                calcForward -= forward;
                calcStrafe -= forward;
                calcForward += strafe;
                calcStrafe -= strafe;
                break;
            case 4:
                calcForward = -forward;
                calcStrafe = -strafe;
                break;
            case 5:
                calcForward -= forward;
                calcStrafe += forward;
                calcForward -= strafe;
                calcStrafe -= strafe;
                break;
            case 6:
                calcForward = -strafe;
                calcStrafe = forward;
                break;
            case 7:
                calcForward += forward;
                calcStrafe += forward;
                calcForward -= strafe;
                calcStrafe += strafe;
                break;
        }

        if (calcForward > 1.0F || (calcForward < 0.9F && calcForward > 0.3F) || calcForward < -1.0F
                || (calcForward > -0.9F && calcForward < -0.3F)) {
            calcForward *= 0.5F;
        }

        if (calcStrafe > 1.0F || (calcStrafe < 0.9F && calcStrafe > 0.3F) || calcStrafe < -1.0F
                || (calcStrafe > -0.9F && calcStrafe < -0.3F)) {
            calcStrafe *= 0.5F;
        }

        event.setForward(calcForward);
        event.setStrafe(calcStrafe);
    }

    @Override
    protected void onEnable() {

    }

    @Override
    protected void onDisable() {

    }
}
