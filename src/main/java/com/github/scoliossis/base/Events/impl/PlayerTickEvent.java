package com.github.scoliossis.base.Events.impl;

import com.github.scoliossis.base.Events.Event;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PlayerTickEvent extends Event {
    private float yaw;
    private float pitch;
}
