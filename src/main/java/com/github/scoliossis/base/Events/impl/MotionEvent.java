package com.github.scoliossis.base.Events.impl;

import com.github.scoliossis.base.Events.Event;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.minecraftforge.fml.common.eventhandler.Cancelable;

@Getter
@Setter
@AllArgsConstructor
public class MotionEvent extends Event {
    protected boolean sprinting;
    protected boolean sneaking;
    protected double x;
    protected double y;
    protected double z;
    protected float yaw;
    protected float pitch;
    protected boolean onGround;

    @Cancelable
    public static class Pre extends MotionEvent {
        public Pre(boolean sprinting, boolean sneaking, double x, double y, double z, float yaw, float pitch,
                   boolean onGround) {
            super(sprinting, sneaking, x, y, z, yaw, pitch, onGround);
        }
    }

    @Cancelable
    public static class Post extends MotionEvent {
        public Post(boolean sprinting, boolean sneaking, double x, double y, double z, float yaw, float pitch,
                    boolean onGround) {
            super(sprinting, sneaking, x, y, z, yaw, pitch, onGround);
        }
    }
}
