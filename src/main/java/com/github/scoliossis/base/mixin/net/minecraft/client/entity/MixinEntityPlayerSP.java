package com.github.scoliossis.base.mixin.net.minecraft.client.entity;

import com.github.scoliossis.base.Events.Bus;
import com.github.scoliossis.base.Events.impl.MotionEvent;
import com.github.scoliossis.base.Events.impl.PlayerTickEvent;
import com.github.scoliossis.base.utils.MovementUtil;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityPlayerSP.class)
public abstract class MixinEntityPlayerSP extends AbstractClientPlayer {
    @Shadow @Final public NetHandlerPlayClient sendQueue;
    @Shadow public int sprintingTicksLeft;
    @Shadow private boolean serverSprintState;
    @Shadow private boolean serverSneakState;
    @Shadow private double lastReportedPosX;
    @Shadow private double lastReportedPosY;
    @Shadow private double lastReportedPosZ;
    @Shadow private float lastReportedYaw;
    @Shadow private float lastReportedPitch;
    @Shadow private int positionUpdateTicks;

    public MixinEntityPlayerSP(World worldIn, GameProfile playerProfile) {
        super(worldIn, playerProfile);
    }

    @Shadow protected abstract boolean isCurrentViewEntity();

    @Inject(method = "onUpdate", at = @At("HEAD"))
    public void onUpdate(CallbackInfo ci) {
        Bus.post(new PlayerTickEvent(this.rotationYaw, this.rotationPitch));
    }

    /**
     * @author scale
     * @reason omnisprint
     */
    @Overwrite
    public void setSprinting(boolean p_setSprinting_1_) {
        // TODO: impl sprint module
//        if (ClientUtil.getPlayer() != null && this.getEntityId() == ClientUtil.getPlayer().getEntityId()
//                && Sprint.shouldSprint()) {
//            p_setSprinting_1_ = Sprint.sprintOverride();
//        }

        super.setSprinting(p_setSprinting_1_);
        this.sprintingTicksLeft = p_setSprinting_1_ ? 600 : 0;
    }

    /**
     * @author Lifix
     * @reason event handling
     */
    @Overwrite
    public void onUpdateWalkingPlayer() {
        MotionEvent.Pre event =  new MotionEvent.Pre(this.isSprinting(), this.isSneaking(),
                this.posX, this.getEntityBoundingBox().minY, this.posZ,
                MovementUtil.playerYaw, MovementUtil.playerPitch, this.onGround);
        if (Bus.post(event)) {
            return;
        }

        boolean flag = event.isSprinting();
        if (flag != this.serverSprintState) {
            if (flag) {
                this.sendQueue.addToSendQueue(new C0BPacketEntityAction(this,
                        C0BPacketEntityAction.Action.START_SPRINTING));
            } else {
                this.sendQueue.addToSendQueue(new C0BPacketEntityAction(this,
                        C0BPacketEntityAction.Action.STOP_SPRINTING));
            }

            this.serverSprintState = flag;
        }

        boolean flag1 = event.isSneaking();
        if (flag1 != this.serverSneakState) {
            if (flag1) {
                this.sendQueue.addToSendQueue(new C0BPacketEntityAction(this,
                        C0BPacketEntityAction.Action.START_SNEAKING));
            } else {
                this.sendQueue.addToSendQueue(new C0BPacketEntityAction(this,
                        C0BPacketEntityAction.Action.STOP_SNEAKING));
            }

            this.serverSneakState = flag1;
        }

        if (this.isCurrentViewEntity()) {
            double d0 = event.getX() - this.lastReportedPosX;
            double d1 = event.getY() - this.lastReportedPosY;
            double d2 = event.getZ() - this.lastReportedPosZ;
            double d3 = (event.getYaw() - this.lastReportedYaw);
            double d4 = (event.getPitch() - this.lastReportedPitch);
            boolean flag2 = d0 * d0 + d1 * d1 + d2 * d2 > 9.0E-4 || this.positionUpdateTicks >= 20;
            boolean flag3 = d3 != 0.0 || d4 != 0.0;
            if (this.ridingEntity == null) {
                if (flag2 && flag3) {
                    this.sendQueue.addToSendQueue(new C03PacketPlayer.C06PacketPlayerPosLook(event.getX(),
                            event.getY(), event.getZ(), event.getYaw(), event.getPitch(), event.isOnGround()));
                } else if (flag2) {
                    this.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(event.getX(),
                            event.getY(), event.getZ(), event.isOnGround()));
                } else if (flag3) {
                    this.sendQueue.addToSendQueue(new C03PacketPlayer.C05PacketPlayerLook(event.getYaw(),
                            event.getPitch(), event.isOnGround()));
                } else {
                    this.sendQueue.addToSendQueue(new C03PacketPlayer(event.isOnGround()));
                }
            } else {
                this.sendQueue.addToSendQueue(new C03PacketPlayer.C06PacketPlayerPosLook(this.motionX, -999.0,
                        this.motionZ, event.getYaw(), event.getPitch(), event.isOnGround()));
                flag2 = false;
            }

            ++this.positionUpdateTicks;
            if (flag2) {
                this.lastReportedPosX = event.getX();
                this.lastReportedPosY = event.getY();
                this.lastReportedPosZ = event.getZ();
                this.positionUpdateTicks = 0;
            }

            if (flag3) {
                this.lastReportedYaw = event.getYaw();
                this.lastReportedPitch = event.getPitch();
            }
        }

        Bus.post(new MotionEvent.Post(event.isSprinting(), event.isSneaking(),
                event.getX(), event.getY(), event.getZ(), event.getYaw(), event.getPitch(), event.isOnGround()));
    }
}
