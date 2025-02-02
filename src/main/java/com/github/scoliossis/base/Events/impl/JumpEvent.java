package com.github.scoliossis.base.Events.impl;

import com.github.scoliossis.base.Events.Event;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class JumpEvent extends Event {
    public float yaw;
    public double height;
}
