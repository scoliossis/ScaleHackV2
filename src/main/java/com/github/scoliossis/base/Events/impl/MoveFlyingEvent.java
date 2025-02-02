package com.github.scoliossis.base.Events.impl;

import com.github.scoliossis.base.Events.Event;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class MoveFlyingEvent extends Event {
    private float strafe;
    private float forward;
    private float friction;
    private float yaw;
}
