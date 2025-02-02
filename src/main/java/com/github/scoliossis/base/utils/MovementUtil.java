package com.github.scoliossis.base.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;

public class MovementUtil {
    public static float playerYaw = 0f;
    public static float playerPitch = 0f;

    public static MovingObjectPosition rayTrace(Vec3 from, double distance, float yaw, float pitch) {
        final float n4 = -yaw * 0.017453292f;
        final float n5 = -pitch * 0.017453292f;
        final float cos = MathHelper.cos(n4 - 3.1415927f);
        final float sin = MathHelper.sin(n4 - 3.1415927f);
        final float n6 = -MathHelper.cos(n5);
        final Vec3 vec3 = new Vec3(sin * n6, MathHelper.sin(n5), cos * n6);

        return C.w().rayTraceBlocks(from, from.addVector(vec3.xCoord * distance,
                        vec3.yCoord * distance, vec3.zCoord * distance), false,
                false, false);
    }

    public static MovingObjectPosition rayTrace(double distance, float yaw, float pitch) {
        return rayTrace(getPlayerEyes(C.p()), distance, yaw, pitch);
    }

    public static Vec3 getPlayerEyes(EntityLivingBase player) {
        return player.getPositionEyes(1f);
    }

    public static MovingObjectPosition fixedLookingPos() {
        return rayTrace(Minecraft.getMinecraft().playerController.getBlockReachDistance(), playerYaw, playerPitch);
    }

    // pasted from mushroom
    public static float getMovementYaw(float playerYaw) {

        float yaw = (C.p().moveStrafing > 0 ? -90 : (C.p().moveStrafing < 0 ? 90 : 0));
        yaw *= (C.p().moveForward < 0 ? -0.5f : (C.p().moveForward > 0 ? 0.5f : 1f));
        yaw += playerYaw + (C.p().moveForward < 0 ? 180 : 0);

        return yaw;
    }
}
