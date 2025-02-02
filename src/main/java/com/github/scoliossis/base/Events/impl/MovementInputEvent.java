package com.github.scoliossis.base.Events.impl;

import com.github.scoliossis.base.Events.Event;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class MovementInputEvent extends Event {
    private float strafe;
    private float forward;
    private boolean jump;
    private boolean sneak;
    private float sneakSpeed;
}
