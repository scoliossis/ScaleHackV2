package com.github.scoliossis.base.mixin.net.minecraft.entity;

import com.github.scoliossis.base.Events.Bus;
import com.github.scoliossis.base.Events.impl.JumpEvent;
import com.github.scoliossis.base.modules.impl.movement.SprintModule;
import com.github.scoliossis.base.modules.impl.player.MovementFixModule;
import com.github.scoliossis.base.utils.C;
import com.github.scoliossis.base.utils.MovementUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityLivingBase.class)
public abstract class MixinEntityLivingBase extends Entity {
    public MixinEntityLivingBase(World worldIn) {
        super(worldIn);
    }

    @Shadow protected abstract float getJumpUpwardsMotion();

    @Shadow public abstract boolean isPotionActive(Potion potionIn);

    @Shadow public abstract PotionEffect getActivePotionEffect(Potion potionIn);

    // custom jump event that i can set yaw of for move fix + set jump height of it for anti jump boost + omnisprint jumping
    @Inject(method = "jump", at = @At("HEAD"), cancellable = true)
    protected void jump(CallbackInfo ci) {
        if (C.p() == null || this.getEntityId() != C.p().getEntityId()) return;

        double jumpHeight = this.getJumpUpwardsMotion();
        if (this.isPotionActive(Potion.jump)) {
            jumpHeight += (this.getActivePotionEffect(Potion.jump).getAmplifier() + 1.0) * 0.1;
        }

        JumpEvent event = new JumpEvent(MovementFixModule.shouldMoveFix() ? MovementUtil.playerYaw : this.rotationYaw, jumpHeight);
        if (Bus.post(event)) {
            ci.cancel();
            return;
        }

        this.motionY = event.height;

        if (SprintModule.isSprinting()) {
            //float f = event.yaw * 0.017453292F;
            float f = MovementUtil.getMovementYaw(event.yaw) * 0.017453292F;

            this.motionX -= MathHelper.sin(f) * 0.2;
            this.motionZ += MathHelper.cos(f) * 0.2;
        }

        this.isAirBorne = true;

        ci.cancel();
    }
}
