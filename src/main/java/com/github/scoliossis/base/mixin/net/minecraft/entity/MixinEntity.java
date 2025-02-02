package com.github.scoliossis.base.mixin.net.minecraft.entity;

import com.github.scoliossis.base.Events.Bus;
import com.github.scoliossis.base.Events.impl.MoveFlyingEvent;
import com.github.scoliossis.base.modules.impl.player.MovementFixModule;
import com.github.scoliossis.base.utils.C;
import com.github.scoliossis.base.utils.MovementUtil;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public class MixinEntity {
    @Shadow private int entityId;

    @Shadow public float rotationYaw;

    @Shadow public float rotationPitch;

    @Shadow public double motionX;

    @Shadow public double motionZ;

    @Inject(method = "moveFlying", at = @At("HEAD"), cancellable = true)
    public void moveFlying(float p_moveFlying_1_, float p_moveFlying_2_, float p_moveFlying_3_, CallbackInfo ci) {
        if (C.p() == null || this.entityId != C.p().getEntityId()) return;

        MoveFlyingEvent moveFlyingEvent = new MoveFlyingEvent(p_moveFlying_1_, p_moveFlying_2_, p_moveFlying_3_, MovementFixModule.shouldMoveFix() ? MovementUtil.playerYaw : this.rotationYaw);
        if (Bus.post(moveFlyingEvent)) return;

        float f = moveFlyingEvent.getStrafe() * moveFlyingEvent.getStrafe() + moveFlyingEvent.getForward() * moveFlyingEvent.getForward();
        if (f >= 1.0E-4F) {
            f = MathHelper.sqrt_float(f);
            if (f < 1.0F) {
                f = 1.0F;
            }

            f = moveFlyingEvent.getFriction() / f;
            moveFlyingEvent.setStrafe(moveFlyingEvent.getStrafe()*f);
            moveFlyingEvent.setForward(moveFlyingEvent.getForward()*f);
            float f1 = MathHelper.sin(moveFlyingEvent.getYaw() * 3.1415927F / 180.0F);
            float f2 = MathHelper.cos(moveFlyingEvent.getYaw() * 3.1415927F / 180.0F);
            this.motionX += (double)(moveFlyingEvent.getStrafe() * f2 - moveFlyingEvent.getForward() * f1);
            this.motionZ += (double)(moveFlyingEvent.getForward() * f2 + moveFlyingEvent.getStrafe() * f1);
        }

        ci.cancel();
    }
}
